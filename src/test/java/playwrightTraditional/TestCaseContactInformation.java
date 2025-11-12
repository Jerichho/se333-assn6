package playwrightTraditional;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TestCaseContactInformation extends BaseUITest {

    @Test
    void testContactInformationPage() {

        page.navigate("https://depaul.bncollege.com/");
        page.waitForLoadState();

        // Search earbuds
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search"))
                .fill("earbuds");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search"))
                .press("Enter");
        page.waitForLoadState();

        // Open brand filter
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("brand")).click();

        // Select JBL (using the same working CSS selector style you used)
        page.locator(".facet__list.js-facet-list.js-facet-top-values > li:nth-child(3) svg")
                .first().click();
        page.waitForTimeout(800);

        // Open JBL wireless product
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("JBL Quantum True Wireless"))
                .click();
        page.waitForLoadState();

        // Add to cart
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to cart"))
                .click();
        page.waitForTimeout(1000);

        // Go to cart
        page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName(java.util.regex.Pattern.compile("Cart.*items")))
                .click();
        page.waitForURL(java.util.regex.Pattern.compile("cart"));

        // Proceed to checkout
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Proceed To Checkout"))
                .first().click();
        page.waitForLoadState();

        // Continue as guest
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Proceed As Guest"))
                .click();
        page.waitForLoadState();

        // Fill contact form
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("First Name (required)"))
                .fill("Jericho");

        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Last Name (required)"))
                .fill("Guiang");

        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email address (required)"))
                .fill("jericho@example.com");

        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Phone Number (required)"))
                .fill("8888888888");

        // Verify Continue button is enabled
        Locator continueBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue"));
        assertThat(continueBtn).isVisible();
        assertThat(continueBtn).isEnabled();

        // Click Continue (optional)
        continueBtn.click();
    }
}