package pages;

import composite.IPage;
import org.junit.Assert;
import org.openqa.selenium.By;
import toolkit.helpers.ActionsHelper;


public class GooglePage extends ActionsHelper {

    private static final String PAGE_URL = BASE_URL + "/";


    private static final By QUERY = By.id("lst-ib");


    @Override
    public String getPageUrl() {
        return PAGE_URL;
    }

    @Override
    public IPage openPage() {
        openUrl(PAGE_URL);
        return this;
    }


    public static Runnable getGooglePageAssertions(IPage page) {
        return () -> Assert.assertTrue("Google QUERY is not visible", page.validateElementVisible(GooglePage.QUERY));
    }

    public IPage typeTextToQueryField(String text) {
        type(QUERY, text);
        return this;
    }

}
