package tests;

import composite.IPage;
import org.testng.annotations.Test;
import pages.GooglePage;
import ru.yandex.qatools.allure.annotations.Features;
import toolkit.CheckingDifferentImages;

import static configs.GeneralConfig.applicationContext;

/**
 * Created by Restore on 11/6/14.
 */
@Features(value = "Google Tests")
public class FrontendGoogleTest extends AbstractTest {


    @Test
    public void googleTest() {
        String name = "googleTest";
        CheckingDifferentImages checkingDifferentImages = applicationContext.getBean(CheckingDifferentImages.class);
        IPage googlePage = new GooglePage();
        googlePage.openPage()
                .type(GooglePage.QUERY, "1")
                .makeScreenshotForDiff(name, CheckingDifferentImages.isTest);
        checkingDifferentImages.turnOnInTest();
        googlePage.openPage()
                .type(GooglePage.QUERY, "3")
                .makeScreenshotForDiff(name, CheckingDifferentImages.isTest);
        checkingDifferentImages.checkDifference(name, 1);
    }
}
