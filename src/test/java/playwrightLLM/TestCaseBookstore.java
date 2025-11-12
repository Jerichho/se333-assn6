package playwrightLLM;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCaseBookstore {
  private Playwright playwright;
  private Browser browser;
  private BrowserContext context;
  private Page page;

  @BeforeEach
  void setUp() {
    playwright = Playwright.create();
    browser = playwright.chromium().launch(
      new BrowserType.LaunchOptions()
        .setHeadless(true) // use headless for CI stability
    );
    context = browser.newContext(new Browser.NewContextOptions()
      .setRecordVideoDir(Paths.get("videos/"))
      .setRecordVideoSize(1280, 720)
    );
    page = context.newPage();
    page.setDefaultTimeout(30000);
  }

  @AfterEach
  void tearDown() {
    if (context != null) context.close();
    if (browser != null) browser.close();
    if (playwright != null) playwright.close();
  }

  @Test
  void testBookstoreJblEarbudsCheckout() {
    // Open homepage
    page.navigate("https://depaul.bncollege.com");
    page.waitForLoadState();

    // Handle potential cookie/consent banners or location modals if present
    optionalClick(page.locator("button:has-text('Accept')").first());
    optionalClick(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("accept|agree", Pattern.CASE_INSENSITIVE))).first());

    // Search for "earbuds"
    Locator searchBox = page.locator("input[type='search'], input[placeholder*='Search' i], input[aria-label*='Search' i], input[name='search']").first();
    if (!searchBox.isVisible()) {
      // Try opening search if it's hidden behind an icon
      optionalClick(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("search", Pattern.CASE_INSENSITIVE))).first());
    }
    searchBox = page.locator("input[type='search'], input[placeholder*='Search' i], input[aria-label*='Search' i], input[name='search']").first();
    searchBox.waitFor();
    searchBox.fill("earbuds");
    searchBox.press("Enter");

    // Wait for results to load
    page.waitForLoadState();

    // Expand the Brand filter (if collapsed)
    Locator brandToggle = page.locator("button:has-text('Brand'), [role='button']:has-text('Brand'), summary:has-text('Brand'), h2:has-text('Brand'), h3:has-text('Brand')").first();
    if (brandToggle.isVisible()) {
      // Ensure JBL option becomes visible; if not visible, click to expand
      if (!page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName(Pattern.compile("^JBL$", Pattern.CASE_INSENSITIVE))).first().isVisible()) {
        brandToggle.click();
      }
    }

    // Select JBL brand
    Locator jblCheckbox = page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName(Pattern.compile("^JBL$", Pattern.CASE_INSENSITIVE))).first();
    if (!jblCheckbox.isVisible()) {
      // Fallback via label association
      jblCheckbox = page.getByLabel("JBL", new Page.GetByLabelOptions().setExact(true));
    }
    // Some facets use clickable labels instead of check action; try both
    if (jblCheckbox.count() > 0 && jblCheckbox.isVisible()) {
      try { jblCheckbox.check(new Locator.CheckOptions().setForce(true)); } catch (PlaywrightException ignored) { jblCheckbox.click(); }
    } else {
      optionalClick(page.locator("label:has-text('JBL')").first());
    }

    // Wait for filter to apply (results refresh)
    page.waitForLoadState();

    // Click the target product link
    Locator productLink = page.getByRole(AriaRole.LINK,
      new Page.GetByRoleOptions().setName(Pattern.compile("JBL\\s+Quantum.*True\\s+Wireless.*Noise.*Cancelling.*Earbuds", Pattern.CASE_INSENSITIVE))
    ).first();
    if (!productLink.isVisible()) {
      // Fallback: any JBL earbuds product
      productLink = page.getByRole(AriaRole.LINK,
        new Page.GetByRoleOptions().setName(Pattern.compile("JBL.*Earbuds", Pattern.CASE_INSENSITIVE))
      ).first();
    }
    productLink.waitFor();
    productLink.click();

    // On PDP: add to cart
    Locator addToCart = page.getByRole(AriaRole.BUTTON,
      new Page.GetByRoleOptions().setName(Pattern.compile("Add to (Cart|Bag)", Pattern.CASE_INSENSITIVE))
    ).first();
    addToCart.waitFor();
    addToCart.click();

    // Wait briefly for any cart drawer animation
    page.waitForTimeout(2000);

    // Always navigate to full cart page for deterministic quantity check
    optionalClick(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("view cart|go to cart", Pattern.CASE_INSENSITIVE))).first());
    optionalClick(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(Pattern.compile("cart|shopping cart|bag", Pattern.CASE_INSENSITIVE))).first());

    // If still not on cart page, attempt URL direct
    if (!page.url().toLowerCase().contains("cart")) {
      page.locator("a[href*='cart']").first().click(new Locator.ClickOptions().setTrial(false));
    }
    page.waitForLoadState();

    // Strategy: identify line items; count quantity cell or fallback to summary text
    int quantity = 0;
    try {
      Locator qtyInputs = page.locator("input[name*='qty' i], input[name*='quantity' i]");
      if (qtyInputs.count() > 0) {
        String raw = qtyInputs.first().inputValue();
        quantity = parseIntSafe(raw);
      } else {
        // Look for a quantity span within line item rows
        Locator qtySpan = page.locator(".qty, .quantity, span:has-text('Qty')").first();
        if (qtySpan.isVisible()) {
          String raw = qtySpan.innerText();
          quantity = extractFirstInteger(raw);
        }
      }
    } catch (Exception ignored) {}

    if (quantity == 0) {
      // Fallback: parse summary blocks
      Locator summary = page.locator("text=/Items?\s*:\s*1/i, text=/\b1\s+item\b/i").first();
      if (summary.isVisible()) quantity = 1;
    }

    assertTrue(quantity == 1, "Expected cart to show 1 item but detected quantity=" + quantity);

    // Proceed to checkout
    Locator checkoutBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("checkout|proceed to checkout", Pattern.CASE_INSENSITIVE))).first();
    if (!checkoutBtn.isVisible()) {
      checkoutBtn = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(Pattern.compile("checkout|proceed to checkout", Pattern.CASE_INSENSITIVE))).first();
    }
    checkoutBtn.waitFor();
    checkoutBtn.click();

    // Basic verification that we're on a checkout-related page
    page.waitForLoadState();
    boolean atCheckout = page.url().toLowerCase().contains("checkout")
      || page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName(Pattern.compile("checkout", Pattern.CASE_INSENSITIVE))).first().isVisible();

    assertTrue(atCheckout, "Expected to navigate to the checkout page");
  }

  // Utility: click if the locator resolves to a visible element
  private void optionalClick(Locator locator) {
    try {
      if (locator != null && locator.count() > 0 && locator.first().isVisible()) {
        locator.first().click();
      }
    } catch (Exception ignored) {
    }
  }

  private int parseIntSafe(String raw) {
    try { return Integer.parseInt(raw.trim()); } catch (Exception e) { return 0; }
  }

  private int extractFirstInteger(String raw) {
    try {
      java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d+)").matcher(raw);
      if (m.find()) return Integer.parseInt(m.group(1));
    } catch (Exception ignored) {}
    return 0;
  }
}
