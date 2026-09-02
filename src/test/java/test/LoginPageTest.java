package test;

import bookstore.HomePage;
import bookstore.LoginPage;
import core.BaseTest;
import core.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

public class LoginPageTest extends BaseTest {

    private static final Logger logger =
            LogManager.getLogger(LoginPageTest.class);

    @Test(
            priority = 1,
            groups = {"smoke"},
            description = "Test checkout and login from shopping cart"
    )
    public void testLogin() {

        logger.info("Memulai test checkout dan login");

        HomePage homePage =
                new HomePage(DriverManager.getDriver());


        // 1. Homepage


        logger.info("Check iklan di Homepage");
        homePage.closeAdIfPresent();

        logger.info("Sort produk dari harga terendah");
        homePage.sortLowToHigh();


        // 2. Setelah sorting


        logger.info("Check iklan setelah sorting");
        homePage.closeAdIfPresent();

        logger.info(
                "Menambahkan produk dengan harga terendah ke cart"
        );

        homePage.addLowestPriceProductToCart();


        // 3. Setelah add to cart


        logger.info("Check iklan setelah add to cart");
        homePage.closeAdIfPresent();

        logger.info("Membuka Shopping Cart");
        homePage.openCart();


        // 4. Shopping Cart


        logger.info("Check iklan di Shopping Cart");

        LoginPage loginPage =
                new LoginPage(DriverManager.getDriver());

        loginPage.closeAdIfPresent();


        // 5. Login


        logger.info("Melakukan login");

        loginPage.login(
                config.getProperty("email"),
                config.getProperty("password")
        );


        // 6. Setelah login


        logger.info("Check iklan setelah login");

        loginPage.closeAdIfPresent();

        logger.info("Test login selesai");
    }
}