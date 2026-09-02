package core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class DriverManager {

    private static final Logger logger =
            LoggerFactory.getLogger(DriverManager.class);

    private static final ThreadLocal<WebDriver> driver =
            new ThreadLocal<>();

    public static void initDriver(String browser) {

        WebDriver webDriver = null;

        logger.info("Initializing browser: {}", browser);

        switch (browser.toLowerCase()) {

            case "chrome" -> {

                String githubActions =
                        System.getenv("GITHUB_ACTIONS");

                boolean isCI =
                        githubActions != null &&
                                githubActions.equals("true");

                logger.info("Running in CI: {}", isCI);

                if (!isCI) {

                    logger.info(
                            "Setting up ChromeDriver via WebDriverManager"
                    );

                    WebDriverManager.chromedriver().setup();

                } else {

                    logger.info(
                            "Using pre-installed ChromeDriver from CI"
                    );

                    String chromeDriverPath =
                            System.getenv("CHROMEDRIVER_PATH");

                    if (chromeDriverPath != null
                            && !chromeDriverPath.isEmpty()) {

                        System.setProperty(
                                "webdriver.chrome.driver",
                                chromeDriverPath
                        );

                        logger.info(
                                "ChromeDriver path: {}",
                                chromeDriverPath
                        );
                    }
                }

                ChromeOptions options =
                        new ChromeOptions();

                Map<String, Object> prefs =
                        new HashMap<>();

                prefs.put(
                        "credentials_enable_service",
                        false
                );

                prefs.put(
                        "profile.password_manager_enabled",
                        false
                );

                prefs.put(
                        "profile.password_manager_leak_detection",
                        false
                );

                options.setExperimentalOption(
                        "prefs",
                        prefs
                );

                options.addArguments(
                        "--disable-blink-features=AutomationControlled"
                );

                options.addArguments(
                        "--disable-extensions"
                );

                options.addArguments(
                        "--disable-plugins"
                );

                options.setExperimentalOption(
                        "useAutomationExtension",
                        false
                );

                options.setExperimentalOption(
                        "excludeSwitches",
                        new String[]{"enable-automation"}
                );

                if (isCI) {

                    logger.info(
                            "Configuring Chrome for CI"
                    );

                    options.addArguments(
                            "--headless=new"
                    );

                    options.addArguments(
                            "--disable-gpu"
                    );

                    options.addArguments(
                            "--window-size=1920,1080"
                    );

                    options.addArguments(
                            "--no-sandbox"
                    );

                    options.addArguments(
                            "--disable-dev-shm-usage"
                    );
                }

                try {

                    logger.info(
                            "Creating ChromeDriver instance"
                    );

                    webDriver =
                            new ChromeDriver(options);

                    logger.info(
                            "ChromeDriver created successfully"
                    );

                } catch (Exception e) {

                    logger.error(
                            "Failed to create ChromeDriver",
                            e
                    );

                    throw e;
                }
            }

            case "firefox" -> {

                logger.info(
                        "Setting up FirefoxDriver"
                );

                WebDriverManager.firefoxdriver().setup();

                webDriver =
                        new FirefoxDriver();

                logger.info(
                        "FirefoxDriver created successfully"
                );
            }

            default -> {

                logger.error(
                        "Unsupported browser: {}",
                        browser
                );

                throw new IllegalArgumentException(
                        "Browser tidak didukung: " + browser
                );
            }
        }

        driver.set(webDriver);
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {

        if (driver.get() != null) {

            logger.info("Quitting browser");

            driver.get().quit();

            driver.remove();

            logger.info(
                    "Browser closed successfully"
            );
        }
    }
}
