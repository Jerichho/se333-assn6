package playwrightTraditional;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

public class BaseUITest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext context;
    protected static Page page;

    protected final String baseUrl = "https://depaul.bncollege.com";

    @BeforeAll
    static void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false) 
        );

        context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(java.nio.file.Paths.get("videos/"))
                .setRecordVideoSize(1280, 720)
        );

        page = context.newPage();
        page.setDefaultTimeout(30000);
    }

    @AfterAll
    static void teardown() {
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    protected void open(String path) {
        page.navigate(baseUrl + path);
        page.waitForLoadState();
    }

    protected void assertText(String text) {
        page.getByText(text).isVisible();
    }
}