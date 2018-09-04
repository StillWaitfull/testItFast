package toolkit.helpers;

import com.google.common.collect.Iterables;
import composite.IPage;
import configs.PlatformConfig;
import configs.StageConfig;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import toolkit.driver.LocalDriverManager;
import toolkit.driver.WebDriverController;

import java.util.List;

import static toolkit.helpers.OperationsHelper.sendPause;


public abstract class AbstractPage implements IPage {

    protected static final String BASE_URL = StageConfig.getInstance().getBaseUrl();
    private static final Logger log = LoggerFactory.getLogger(AbstractPage.class);
    private final WebDriverController driver = LocalDriverManager.getDriverController() == null
            ? new WebDriverController(PlatformConfig.determinePlatform())
            : LocalDriverManager.getDriverController();
    private final WaitHelper waitHelper = new WaitHelper();

    public void logoutHook() {
        if (LocalDriverManager.getDriverController() != null) {
            LocalDriverManager.getDriverController().goToUrl(BASE_URL);
            LocalDriverManager.getDriverController().deleteAllCookies();
        }

    }


    //WINDOW ACTIONS
    public boolean checkDimensionIsLess(int width) {
        return driver.getDimension().getWidth() <= width;
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
        waitHelper.waitForNumberOfWindowsIsMore(2);
        driver.switchTo(iFrame);
        return this;
    }


    public IPage switchToOtherWindow() {
        waitHelper.waitForNumberOfWindowsIsMore(2);
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


    public IPage clickCancelInAlert() {
        // Get a handle to the open alert, prompt or confirmation
        Alert alert = driver.getDriver().switchTo().alert();
        // Get the text of the alert or prompt
        log.debug("alert: " + alert.getText());
        // And acknowledge the alert (equivalent to clicking "Cancel")
        alert.dismiss();
        return this;
    }


    //ACTIONS
    public WebElement findElement(By by) {
        waitHelper.waitForVisible(by);
        return driver.findElement(by);
    }


    public List<WebElement> findElements(final By by) {
        waitHelper.waitForElementPresent(by);
        return driver.findElements(by);
    }


    public String getSrcOfElement(By by) {
        waitHelper.waitForElementPresent(by);
        return driver.findElement(by).getAttribute("src");
    }


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
        waitHelper.waitForElementPresent(by);
        driver.findElement(by).submit();
        return this;

    }

    public IPage navigateBack() {
        driver.navigationBack();
        return this;
    }


    public IPage moveToElement(By by) {
        Actions actions = new Actions(driver.getDriver());
        waitHelper.waitForElementPresent(by);
        actions.moveToElement(driver.findElement(by)).build().perform();
        return this;
    }


    private void highlightTheElement(By by) {
        WebElement element = driver.findElement(by);
        driver.executeScript("arguments[0].style.border='2px solid yellow'", element);
    }


    public IPage pressEnter() {
        Actions action = new Actions(driver.getDriver());
        action.sendKeys(Keys.ENTER).perform();
        return this;

    }

    public String getText(By by) {
        log.debug("Text from: " + by.toString());
        waitHelper.waitForVisible(by);
        return driver.findElement(by).getText();
    }


    public String getAttribute(By by, String nameAttribute) {
        log.debug("Text from: " + by.toString());
        waitHelper.waitForVisible(by);
        return driver.findElement(by).getAttribute(nameAttribute);
    }


    public String getPageSource() {
        return driver.getDriver().getPageSource();
    }


    public void type(By by, String someText) {
        log.debug("Type:" + someText + " to:" + by.toString());
        waitHelper.waitForVisible(by);
        highlightTheElement(by);
        driver.findElement(by).clear();
        driver.findElement(by).sendKeys(someText);
    }


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


    //CLICKS
    public IPage click(By by) {
        log.debug("Click on: " + by.toString());
        waitHelper.waitForElementPresent(by);
        highlightTheElement(by);
        driver.findElement(by).click();
        return this;

    }


    public IPage clickWithJs(By by) {
        log.debug("Click on: " + by.toString());
        waitHelper.waitForVisible(by);
        highlightTheElement(by);
        driver.executeScript("arguments[0].click();", driver.findElement(by));
        return this;
    }


    public IPage actionClick(By by) {
        log.debug("Click on: " + by.toString());
        waitHelper.waitForVisible(by);
        highlightTheElement(by);
        new Actions(driver.getDriver()).click(driver.findElement(by)).build().perform();
        return this;
    }


    public IPage refreshPage() {
        driver.refresh();
        return this;
    }

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


    //PROPERTIES
    private boolean isEnable(By by) {
        try {
            return driver.findElement(by).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }


    public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }


    public boolean isVisible(By by) {
        try {
            return driver.findElement(by).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public int getCountElements(By by) {
        waitHelper.waitForElementPresent(by);
        return driver.findElements(by).size();
    }


    //VALIDATE
    public boolean validateElementPresent(By by) {
        try {
            waitHelper.waitForElementPresent(by);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }


    public boolean validateElementIsNotVisible(By by) {
        try {
            waitHelper.waitForVisible(by);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }


    public boolean validateElementVisible(By by) {
        try {
            waitHelper.waitForVisible(by);
            return true;
        } catch (AssertionError e) {
            return false;
        }

    }


    public boolean validateUrlContains(String s) {
        for (int i = 0; i < driver.getTimeout(); i++) {
            if (driver.getDriver().getCurrentUrl().contains(s))
                return true;
            else sendPause(1);
        }
        return false;
    }


    public boolean validateTextEquals(By by, String text) {
        try {
            waitHelper.waitForVisible(by);
        } catch (AssertionError ignored) {
        }
        return getText(by).equals(text);

    }


    public boolean validateElementAttributeNotEmpty(By by, String nameAttribute) {
        try {
            driver.getInstanceWaitDriver().until((WebDriver webDriver) -> {
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
            driver.getInstanceWaitDriver().until((WebDriver webDriver) -> !getText(by).equals(""));
            return true;
        } catch (TimeoutException e) {
            return false;
        }

    }


    public boolean validateElementHasText(By by, String text) {
        try {
            driver.getInstanceWaitDriver().until((WebDriver webDriver) -> getText(by).contains(text));
            return true;
        } catch (TimeoutException e) {
            return false;
        } catch (StaleElementReferenceException e) {
            return validateElementHasText(by, text);
        }

    }


    public final void assertThat(List<Runnable> assertions) {
        assertions.forEach(Runnable::run);
    }

    public final void assertThat(Runnable assertions) {
        assertions.run();
    }


}
