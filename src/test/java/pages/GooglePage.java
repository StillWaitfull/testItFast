package pages;

import composite.IPage;
import org.openqa.selenium.By;
import org.testng.Assert;
import toolkit.helpers.OperationsHelper;


public class GooglePage extends OperationsHelper {

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
                () -> Assert.assertTrue(page.validateElementVisible(GooglePage.QUERY), "Google QUERY is not visible")
        };
    }

    public IPage typeTextToQueryField(String text) {
        type(QUERY, text);
        return this;
    }

}
