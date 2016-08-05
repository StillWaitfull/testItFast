package tests;

import org.testng.Assert;
import org.testng.annotations.*;
import toolkit.CheckingDifferentImages;
import toolkit.driver.LocalDriverManager;
import toolkit.driver.ProxyHelper;
import toolkit.driver.WebDriverListener;

import java.util.stream.Collectors;



@Listeners({WebDriverListener.class})
public abstract class AbstractTest {


    @BeforeTest
    @Parameters({"browser"})
    public void setBrowser(@Optional String browser) {
    }

    @BeforeTest
    @Parameters({"dimensionH", "dimensionW"})
    public void setDimension(@Optional String dimensionH, @Optional String dimensionW) {
    }

    @AfterMethod
    public void after() {
        if (LocalDriverManager.getDriverController() != null)
            LocalDriverManager.getDriverController().deleteAllCookies();
    }

    @AfterTest
    public void cleanPool() {
        LocalDriverManager.cleanThreadPool();
    }

    @AfterSuite
    public void stopServices(){
        if (!CheckingDifferentImages.failedTests.isEmpty())
            Assert.fail("There was errors in frontend tests \n" + CheckingDifferentImages.failedTests.stream().collect(Collectors.joining("\n")));
        ProxyHelper.stopProxy();
    }
}
