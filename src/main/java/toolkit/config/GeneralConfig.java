package toolkit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import toolkit.CheckingDifferentImages;
import toolkit.driver.ProxyHelper;
import toolkit.driver.WebDriverListener;
import common.RequestClient;

import java.util.HashMap;

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
    public static ApplicationContext applicationContext;

    @Autowired
    public void initContext(ApplicationContext applicationContext, ApplicationConfig applicationConfig) {
        String stage = applicationConfig.CONFIG_NAME;
        if (System.getenv("stage") != null) stage = System.getenv("stage");
        ConfigurableEnvironment environment = (ConfigurableEnvironment) applicationContext.getEnvironment();
        String finalStage = stage;
        environment.getPropertySources().addFirst(new MapPropertySource("configName", new HashMap<String, Object>() {{
            put("configName", finalStage);
        }}));
        GeneralConfig.applicationContext = applicationContext;
    }

}
