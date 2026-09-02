package bookstore;

import commons.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class HomePage extends BasePage {

    // Homepage

    @FindBy(xpath = "//h1[normalize-space()='Books List']")
    private WebElement pageTitle;

    // Price / Sorting
    @FindBy(xpath = "//*[normalize-space()='Price']")
    private WebElement priceMenu;

    @FindBy(xpath = "//a[normalize-space()='Sort By ASC']")
    private WebElement sortLowToHighButton;

    @FindBy(xpath = "//a[normalize-space()='Sort By DESC']")
    private WebElement sortHighToLowButton;

    // Product Prices
    @FindBy(xpath = "//*[@id='books']//*[contains(text(),'€')]")
    private List<WebElement> productPrices;

    // Add To Cart
    @FindBy(css = "a[data-testid^='cart-']")
    private List<WebElement> addToCartButtons;


    // Cart
    @FindBy(xpath = "//*[@id='core']/div/div/nav[2]/div/a[2]")
    private WebElement cartIcon;

    @FindBy(xpath = "//*[@id='core']/div/div/nav[2]/div/a[2]/span")
    private WebElement cartBadge;


    public HomePage(WebDriver driver) {
        super(driver);
    }


    // Verify Homepage
    public boolean isOnHomePage() {

        try {

            closeAdIfPresent();

            waitForElementToBeVisible(pageTitle);

            return pageTitle.isDisplayed()
                    && pageTitle.getText().equals("Books List");

        } catch (Exception e) {

            return false;
        }
    }


    // Sort Low To High
    public void sortLowToHigh() {

        closeAdIfPresent();

        // Scroll ke Price
        scrollToElement(priceMenu);

        // Tunggu scrolling selesai
        waitAfterScroll();

        // Cek popup
        closeAdIfPresent();

        waitForElementToBeVisible(priceMenu);

        // Hover ke Price
        new Actions(driver)
                .moveToElement(priceMenu)
                .perform();

        // Tunggu ASC muncul
        waitForElementToBeVisible(sortLowToHighButton);

        // Cek popup
        closeAdIfPresent();

        // Hover kembali ke Price agar menu tetap terbuka
        new Actions(driver)
                .moveToElement(priceMenu)
                .perform();

        waitForElementClickable(sortLowToHighButton);

        sortLowToHighButton.click();

        // Tunggu hasil sorting
        wait.until(
                ExpectedConditions.urlContains("sort=asc")
        );

        closeAdIfPresent();
    }


    // Sort High To Low
    public void sortHighToLow() {

        closeAdIfPresent();

        scrollToElement(priceMenu);

        waitAfterScroll();

        closeAdIfPresent();

        waitForElementToBeVisible(priceMenu);

        new Actions(driver)
                .moveToElement(priceMenu)
                .perform();

        waitForElementToBeVisible(sortHighToLowButton);

        closeAdIfPresent();

        new Actions(driver)
                .moveToElement(priceMenu)
                .perform();

        waitForElementClickable(sortHighToLowButton);

        sortHighToLowButton.click();

        wait.until(
                ExpectedConditions.urlContains("sort=desc")
        );

        closeAdIfPresent();
    }

    // Get Product Prices
    public List<Double> getProductPrices() {

        closeAdIfPresent();

        wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.xpath(
                                "//*[@id='books']//*[contains(text(),'€')]"
                        )
                )
        );

        return productPrices.stream()
                .map(element ->
                        Double.parseDouble(
                                element.getText()
                                        .replace("€", "")
                                        .trim()
                        )
                )
                .toList();
    }


    // Add Lowest Price Product To Cart
    public void addLowestPriceProductToCart() {

        closeAdIfPresent();

        // Tunggu tombol Add To Cart tersedia
        wait.until(
                ExpectedConditions.visibilityOfAllElements(
                        addToCartButtons
                )
        );

        if (addToCartButtons.isEmpty()) {
            throw new IllegalStateException(
                    "No Add To Cart button was found"
            );
        }
        WebElement firstProduct =
                addToCartButtons.get(0);

        // Scroll ke produk
        scrollToElement(firstProduct);

        waitAfterScroll();

        // Cek popup
        closeAdIfPresent();

        waitForElementToBeVisible(firstProduct);

        // Hover dan klik
        new Actions(driver)
                .moveToElement(firstProduct)
                .click()
                .perform();

        // Cek popup setelah klik
        closeAdIfPresent();

        // Pastikan cart badge muncul
        waitForElementToBeVisible(cartBadge);
    }


    // Cart Badge
    public boolean isCartBadgeDisplayed() {

        try {

            closeAdIfPresent();

            waitForElementToBeVisible(cartBadge);

            return cartBadge.isDisplayed();

        } catch (Exception e) {

            return false;
        }
    }


    // Open Cart
    public void openCart() {

        closeAdIfPresent();

        scrollToElement(cartIcon);

        waitAfterScroll();

        closeAdIfPresent();

        waitForElementToBeVisible(cartIcon);

        waitForElementClickable(cartIcon);

        new Actions(driver)
                .moveToElement(cartIcon)
                .click()
                .perform();

        wait.until(
                ExpectedConditions.urlContains(
                        "/bookstore/cart"
                )
        );

        closeAdIfPresent();
    }
}