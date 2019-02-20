package pages;

import components.GoogleSearch;
import composite.IPage;
import toolkit.helpers.AbstractPage;


public class GooglePage extends AbstractPage {

    public static final String PAGE_URL = BASE_URL + "/";

    private GoogleSearch googleSearch;

    public GoogleSearch getGoogleSearch() {
        return googleSearch;
    }

    @Override
    public String getPageUrl() {
        return PAGE_URL;
    }

    @Override
    public IPage openPage() {
        openUrl(PAGE_URL);
        initComponents();
        return this;
    }


    @Override
    protected void initComponents() {
        googleSearch = new GoogleSearch(this);
    }
}
