package configs;

import common.Platform;
import org.openqa.selenium.Dimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Configuration
public class PlatformConfig {
    private String browser;
    private String dimensionH;
    private String dimensionW;
    private String platform;
    private String platformVersion;
    private String deviceName;
    private String mobileBrowser;
    private String udid;
    private String addressAppium;
    private ApplicationConfig applicationConfig;
    private static PlatformConfig staticConfig;

    public static void setPlatformConfig(PlatformConfig staticConfig) {
        if (staticConfig!=null && PlatformConfig.staticConfig != null && PlatformConfig.staticConfig != staticConfig)
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
                          String platform,
                          String platformVersion,
                          String deviceName,
                          String mobileBrowser,
                          String udid,
                          String addressAppium) {
        this.browser = browser;
        this.dimensionH = dimensionH;
        this.dimensionW = dimensionW;
        this.platform = platform;
        this.platformVersion = platformVersion;
        this.deviceName = deviceName;
        this.mobileBrowser = mobileBrowser;
        this.udid = udid;
        this.addressAppium = addressAppium;
    }


    @Bean
    @Lazy
    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
    public Platform determinePlatform() {
        Platform platform4Test = new Platform();
        if (staticConfig != null) {
            PlatformConfig platformConfig = staticConfig;
            browser = platformConfig.browser;
            dimensionH = platformConfig.dimensionH;
            dimensionW = platformConfig.dimensionW;
            platform = platformConfig.platform;
            platformVersion = platformConfig.platformVersion;
            deviceName = platformConfig.deviceName;
            mobileBrowser = platformConfig.mobileBrowser;
            udid = platformConfig.udid;
            addressAppium = platformConfig.addressAppium;
        }
        String remoteEnv = System.getenv("remote");
        platform4Test.setRemote(remoteEnv == null ? applicationConfig.REMOTE : Boolean.parseBoolean(remoteEnv));
        Dimension dimension = determineDimension(dimensionH, dimensionW);
        if (udid != null && deviceName != null)
            platform4Test.setMobile(platform,
                    platformVersion,
                    deviceName,
                    mobileBrowser,
                    udid,
                    addressAppium,
                    dimension);
        else if (applicationConfig.IS_MOBILE)
            platform4Test.setMobile(applicationConfig.MOBILE_PLATFORM,
                    applicationConfig.MOBILE_PLATFORM_VERSION,
                    applicationConfig.MOBILE_DEVICE_NAME,
                    applicationConfig.MOBILE_BROWSER,
                    applicationConfig.UDID,
                    applicationConfig.APPIUM_ADDRESS,
                    new Dimension(Integer.parseInt(applicationConfig.DIMENSION_W), Integer.parseInt(applicationConfig.DIMENSION_H)));
        else platform4Test.setDesktop(dimension, determineBrowser(browser));
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
