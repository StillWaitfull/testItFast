package toolkit.config;

import common.OperationSystem;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.context.annotation.*;
import toolkit.driver.ProxyHelper;

import java.io.File;
import java.net.URL;

import static toolkit.helpers.Context.applicationConfig;

/**
 * Created by skashapov on 27.09.16.
 */
@Import({ApplicationConfig.class, StageConfig.class, PlatformConfig.class})
@ComponentScan(value = "toolkit.driver")
@Configuration
public class BrowserConfig {

    @Lazy
    @Scope("prototype")
    @Bean(name = "pc")
    public WebDriver getBrowser(toolkit.config.Platform platform) {
        if (platform.isMobile()) {
            switch (platform.getPlatform()) {
                case ANDROID:
                    return getAndroid(platform);
                default:
                    throw new RuntimeException("There is no such platform for mobile");
            }

        } else {
            switch (platform.getBrowser()) {
                case "firefox": {
                    return getDriverFF();
                }
                case "chrome": {
                    return getDriverChrome();
                }
                case "ie": {
                    return getDriverIE();
                }
                case "opera": {
                    return getDriverOpera();
                }
                case "phantom": {
                    return getDriverPhantom();
                }

                default:
                    throw new RuntimeException("There is no such driver");
            }
        }

    }


    private WebDriver getAndroid(toolkit.config.Platform platform) {
        try {
            DesiredCapabilities capabilities = DesiredCapabilities.android();
            capabilities.setCapability(MobileCapabilityType.PLATFORM, platform.getPlatform());
            capabilities.setCapability(MobileCapabilityType.UDID, platform.getUdid());
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, platform.getPlatform());
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platform.getPlatformVersion());
            capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, platform.getMobileBrowser());
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, platform.getDeviceName());
            return new RemoteWebDriver(new URL(platform.getAddress()), capabilities);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("There was a problem with android driver");
        }
    }


    private WebDriver getDriverFF() {
        WebDriver driver;
        try {
            DesiredCapabilities capabilitiesFF = createCapabilitiesFF();
            driver = new FirefoxDriver(capabilitiesFF);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("There was a problem with start firefox driver");
        }
        return driver;
    }

    private WebDriver getDriverIE() {
        WebDriver driver;
        try {
            DesiredCapabilities capabilitiesIe = DesiredCapabilities.internetExplorer();
            ProxyHelper.setCapabilities(capabilitiesIe);
            capabilitiesIe.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            System.setProperty("webdriver.ie.driver", "lib" + File.separator + "IEDriverServer64.exe");
            driver = new InternetExplorerDriver(capabilitiesIe);
        } catch (Exception e) {
            throw new RuntimeException("There was a problem with start ie driver");

        }
        return driver;
    }

    private WebDriver getDriverChrome() {
        try {
            DesiredCapabilities capabilitiesChrome = DesiredCapabilities.chrome();
            ProxyHelper.setCapabilities(capabilitiesChrome);
            System.setProperty("webdriver.chrome.driver", "lib" + File.separator + "chromedriver" + OperationSystem.instance.getExecutableSuffix());
            return new ChromeDriver(capabilitiesChrome);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("There was a problem with start chrome driver");
        }
    }

    private WebDriver getDriverOpera() {
        try {
            DesiredCapabilities capabilitiesOpera = DesiredCapabilities.operaBlink();
            capabilitiesOpera.setCapability("opera.arguments", "-fullscreen");
            capabilitiesOpera.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            return new org.openqa.selenium.opera.OperaDriver(capabilitiesOpera);
        } catch (Exception e) {
            throw new RuntimeException("There was a problem with start opera driver");
        }
    }

    private WebDriver getDriverPhantom() {
        WebDriver driver;
        try {
            driver = new PhantomJSDriver(createCapabilitiesPhantom());
        } catch (Exception e) {
            throw new RuntimeException("There was a problem with start phantom driver");
        }
        return driver;
    }


    private static DesiredCapabilities createCapabilitiesFF() {
        DesiredCapabilities capabilitiesFF = new DesiredCapabilities();
        ProxyHelper.setCapabilities(capabilitiesFF);
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        System.setProperty("webdriver.gecko.driver", "lib" + File.separator + "geckodriver" + OperationSystem.instance.getExecutableSuffix());
        String versionFirebug = applicationConfig.FIREBUG_VERSION;
        if (applicationConfig.IS_FIREBUG) {
            firefoxProfile.addExtension(new File("src" + File.separator + "test" +
                    File.separator + "resources" + File.separator + "extensions" +
                    File.separator + "firebug-" + versionFirebug + ".xpi"));
            firefoxProfile.setPreference("extensions.firebug.currentVersion", versionFirebug); // Avoid startup screen
        }
        firefoxProfile.setAcceptUntrustedCertificates(true);
        firefoxProfile.setAssumeUntrustedCertificateIssuer(false);
        firefoxProfile.setPreference("browser.download.folderList", 2);
        firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false);
        firefoxProfile.setPreference("intl.accept_languages", "ru");
        firefoxProfile.setPreference("general.useragent.local", "ru");
        firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/octet-stream");
        capabilitiesFF.setBrowserName("firefox");
        capabilitiesFF.setPlatform(Platform.ANY);
        capabilitiesFF.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
        capabilitiesFF.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
        return capabilitiesFF;
    }


    private static DesiredCapabilities createCapabilitiesPhantom() {
        String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36";
        DesiredCapabilities capabilitiesPhantom = DesiredCapabilities.phantomjs();
        String[] phantomArgs = new String[]{"--webdriver-loglevel=NONE"};
        capabilitiesPhantom.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomArgs);
        capabilitiesPhantom.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "lib" + File.separator + "phantomjs" + OperationSystem.instance.getExecutableSuffix());
        capabilitiesPhantom.setCapability("phantomjs.page.settings.userAgent", userAgent);
        return capabilitiesPhantom;
    }
}
