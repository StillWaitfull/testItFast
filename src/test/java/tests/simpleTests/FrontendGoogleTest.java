package tests.simpleTests;

import org.testng.annotations.Test;
import pages.GooglePage;
import ru.yandex.qatools.allure.annotations.Features;
import toolkit.CheckingDifferentImages;


@Features(value = "Google Tests")
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
    }
}