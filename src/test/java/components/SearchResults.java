package components;

import org.junit.Assert;
import org.openqa.selenium.By;
import toolkit.helpers.AbstractPage;

public class SearchResults {

    private static final By SEARCH_DIV = By.id("search");
    private static final By SEARCH_RESULT = By.cssSelector("#search .g");
    private AbstractPage page;

    public SearchResults(AbstractPage page) {
        this.page = page;
        page.assertThat(getSearchResultsAssertions());
    }

    public int getCountOfSearchResults() {
        return page.findElements(SEARCH_RESULT).size();
    }


    private Runnable getSearchResultsAssertions() {
        return () -> Assert.assertTrue("Search results are not visible", page.validateElementVisible(SEARCH_DIV));
    }
}