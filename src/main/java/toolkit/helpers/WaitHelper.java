package toolkit.helpers;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import toolkit.driver.WebDriverController;

import java.util.function.Function;

public class WaitHelper {


    private final WebDriverController driver;
    private final int timeout;

    public WaitHelper(WebDriverController driver) {
        this.driver = driver;
        timeout = driver.getTimeout();
    }


    public void waitForNumberOfWindowsIsMore(final int numberOfWindows) {
        driver.getInstanceWaitDriver().until(driver1 -> driver.getDriver().getWindowHandles().size() >= numberOfWindows);
    }

    public void waitForPageLoaded() {
        Function<WebDriver, Boolean> expectation = driver1 -> driver.executeScript("return document.readyState").toString().equals("complete");
        try {
            driver.getInstanceWaitDriver().until(expectation);
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for Page Load Request to complete.");
        }
    }


    public void waitForNotAttribute(final By by, final String attribute, final String value) {
        try {

            driver.getInstanceWaitDriver().until((WebDriver d) -> d.findElement(by)
                    .getAttribute(attribute)
                    .equals(value));
        } catch (TimeoutException e) {
            Assert.fail("Element attribute is not visible after " + timeout);
        }

    }


    public void waitForTextPresent(final String text) {
        try {
            driver.getInstanceWaitDriver().until((WebDriver d) -> d.getPageSource().contains(text));
        } catch (TimeoutException e) {
            Assert.fail("Element is not visible after " + timeout);
        }
    }


    public void waitForVisible(By by) {
        try {
            driver.getInstanceWaitDriver().until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (WebDriverException e) {
            Assert.fail("Element " + by.toString() + " is not visible after " + timeout);
        }
    }

    public void waitForVisible(WebElement webElement) {
        try {
            driver.getInstanceWaitDriver().until(ExpectedConditions.visibilityOf(webElement));
        } catch (TimeoutException e) {
            Assert.fail("Element " + webElement.toString() + " is not visible after " + timeout);
        }
    }


    public void waitForElementPresent(By by) {
        try {
            driver.getInstanceWaitDriver().until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (WebDriverException e) {
            Assert.fail("Element is not visible after " + timeout);
        }

    }

    public void waitForNotVisible(By by) {
        try {
            driver.getInstanceWaitDriver().until(ExpectedConditions.invisibilityOfElementLocated(by));
        } catch (WebDriverException e) {
            Assert.fail("Element is visible after " + timeout);
        }
    }
}
