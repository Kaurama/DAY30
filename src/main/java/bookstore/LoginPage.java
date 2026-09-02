package bookstore;

import commons.BasePage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    @FindBy(css = "#core > div > div > h1")
    private WebElement pageTitle;

    @FindBy(xpath = "//*[@id=\"core\"]/div/div/div/div[2]/div/a")
    private WebElement buttonCheckout;

    @FindBy(id = "email")
    private WebElement usernameTextInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "submit")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String email, String password) {

        // 1. Verify Shopping Cart

        waitForElementToBeVisible(pageTitle);

        String actualTitle = pageTitle.getText();

        if (!actualTitle.equals("Shopping Cart")) {
            throw new AssertionError(
                    "Title tidak sesuai. Expected: Shopping Cart, Actual: "
                            + actualTitle
            );
        }

        // 2. Handle Advertisement
        closeAdIfPresent();

        // 3. Click Checkout

        scrollToElement(buttonCheckout);
        waitAfterScroll();

        try {
            waitForElementClickable(buttonCheckout);
            buttonCheckout.click();

        } catch (Exception e) {

            // Fallback JavaScript click
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();",
                    buttonCheckout
            );
        }

        // 4. Wait Login Form
        waitForElementToBeVisible(usernameTextInput);

        // 5. Scroll ke Email
        scrollToElement(usernameTextInput);
        waitAfterScroll();

        // 6. Isi Email
        usernameTextInput.sendKeys(email);

        // 7. Scroll ke Password
        scrollToElement(passwordInput);
        waitAfterScroll();

        passwordInput.sendKeys(password);

        // 8. Scroll ke Login Button
        scrollToElement(loginButton);
        waitAfterScroll();

        // 9. Click Login
        waitForElementClickable(loginButton);

        try {
            loginButton.click();

        } catch (Exception e) {

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();",
                    loginButton
            );
        }
    }
}