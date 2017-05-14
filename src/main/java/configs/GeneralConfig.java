package configs;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;



@ComponentScan(basePackages = {"configs", "toolkit"})
public class GeneralConfig {
    public static ApplicationContext applicationContext;
    public static String baseUrl;

    static {
        applicationContext = new AnnotationConfigApplicationContext(GeneralConfig.class);
        String envBaseUrl = System.getenv("baseUrl");
        baseUrl = (envBaseUrl == null) ? applicationContext.getBean(StageConfig.class).BASE_URL : envBaseUrl;
        baseUrl = StringUtils.removeEnd(baseUrl, "/");
    }

}
