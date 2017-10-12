package configs;

import common.Platform;
import org.openqa.selenium.Dimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

@Configuration
public class PlatformConfig {
    private String browser;
    private String dimensionH;
    private String dimensionW;
    private Platform.PLATFORM platform;
    private String platformVersion;
    private String deviceName;
    private String mobileBrowser;
    private String udid;
    private String addressHub;
    private ApplicationConfig applicationConfig;
    private static volatile PlatformConfig staticConfig;

    public static void setPlatformConfig(PlatformConfig staticConfig) {
        if (staticConfig != null && PlatformConfig.staticConfig != null && PlatformConfig.staticConfig != staticConfig)
            throw new RuntimeException("Конфигурация платформы уже задана");
        PlatformConfig.staticConfig = staticConfig;
    }

    @Autowired
    public PlatformConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    public PlatformConfig(String browser,
                          String dimensionH,
                          String dimensionW,
                          Platform.PLATFORM platform,
                          String platformVersion,
                          String deviceName,
                          String mobileBrowser,
                          String udid,
                          String address) {
        this.browser = browser;
        this.dimensionH = dimensionH;
        this.dimensionW = dimensionW;
        this.platform = platform;
        this.platformVersion = platformVersion;
        this.deviceName = deviceName;
        this.mobileBrowser = mobileBrowser;
        this.udid = udid;
        this.addressHub = address;
    }

    public PlatformConfig(String browser,
                          String dimensionH,
                          String dimensionW,
                          Platform.PLATFORM platform,
                          String address) {
        this.browser = browser;
        this.dimensionH = dimensionH;
        this.dimensionW = dimensionW;
        this.platform = platform;
        this.addressHub = address;
    }


    @Bean
    @Lazy
    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
    public Platform determinePlatform() {
        Platform platform4Test = new Platform();
        if (staticConfig != null) {
            browser = staticConfig.browser;
            dimensionH = staticConfig.dimensionH;
            dimensionW = staticConfig.dimensionW;
            platform = staticConfig.platform;
            platformVersion = staticConfig.platformVersion;
            deviceName = staticConfig.deviceName;
            mobileBrowser = staticConfig.mobileBrowser;
            udid = staticConfig.udid;
            addressHub = staticConfig.addressHub;
        }
        //IsRemote
        String remoteEnv = System.getenv("remote");
        platform4Test.setRemote(remoteEnv == null ? applicationConfig.REMOTE : Boolean.parseBoolean(remoteEnv));

        //Dimension for assertions
        Dimension dimension = determineDimension(dimensionH, dimensionW);

        //Set Platform
        if (platform == null) platform = Platform.PLATFORM.valueOf(applicationConfig.PLATFORM.toUpperCase());

        if (udid != null && deviceName != null)
            platform4Test.setMobile(
                    platform,
                    platformVersion,
                    deviceName,
                    mobileBrowser,
                    udid,
                    addressHub,
                    dimension);

        else if (platform.equals(Platform.PLATFORM.ANDROID) || platform.equals(Platform.PLATFORM.IOS))
            platform4Test.setMobile(
                    platform,
                    applicationConfig.MOBILE_PLATFORM_VERSION,
                    applicationConfig.MOBILE_DEVICE_NAME,
                    applicationConfig.BROWSER,
                    applicationConfig.UDID,
                    applicationConfig.HUB_ADDRESS,
                    dimension);

        else platform4Test.setDesktop(dimension, determineBrowser(browser), applicationConfig.HUB_ADDRESS);
        return platform4Test;
    }


    private String determineBrowser(String browser) {
        String browserEnv = System.getenv("browser");
        if (browser == null && browserEnv == null)
            browser = applicationConfig.BROWSER;
        if (browserEnv != null)
            browser = browserEnv;
        return browser;
    }


    private Dimension determineDimension(String dimensionH, String dimensionW) {
        Dimension dimension;
        if (dimensionH != null && dimensionW != null)
            dimension = new Dimension(Integer.parseInt(dimensionW), Integer.parseInt(dimensionH));
        else {
            if (applicationConfig.DIMENSION_W.isEmpty() && applicationConfig.DIMENSION_H.isEmpty())
                throw new RuntimeException("You should set dimension for test in configs");
            else
                dimension = new Dimension(Integer.parseInt(applicationConfig.DIMENSION_W), Integer.parseInt(applicationConfig.DIMENSION_H));
        }
        return dimension;
    }
}
