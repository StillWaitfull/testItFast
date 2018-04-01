package toolkit.driver;

import configs.ApplicationConfig;
import configs.BrowserConfig;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;


public class WebDriverController {

    private final WebDriver driver;
    private final String browser;
    private final Dimension dimension;
    private int timeout;
    private static ApplicationConfig applicationConfig = ApplicationConfig.getInstance();

    public WebDriverController() {
        this.browser = applicationConfig.BROWSER;
        this.dimension = new Dimension(applicationConfig.DIMENSION_W, applicationConfig.DIMENSION_H);
        this.driver = BrowserConfig.getBrowser(browser);
        this.driver.manage().window().setSize(dimension);
        this.driver.switchTo();
        this.timeout = ApplicationConfig.getInstance().TIMEOUT;
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        LocalDriverManager.setWebDriverController(this);
    }


    public String getBrowser() {
        return browser;
    }

    private void waitForPageLoaded() {
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

    public WebDriver getDriver() {
        return driver;
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

    public Object executeScript(String script, Object... args) {
        if (driver == null)
            throw new RuntimeException("Driver is null in method executeScript");
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    /**
     * Refreshes current page
     */
    public void refresh() {
        driver.navigate().refresh();
        waitForPageLoaded();
    }


    public Dimension getDimension() {
        return dimension;
    }

    public int getTimeout() {
        return timeout;
    }
}
