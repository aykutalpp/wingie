package com.enUygun.base;

import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import com.thoughtworks.gauge.BeforeStep;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class BaseTest {
    protected static Wait<WebDriver> webFluentWait;

    protected static WebDriver driver;
    protected static Actions actions;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    ChromeOptions chromeOptions;
    String browserName = "chrome";
    String osName = System.getProperty("os.name").toLowerCase();
    int sleepTime = 1;

    @BeforeScenario
    public void setUp() {
        logger.info("************************************  BeforeScenario  ************************************");
        try {
            // Initialize WebDriver and StoreHelper before FluentWait
            if (StringUtils.isEmpty(System.getenv("key"))) {
                logger.info("Test will run on the local device with " + osName + " operating system in " + browserName + " browser.");
                logger.info("Operating system: " + osName);

                // Initialize WebDriver based on OS
                if (osName.contains("win")) {
                    if ("chrome".equalsIgnoreCase(browserName)) {
                        System.setProperty("webdriver.chrome.driver", "webdriver/chromedriver.exe");
                        driver = new ChromeDriver(chromeOptions());
                        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
                    }
                } else if (osName.contains("mac")) {
                    if ("chrome".equalsIgnoreCase(browserName)) {
                        System.setProperty("webdriver.chrome.driver", "webdriver/chromedriver");
                        driver = new ChromeDriver(chromeOptions());
                        driver.manage().deleteAllCookies();
                        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
                    }
                    actions = new Actions(driver);
                }
            }
            webFluentWait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(30)) // Maksimum bekleme süresi
                    .pollingEvery(Duration.ofSeconds(2)) // Kontrol sıklığı
                    .ignoring(Exception.class);


        } catch (Exception e) {
            logger.error("Error setting up WebDriver: ", e);
            throw new RuntimeException("Failed to initialize WebDriver", e);
        }
    }

    @AfterScenario
    public void tearDown() {
        logger.info("************************************  AfterScenario  ************************************");
        if (driver != null) {
            driver.quit();
        }
    }

    public ChromeOptions chromeOptions() {
        chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--kiosk");
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("--start-fullscreen");
        chromeOptions.addArguments("--ignore-certificate-errors");
        chromeOptions.addArguments("--clear-browsing-data");
        return chromeOptions;
    }
}
