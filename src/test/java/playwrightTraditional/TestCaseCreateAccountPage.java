package playwrightTraditional;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TestCaseCreateAccountPage extends BaseUITest {

    @Test
    void testCreateAccountPage() {
        // Assert we are on the Create Account page
        assertThat(page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Create Account")))
                .isVisible();

        // Click "Proceed as Guest"
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Proceed as Guest")).click();
    }
}