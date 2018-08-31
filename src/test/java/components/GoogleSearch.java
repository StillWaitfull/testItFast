package components;

import org.junit.Assert;
import org.openqa.selenium.By;
import toolkit.helpers.AbstractPage;

public class GoogleSearch {

    private static final By QUERY = By.id("lst-ib");
    private static final By SEARCH_BUTTON = By.cssSelector("input[name='btnK']");
    private AbstractPage page;

    public GoogleSearch(AbstractPage page) {
        this.page = page;
        page.assertThat(getGoogleSearchAssertions());
    }

    public GoogleSearch typeTextToQueryField(String text) {
        page.type(QUERY, text);
        return this;
    }

    public SearchResults clickSearchButton() {
        page.actionClick(SEARCH_BUTTON);
        return new SearchResults(page);
    }


    private Runnable getGoogleSearchAssertions() {
        return () -> Assert.assertTrue("Google QUERY is not visible", page.validateElementVisible(QUERY));
    }
}
