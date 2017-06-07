package toolkit.helpers;

import com.google.common.collect.Iterables;
import composite.IPage;
import configs.GeneralConfig;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import toolkit.driver.LocalDriverManager;
import toolkit.driver.WebDriverController;

import java.util.Arrays;


public abstract class OperationsHelper implements IPage {

    private static final Logger log = LoggerFactory.getLogger(OperationsHelper.class);
    private final WebDriverController driver = LocalDriverManager.getDriverController();
    private final WebDriverWait waitDriver = driver.getInstanceWaitDriver();
    protected static final String baseUrl = GeneralConfig.baseUrl;


    public static void logoutHook() {
        if (LocalDriverManager.getDriverController() != null) {
            LocalDriverManager.getDriverController().goToUrl(baseUrl);
            LocalDriverManager.getDriverController().deleteAllCookies();
        }

    }


    public boolean checkDimensionIsLess(int width) {
        return driver.getDimension().getWidth() <= width;
    }

    public IPage pressEnter() {
        Actions action = new Actions(driver.getDriver());
        action.sendKeys(Keys.ENTER).perform();
        return this;

    }

    private boolean isEnable(By by) {
        try {
            return driver.findElement(by).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }


    public WebElement findElement(By by) {
        waitForVisible(by);
        return driver.findElement(by);
    }


    public static String getRandomEmail() {
        long currentTime = System.nanoTime();
        String longNumber = Long.toString(currentTime);
        return "notifytest." + "1" + longNumber + "@test.ru";
    }

    public static String getRandomLogin() {
        long currentTime = System.nanoTime();
        String longNumber = String.valueOf(currentTime);
        return "login" + longNumber.substring(4, 9);
    }


    public IPage windowSetSize(Dimension windowSize) {
        WebDriver.Window window = driver.getDriver().manage().window();
        Dimension size = window.getSize();
        log.debug("Current windowSize = " + size);
        window.setSize(windowSize);
        log.debug("New windowSize = " + size);
        return this;
    }


    public IPage switchTo(String iFrame) {
        waitForNumberOfWindowsToEqual(2);
        driver.switchTo(iFrame);
        return this;
    }


    public IPage switchToOtherWindow() {
        waitForNumberOfWindowsToEqual(2);
        driver.switchToWindow(Iterables.getLast(driver.getWindowHandles()));
        return this;
    }


    public String getAlertText() {
        // Get a handle to the open alert, prompt or confirmation
        Alert alert = driver.getDriver().switchTo().alert();
        // Get the text of the alert or prompt
        return alert.getText();
    }


    public IPage clickOkInAlert() {
        // Get a handle to the open alert, prompt or confirmation
        Alert alert = driver.getDriver().switchTo().alert();
        // Get the text of the alert or prompt
        log.debug("alert: " + alert.getText());
        // And acknowledge the alert (equivalent to clicking "OK")
        alert.accept();
        return this;
    }


    private void waitForElementPresent(By by) {
        try {
            waitDriver.until((WebDriver webDriver) -> isElementPresent(by));
        } catch (TimeoutException e) {
            Assert.fail("Element is not visible after " + WebDriverController.TIMEOUT);
        }

    }

    private void waitForNumberOfWindowsToEqual(final int numberOfWindows) {
        ExpectedCondition<Boolean> expectation = driver1 -> driver1.getWindowHandles().size() == numberOfWindows;
        // Function<WebDriver, Boolean> expectation = driver1 -> driver1.getWindowHandles().size() == numberOfWindows;
        waitDriver.until(expectation);
    }


    public IPage clickCancelInAlert() {
        // Get a handle to the open alert, prompt or confirmation
        Alert alert = driver.getDriver().switchTo().alert();
        // Get the text of the alert or prompt
        log.debug("alert: " + alert.getText());
        // And acknowledge the alert (equivalent to clicking "Cancel")
        alert.dismiss();
        return this;
    }


    public IPage waitForNotAttribute(final By by, final String attribute, final String value) {
        try {

            waitDriver.until((WebDriver d) -> d.findElement(by)
                    .getAttribute(attribute)
                    .equals(value));
        } catch (TimeoutException e) {
            Assert.fail("Element attribute is not visible after " + WebDriverController.TIMEOUT);
        }
        return this;
    }


    public IPage waitForTextPresent(final String text) {
        try {
            waitDriver.until((WebDriver d) -> d.getPageSource().contains(text));
        } catch (TimeoutException e) {
            Assert.fail("Text " + text + " is not visible after " + WebDriverController.TIMEOUT);
        }
        return this;
    }


    public java.util.List<WebElement> findElements(final By by) {
        waitForElementPresent(by);
        return driver.findElements(by);
    }


    private void waitForVisible(By by) {
        try {
            waitDriver.until((WebDriver webDriver) -> isVisible(by));
        } catch (TimeoutException e) {
            Assert.fail("Element is not visible after " + WebDriverController.TIMEOUT);
        }
    }


    private void waitForNotVisible(By by) {
        try {
            waitDriver.until((WebDriver webDriver) -> !isVisible(by));
        } catch (TimeoutException e) {
            Assert.fail("Element is visible after " + WebDriverController.TIMEOUT);
        }
    }


    public String getSrcOfElement(By by) {
        waitForElementPresent(by);
        return driver.findElement(by).getAttribute("src");
    }


    /**
     * Gets the absolute URL of the current page.
     *
     * @return the absolute URL of the current page
     */

    public String getCurrentUrl() {
        return driver.getDriver().getCurrentUrl();
    }


    public IPage selectValueInDropDown(By by, String optionValue) {
        Select select = new Select(driver.findElement(by));
        select.selectByValue(optionValue);
        return this;
    }


    public IPage submit(By by) {
        log.debug("Submit:");
        waitForElementPresent(by);
        driver.findElement(by).submit();
        return this;

    }


    /**
     * Sends to API browser command back
     */

    public IPage navigateBack() {
        driver.navigationBack();
        return this;
    }


    public IPage moveToElement(By by) {
        Actions actions = new Actions(driver.getDriver());
        waitForElementPresent(by);
        actions.moveToElement(driver.findElement(by)).build().perform();
        return this;
    }


    private void highlightTheElement(By by) {
        WebElement element = driver.findElement(by);
        driver.executeScript("arguments[0].style.border='2px solid yellow'", element);
    }


    public IPage click(By by) {
        log.debug("Click on: " + by.toString());
        waitForElementPresent(by);
        highlightTheElement(by);
        driver.findElement(by).click();
        return this;

    }


    public IPage clickWithJs(By by) {
        log.debug("Click on: " + by.toString());
        waitForVisible(by);
        highlightTheElement(by);
        driver.executeScript("arguments[0].click();", driver.findElement(by));
        return this;
    }


    public IPage actionClick(By by) {
        log.debug("Click on: " + by.toString());
        waitForVisible(by);
        highlightTheElement(by);
        new Actions(driver.getDriver()).click(driver.findElement(by)).build().perform();
        return this;
    }


    public final void assertThat(Runnable... assertions) {
        Arrays.asList(assertions).forEach(Runnable::run);

    }


    public String getText(By by) {
        log.debug("Text from: " + by.toString());
        waitForVisible(by);
        return driver.findElement(by).getText();
    }


    public String getAttribute(By by, String nameAttribute) {
        log.debug("Text from: " + by.toString());
        waitForVisible(by);
        return driver.findElement(by).getAttribute(nameAttribute);
    }


    public String getPageSource() {
        return driver.getDriver().getPageSource();
    }


    public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }


    private boolean isVisible(By by) {
        try {
            return driver.findElement(by).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }


    protected void type(By by, String someText) {
        log.debug("Type:" + someText + " to:" + by.toString());
        waitForVisible(by);
        highlightTheElement(by);
        driver.findElement(by).clear();
        driver.findElement(by).sendKeys(someText);
    }


    /**
     * Open page
     */

    public IPage openUrl(String url) {
        log.info("Open page: " + url);
        driver.get(url);
        return this;
    }


    public IPage openTab(String url) {
        String script = "var d=document,a=d.createElement('a');a.target='_blank';a.href='%s';a.innerHTML='.';d.body.appendChild(a);return a";
        Object element = driver.executeScript(String.format(script, url));
        if (element instanceof WebElement) {
            WebElement anchor = (WebElement) element;
            anchor.click();
            driver.executeScript("var a=arguments[0];a.parentNode.removeChild(a);",
                    anchor);
        } else {
            throw new RuntimeException("Unable to open tab");
        }
        return this;
    }


    public boolean validateElementPresent(By by) {
        try {
            waitForElementPresent(by);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }


    public boolean validateElementIsNotVisible(By by) {
        try {
            waitForNotVisible(by);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }


    public boolean validateElementVisible(By by) {
        try {
            waitForVisible(by);
            return true;
        } catch (AssertionError e) {
            return false;
        }

    }


    public boolean validateUrlContains(String s) {
        for (int i = 0; i < WebDriverController.TIMEOUT; i++) {
            if (driver.getDriver().getCurrentUrl().contains(s))
                return true;
            else sendPause(1);
        }
        return false;
    }


    public boolean validateTextEquals(By by, String text) {
        try {
            waitForVisible(by);
        } catch (AssertionError ignored) {
        }
        return getText(by).equals(text);

    }


    public boolean validateElementAttributeContains(By by, String nameAttribute, String containsValue) {
        try {
            waitDriver.until((WebDriver webDriver) -> {
                boolean result = false;
                try {
                    result = getAttribute(by, nameAttribute).contains(containsValue);
                } catch (StaleElementReferenceException ignored) {
                }
                return result;
            });
            return true;
        } catch (TimeoutException | AssertionError e) {
            return false;
        }

    }


    public boolean validateElementAttributeNotEmpty(By by, String nameAttribute) {
        try {
            waitDriver.until((WebDriver webDriver) -> {
                boolean result = false;
                try {
                    result = !getAttribute(by, nameAttribute).isEmpty();
                } catch (StaleElementReferenceException ignored) {
                }
                return result;
            });
            return true;
        } catch (AssertionError e) {
            return false;
        }

    }

    public boolean validateElementHasText(By by) {
        try {
            waitDriver.until((WebDriver webDriver) -> !getText(by).equals(""));
            return true;
        } catch (TimeoutException e) {
            return false;
        }

    }


    public boolean validateElementHasText(By by, String text) {
        try {
            waitDriver.until((WebDriver webDriver) -> getText(by).contains(text));
            return true;
        } catch (TimeoutException e) {
            return false;
        } catch (StaleElementReferenceException e) {
            return validateElementHasText(by, text);
        }

    }


    /**
     * Reloads page
     */

    public IPage refreshPage() {
        driver.refresh();
        return this;
    }


    // Set a cookie

    public IPage addCookie(String key, String value) {
        driver.addCookie(key, value);
        return this;
    }


    public IPage hoverOn(By by) {
        new Actions(driver.getDriver()).moveToElement(findElement(by)).build().perform();
        log.info("Action - hover on to locator: " + by.toString());
        return this;
    }


    public IPage scrollOnTop() {
        driver.executeScript("window.scrollTo(0,0)");
        return this;
    }

    public static void sendPause(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException iex) {
            Thread.interrupted();
        }
    }

    /**
     * Returns count of elements on a page with this locator
     */

    public int getCountElements(By by) {
        waitForElementPresent(by);
        return driver.findElements(by).size();
    }


}
