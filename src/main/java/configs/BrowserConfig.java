package configs;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import toolkit.driver.ProxyHelper;

import java.io.File;
import java.net.URL;


@Configuration
public class BrowserConfig {

    private final ProxyHelper proxyHelper;

    private final ApplicationConfig applicationConfig;

    @Autowired
    public BrowserConfig(ProxyHelper proxyHelper, ApplicationConfig applicationConfig) {
        this.proxyHelper = proxyHelper;
        this.applicationConfig = applicationConfig;
    }

    @Lazy
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    @Bean
    public WebDriver getBrowser(common.Platform platform) {
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
                    return getDriverFF(platform.isRemote());
                }
                case "chrome": {
                    return getDriverChrome(platform.isRemote());
                }
                case "ie": {
                    return getDriverIE(platform.isRemote());
                }
                case "opera": {
                    return getDriverOpera(platform.isRemote());
                }
                case "phantom": {
                    return getDriverPhantom(platform.isRemote());
                }
                default:
                    throw new RuntimeException("There is no such driver");
            }
        }

    }


    private WebDriver getAndroid(common.Platform platform) {
        try {
            DesiredCapabilities capabilities = DesiredCapabilities.android();
            capabilities.setCapability(MobileCapabilityType.PLATFORM, platform.getPlatform());
            capabilities.setCapability(MobileCapabilityType.UDID, platform.getUdid());
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, platform.getPlatform());
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platform.getPlatformVersion());
            capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, platform.getMobileBrowser());
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, platform.getDeviceName());
            return /*platform.isRemote() ? */new RemoteWebDriver(new URL(applicationConfig.HUB_ADDRESS), capabilities) /*: new AndroidDriver(new URL(platform.getAddress()), capabilities)*/;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("There was a problem with android driver");
        }
    }


    private WebDriver getDriverFF(boolean isRemote) {
        WebDriver driver;
        try {
            DesiredCapabilities capabilitiesFF = createCapabilitiesFF();
            proxyHelper.setCapabilities(capabilitiesFF);
            driver = isRemote ? new RemoteWebDriver(new URL(applicationConfig.HUB_ADDRESS), capabilitiesFF) : new FirefoxDriver(capabilitiesFF);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("There was a problem with start firefox driver");
        }
        return driver;
    }

    private WebDriver getDriverIE(boolean isRemote) {
        WebDriver driver;
        try {
            DesiredCapabilities capabilitiesIe = DesiredCapabilities.internetExplorer();
            proxyHelper.setCapabilities(capabilitiesIe);
            capabilitiesIe.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            System.setProperty("webdriver.ie.driver", "lib" + File.separator + "IEDriverServer64.exe");
            driver = isRemote ? new RemoteWebDriver(new URL(applicationConfig.HUB_ADDRESS), capabilitiesIe) : new InternetExplorerDriver(capabilitiesIe);
        } catch (Exception e) {
            throw new RuntimeException("There was a problem with start ie driver");

        }
        return driver;
    }

    private WebDriver getDriverChrome(boolean isRemote) {
        try {
            DesiredCapabilities capabilitiesChrome = DesiredCapabilities.chrome();
            proxyHelper.setCapabilities(capabilitiesChrome);
            System.setProperty("webdriver.chrome.driver", "lib" + File.separator + "chromedriver" + OperationSystem.instance.getExecutableSuffix());
            return isRemote ? new RemoteWebDriver(new URL(applicationConfig.HUB_ADDRESS), capabilitiesChrome) : new ChromeDriver(capabilitiesChrome);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("There was a problem with start chrome driver");
        }
    }

    private WebDriver getDriverOpera(boolean isRemote) {
        try {
            DesiredCapabilities capabilitiesOpera = DesiredCapabilities.operaBlink();
            System.setProperty("webdriver.opera.driver", "lib" + File.separator + "operadriver" + OperationSystem.instance.getExecutableSuffix());
            return isRemote ? new RemoteWebDriver(new URL(applicationConfig.HUB_ADDRESS), capabilitiesOpera) : new org.openqa.selenium.opera.OperaDriver(capabilitiesOpera);
        } catch (Exception e) {
            throw new RuntimeException("There was a problem with start opera driver");
        }
    }

    private WebDriver getDriverPhantom(boolean isRemote) {
        WebDriver driver;
        try {
            DesiredCapabilities capabilitiesPhantom = createCapabilitiesPhantom();
            proxyHelper.setCapabilities(capabilitiesPhantom);
            driver = isRemote ? new RemoteWebDriver(new URL(applicationConfig.HUB_ADDRESS), capabilitiesPhantom) : new PhantomJSDriver(capabilitiesPhantom);
        } catch (Exception e) {
            throw new RuntimeException("There was a problem with start phantom driver");
        }
        return driver;
    }


    private DesiredCapabilities createCapabilitiesFF() {
        DesiredCapabilities capabilitiesFF = new DesiredCapabilities();
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


    private DesiredCapabilities createCapabilitiesPhantom() {
        String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36";
        DesiredCapabilities capabilitiesPhantom = DesiredCapabilities.phantomjs();
        String[] phantomArgs = new String[]{"--webdriver-loglevel=NONE"};
        capabilitiesPhantom.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomArgs);
        capabilitiesPhantom.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "lib" + File.separator + "phantomjs" + OperationSystem.instance.getExecutableSuffix());
        capabilitiesPhantom.setCapability("phantomjs.page.settings.userAgent", userAgent);
        return capabilitiesPhantom;
    }
}
