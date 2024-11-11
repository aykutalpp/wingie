package com.enUygun.step;

import com.enUygun.base.StoreHelper;
import com.enUygun.base.BaseTest;
import com.enUygun.model.ElementInfo;
import com.thoughtworks.gauge.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class BaseSteps extends BaseTest {

    public BaseSteps() {
    }

    public ElementInfo findElementInfoByKey(String key) {
        return (ElementInfo) StoreHelper.INSTANCE.elementMapList.get(key);
    }

    public WebElement findElement(String key) {
        By infoParam = getElementInfoToBy(findElementInfoByKey(key));

        WebElement webElement = webFluentWait
                .until(ExpectedConditions.visibilityOfElementLocated(infoParam));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;
    }
    List<WebElement> findElements(String key) {
        By infoParam = getElementInfoToBy(findElementInfoByKey(key));
        int attempts = 0;
        List<WebElement> elements = null;

        while (attempts < 3) {
            try {
                webFluentWait.until(ExpectedConditions.visibilityOfElementLocated(infoParam));
                elements = driver.findElements(infoParam);
                if (!elements.isEmpty()) {
                    return elements;
                }
            } catch (StaleElementReferenceException e) {
                attempts++;
                logger.warn("StaleElementReferenceException encountered. Retrying... Attempt " + attempts);
                try {
                    Thread.sleep(1000);  // Wait for a second before retrying
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();  // Handle interruption
                }
            } catch (Exception e) {
                logger.error("An error occurred while trying to find elements: " + e.getMessage());
                break;
            }
        }

        // If no elements were found after the retries
        logger.error("Failed to find elements after " + attempts + " attempts.");
        return elements;  // Returning null or empty list, depending on your needs
    }



    public By getElementInfoToBy(ElementInfo elementInfo) {
        By by = null;
        if (elementInfo.getType().equals("css")) {
            by = By.cssSelector(elementInfo.getValue());
        } else if (elementInfo.getType().equals(("name"))) {
            by = By.name(elementInfo.getValue());
        } else if (elementInfo.getType().equals("id")) {
            by = By.id(elementInfo.getValue());
        } else if (elementInfo.getType().equals("xpath")) {
            by = By.xpath(elementInfo.getValue());
        } else if (elementInfo.getType().equals("linkText")) {
            by = By.linkText(elementInfo.getValue());
        } else if (elementInfo.getType().equals(("partialLinkText"))) {
            by = By.partialLinkText(elementInfo.getValue());
        }
        return by;
    }

    private void clickElement(WebElement element) {
        int retryCount = 3;
        int attempts = 0;

        while (attempts < retryCount) {
            try {
                webFluentWait.until(ExpectedConditions.elementToBeClickable(element));
                element.click();
                return;
            } catch (ElementClickInterceptedException e) {
                attempts++;
                logger.info("Element is intercepted, attempt " + attempts + " of " + retryCount);

                if (attempts >= retryCount) {
                    logger.error("Failed to click the element after " + retryCount + " attempts.");
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            } catch (Exception e) {
                logger.error("Failed to click the element: " + e.getMessage());
                break;
            }
        }
    }



    @Step({"Click on <key> element"})
    public void clickElement(String key) {
        if (!key.isEmpty()) {
            scrollToElementToBeVisible(key);
            clickElement(findElement(key));

            logger.info(key + " element clicked.");
        }
    }
    @Step({"Click on element with attribute <attribute> and value <value>"})
    public void clickDynamicElement(String attribute, String value) {
        String cssSelector = String.format("[%s='%s']", attribute, value);
        WebElement element = webFluentWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
        element.click();
        logger.info(String.format("Clicked on the element with %s='%s'", attribute, value));
    }


    public WebElement scrollToElementToBeVisible(String key) {
        By infoParam = getElementInfoToBy(findElementInfoByKey(key));
        WebElement webElement = webFluentWait.until(ExpectedConditions.visibilityOfElementLocated(infoParam));
        if (webElement != null) {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                    webElement);
            logger.info(key + " element found and scrolled into view.");
        }

        return webElement;
    }

    @Step({"Go to <url> address"})
    public void goToUrl(String url) {
        driver.get(url);
        logger.info("Navigating to the address: " + url);
    }
    @Step({"Check current url starts with <expectedUrl> address"})
    public void checkUrl(String expectedUrl) {
        String currentUrl = driver.getCurrentUrl();

        if (!currentUrl.startsWith(expectedUrl)) {
            logger.error("Expected URL to start with: " + expectedUrl + " but got: " + currentUrl);
            throw new AssertionError("The current URL does not start with the expected URL.");
        } else {
            logger.info("Current URL is as expected: " + currentUrl);
        }
    }


    @Step({"Write value <text> to element <key>"})
    public void sendKeys(String text, String key) {
        WebElement textbox = findElement(key);
        textbox.click();
        textbox.sendKeys(text);
        logger.info("Text \"" + text + "\" has been entered into the element with key: " + key);
    }

    @Step({"Change the <key> element's <attribute> attribute's value to <value> with JS"})
    public void setElementAttributeValue(String key, String attribute, String value) {
        try {
            WebElement element = findElement(key);

            String currentValue = element.getAttribute(attribute);
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].setAttribute(arguments[1], arguments[2]);",
                    element, attribute, value);

            ((JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new Event('input'));", element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new Event('change'));", element);


            logger.info("Changed the '" + attribute + "' attribute of the element with key '" + key +
                    "' from current value: '" + currentValue + "' to new value: '" + value + "'");

        } catch (Exception e) {
            logger.error("Failed to change the attribute value for element with key '" + key + "'", e);
            throw new RuntimeException("Failed to change the attribute value for element: " + key, e);
        }
    }
    @Step({"Send <button> key to element <key>"})
    public void sendKeysButtonToElement(String button, String key) {
        try {
            Keys keyToSend = Keys.valueOf(button);
            findElement(key).sendKeys(keyToSend);
            logger.info(button + " key sent to element " + key);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid key: " + button);
            throw new RuntimeException("Invalid key: " + button, e);
        }
    }

    @Step({"Check if the <key> element is visible"})
    public boolean doesElementExistByKey(String key) {
        try {
            By infoParam = getElementInfoToBy(findElementInfoByKey(key));
            webFluentWait.until(ExpectedConditions.visibilityOfElementLocated(infoParam));
            logger.info(key + " element is visible.");
            return true;
        } catch (Exception e) {
            logger.error("Error while waiting before retrying: ", e);
            return false;
        }
    }
    @Step({"Check if the <key> element's texts between <startTime> and <endTime>"})
    public void checkElementsTextBetween(String key, int startTime, int endTime) {
        List<WebElement> departureTimes = findElements(key);
        for (WebElement departureTime : departureTimes) {
            String timeText = departureTime.getText();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime time = LocalTime.parse(timeText, formatter);

            LocalTime getStartTime = LocalTime.of(startTime, 0);
            LocalTime getEndTime = LocalTime.of(endTime, 0);

            if ((time.isAfter(getStartTime) || time.equals(getStartTime)) &&
                    (time.isBefore(getEndTime) || time.equals(getEndTime))) {
                logger.info("The departure time " + timeText + " is between " + startTime + ":00 and " + endTime + ":00.");
            } else {
                throw new AssertionError("The departure time " + timeText + " is not between " + startTime + ":00 and " + endTime + ":00.");
            }
        }
    }
    @Step({"Assert that the <key> element's text's are in decreasing order"})
    public void checkElementsInOrder(String key) {
        List<WebElement> elements = findElements(key);
        double previousValue = 0;
        for (WebElement element : elements) {
            String text = element.getText().replaceAll("[^\\d.,]", "");

            double currentValue = parseValue(text);
            if (currentValue < previousValue) {
                throw new AssertionError("The element with value " + currentValue + " is not in decreasing order. Previous value: " + previousValue);
            }
            logger.info(previousValue+" is less or equals "+currentValue );
            previousValue = currentValue;
        }

        logger.info("All elements are in decreasing order.");
    }

    private double parseValue(String value) {
        value = value.replace(",", ".");
        return Double.parseDouble(value);
    }


    @Step("Wait for <seconds> seconds")
    public void waitBySecond(int seconds) {
        logger.info(" Waiting for "+seconds+" seconds");
        try {
            Thread.sleep(1000 * seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Step({"Assert that the <key> element's text's are <expectedText>"})
    public void assertElementsText(String key, String expectedText) {
        List<WebElement> airlineNames = findElements(key);

        for (WebElement airlineName : airlineNames) {
            String actualText = airlineName.getText().trim();
            if (!actualText.equals(expectedText)) {
                throw new AssertionError("Expected text '"
                        + expectedText + "' but found '" + actualText + "' for element with key: " + key);
            }
        }

        logger.info("All elements with the key '" + key + "' have the expected text: '" + expectedText + "'");
    }
    @Step({"Assert that the <key> element is disappeared"})
    public void waitForLoaderToDisappear(String key) {
        By infoParam = getElementInfoToBy(findElementInfoByKey(key));
        webFluentWait.until(driver -> {
            try {
                WebElement loader = driver.findElement(infoParam);
                return !loader.isDisplayed();
            } catch (NoSuchElementException e) {
                return true;
            }
        });

        logger.info("Loader has disappeared.");
    }

}




