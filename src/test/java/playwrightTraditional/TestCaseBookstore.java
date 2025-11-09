package playwrightTraditional;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import java.nio.file.Paths;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TestCaseBookstore extends BaseUITest {

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
    }

    @AfterEach
    void tearDown() {
        context.close();
    }

    @AfterAll
    static void tearDownAll() {
        browser.close();
        playwright.close();
    }

    @Test
    void testBookstorePurchaseFlow() {
        page.navigate("https://depaul.bncollege.com/");

        page.getByPlaceholder("Enter your search details (").click();
        page.getByPlaceholder("Enter your search details (").fill("earbuds");
        page.getByPlaceholder("Enter your search details (").press("Enter");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("brand")).click();
        page.locator("#facet-brand").getByRole(AriaRole.LIST).locator("label")
                .filter(new Locator.FilterOptions().setHasText("JBL"))
                .getByRole(AriaRole.IMG).click();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Color")).click();
        page.locator("label").filter(new Locator.FilterOptions().setHasText("Black")).locator("svg").first().click();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Price")).click();
        page.getByText("Over $50").click();

        // Click the JBL product
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("JBL Quantum True Wireless")).click();

        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("JBL Quantum True Wireless Noise Cancelling Gaming"))).isVisible();

        // Add to cart
        page.getByLabel("Add to cart").click();

        // Assert cart shows 1 item
        page.waitForTimeout(1500);
        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Cart 1 items"))).isVisible();

        // Open Cart
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Cart 1 items")).click();
    }
}