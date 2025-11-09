package playwrightTraditional;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TestCaseShoppingCartPage extends BaseUITest {

    @Test
    void testShoppingCartPage() {

        // 1. Assert page title
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Your Shopping Cart")))
                .isVisible();

        // 2. Assert product details (name, quantity, price)
        assertThat(page.getByText("JBL Quantum True Wireless Noise Cancelling Gaming Earbuds - Black"))
                .isVisible();
        assertThat(page.getByText("Qty 1")).isVisible();
        assertThat(page.getByText("$149.98")).isVisible();

        // 3. Select FAST In-Store Pickup
        page.getByLabel("FAST In-Store Pickup").click();

        // 4. Assert sidebar price panel
        assertThat(page.getByText("Subtotal $149.98")).isVisible();
        assertThat(page.getByText("Handling $2.00")).isVisible();
        assertThat(page.getByText("Taxes TBD")).isVisible();
        assertThat(page.getByText("Estimated Total $151.98")).isVisible();

        // 5. Attempt promo code "TEST"
        page.getByPlaceholder("Promo Code").fill("TEST");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("APPLY")).click();

        // 6. Assert promo rejection message
        assertThat(page.getByText("The code you entered is invalid")).isVisible();

        // 7. Proceed to checkout
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("PROCEED TO CHECKOUT")).click();
    }
}