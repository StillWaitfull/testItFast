package tests.simpleTests;

import org.testng.annotations.Test;
import pages.GooglePage;
import ru.yandex.qatools.allure.annotations.Features;

@Features(value = "Google Tests")
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
