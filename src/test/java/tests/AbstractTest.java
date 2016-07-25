package tests;

import org.testng.Assert;
import org.testng.annotations.*;
import toolkit.CheckingDifferentImages;
import toolkit.driver.LocalDriverManager;
import toolkit.driver.ProxyHelper;
import toolkit.driver.TestListenerAd;
import toolkit.driver.WebDriverListener;

import java.util.stream.Collectors;



@Listeners({WebDriverListener.class, TestListenerAd.class})
public abstract class AbstractTest {


    @BeforeMethod
    @Parameters({"browser"})
    public void setBrowser(@Optional String browser) {
    }

    @AfterMethod
    public void after() {
        if (LocalDriverManager.getDriverController() != null)
            LocalDriverManager.getDriverController().deleteAllCookies();
    }

    @AfterSuite
    public void cleanPool() {
        LocalDriverManager.cleanThreadPool();
        if (!CheckingDifferentImages.failedTests.isEmpty())
            Assert.fail("There was errors in frontend tests \n" + CheckingDifferentImages.failedTests.stream().collect(Collectors.joining("\n")));
        ProxyHelper.stopProxy();
    }
}
