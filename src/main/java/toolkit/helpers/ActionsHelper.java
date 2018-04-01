package toolkit.helpers;

import composite.IPage;
import configs.ApplicationConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import toolkit.driver.LocalDriverManager;
import toolkit.driver.WebDriverController;

import java.util.Arrays;


public abstract class ActionsHelper implements IPage {


    protected static final String baseUrl = ApplicationConfig.getInstance().BASE_URL;
    private static final Logger log = LoggerFactory.getLogger(ActionsHelper.class);
    private final WebDriverController driver = LocalDriverManager.getDriverController() == null
            ? new WebDriverController()
            : LocalDriverManager.getDriverController();
    private final WaitHelper waitHelper = new WaitHelper();


    //WINDOW ACTIONS

    public boolean checkDimensionIsLess(int width) {
        return driver.getDimension().getWidth() <= width;
    }


    //ACTIONS
    public String getCurrentUrl() {
        return driver.getDriver().getCurrentUrl();
    }


    private void highlightTheElement(By by) {
        WebElement element = driver.findElement(by);
        driver.executeScript("arguments[0].style.border='2px solid yellow'", element);
    }


    protected void type(By by, String someText) {
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


    //CLICKS
    public IPage click(By by) {
        log.debug("Click on: " + by.toString());
        waitHelper.waitForElementPresent(by);
        highlightTheElement(by);
        driver.findElement(by).click();
        return this;

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


    //VALIDATE
    public boolean validateElementVisible(By by) {
        try {
            waitHelper.waitForVisible(by);
            return true;
        } catch (AssertionError e) {
            return false;
        }

    }

    public final void assertThat(Runnable... assertions) {
        Arrays.asList(assertions).forEach(Runnable::run);

    }


}
