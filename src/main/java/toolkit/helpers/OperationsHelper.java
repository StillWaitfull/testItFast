package toolkit.helpers;

import com.google.common.collect.Iterables;
import composite.IPage;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import toolkit.CheckingDifferentImages;
import toolkit.driver.LocalDriverManager;
import toolkit.driver.WebDriverController;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static toolkit.helpers.Context.stageConfig;


public abstract class OperationsHelper implements IPage {

    private static Logger log = LoggerFactory.getLogger(OperationsHelper.class);
    private WebDriverController driver = LocalDriverManager.getDriverController();
    private WebDriverWait waitDriver = driver.getInstanceWaitDriver();
    protected static String baseUrl;


    static {
        String envBaseUrl = System.getenv("baseUrl");
        if (envBaseUrl == null)
            baseUrl = stageConfig.BASE_URL;
    }


    public static void logoutHook() {
        if (LocalDriverManager.getDriverController() != null) {
            LocalDriverManager.getDriverController().goToUrl(baseUrl);
            LocalDriverManager.getDriverController().deleteAllCookies();
        }

    }


    @Override
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

    @Override
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

    @Override
    public IPage windowSetSize(Dimension windowSize) {
        WebDriver.Window window = driver.getDriver().manage().window();
        Dimension size = window.getSize();
        log.debug("Current windowSize = " + size);
        window.setSize(windowSize);
        log.debug("New windowSize = " + size);
        return this;
    }

    @Override
    public IPage switchTo(String iFrame) {
        waitForNumberOfWindowsToEqual(2);
        driver.switchTo(iFrame);
        return this;
    }

    @Override
    public IPage switchToOtherWindow() {
        waitForNumberOfWindowsToEqual(2);
        driver.switchToWindow(Iterables.getLast(driver.getWindowHandles()));
        return this;
    }

    @Override
    public String getAlertText() {
        // Get a handle to the open alert, prompt or confirmation
        Alert alert = driver.getDriver().switchTo().alert();
        // Get the text of the alert or prompt
        return alert.getText();
    }

    @Override
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
        waitDriver.until((ExpectedCondition<Boolean>) driver1 -> driver1.getWindowHandles().size() == numberOfWindows);
    }

    @Override
    public IPage clickOnStalenessElement(final By by) {
        try {
            waitDriver.until((WebDriver webDriver) -> {
                try {
                    final WebElement element = webDriver.findElement(by);
                    if (element != null && element.isDisplayed() && element.isEnabled()) {
                        element.click();
                        return element;
                    }
                } catch (StaleElementReferenceException e) {
                    log.error("Stale exception");
                }
                return null;
            });

        } catch (TimeoutException e) {
            Assert.fail("Element is not visible after " + WebDriverController.TIMEOUT + " on page " + getCurrentUrl());
        }
        return this;
    }

    @Override
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


    @Override
    public java.util.List<WebElement> findElements(final By by) {
        waitForElementPresent(by);
        return driver.findElements(by);
    }


    public void waitForVisible(By by) {
        try {
            waitDriver.until((WebDriver webDriver) -> isVisible(by));
        } catch (TimeoutException e) {
            Assert.fail("Element is not visible after " + WebDriverController.TIMEOUT);
        }
    }


    public void waitForNotVisible(By by) {
        try {
            waitDriver.until((WebDriver webDriver) -> !isVisible(by));
        } catch (TimeoutException e) {
            Assert.fail("Element is visible after " + WebDriverController.TIMEOUT);
        }
    }

    @Override
    public String getSrcOfElement(By by) {
        waitForElementPresent(by);
        return driver.findElement(by).getAttribute("src");
    }


    /**
     * Gets the absolute URL of the current page.
     *
     * @return the absolute URL of the current page
     */
    @Override
    public String getCurrentUrl() {
        return driver.getDriver().getCurrentUrl();
    }

    @Override
    public IPage selectValueInDropDown(By by, String optionValue) {
        Select select = new Select(driver.findElement(by));
        select.selectByValue(optionValue);
        return this;
    }


    @Override
    public IPage submit(By by) {
        log.debug("Submit:");
        waitForElementPresent(by);
        driver.findElement(by).submit();
        return this;

    }


    /**
     * Sends to API browser command back
     */
    @Override
    public IPage navigateBack() {
        driver.navigationBack();
        return this;
    }


    @Override
    public IPage moveToElement(By by) {
        Actions actions = new Actions(driver.getDriver());
        waitForElementPresent(by);
        actions.moveToElement(driver.findElement(by)).build().perform();
        return this;
    }


    @Override
    public void highlightTheElement(By by) {
        WebElement element = driver.findElement(by);
        driver.executeScript("arguments[0].style.border='2px solid yellow'", element);
    }


    @Override
    public IPage click(By by) {
        log.debug("Click on: " + by.toString());
        waitForElementPresent(by);
        highlightTheElement(by);
        driver.findElement(by).click();
        return this;

    }

    @Override
    public IPage clickWithJs(By by) {
        log.debug("Click on: " + by.toString());
        waitForVisible(by);
        highlightTheElement(by);
        driver.executeScript("arguments[0].click();", driver.findElement(by));
        return this;
    }

    @Override
    public IPage actionClick(By by) {
        log.debug("Click on: " + by.toString());
        waitForVisible(by);
        highlightTheElement(by);
        new Actions(driver.getDriver()).click(driver.findElement(by)).build().perform();
        return this;
    }


    @Override
    public final void assertThat(Runnable... assertions) {
        Arrays.asList(assertions).forEach(Runnable::run);

    }


    @Override
    public String getText(By by) {
        log.debug("Text from: " + by.toString());
        waitForVisible(by);
        return driver.findElement(by).getText();
    }


    @Override
    public String getAttribute(By by, String nameAttribute) {
        log.debug("Text from: " + by.toString());
        waitForVisible(by);
        return driver.findElement(by).getAttribute(nameAttribute);
    }


    @Override
    public String getPageSource() {
        return driver.getDriver().getPageSource();
    }


    @Override
    public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }


    @Override
    public boolean isVisible(By by) {
        try {
            return driver.findElement(by).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }


    @Override
    public IPage type(By by, String someText) {
        log.debug("Type:" + someText + " to:" + by.toString());
        waitForVisible(by);
        highlightTheElement(by);
        driver.findElement(by).clear();
        driver.findElement(by).sendKeys(someText);
        return this;
    }


    /**
     * Open page
     */
    @Override
    public IPage openUrl(String url) {
        log.info("Open page: " + url);
        driver.get(url);
        return this;
    }


    @Override
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


    @Override
    public boolean validateElementPresent(By by) {
        try {
            waitForElementPresent(by);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }


    @Override
    public boolean validateElementIsNotVisible(By by) {
        try {
            waitForNotVisible(by);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }


    @Override
    public boolean validateElementVisible(By by) {
        try {
            waitForVisible(by);
            return true;
        } catch (AssertionError e) {
            return false;
        }

    }

    @Override
    public boolean validateUrlContains(String s) {
        for (int i = 0; i < WebDriverController.TIMEOUT; i++) {
            if (driver.getDriver().getCurrentUrl().contains(s))
                return true;
            else sendPause(1);
        }
        return false;
    }


    @Override
    public boolean validateTextEquals(By by, String text) {
        try {
            waitForVisible(by);
        } catch (AssertionError ignored) {
        }
        return getText(by).equals(text);

    }


    /**
     * Reloads page
     */
    @Override
    public IPage refreshPage() {
        driver.refresh();
        return this;
    }


    // Set a cookie
    @Override
    public IPage addCookie(String key, String value) {
        driver.addCookie(key, value);
        return this;
    }


    @Override
    public IPage hoverOn(By by) {
        new Actions(driver.getDriver()).moveToElement(findElement(by)).build().perform();
        log.info("Action - hover on to locator: " + by.toString());
        return this;
    }


    @Override
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
    @Override
    public int getCountElements(By by) {
        waitForElementPresent(by);
        return driver.findElements(by).size();
    }


    @Override
    public void makeScreenshotForDiff(String name, boolean isTest) {
        String path = isTest ? CheckingDifferentImages.TEST_PATH : CheckingDifferentImages.ETALON_PATH;
        File scrFile;
        log.info("Screen path " + path + " name is " + name);
        try {
            scrFile = ((TakesScreenshot) driver.getDriver()).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File("screenshots" +
                    File.separator + path + File.separator + name + ".png"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


}