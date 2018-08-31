package tests.jbehaveTests;


import components.SearchResults;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import pages.GooglePage;

class GoogleSteps {

    private final GooglePage googlePage = new GooglePage();
    private SearchResults searchResults;

    @Given("Пользователь входит на страницу гугла")
    public void goToGooglePage() {
        googlePage.openPage();
    }


    @When("Пользователь вводит $text в поле поиска")
    public void typeTextToTextField(String text) {
        searchResults = googlePage.getGoogleSearch()
                .typeTextToQueryField(text)
                .clickSearchButton();
    }

    @Then("Проверить что после поиска присутствуют результаты")
    public void assertElementsOnPage() {
        Assert.assertTrue("There are no results after search ", searchResults.getCountOfSearchResults() > 0);

    }


}
