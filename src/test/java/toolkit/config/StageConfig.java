package toolkit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by skashapov on 28.09.16.
 */

@Configuration
@PropertySource(value = "classpath:configs/${configName}.yml")
public class StageConfig {
    @Value("${baseUrl}")
    String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

}
