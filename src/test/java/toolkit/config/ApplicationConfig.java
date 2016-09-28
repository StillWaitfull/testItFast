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
    private String configName;

    @Value("${browser}")
    private String browser;

    @Value("${firebug-version}")
    private String isFirebug;

    @Value("${firebug-version}")
    private String firebugVersion;

    @Value("${Timeout}")
    private String timeout;

    @Value("${dimensionH}")
    private String dimensionH;

    @Value("${dimensionW}")
    private String dimensionW;

    @Value("${enableProxy}")
    private String enableProxy;

    @Value("${proxyPort}")
    private String proxyPort;

    @Value("${isTest}")
    private String isTest;


    public String getConfigName() {
        return configName;
    }

    public String getBrowser() {
        return browser;
    }

    public String getIsFirebug() {
        return isFirebug;
    }

    public String getFirebugVersion() {
        return firebugVersion;
    }

    public String getTimeout() {
        return timeout;
    }

    public String getDimensionH() {
        return dimensionH;
    }

    public String getDimensionW() {
        return dimensionW;
    }

    public String getEnableProxy() {
        return enableProxy;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public String getIsTest() {
        return isTest;
    }


}
