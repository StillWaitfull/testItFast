package tests;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Listeners;
import toolkit.CheckingDifferentImages;
import toolkit.driver.LocalDriverManager;
import toolkit.driver.ProxyHelper;
import toolkit.driver.WebDriverListener;
import toolkit.helpers.OperationsHelper;
import toolkit.helpers.YamlConfigProvider;


/**
 * Abstract test
 *
 * @author Aleksey Niss,Sergey Kashapov
 */
@Listeners(WebDriverListener.class)
public abstract class AbstractTest {

    protected static Logger log4j = Logger.getLogger(AbstractTest.class);
    public static boolean isTest = Boolean.parseBoolean(System.getenv("isTest"));

    public AbstractTest() {
        if (System.getenv("isTest") == null)
            isTest = Boolean.parseBoolean(YamlConfigProvider.getAppParameters("isTest"));
        OperationsHelper.initBaseUrl();
    }


    @AfterMethod
    public void after() {
        if (LocalDriverManager.getDriverController() != null)
            LocalDriverManager.getDriverController().deleteAllCookies();
    }

    @AfterSuite
    public void cleanPool() {
        LocalDriverManager.cleanThreadPool();
        CheckingDifferentImages.deleteFileInDirectory(CheckingDifferentImages.TEST_SCREENS_PATH);
        if (!CheckingDifferentImages.failedTests.isEmpty())
            Assert.fail("There was errors in frontend tests");
        ProxyHelper.stopProxy();
    }
}
