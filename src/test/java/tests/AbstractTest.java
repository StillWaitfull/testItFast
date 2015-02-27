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

import java.util.stream.Collectors;


/**
 * Abstract test
 *
 * @author Aleksey Niss,Sergey Kashapov
 */
@Listeners(WebDriverListener.class)
public abstract class AbstractTest {

    protected static Logger log4j = Logger.getLogger(AbstractTest.class);

    public AbstractTest() {
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
            Assert.fail("There was errors in frontend tests \n" + CheckingDifferentImages.failedTests.stream().collect(Collectors.joining("\n")));
        ProxyHelper.stopProxy();
    }
}
