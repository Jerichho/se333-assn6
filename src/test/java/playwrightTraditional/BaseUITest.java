package playwrightTraditional;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class BaseUITest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext context;
    protected static Page page;

    @BeforeAll
    public static void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(java.nio.file.Paths.get("videos/"))
                .setRecordVideoSize(1280, 720));
        page = context.newPage();
    }

    @AfterAll
    public static void teardown() {
        context.close();
        browser.close();
        playwright.close();
    }
}