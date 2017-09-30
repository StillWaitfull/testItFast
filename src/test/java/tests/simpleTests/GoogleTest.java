package tests.simpleTests;

import io.qameta.allure.Feature;
import org.testng.annotations.Test;
import pages.GooglePage;


@Feature(value = "Google Tests")
public class GoogleTest extends AbstractTest {

    @Test
    public void googleTest() {
        GooglePage googlePage = new GooglePage();
        googlePage.openPage().assertThat(
                GooglePage.getGooglePageAssertions(googlePage)
        );
        googlePage.typeTextToQueryField("0").assertThat(
                GooglePage.getGooglePageAssertions(googlePage)
        );

    }

    @Test
    public void googleTest1() {
        GooglePage googlePage = new GooglePage();
        googlePage.openPage().assertThat(
                GooglePage.getGooglePageAssertions(googlePage)
        );
        googlePage.typeTextToQueryField("1").assertThat(
                GooglePage.getGooglePageAssertions(googlePage)
        );


    }

    @Test
    public void googleTest2() {
        GooglePage googlePage = new GooglePage();
        googlePage.openPage().assertThat(
                GooglePage.getGooglePageAssertions(googlePage)
        );
        googlePage.typeTextToQueryField("2").assertThat(
                GooglePage.getGooglePageAssertions(googlePage)
        );

    }


}
