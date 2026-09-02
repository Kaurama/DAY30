package test;

import bookstore.CheckOutPage;
import bookstore.HomePage;
import bookstore.LoginPage;
import core.BaseTest;
import core.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

public class CheckOutPageTest extends BaseTest {

    private static final Logger logger =
            LogManager.getLogger(CheckOutPageTest.class);

    @Test(
            priority = 2,
            groups = {"smoke"},
            description = "Test checkout from homepage to purchase"
    )
    public void testCheckout() {

        logger.info("Memulai test checkout");


        // 1. Homepage


        HomePage homePage =
                new HomePage(DriverManager.getDriver());

        logger.info("Check iklan di Homepage");
        homePage.closeAdIfPresent();

        logger.info("Sort produk dari harga terendah");

        homePage.closeAdIfPresent();
        homePage.sortLowToHigh();


        // 2. Setelah sorting


        logger.info("Check iklan setelah sorting");
        homePage.closeAdIfPresent();

        logger.info(
                "Menambahkan produk dengan harga terendah ke cart"
        );

        homePage.closeAdIfPresent();
        homePage.addLowestPriceProductToCart();


        // 3. Setelah add to cart


        logger.info("Check iklan setelah add to cart");
        homePage.closeAdIfPresent();

        logger.info("Membuka Shopping Cart");

        homePage.closeAdIfPresent();
        homePage.openCart();


        // 4. Shopping Cart → Login


        LoginPage loginPage =
                new LoginPage(DriverManager.getDriver());

        logger.info("Check iklan di Shopping Cart");
        loginPage.closeAdIfPresent();

        logger.info("Login untuk melanjutkan checkout");

        loginPage.closeAdIfPresent();

        loginPage.login(
                config.getProperty("email"),
                config.getProperty("password")
        );


        // 5. Setelah Login → Checkout Page


        CheckOutPage checkOutPage =
                new CheckOutPage(DriverManager.getDriver());

        logger.info("Check iklan setelah login");
        checkOutPage.closeAdIfPresent();

        logger.info("Check iklan di Checkout Page");
        checkOutPage.closeAdIfPresent();


        // 6. Isi Checkout Form


        logger.info("Mengisi form checkout");

        checkOutPage.closeAdIfPresent();

        checkOutPage.checkout(
                config.getProperty("name"),
                config.getProperty("address"),
                config.getProperty("card-name"),
                config.getProperty("card-number"),
                config.getProperty("card-expiry-month"),
                config.getProperty("card-expiry-year"),
                config.getProperty("card-cvc")
        );


        // 7. Sebelum Purchase


        logger.info("Check iklan sebelum klik Purchase");
        checkOutPage.closeAdIfPresent();

        logger.info("Klik tombol Purchase");

        checkOutPage.clickPurchase();


        // 8. Setelah Purchase


        logger.info("Check iklan setelah Purchase");
        checkOutPage.closeAdIfPresent();

        logger.info("Test checkout selesai");
    }
}