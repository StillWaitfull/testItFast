package tests.simpleTests;

import components.SearchResults;
import io.qameta.allure.Feature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pages.GooglePage;


@Feature(value = "Google Tests")
public class GoogleTest extends AbstractTest {

    private GooglePage googlePage;

    @Before
    public void init() {
        googlePage = new GooglePage();
        googlePage.openPage();
    }

    @Test
    public void googleTest() {
        String value = "0";
        SearchResults searchResults = googlePage.getGoogleSearch()
                .typeTextToQueryField(value)
                .clickSearchButton();
        Assert.assertTrue("There are no results after search " + value, searchResults.getCountOfSearchResults() > 0);
    }

    @Test
    public void googleTest1() {
        String value = "1";
        SearchResults searchResults = googlePage.getGoogleSearch()
                .typeTextToQueryField(value)
                .clickSearchButton();
        Assert.assertTrue("There are no results after search " + value, searchResults.getCountOfSearchResults() > 0);
    }

    @Test
    public void googleTest2() {
        String value = "2";
        SearchResults searchResults = googlePage.getGoogleSearch()
                .typeTextToQueryField(value)
                .clickSearchButton();
        Assert.assertTrue("There are no results after search " + value, searchResults.getCountOfSearchResults() > 0);
    }


}
