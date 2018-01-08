package toolkit.helpers;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import toolkit.driver.LocalDriverManager;
import toolkit.driver.WebDriverController;

import java.util.Objects;

public class WaitHelper {
    private final WebDriverWait waitDriver = LocalDriverManager.getDriverController().getInstanceWaitDriver();


    WebDriverWait getWaitDriver() {
        return waitDriver;
    }


    void waitForElementPresent(By locator) {
        try {
            waitDriver.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            Assert.fail("Element is not visible after " + WebDriverController.TIMEOUT);
        }
    }

    void waitForTextPresent(final String text) {
        try {
            waitDriver.until((WebDriver d) -> d.getPageSource().contains(text));
        } catch (TimeoutException e) {
            Assert.fail("Text " + text + " is not visible after " + WebDriverController.TIMEOUT);
        }
    }


    void waitForVisible(By locator) {
        try {
            waitDriver.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        } catch (TimeoutException e) {
            Assert.fail("Element is not visible after " + WebDriverController.TIMEOUT);
        }
    }


    void waitForNotVisible(By locator) {
        try {
            waitDriver.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            Assert.fail("Element is visible after " + WebDriverController.TIMEOUT);
        }
    }

    void waitForNumberOfWindowsToEqual(final int numberOfWindows) {
        ExpectedCondition<Boolean> expectation = driver1 -> Objects.requireNonNull(driver1).getWindowHandles().size() == numberOfWindows;
        // Function<WebDriver, Boolean> expectation = driver1 -> driver1.getWindowHandles().size() == numberOfWindows;
        waitDriver.until(expectation);
    }

    void waitForNotAttribute(final By by, final String attribute, final String value) {
        try {

            waitDriver.until((WebDriver d) -> d.findElement(by)
                    .getAttribute(attribute)
                    .equals(value));
        } catch (TimeoutException e) {
            Assert.fail("Element attribute is not visible after " + WebDriverController.TIMEOUT);
        }
    }





}
