package tests;

import composite.IPage;
import org.testng.annotations.Test;
import pages.GooglePage;
import ru.yandex.qatools.allure.annotations.Features;

@Features(value = "Google Tests")
public class GoogleTest extends AbstractTest {


    @Test
    public void googleTest() {
        IPage googlePage = new GooglePage();
        googlePage.openPage()
                .type(GooglePage.QUERY, "0")
                .assertThat(
                        GooglePage.getGooglePageAssertions(googlePage)
                );
    }

    @Test
    public void googleTest1() {
        IPage googlePage = new GooglePage();
        googlePage.openPage()
                .type(GooglePage.QUERY, "1");


    }

    @Test
    public void googleTest2() {
        IPage googlePage = new GooglePage();
        googlePage.openPage()
                .type(GooglePage.QUERY, "2");
    }

    @Test
    public void googleTest3() {
        IPage googlePage = new GooglePage();
        googlePage.openPage()
                .type(GooglePage.QUERY, "3");
    }


}
