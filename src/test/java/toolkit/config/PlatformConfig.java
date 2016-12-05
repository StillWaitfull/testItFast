package toolkit.config;

import org.openqa.selenium.Dimension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.testng.ITestResult;

import java.util.Map;

import static toolkit.driver.WebDriverListener.testResultThreadLocal;
import static toolkit.helpers.Context.applicationConfig;

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
    private String address;

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
            address = params.get("address");
        }
        Dimension dimension = determineDimension(dimensionH, dimensionW);
        if ((platform != null && deviceName != null && mobileBrowser != null) || applicationConfig.IS_MOBILE) {
            if (platform != null && udid != null)
                platform4Test.setMobile(platform, platformVersion, deviceName, mobileBrowser, udid, address, dimension);
            else
                setPlatformAppConfig(platform4Test);
        } else {
            platform4Test.setDesktop(dimension, determineBrowser(browser));
        }
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


    private Platform setPlatformAppConfig(Platform platform) {
        platform.setMobile(applicationConfig.MOBILE_PLATFORM,
                applicationConfig.MOBILE_PLATFORM_VERSION,
                applicationConfig.MOBILE_DEVICE_NAME,
                applicationConfig.MOBILE_BROWSER,
                applicationConfig.UDID,
                applicationConfig.ADDRESS,
                new Dimension(Integer.parseInt(applicationConfig.DIMENSION_W), Integer.parseInt(applicationConfig.DIMENSION_H)));
        return platform;
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
