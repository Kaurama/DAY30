package core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.util.Properties;

public class BaseTest {

    protected static Properties config;

    private static final Logger logger =
            LoggerFactory.getLogger(BaseTest.class);

    @BeforeSuite(alwaysRun = true)
    public void loadConfig() {

        String env = System.getProperty("env");

        env = (env == null || env.isEmpty())
                ? "staging"
                : env;

        config = ConfigManager.loadProperties(env);

        logger.info("Configuration loaded for environment: {}", env);
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) {

        logger.info("Starting test with browser: {}", browser);

        DriverManager.initDriver(browser);

        DriverManager.getDriver()
                .manage()
                .window()
                .maximize();

        String baseUrl = config.getProperty("baseUrl");

        logger.info("Opening URL: {}", baseUrl);

        DriverManager.getDriver().get(baseUrl);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        logger.info("Closing browser");

        DriverManager.quitDriver();
    }
}

