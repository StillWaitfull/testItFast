package toolkit.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import toolkit.CheckingDifferentImages;
import toolkit.driver.ProxyHelper;
import toolkit.driver.WebDriverListener;
import toolkit.helpers.RequestClient;

/**
 * Created by skashapov on 31.01.17.
 */
@Import({ApplicationConfig.class,
        StageConfig.class,
        PlatformConfig.class,
        ProxyHelper.class,
        BrowserConfig.class,
        WebDriverListener.class,
        RequestClient.class,
        CheckingDifferentImages.class})
@ComponentScan(value = "toolkit.driver")
public class GeneralConfig {


}
