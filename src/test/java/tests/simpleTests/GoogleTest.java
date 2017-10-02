package tests.simpleTests;

import io.qameta.allure.Feature;
import org.junit.Before;
import org.junit.Test;
import pages.GooglePage;


@Feature(value = "Google Tests")
public class GoogleTest extends AbstractTest {

    private GooglePage googlePage;

    @Before
    public void init() {
        googlePage = new GooglePage();
        googlePage.openPage().assertThat(
                GooglePage.getGooglePageAssertions(googlePage)
        );
    }

    @Test
    public void googleTest() {

        googlePage.typeTextToQueryField("0").assertThat(
                GooglePage.getGooglePageAssertions(googlePage)
        );

    }

    @Test
    public void googleTest1() {
        googlePage.typeTextToQueryField("1").assertThat(
                GooglePage.getGooglePageAssertions(googlePage)
        );


    }

    @Test
    public void googleTest2() {
        googlePage.typeTextToQueryField("2").assertThat(
                GooglePage.getGooglePageAssertions(googlePage)
        );

    }


}
