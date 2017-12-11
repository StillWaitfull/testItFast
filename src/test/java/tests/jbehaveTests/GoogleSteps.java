package tests.jbehaveTests;


import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import pages.GooglePage;

class GoogleSteps {

    private final GooglePage googlePage = new GooglePage();

    @Given("Пользователь входит на страницу гугла")
    public void goToGooglePage() {
        googlePage.openPage();
    }


    @When("Пользователь вводит $text в поле поиска")
    public void typeTextToTextField(String text) {
        googlePage.typeTextToQueryField(text);
    }

    @Then("Проверить что на странице гугла есть нужные элементы")
    public void assertElementsOnPage() {
        googlePage.assertThat(
                GooglePage.getGooglePageAssertions(googlePage)
        );
    }


}
