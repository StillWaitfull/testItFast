package tests;

import composite.IPage;
import composite.pages.GooglePage;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;

@Features(value = "Google Tests")
public class GoogleTest extends AbstractTest {


    @Test
    public void googleTest() {
        IPage googlePage = new GooglePage();
        googlePage.openPage()
                .type(GooglePage.query, "0")
                .click(GooglePage.button)
                .assertThat(
                        GooglePage.getGooglePageAssertions(googlePage)
                );

    }

    @Test
    public void googleTest1() {
        IPage googlePage = new GooglePage();
        googlePage.openPage()
                .type(GooglePage.query, "1")
                .click(GooglePage.button);


    }

    @Test
    public void googleTest2() {
        IPage googlePage = new GooglePage();
        googlePage.openPage()
                .type(GooglePage.query, "2")
                .click(GooglePage.button);
    }

    @Test
    public void googleTest3() {
        IPage googlePage = new GooglePage();
        googlePage.openPage()
                .type(GooglePage.query, "3")
                .click(GooglePage.button);
    }


}
