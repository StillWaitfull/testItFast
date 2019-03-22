package components;

import elements.AbstractPage;
import elements.Component;
import org.junit.Assert;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.List;

public class GoogleSearch implements Component {

    private static final By SEARCH_FORM=By.cssSelector("#searchform");
    private static final By QUERY = By.cssSelector("input[name=q]");
    private static final By SEARCH_BUTTON = By.xpath("(//input[@name='btnK'])[last()]");
    private AbstractPage page;


    @Override
    public By getBaseLocator() {
        return SEARCH_FORM;
    }

    @Override
    public AbstractPage getPage() {
        return page;
    }

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
