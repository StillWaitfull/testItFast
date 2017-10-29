package configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;


@Configuration
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
@PropertySource(value = "file:application.yml", ignoreResourceNotFound = true)
public class ApplicationConfig {

    @Value("${configName:live}")
    public String CONFIG_NAME;

    @Value("${platform:pc}")
    public String PLATFORM;

    @Value("${remote:false}")
    public boolean REMOTE;


    @Value("${browser:chrome}")
    public String BROWSER;


    @Value("${hubAddress:http://172.27.5.65:4444/wd/hub}")
    public String HUB_ADDRESS;

    @Value("${Timeout:10}")
    public int TIMEOUT;

    @Value("${dimensionW:1920}")
    public String DIMENSION_W;

    @Value("${dimensionH:1080}")
    public String DIMENSION_H;

    @Value("${enableProxy:true}")
    public boolean ENABLE_PROXY;


    @Value("${remoteProxyHost:172.27.5.65}")
    public String REMOTE_PROXY_HOST;

    @Value("${proxyPort:7000}")
    public int PROXY_PORT;

    @Value("${isTest:false}")
    public boolean IS_TEST;


    //MOBILE PROPERTIES

    @Value("${mobilePlatformVersion:7.0}")
    public String MOBILE_PLATFORM_VERSION;

    @Value("${mobileDeviceName:phone}")
    public String MOBILE_DEVICE_NAME;

    @Value("${udid:emulator-5554}")
    public String UDID;


    private final ConfigurableEnvironment env;

    @Autowired
    public ApplicationConfig(ConfigurableEnvironment env) {
        this.env = env;
    }

    @PostConstruct
    public void init() {
        try {
            String stage = System.getenv("stage");
            if (stage == null) stage = CONFIG_NAME;
            env.getPropertySources().addFirst(new ResourcePropertySource("configs" + File.separator + stage + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
