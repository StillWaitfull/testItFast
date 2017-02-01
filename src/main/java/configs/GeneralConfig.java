package configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;

/**
 * Created by skashapov on 31.01.17.
 */

@ComponentScan(basePackages = {"configs", "toolkit"})
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
