package toolkit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

/**
 * Created by skashapov on 28.09.16.
 */

@Configuration
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
@PropertySource(value = "classpath:configs/${configName}.yml", ignoreResourceNotFound = true)
public class StageConfig {

    @Value("${baseUrl}")
    public String BASE_URL;


}
