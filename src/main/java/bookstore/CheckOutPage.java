package bookstore;

import commons.BasePage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckOutPage extends BasePage {

    @FindBy(css = "#core h1")
    private WebElement pageTitle;

    @FindBy(id = "name")
    private WebElement nameInput;

    @FindBy(id = "address")
    private WebElement addressInput;

    @FindBy(id = "card-name")
    private WebElement cardNameInput;

    @FindBy(id = "card-number")
    private WebElement cardNumberInput;

    @FindBy(id = "card-expiry-month")
    private WebElement cardExpiryMonthInput;

    @FindBy(id = "card-expiry-year")
    private WebElement cardExpiryYearInput;

    @FindBy(id = "card-cvc")
    private WebElement cardCvcInput;

    @FindBy(css = "button[data-testid='purchase']")
    private WebElement purchaseButton;

    @FindBy(xpath = "//h1[contains(@class,'mt-3') and normalize-space()='Profile']")
    private WebElement profileTitle;

    public CheckOutPage(WebDriver driver) {
        super(driver);
    }

    public void checkout(
            String name,
            String address,
            String cardName,
            String cardNumber,
            String expiryMonth,
            String expiryYear,
            String cvc) {

        // 1. Verify Checkout Page
        waitForElementToBeVisible(pageTitle);

        String actualTitle = pageTitle.getText();

        if (!actualTitle.equals("Checkout")) {
            throw new AssertionError(
                    "Title tidak sesuai. Expected: Checkout, Actual: "
                            + actualTitle
            );
        }

        // 2. Scroll ke Form
        scrollToElement(nameInput);
        waitAfterScroll();

        // 3. Isi Name
        waitForElementToBeVisible(nameInput);
        nameInput.sendKeys(name);

        // 4. Isi Address
        waitForElementToBeVisible(addressInput);
        addressInput.sendKeys(address);

        // 5. Isi Card Name
        waitForElementToBeVisible(cardNameInput);
        cardNameInput.sendKeys(cardName);

        // 6. Isi Card Number
        waitForElementToBeVisible(cardNumberInput);
        cardNumberInput.sendKeys(cardNumber);

        // 7. Isi Expiry Month
        waitForElementToBeVisible(cardExpiryMonthInput);
        cardExpiryMonthInput.sendKeys(expiryMonth);

        // 8. Isi Expiry Year
        waitForElementToBeVisible(cardExpiryYearInput);
        cardExpiryYearInput.sendKeys(expiryYear);

        // 9. Isi CVC
        waitForElementToBeVisible(cardCvcInput);
        cardCvcInput.sendKeys(cvc);
    }

    public void clickPurchase() {

        // 1. Scroll ke Purchase
        scrollToElement(purchaseButton);
        waitAfterScroll();

        // 2. Click Purchase
        waitForElementClickable(purchaseButton);

        try {
            purchaseButton.click();

        } catch (Exception e) {

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();",
                    purchaseButton
            );
        }

        // 3. Wait sampai Profile muncul
        waitForElementToBeVisible(profileTitle);

        // 4. Verify Profile Page
        String actualProfileTitle = profileTitle.getText();

        if (!actualProfileTitle.equals("Profile")) {
            throw new AssertionError(
                    "Title tidak sesuai. Expected: Profile, Actual: "
                            + actualProfileTitle
            );
        }
    }
}