package playwrightTraditional;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TestCaseEmptyCart extends BaseUITest {

    @Test
    void testEmptyCartAfterRemovingItem() {

        // Assert product is currently in cart
        assertThat(page.getByText("JBL Quantum True Wireless Noise Cancelling Gaming")).isVisible();
        assertThat(page.getByText("$149.98")).isVisible();

        // Click Remove / Delete item
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Remove")).click();

        // Assert cart is now empty
        assertThat(page.getByText("Your cart is currently empty.")).isVisible();

        // Close browser window
        page.context().browser().close();
    }
}