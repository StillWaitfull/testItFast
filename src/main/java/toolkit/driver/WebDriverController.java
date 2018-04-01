package toolkit.driver;

import configs.ApplicationConfig;
import configs.BrowserConfig;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Set;


public class WebDriverController {

    private final WebDriver driver;
    private final String browser;
    private final Dimension dimension;
    private int timeout;


    public WebDriverController(common.Platform platform) {
        this.driver = BrowserConfig.getBrowser(platform);
        this.browser = platform.getBrowser();
        this.dimension = platform.getDimension();
        this.timeout = ApplicationConfig.getInstance().TIMEOUT;
        if (!platform.isMobile()) driver.manage().window().setSize(dimension);
        if (platform.isRemote()) ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
        driver.switchTo();
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        LocalDriverManager.setWebDriverController(this);
    }


    public void setWindowSize(Dimension dimension) {
        driver.manage().window().setSize(dimension);
    }

    public void maximizeWindow() {
        driver.manage().window().maximize();
    }

    public String getBrowser() {
        return browser;
    }


    private void waitForPageLoaded() {
        // Function<WebDriver, Boolean> expectation = driver1 -> executeScript("return document.readyState").toString().equals("complete");
        ExpectedCondition<Boolean> expectation = driver1 -> executeScript("return document.readyState").toString().equals("complete");
        try {
            getInstanceWaitDriver().until(expectation);
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for Page Load Request to complete.");
        }
    }


    public WebDriverWait getInstanceWaitDriver() {
        return new WebDriverWait(driver, timeout);
    }


    public void goToUrl(String url) {
        driver.get(url);
        waitForPageLoaded();
    }

    /**
     * Returns WebDriver
     */
    public WebDriver getDriver() {
        return driver;
    }

    public org.openqa.selenium.Cookie getCookie(String key) {
        return driver.manage().getCookieNamed(key);
    }

    public Set<org.openqa.selenium.Cookie> getCookies() {
        return driver.manage().getCookies();
    }

    /**
     * Sends into a browser
     */
    public void navigationBack() {
        driver.navigate().back();
        waitForPageLoaded();
    }

    //Delete all cookies
    public void deleteAllCookies() {
        driver.manage().deleteAllCookies();
    }

    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    // Set a cookie
    public void addCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        driver.manage().addCookie(cookie);
    }

    public WebElement findElement(final By by) {
        return driver.findElement(by);
    }

    public List<WebElement> findElements(final By by) {
        return driver.findElements(by);
    }


    public void get(String url) {
        if (url.isEmpty()) {
            throw new IllegalArgumentException();
        }
        try {
            driver.get(url);
        } catch (Exception ignored) {
        }
        waitForPageLoaded();
    }


    void shutdown() {
        try {
            driver.quit();
        } catch (Exception ignored) {
        } finally {
            LocalDriverManager.removeWebDriverController();
        }
    }

    /**
     * Executes JavaScript in the context of the currently selected frame or window. (See also {@link JavascriptExecutor})
     */
    public Object executeScript(String script, Object... args) {
        if (driver == null)
            throw new RuntimeException("Driver is null in method executeScript");
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    public Object executeAsyncScript(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeAsyncScript(script, args);
    }

    // Change the cookie
    public void changeCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        driver.manage().deleteCookieNamed(key);
        driver.manage().addCookie(cookie);
    }


    /**
     * Returns attribute value
     *
     * @param by        -
     * @param attribute -
     */
    public String getAttributeValue(By by, String attribute) {
        return driver.findElement(by).getAttribute(attribute);
    }


    /**
     * Refreshes current page
     */
    public void refresh() {
        driver.navigate().refresh();
        waitForPageLoaded();
    }

    /**
     * Switches to iFrame, if it needed
     *
     * @param iFrame - the name or id of iFrame
     */
    public void switchTo(String iFrame) {
        driver.switchTo().frame(iFrame);
    }

    public void switchToWindow(String iFrame) {
        driver.switchTo().window(iFrame);
    }

    public void closeWindow() {
        driver.close();
    }

    /**
     * Returns link to main content on the page
     */
    public void switchToMainContent() {
        driver.switchTo().defaultContent();
    }

    public Dimension getDimension() {
        return dimension;
    }

    public int getTimeout() {
        return timeout;
    }
}
