package configs;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


public class StageConfig {

    @Value("${baseUrl}")
    private String baseUrl;


    public String getBaseUrl() {
        String envBaseUrl = System.getenv("baseUrl");
        baseUrl = (envBaseUrl == null) ? baseUrl : envBaseUrl;
        return StringUtils.removeEnd(baseUrl, "/");
    }
}
