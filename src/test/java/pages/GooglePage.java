package pages;

import components.GoogleSearch;
import elements.AbstractPage;


public class GooglePage extends AbstractPage {


    private GoogleSearch googleSearch;

    public GoogleSearch getGoogleSearch() {
        return googleSearch;
    }


    @Override
    protected void setURI() {
        URI = "/";
    }


    @Override
    protected void initComponents() {
        googleSearch = new GoogleSearch(this);
    }
}
