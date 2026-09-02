package test;

import bookstore.HomePage;
import core.BaseTest;
import core.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class HomePageTest extends BaseTest {

    private static final Logger logger =
            LogManager.getLogger(HomePageTest.class);

    @Test(
            priority = 1,
            groups = {"smoke"},
            description = "User verify homepage"
    )
    public void verifyUserOnHomepage() {

        logger.info("Memulai test verify homepage");

        HomePage homePage =
                new HomePage(DriverManager.getDriver());

        // Check popup
        homePage.closeAdIfPresent();

        Assert.assertTrue(
                homePage.isOnHomePage(),
                "User should be on Books List homepage"
        );
    }


    @Test(
            priority = 2,
            groups = {"smoke"},
            description = "User sort products from low price to high price"
    )
    public void testSortLowToHigh() {

        logger.info("Memulai test sort price low to high");

        HomePage homePage =
                new HomePage(DriverManager.getDriver());

        // Check popup sebelum sort
        homePage.closeAdIfPresent();

        logger.info(
                "Scroll ke Price, hover, kemudian pilih ASC"
        );

        homePage.sortLowToHigh();

        // Check popup setelah sort
        homePage.closeAdIfPresent();

        List<Double> prices =
                homePage.getProductPrices();

        logger.info(
                "Harga produk setelah ASC: {}",
                prices
        );

        for (int i = 0; i < prices.size() - 1; i++) {

            Assert.assertTrue(
                    prices.get(i) <= prices.get(i + 1),
                    "Products should be sorted from lowest price to highest price. "
                            + "Current prices: " + prices
            );
        }
    }


    @Test(
            priority = 3,
            groups = {"smoke"},
            description = "User sort product then add lowest price product to cart"
    )
    public void testAddToCart() {

        logger.info(
                "Memulai test sort kemudian add product ke cart"
        );

        HomePage homePage =
                new HomePage(DriverManager.getDriver());

        // Check popup sebelum sort
        homePage.closeAdIfPresent();

        logger.info(
                "Scroll ke Price, hover, kemudian pilih ASC"
        );

        homePage.sortLowToHigh();

        // Check popup setelah sort
        homePage.closeAdIfPresent();

        List<Double> prices =
                homePage.getProductPrices();

        logger.info(
                "Harga setelah ASC: {}",
                prices
        );

        for (int i = 0; i < prices.size() - 1; i++) {

            Assert.assertTrue(
                    prices.get(i) <= prices.get(i + 1),
                    "Products should be sorted from lowest price to highest price. "
                            + "Current prices: " + prices
            );
        }

        // Check popup sebelum add to cart
        homePage.closeAdIfPresent();

        logger.info(
                "Memilih produk pertama setelah sorting"
        );

        homePage.addLowestPriceProductToCart();

        // Check popup setelah add to cart
        homePage.closeAdIfPresent();

        logger.info(
                "Verify cart badge tampil"
        );

        Assert.assertTrue(
                homePage.isCartBadgeDisplayed(),
                "Cart badge should be displayed after adding product to cart"
        );
    }


    @Test(
            priority = 4,
            groups = {"smoke"},
            description = "User sort product, add product, then open cart"
    )
    public void testOpenCart() {

        logger.info(
                "Memulai test sort, add product, dan open cart"
        );

        HomePage homePage =
                new HomePage(DriverManager.getDriver());

        // Check popup sebelum sort
        homePage.closeAdIfPresent();

        logger.info(
                "Scroll ke Price, hover, kemudian pilih ASC"
        );

        homePage.sortLowToHigh();

        // Check popup setelah sort
        homePage.closeAdIfPresent();

        logger.info(
                "Memilih produk pertama setelah sorting"
        );

        homePage.addLowestPriceProductToCart();

        // Check popup setelah add to cart
        homePage.closeAdIfPresent();

        logger.info(
                "Membuka cart"
        );

        homePage.openCart();

        // Check popup setelah masuk cart
        homePage.closeAdIfPresent();

        logger.info(
                "Verify user berhasil masuk ke cart"
        );

        Assert.assertTrue(
                DriverManager.getDriver()
                        .getCurrentUrl()
                        .contains("/bookstore/cart"),
                "User should be redirected to cart page"
        );
    }
}