package components;

import org.junit.Assert;
import org.openqa.selenium.By;
import toolkit.helpers.AbstractPage;

import java.util.Arrays;
import java.util.List;

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


    private List<Runnable> getGoogleSearchAssertions() {
        return Arrays.asList(
                () -> Assert.assertTrue("Google query is not visible", page.validateElementVisible(QUERY)),
                () -> Assert.assertTrue("Google search button is not visible", page.validateElementVisible(SEARCH_BUTTON))
        );
    }
}
