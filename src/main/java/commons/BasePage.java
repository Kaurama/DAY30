package commons;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        );

        PageFactory.initElements(driver, this);
    }

    public void waitForElementToBeVisible(WebElement element) {
        wait.until(
                ExpectedConditions.visibilityOf(element)
        );
    }

    public void waitForElementClickable(WebElement element) {
        wait.until(
                ExpectedConditions.elementToBeClickable(element)
        );
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});",
                element
        );
    }

    public void waitAfterScroll() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void closeAdIfPresent() {

        try {

            List<By> closeLocators = List.of(

                    // Full-screen popup
                    By.cssSelector(
                            ".continue-prompt-text"
                    ),

                    By.xpath(
                            "//div[contains(@class,'continue-prompt-text') " +
                                    "and normalize-space()='Close']"
                    ),


                    // Google Ad / Dismiss
                    // Elemen utama tombol Close Ad
                    By.cssSelector(
                            "#dismiss-button"
                    ),

                    // Element di dalam tombol
                    By.cssSelector(
                            "#dismiss-button-element"
                    ),

                    // Text "Close"
                    By.cssSelector(
                            "#dismiss-button-element > div"
                    ),

                    // Berdasarkan role dan aria-label
                    By.cssSelector(
                            "[role='button'][aria-label='Close ad']"
                    ),

                    // Tombol Close

                    By.cssSelector(
                            "button[aria-label*='Close']"
                    ),

                    By.cssSelector(
                            "button[title*='Close']"
                    ),

                    By.cssSelector(
                            "[role='button'][aria-label*='Close']"
                    ),

                    By.cssSelector(
                            "[role='button'][title*='Close']"
                    ),

                    // Generic Close

                    By.cssSelector(".close"),

                    By.cssSelector(".btn-close"),

                    // Icon X

                    By.xpath(
                            "//*[normalize-space()='×']"
                    ),

                    By.xpath(
                            "//*[normalize-space()='✕']"
                    ),

                    By.xpath(
                            "//*[normalize-space()='X']"
                    ),

                    // Panah iklan
                    By.xpath(
                            "//*[normalize-space()='↓']"
                    ),

                    By.xpath(
                            "//*[normalize-space()='▼']"
                    ),

                    By.xpath(
                            "//*[normalize-space()='⌄']"
                    ),

                    // SVG path

                    By.xpath(
                            "//*[name()='path' and contains(@d,'M10,26')]"
                    ),

                    // Parent span dari SVG

                    By.xpath(
                            "//*[name()='path' and contains(@d,'M10,26')]" +
                                    "/ancestor::span[1]"
                    ),
                    // Parent SVG


                    By.xpath(
                            "//*[name()='path' and contains(@d,'M10,26')]" +
                                    "/ancestor::svg[1]"
                    ),


                    // Struktur iklan


                    By.xpath(
                            "//ow-root//ins[contains(@class,'ee')]//span[.//svg]"
                    ),


                    // Parent button dari SVG


                    By.xpath(
                            "//*[name()='path' and contains(@d,'M10,26')]" +
                                    "/ancestor::button[1]"
                    ),


                    // Parent role=button


                    By.xpath(
                            "//*[name()='path' and contains(@d,'M10,26')]" +
                                    "/ancestor::*[@role='button'][1]"
                    )
            );


            // 1. Coba halaman utama


            if (tryClickAd(closeLocators)) {

                driver.switchTo().defaultContent();

                return;
            }


            // 2. Cari iframe


            List<WebElement> frames =
                    driver.findElements(
                            By.tagName("iframe")
                    );


            // 3. Coba setiap iframe


            for (WebElement frame : frames) {

                try {

                    if (!frame.isDisplayed()) {
                        continue;
                    }

                    driver.switchTo().frame(frame);

                    if (tryClickAd(closeLocators)) {

                        driver.switchTo().defaultContent();

                        return;
                    }

                    driver.switchTo().defaultContent();

                } catch (Exception ignored) {

                    driver.switchTo().defaultContent();
                }
            }

            driver.switchTo().defaultContent();

        } catch (Exception ignored) {

            driver.switchTo().defaultContent();
        }
    }


    // Helper untuk mencoba klik popup / iklan


    private boolean tryClickAd(List<By> locators) {

        for (By locator : locators) {

            try {

                List<WebElement> elements =
                        driver.findElements(locator);

                for (WebElement element : elements) {

                    try {

                        if (!element.isDisplayed()) {
                            continue;
                        }

                        // Scroll ke element
                        ((JavascriptExecutor) driver)
                                .executeScript(
                                        "arguments[0].scrollIntoView({block:'center'});",
                                        element
                                );

                        waitAfterScroll();


                        // Coba klik normal


                        try {

                            element.click();

                        } catch (Exception e) {


                            // Fallback JavaScript


                            ((JavascriptExecutor) driver)
                                    .executeScript(
                                            "arguments[0].click();",
                                            element
                                    );
                        }

                        return true;

                    } catch (Exception ignored) {
                    }
                }

            } catch (Exception ignored) {
            }
        }

        return false;
    }
}