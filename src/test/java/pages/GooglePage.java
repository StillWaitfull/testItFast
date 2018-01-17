package pages;

import composite.IPage;
import org.junit.Assert;
import org.openqa.selenium.By;
import toolkit.helpers.ActionsHelper;


public class GooglePage extends ActionsHelper {

    private static final String pageUrl = baseUrl + "/";


    private static final By QUERY = By.id("lst-ib");


    @Override
    public String getPageUrl() {
        return pageUrl;
    }

    @Override
    public IPage openPage() {
        openUrl(pageUrl);
        return this;
    }


    public static Runnable[] getGooglePageAssertions(IPage page) {
        return new Runnable[]{
                () -> Assert.assertTrue("Google QUERY is not visible", page.validateElementVisible(GooglePage.QUERY))
        };
    }

    public IPage typeTextToQueryField(String text) {
        type(QUERY, text);
        return this;
    }

}
