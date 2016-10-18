package toolkit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by skashapov on 28.09.16.
 */
@Configuration
@PropertySource("file:application.yml")
public class ApplicationConfig {

    @Value("${configName}")
    public String CONFIG_NAME;

    @Value("${browser}")
    public String BROWSER;

    @Value("${firebug}")
    public boolean IS_FIREBUG;

    @Value("${firebug-version}")
    public String FIREBUG_VERSION;

    @Value("${Timeout}")
    public int TIMEOUT;

    @Value("${dimensionH}")
    public String DIMENSION_H;

    @Value("${dimensionW}")
    public String DIMENSION_W;

    @Value("${enableProxy}")
    public boolean ENABLE_PROXY;

    @Value("${proxyPort}")
    public int PROXY_PORT;

    @Value("${isTest}")
    public boolean IS_TEST;


}