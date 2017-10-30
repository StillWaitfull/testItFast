package configs;

import common.OperationSystem;
import io.appium.java_client.android.AndroidDriver;
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
import toolkit.driver.LocalDriverManager;
import toolkit.driver.ProxyHelper;
import toolkit.driver.WebDriverController;

import java.io.File;
import java.net.URL;


@Configuration
public class BrowserConfig {

    private final ProxyHelper proxyHelper;

    @Autowired
    public BrowserConfig(ProxyHelper proxyHelper) {
        this.proxyHelper = proxyHelper;
    }


    @Lazy
    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    WebDriverController getWebDriverController(Platform platform, ApplicationConfig applicationConfig) {
        if (LocalDriverManager.getDriverController() == null)
            return new WebDriverController(platform, getBrowser(platform), applicationConfig.TIMEOUT);
        else return LocalDriverManager.getDriverController();
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
                    return getDriverFF(platform);
                }
                case "chrome": {
                    return getDriverChrome(platform);
                }
                case "ie": {
                    return getDriverIE(platform);
                }
                case "opera": {
                    return getDriverOpera(platform);
                }
                case "phantom": {
                    return getDriverPhantom(platform);
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
            capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, platform.getBrowser());
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, platform.getDeviceName());
            return platform.isRemote() ? new RemoteWebDriver(new URL(platform.getAddress()), capabilities) : new AndroidDriver(new URL(platform.getAddress()), capabilities);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("There was a problem with android driver");
        }
    }


    private WebDriver getDriverFF(common.Platform platform) {
        WebDriver driver;
        try {
            DesiredCapabilities capabilitiesFF = createCapabilitiesFF();
            proxyHelper.setCapabilities(capabilitiesFF);
            driver = platform.isRemote() ? new RemoteWebDriver(new URL(platform.getAddress()), capabilitiesFF) : new FirefoxDriver(capabilitiesFF);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("There was a problem with start firefox driver");
        }
        return driver;
    }

    private WebDriver getDriverIE(common.Platform platform) {
        WebDriver driver;
        try {
            DesiredCapabilities capabilitiesIe = DesiredCapabilities.internetExplorer();
            proxyHelper.setCapabilities(capabilitiesIe);
            capabilitiesIe.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            System.setProperty("webdriver.ie.driver", "lib" + File.separator + "IEDriverServer64.exe");
            driver = platform.isRemote() ? new RemoteWebDriver(new URL(platform.getAddress()), capabilitiesIe) : new InternetExplorerDriver(capabilitiesIe);
        } catch (Exception e) {
            throw new RuntimeException("There was a problem with start ie driver");

        }
        return driver;
    }

    private WebDriver getDriverChrome(common.Platform platform) {
        try {
            DesiredCapabilities capabilitiesChrome = DesiredCapabilities.chrome();
            proxyHelper.setCapabilities(capabilitiesChrome);
            System.setProperty("webdriver.chrome.driver", "lib" + File.separator + "chromedriver" + OperationSystem.instance.getExecutableSuffix());
            return platform.isRemote() ? new RemoteWebDriver(new URL(platform.getAddress()), capabilitiesChrome) : new ChromeDriver(capabilitiesChrome);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("There was a problem with start chrome driver");
        }
    }

    private WebDriver getDriverOpera(common.Platform platform) {
        try {
            DesiredCapabilities capabilitiesOpera = DesiredCapabilities.operaBlink();
            System.setProperty("webdriver.opera.driver", "lib" + File.separator + "operadriver" + OperationSystem.instance.getExecutableSuffix());
            return platform.isRemote() ? new RemoteWebDriver(new URL(platform.getAddress()), capabilitiesOpera) : new org.openqa.selenium.opera.OperaDriver(capabilitiesOpera);
        } catch (Exception e) {
            throw new RuntimeException("There was a problem with start opera driver");
        }
    }

    private WebDriver getDriverPhantom(common.Platform platform) {
        WebDriver driver;
        try {
            DesiredCapabilities capabilitiesPhantom = createCapabilitiesPhantom();
            proxyHelper.setCapabilities(capabilitiesPhantom);
            driver = platform.isRemote() ? new RemoteWebDriver(new URL(platform.getAddress()), capabilitiesPhantom) : new PhantomJSDriver(capabilitiesPhantom);
        } catch (Exception e) {
            throw new RuntimeException("There was a problem with start phantom driver");
        }
        return driver;
    }


    private DesiredCapabilities createCapabilitiesFF() {
        DesiredCapabilities capabilitiesFF = new DesiredCapabilities();
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        System.setProperty("webdriver.gecko.driver", "lib" + File.separator + "geckodriver" + OperationSystem.instance.getExecutableSuffix());
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
