package playwrightTraditional;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TestCasePickupInformationPage extends BaseUITest {

    @Test
    void testPickupInformationPage() {

        // Assert Contact Information
        assertThat(page.getByText("Jericho Guiang")).isVisible();
        assertThat(page.getByText("jericho@example.com")).isVisible();
        assertThat(page.getByText("3125551234")).isVisible();

        // Assert Pickup Location
        assertThat(page.getByText("DePaul University Loop Campus & SAIC")).isVisible();

        // Assert Pickup Person
        assertThat(page.getByText("I'll pick them up")).isVisible();

        // Assert Price Sidebar
        assertThat(page.getByText("Subtotal $149.98")).isVisible();
        assertThat(page.getByText("Handling $2.00")).isVisible();
        assertThat(page.getByText("Taxes TBD")).isVisible(); // STILL TBD here
        assertThat(page.getByText("Estimated Total $151.98")).isVisible();

        // Assert Item and Price in Pickup Review
        assertThat(page.getByText("JBL Quantum True Wireless Noise Cancelling Gaming")).isVisible();
        assertThat(page.getByText("$149.98")).isVisible();

        // Continue to payment
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();
    }
}