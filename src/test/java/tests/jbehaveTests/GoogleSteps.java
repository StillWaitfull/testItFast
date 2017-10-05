package tests.jbehaveTests;


import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import pages.GooglePage;

public class GoogleSteps {

    private GooglePage googlePage = new GooglePage();

    @Given("Пользователь входит на страницу гугла")
    public void goToGooglePage() throws Throwable {
        googlePage.openPage();
    }


    @When("Пользователь вводит $text в поле поиска")
    public void typeTextToTextField(String text) throws Throwable {
        googlePage.typeTextToQueryField(text);
    }

    @Then("Проверить что на странице гугла есть нужные элементы")
    public void assertElementsOnPage() throws Throwable {
        googlePage.assertThat(
                GooglePage.getGooglePageAssertions(googlePage)
        );
    }


}
