package playwrightTraditional;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TestCaseContactInformationPage extends BaseUITest {

    @Test
    void testContactInformationPage() {

        // Assert we are on Contact Information page
        assertThat(page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Contact Information")))
                .isVisible();

        // Fill Contact Form
        page.getByLabel("First Name").fill("Jericho");
        page.getByLabel("Last Name").fill("Guiang");
        page.getByLabel("Email").fill("jericho@example.com");
        page.getByLabel("Phone Number").fill("3125551234");

        // Assert Sidebar Summary
        assertThat(page.getByText("Subtotal $149.98")).isVisible();
        assertThat(page.getByText("Handling $2.00")).isVisible();
        assertThat(page.getByText("Taxes TBD")).isVisible(); // value should remain TBD here
        assertThat(page.getByText("Estimated Total $151.98")).isVisible();

        // Continue to Pickup Information
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Continue")).click();
    }
}