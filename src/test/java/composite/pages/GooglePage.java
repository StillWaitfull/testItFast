package composite.pages;

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
public class GooglePage extends OperationsHelper implements IPage {

    private static final String pageUrl = baseUrl + "/";


    public static final By query = By.id("lst-ib");
    public static final By button = By.id("sblsbb");


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
                () -> Assert.assertTrue(page.isVisible(GooglePage.button), "Google button is not visible"),
                () -> Assert.assertTrue(page.isVisible(GooglePage.query), "Google query is not visible")
        };
    }

}
