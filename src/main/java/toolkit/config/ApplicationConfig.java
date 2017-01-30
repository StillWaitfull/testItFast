package toolkit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by skashapov on 28.09.16.
 */
@Configuration
@PropertySource(value = "file:application.yml", ignoreResourceNotFound = true)
public class ApplicationConfig {

    @Value("${configName:live}")
    public String CONFIG_NAME;


    @Value("${remote:false}")
    public boolean REMOTE;


    @Value("${browser:chrome}")
    public String BROWSER;


    @Value("${hubAddress:http://172.27.5.65:4444/wd/hub}")
    public String HUB_ADDRESS;

    @Value("${firebug:false}")
    public boolean IS_FIREBUG;

    @Value("${firebug-version:2.0.7}")
    public String FIREBUG_VERSION;

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

    @Value("${isMobile:false}")
    public boolean IS_MOBILE;

    @Value("${mobilePlatform:android}")
    public String MOBILE_PLATFORM;

    @Value("${mobilePlatformVersion:7.0}")
    public String MOBILE_PLATFORM_VERSION;

    @Value("${mobileBrowser:chrome}")
    public String MOBILE_BROWSER;

    @Value("${mobileDeviceName:phone}")
    public String MOBILE_DEVICE_NAME;

    @Value("${udid:emulator-5554}")
    public String UDID;

    @Value("${appiumAddress:http://127.0.0.1:4723/wd/hub}")
    public String APPIUM_ADDRESS;


}
