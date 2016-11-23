package tests;

import org.testng.Assert;
import org.testng.annotations.*;
import toolkit.CheckingDifferentImages;
import toolkit.driver.LocalDriverManager;
import toolkit.driver.ProxyHelper;
import toolkit.driver.WebDriverListener;
import toolkit.helpers.OperationsHelper;

import java.util.stream.Collectors;


@Listeners({WebDriverListener.class})
public abstract class AbstractTest {


    @BeforeSuite
    public void initProxy() {
        ProxyHelper.initProxy();
    }


    @AfterMethod
    public void after() {
        OperationsHelper.logoutHook();
    }

    @AfterTest
    public void cleanPool() {
        LocalDriverManager.cleanThreadPool();
    }

    @AfterSuite
    public void stopServices() {
        if (!CheckingDifferentImages.failedTests.isEmpty())
            Assert.fail("There was errors in frontend tests \n" + CheckingDifferentImages.failedTests.stream().collect(Collectors.joining("\n")));
        ProxyHelper.stopProxy();
    }
}
