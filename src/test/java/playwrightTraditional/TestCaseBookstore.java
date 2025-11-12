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
        page.setDefaultTimeout(45000);
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
    void testBookstorePurchaseFlow() {

        // Homepage
        page.navigate("https://depaul.bncollege.com/");
        page.waitForLoadState();

        // Search earbuds
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).fill("earbuds");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).press("Enter");
        page.waitForLoadState();

        // Expand BRAND
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("brand")).click();
        page.waitForTimeout(800);

        // Click JBL brand filter
        page.getByText("brand JBL", new Page.GetByTextOptions().setExact(false)).first().click();
        page.waitForLoadState();
        page.waitForTimeout(1200);

        // Expand COLOR
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Color")).click();
        page.waitForTimeout(800);

        // Click Black color filter
        page.getByText("Color Black", new Page.GetByTextOptions().setExact(false)).click();
        page.waitForLoadState();
        page.waitForTimeout(1200);

        // Expand PRICE (optional but matches your recording)
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Price")).click();
        page.waitForTimeout(500);

        // Select Over $ filter
        page.getByText("Price Over $", new Page.GetByTextOptions().setExact(false)).click();
        page.waitForLoadState();
        page.waitForTimeout(1200);

        // Select the JBL product
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("JBL Quantum True Wireless")).click();
        page.waitForLoadState();

        // Assert product page loaded
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("JBL Quantum True Wireless"))).isVisible();

        // Add to cart
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to cart")).click();
        page.waitForTimeout(1200);

        // Open cart
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(java.util.regex.Pattern.compile("Cart.*1 items"))).click();
        page.waitForURL(java.util.regex.Pattern.compile(".*/cart.*"));

        // Assert cart contents displayed
        assertThat(page.locator("main, #maincontent, [role='main']")).isVisible();
    }
}