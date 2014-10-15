package toolkit.helpers;

import composite.IPage;
import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import toolkit.driver.LocalDriverManager;
import toolkit.driver.WebDriverController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public abstract class OperationsHelper implements IPage {

    private static Logger log = Logger.getLogger(OperationsHelper.class);
    private WebDriverWait waitDriver = WebDriverController.getInstanceWaitDriver();
    private WebDriverController driver = LocalDriverManager.getDriverController();
    public static String baseUrl;


    public static void initBaseUrl() {
        String host = System.getenv("host");
        if (host == null)
            baseUrl = YamlConfigProvider.getStageParameters("baseUrl");

    }

    /**
     * Exits from current user
     */
    public static void logoutHook() {
        if (LocalDriverManager.getDriverController() != null) {
            LocalDriverManager.getDriverController().goToUrl(baseUrl);
            LocalDriverManager.getDriverController().deleteAllCookies();
        }

    }




    @Override
    public IPage pressEnter() {
        Actions action = new Actions(LocalDriverManager.getDriverController().getDriver());
        action.sendKeys(Keys.ENTER).perform();
        return this;

    }

    private boolean isEnable(By by) {
        try {
            if (driver.findElement(by).isEnabled())
                return true;
        } catch (Exception e) {
            return false;
        }
        return false;
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


    @Override
    public IPage windowSetSize(int widthWindow, int heightWindow) {
        java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (((screenSize.width >= widthWindow) && (screenSize.height >= heightWindow)) ||
                ((0 >= widthWindow) && (0 >= heightWindow))) {
            Dimension targetSize = new Dimension(widthWindow, heightWindow);
            windowSetSize(targetSize);
        } else {
            log.debug("it is impossible");
        }
        return this;
    }

    @Override
    public void waitForElementPresent(By by) {
        waitDriver.until(ExpectedConditions.presenceOfElementLocated(by));
    }


    @Override
    public IPage waitElementForSec(By by, int seconds) {
        for (int i = 0; i < seconds; i++) {
            if (findElement(by).isDisplayed()) break;
            else sendPause(1);
        }
        return this;
    }


    @Override
    public IPage clickOnStalenessElement(final By by) {
        waitDriver.until((WebDriver webDriver) -> {
            try {
                final WebElement element = driver.findElement(by);
                if (element != null && element.isDisplayed() && element.isEnabled()) {
                    element.click();
                    return element;
                }
            } catch (StaleElementReferenceException e) {
                log.error("Stale exception");
            }
            return null;
        });
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

    @Override
    public IPage waitForNotAttribute(final By by, final String attribute, final String value) {
        waitDriver.until((WebDriver d) -> d.findElement(by)
                .getAttribute(attribute)
                .equals(value));
        return this;
    }


    @Override
    public IPage waitForTextPresent(final String text) {
        waitDriver.until((WebDriver d) -> d.getPageSource().contains(text));
        return this;
    }


    @Override
    public java.util.List<WebElement> findElements(final By by) {
        waitForElementPresent(by);
        return driver.findElements(by);
    }


    @Override
    public void waitForVisible(By by) {
        waitDriver.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
    }

    @Override
    public void waitForNotVisible(By by) {
        waitDriver.until(ExpectedConditions.invisibilityOfElementLocated(by));
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
        waitForVisible(by);
        highlightTheElement(by);
        driver.findElement(by).click();
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
        } catch (NoSuchElementException e) {
            return false;
        } catch (StaleElementReferenceException se) {
            return false;
        }
    }


    @Override
    public boolean isVisible(By by) {
        try {
            return driver.findElement(by).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        } catch (StaleElementReferenceException se) {
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


    public static void makeScreenshot(String methodName) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage capture;
        try {
            capture = new Robot().createScreenCapture(screenRect);
            File fileScreenshot = new File("target" + File.separator + "failure_screenshots" +
                    File.separator + methodName + "_" + formater.format(calendar.getTime()) + "_javarobot.jpg");
            fileScreenshot.getParentFile().mkdirs();
            ImageIO.write(capture, "jpg", fileScreenshot);
            File scrFile = ((TakesScreenshot) LocalDriverManager.getDriverController().getDriver()).getScreenshotAs(
                    OutputType.FILE);
            FileUtils.copyFile(scrFile, new File("target" + File.separator + "failure_screenshots" +
                    File.separator + methodName + "_" + formater.format(calendar.getTime()) + "_webdriver.png"));
        } catch (AWTException | IOException awte) {
            awte.printStackTrace();
        }

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


    /**
     * Opens a new tab for the given URL
     *
     * @param url The URL to
     * @throws JavaScriptException If unable to open tab
     */
    @Override
    public IPage openTab(String url) {
        String script = "var d=document,a=d.createElement('a');a.target='_blank';a.href='%s';a.innerHTML='.';d.body.appendChild(a);return a";
        Object element = LocalDriverManager.getDriverController().executeScript(String.format(script, url));
        if (element instanceof WebElement) {
            WebElement anchor = (WebElement) element;
            anchor.click();
            LocalDriverManager.getDriverController().executeScript("var a=arguments[0];a.parentNode.removeChild(a);",
                    anchor);
        } else {
            throw new JavaScriptException(element, "Unable to open tab", 1);
        }
        return this;
    }


    @Override
    public boolean validateElementPresent(By by) {
        try {
            waitDriver.until((WebDriver webDriver) -> isElementPresent(by));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }


    @Override
    public boolean validateElementIsNotVisible(By by) {
        try {
            waitDriver.until((WebDriver webDriver) -> !isVisible(by));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }


    @Override
    public boolean validateElementVisible(By by) {
        try {
            waitDriver.until((WebDriver webDriver) -> isVisible(by));
            return true;
        } catch (TimeoutException e) {
            return false;
        }

    }

    @Override
    public boolean validateUrlContains(String s) {
        try {
            waitDriver.until((WebDriver webDriver) -> webDriver.getCurrentUrl().contains(s));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }


    @Override
    public boolean validateElementEnable(By by) {
        try {
            waitDriver.until((WebDriver webDriver) -> isEnable(by));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Override
    public boolean validateTextEquals(By by, String text) {
        waitForVisible(by);
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
        Actions action = new Actions(driver.getDriver());
        action.moveToElement(findElement(by)).build().perform();
        log.info("Action - hover on to locator: " + by.toString());
        return this;
    }


    @Override
    public IPage scrollOnTop() {
        LocalDriverManager.getDriverController().executeScript("window.scrollTo(0,0)");
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


}
