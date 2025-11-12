package playwrightTraditional;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Paths;

public class TestCaseCreateAccount {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions().setHeadless(false)
        );
    }

    @BeforeEach
    void setUp() {
        context = browser.newContext(new Browser.NewContextOptions()
            .setRecordVideoDir(Paths.get("videos/"))
            .setRecordVideoSize(1280, 720));
        page = context.newPage();
    }

    @Test
    public void testCreateAccountPage() {
        // Navigate to bookstore
        page.navigate("https://depaul.bncollege.com/");
        page.waitForLoadState();

        // Search for earbuds
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).fill("earbuds");
        page.keyboard().press("Enter");
        page.waitForLoadState();

        // Expand brand filter and select JBL
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("brand")).click();
        page.waitForSelector(".facet__list.js-facet-list");
        page.locator(".facet__list.js-facet-list.js-facet-top-values > li:nth-child(3) form label").first().click();
        page.waitForTimeout(2000);

        // Click JBL Quantum True Wireless
        Locator product = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("JBL Quantum True Wireless"));
        product.waitFor(new Locator.WaitForOptions().setTimeout(20000));
        product.click();

        // Add to cart and go to checkout
        page.waitForSelector("button:has-text('Add to cart')");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to cart")).click();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Cart 1 items")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Proceed To Checkout")).first().click();

        // Assert heading "Create Account" appears (fix)
        Locator createAccountLabel = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Create Account"));
        createAccountLabel.waitFor(new Locator.WaitForOptions().setTimeout(10000));
        assertTrue(createAccountLabel.isVisible(), "Create Account heading not visible");

        // Proceed as guest
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Proceed As Guest")).click();
        System.out.println("Proceeded as guest successfully");
    }

    @AfterEach
    void tearDown() {
        context.close();
    }

    @AfterAll
    static void closeBrowser() {
        browser.close();
        playwright.close();
    }
}