package tests;

import composite.IPage;
import composite.pages.GooglePage;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import toolkit.CheckingDifferentImages;

/**
 * Created by Restore on 11/6/14.
 */
@Features(value = "Google Tests")
public class FrontendGoogleTest extends AbstractTest {


    @Test
    public void googleTest() {
        String name = "googleTest";
        IPage googlePage = new GooglePage();
        CheckingDifferentImages checkingDifferentImages = new CheckingDifferentImages();
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
