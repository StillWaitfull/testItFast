package elements;

import configs.StageConfig;

public abstract class AbstractPage extends AbstractActions {
    protected static final String BASE_URL = StageConfig.getInstance().getBaseUrl();
    protected String URI="/";

    protected abstract void setURI();

    public String getURI() {
        return BASE_URL+URI;
    }

    public <T extends AbstractPage> T openPage() {
        AbstractActions abstractActions = openUrl(BASE_URL + URI);
        initComponents();
        return (T) abstractActions;
    }
}
