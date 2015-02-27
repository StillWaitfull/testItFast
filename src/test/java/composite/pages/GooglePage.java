package composite.pages;

import composite.IPage;
import org.openqa.selenium.By;
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
    public static final By button = By.id("gbqfb");


    @Override
    public String getPageUrl() {
        return pageUrl;
    }

    @Override
    public IPage openPage() {
        openUrl(pageUrl);
        return this;
    }


}
