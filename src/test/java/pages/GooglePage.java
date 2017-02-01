package pages;

import composite.IPage;
import org.openqa.selenium.By;
import org.testng.Assert;
import toolkit.helpers.OperationsHelper;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey.Kashapov
 * Date: 14.05.14
 * Time: 11:13
 * To change this template use File | Settings | File Templates.
 */
public class GooglePage extends OperationsHelper {

    private static final String pageUrl = baseUrl + "/";


    public static final By QUERY = By.id("lst-ib");


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
                () -> Assert.assertTrue(page.isVisible(GooglePage.QUERY), "Google QUERY is not visible")
        };
    }

}
