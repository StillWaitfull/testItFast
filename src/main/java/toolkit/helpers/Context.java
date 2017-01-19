package toolkit.helpers;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import toolkit.config.ApplicationConfig;
import toolkit.config.BrowserConfig;
import toolkit.config.StageConfig;

import java.util.HashMap;

/**
 * Created by skashapov on 17.10.16.
 */
public class Context {

    public static ApplicationContext applicationContext;
    public static ApplicationConfig applicationConfig;
    public static StageConfig stageConfig;


    static {
        applicationContext = new AnnotationConfigApplicationContext(BrowserConfig.class);
        applicationConfig = applicationContext.getBean(ApplicationConfig.class);
        String stage = applicationConfig.CONFIG_NAME;
        if (System.getenv("stage") != null) stage = System.getenv("stage");
        ConfigurableEnvironment environment = (ConfigurableEnvironment) applicationContext.getEnvironment();
        String finalStage = stage;
        environment.getPropertySources().addFirst(new MapPropertySource("configName", new HashMap<String, Object>() {{
            put("configName", finalStage);
        }}));
        stageConfig = applicationContext.getBean(StageConfig.class);
    }
}