package toolkit.config;

import common.Platform;
import org.openqa.selenium.Dimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.testng.ITestResult;

import java.util.Map;

import static toolkit.driver.WebDriverListener.testResultThreadLocal;

/**
 * Created by skashapov on 05.12.16.
 */
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

    @Autowired
    ApplicationConfig applicationConfig;

    @Bean
    @Lazy
    @Scope("prototype")
    public Platform determinePlatform() {
        ITestResult iTestResult = testResultThreadLocal.get();
        Platform platform4Test = new Platform();
        if (iTestResult != null) {
            Map<String, String> params = iTestResult.getTestClass().getXmlTest().getAllParameters();
            browser = params.get("browser");
            dimensionH = params.get("dimensionH");
            dimensionW = params.get("dimensionW");
            platform = params.get("platform");
            platformVersion = params.get("platformVersion");
            deviceName = params.get("deviceName");
            mobileBrowser = params.get("mobileBrowser");
            udid = params.get("udid");
            addressAppium = params.get("addressAppium");
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
                throw new RuntimeException("You should set dimension for test in config");
            else
                dimension = new Dimension(Integer.parseInt(applicationConfig.DIMENSION_W), Integer.parseInt(applicationConfig.DIMENSION_H));
        }
        return dimension;
    }
}
