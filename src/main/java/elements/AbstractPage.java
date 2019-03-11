package elements;

import configs.StageConfig;

public abstract class AbstractPage extends AbstractActions {
    protected static final String BASE_URL = StageConfig.getInstance().getBaseUrl();

    public abstract AbstractPage openPage();

    public abstract String getPageUrl();
}
