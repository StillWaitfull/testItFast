package tests.cucumberTests;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pages.GooglePage;

public class GoogleSteps {

    private final GooglePage googlePage = new GooglePage();

    @Given("^Пользователь входит на страницу гугла$")
    public void goToGooglePage() throws Throwable {
        googlePage.openPage();
    }


    @When("^Пользователь вводит \"([^\"]*)\" в поле поиска$")
    public void typeTextToTextField(String text) throws Throwable {
        googlePage.typeTextToQueryField(text);
    }

    @Then("^Проверить что на странице гугла есть нужные элементы$")
    public void assertElementsOnPage() throws Throwable {
        googlePage.assertThat(
                GooglePage.getGooglePageAssertions(googlePage)
        );
    }


}
