package pages;

import components.GoogleSearch;
import composite.IPage;
import toolkit.helpers.AbstractPage;


public class GooglePage extends AbstractPage {

    private static final String PAGE_URL = BASE_URL + "/";

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
        googleSearch = new GoogleSearch(this);
        return this;
    }


}
