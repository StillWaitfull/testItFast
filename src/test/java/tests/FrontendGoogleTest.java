package tests;

import composite.IPage;
import composite.pages.GooglePage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import toolkit.CheckingDifferentImages;

import java.lang.reflect.Method;

/**
 * Created by Restore on 11/6/14.
 */
public class FrontendGoogleTest extends AbstractTest {

    String nameMethod = "";

    @BeforeMethod
    public void init(Method method) {
        nameMethod = method.getName();
    }

    @Test
    public void googleTest1() {
        IPage googlePage = new GooglePage();
        googlePage.openPage()
                .type(GooglePage.query, "1")
                .makeScreenshotForDiff(nameMethod);


    }

    @Test
    public void googleTest2() {
        IPage googlePage = new GooglePage();
        googlePage.openPage()
                .type(GooglePage.query, "2")
                .makeScreenshotForDiff(nameMethod);
    }

    @AfterMethod
    public void CheckScreens() {
        if (AbstractTest.isTest) {
            CheckingDifferentImages.checkDifference(nameMethod, nameMethod + "_diff", 1);
        }
    }
}
