package playwrightTraditional;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TestCasePaymentInformation extends BaseUITest {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeAll
    static void setUpAll() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );
    }

    @BeforeEach
    void setUp() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("videos/"))
                .setRecordVideoSize(1280, 720)
        );
        page = context.newPage();
        page.setDefaultTimeout(40000);
    }

    @AfterEach
    void tearDown() {
        if (context != null) context.close();
    }

    @AfterAll
    static void tearDownAll() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }


    @Test
    void testPaymentInformationPage() {

        page.navigate("https://depaul.bncollege.com/");
        page.waitForLoadState();

        // Search earbuds
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).fill("earbuds");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).press("Enter");
        page.waitForLoadState();

        // Expand BRAND filter and click JBL (3rd checkbox)
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("brand")).click();
        page.locator(".facet__list.js-facet-list.js-facet-top-values > li:nth-child(3) .facet__list__mark").first().click();
        page.waitForLoadState();

        // Open JBL product (force same tab)
        Locator product = page.getByTitle("JBL Quantum True Wireless").first();
        product.evaluate("el => el.removeAttribute('target')");
        product.click();
        page.waitForLoadState();

        // Add to cart
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to cart")).click();

        // Go to cart
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Cart 1 items")).click();
        page.waitForURL("**/cart**");

        // Proceed checkout → guest
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Proceed To Checkout")).first().click();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Proceed As Guest")).click();

        // Fill contact info
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("First Name (required)")).fill("Jericho");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Last Name (required)")).fill("Guiang");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email address (required)")).fill("jericho@example.com");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Phone Number (required)")).fill("2222222222");

        // Click Continue to Payment
        page.locator(".bned-checkout-box-content").first().click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();

        //  Assert Payment Information Page Loaded
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Payment Information"))).isVisible();

        // Return back to cart
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Back to cart")).click();
        page.waitForURL("**/cart**");
    }
}