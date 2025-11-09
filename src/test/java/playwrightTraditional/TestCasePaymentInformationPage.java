package playwrightTraditional;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TestCasePaymentInformationPage extends BaseUITest {

    @Test
    void testPaymentInformationPage() {

        // Assert Sidebar Values
        assertThat(page.getByText("Subtotal $149.98")).isVisible();
        assertThat(page.getByText("Handling $2.00")).isVisible();
        assertThat(page.getByText("Taxes $15.58")).isVisible();
        assertThat(page.getByText("Total $167.56")).isVisible();

        // Assert correct item name and price
        assertThat(page.getByText("JBL Quantum True Wireless Noise Cancelling Gaming")).isVisible();
        assertThat(page.getByText("$149.98")).isVisible();

        // Click < BACK TO CART (top right)
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("< BACK TO CART")).click();
    }
}