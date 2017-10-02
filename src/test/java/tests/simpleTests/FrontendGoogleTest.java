package tests.simpleTests;

import io.qameta.allure.Feature;
import org.junit.Assert;
import org.junit.Test;
import pages.GooglePage;
import toolkit.CheckingDifferentImages;


@Feature(value = "Google Tests")
public class FrontendGoogleTest extends AbstractTest {


    @Test
    public void googleTest() {
        String name = "googleTest";
        CheckingDifferentImages checkingDifferentImages = new CheckingDifferentImages();
        GooglePage googlePage = new GooglePage();
        googlePage.openPage().assertThat(
                GooglePage.getGooglePageAssertions(googlePage)
        );
        googlePage.typeTextToQueryField("0").assertThat(
                GooglePage.getGooglePageAssertions(googlePage)
        );
        checkingDifferentImages.makeScreenshotForDiff(name);
        checkingDifferentImages.turnOnInTest();
        googlePage.typeTextToQueryField("3").assertThat(
                GooglePage.getGooglePageAssertions(googlePage)
        );
        checkingDifferentImages.makeScreenshotForDiff(name)
                .checkDifference(name, 1);
        Assert.assertTrue("There was errors in frontend tests ",
                checkingDifferentImages.getResult());

    }
}
