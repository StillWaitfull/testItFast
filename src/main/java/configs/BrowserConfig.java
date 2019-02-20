package configs;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;


public class BrowserConfig {


    public static WebDriver getBrowser(PlatformConfig platform) {
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


    private static WebDriver getAndroid(PlatformConfig platform) {
        try {
            DesiredCapabilities desiredCapabilities = createCapabilitiesAndroid(platform);
            return platform.isRemote() ? new RemoteWebDriver(new URL(platform.getAddressHub()), desiredCapabilities) : new AndroidDriver(new URL(platform.getAddressHub()), desiredCapabilities);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("There was a problem with android driver");
        }
    }


    private static WebDriver getDriverFF(PlatformConfig platform) {
        WebDriver driver;
        try {
            FirefoxOptions capabilitiesFF = createCapabilitiesFF(platform);
            driver = platform.isRemote()
                    ? new RemoteWebDriver(new URL(platform.getAddressHub()), capabilitiesFF)
                    : new FirefoxDriver(GeckoDriverService.createDefaultService(), capabilitiesFF);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("There was a problem with start firefox driver");
        }
        return driver;
    }

    private static WebDriver getDriverIE(PlatformConfig platform) {
        WebDriver driver;
        try {
            InternetExplorerOptions capabilitiesIe = createCapabilitiesIe(platform);
            driver = platform.isRemote()
                    ? new RemoteWebDriver(new URL(platform.getAddressHub()), capabilitiesIe)
                    : new InternetExplorerDriver(capabilitiesIe);
        } catch (Exception e) {
            throw new RuntimeException("There was a problem with start ie driver");

        }
        return driver;
    }

    private static WebDriver getDriverChrome(PlatformConfig platform) {
        try {
            ChromeOptions capabilitiesChrome = createCapabilitiesChrome(platform);
            return platform.isRemote()
                    ? new RemoteWebDriver(new URL(platform.getAddressHub()), capabilitiesChrome)
                    : new ChromeDriver(capabilitiesChrome);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("There was a problem with start chrome driver");
        }
    }

    private static WebDriver getDriverOpera(PlatformConfig platform) {
        try {
            OperaOptions capabilitiesOpera = createCapabilitiesOpera(platform);
            return platform.isRemote()
                    ? new RemoteWebDriver(new URL(platform.getAddressHub()), capabilitiesOpera)
                    : new OperaDriver(capabilitiesOpera);
        } catch (Exception e) {
            throw new RuntimeException("There was a problem with start opera driver");
        }
    }

    private static WebDriver getDriverPhantom(PlatformConfig platform) {
        try {
            DesiredCapabilities capabilitiesPhantom = createCapabilitiesPhantom(platform);
            return platform.isRemote()
                    ? new RemoteWebDriver(new URL(platform.getAddressHub()), capabilitiesPhantom)
                    : new PhantomJSDriver(capabilitiesPhantom);
        } catch (Exception e) {
            throw new RuntimeException("There was a problem with start phantom driver");
        }
    }

    private static FirefoxOptions createCapabilitiesFF(PlatformConfig platform) {
        FirefoxOptions options = new FirefoxOptions();
        options.setCapability("enableVNC", true);
        options.setCapability(CapabilityType.PROXY, platform.getProxy());
        options.setCapability("marionette", true);
        if (!platform.isRemote()) WebDriverManager.firefoxdriver().setup();
        return options;
    }

    private static ChromeOptions createCapabilitiesChrome(PlatformConfig platform) {
        ChromeOptions capabilitiesChrome = new ChromeOptions();
        capabilitiesChrome.setCapability(CapabilityType.PROXY, platform.getProxy());
        capabilitiesChrome.setCapability("enableVNC", true);
        if (!platform.isRemote()) WebDriverManager.chromedriver().setup();
        return capabilitiesChrome;
    }

    private static InternetExplorerOptions createCapabilitiesIe(PlatformConfig platform) {
        InternetExplorerOptions capabilitiesIe = new InternetExplorerOptions();
        capabilitiesIe.setCapability(CapabilityType.PROXY, platform.getProxy());
        capabilitiesIe.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        if (!platform.isRemote()) WebDriverManager.iedriver().setup();
        return capabilitiesIe;
    }

    private static OperaOptions createCapabilitiesOpera(PlatformConfig platform) {
        OperaOptions capabilitiesOpera = new OperaOptions();
        if (!platform.isRemote()) WebDriverManager.chromedriver().setup();
        return capabilitiesOpera;
    }

    private static DesiredCapabilities createCapabilitiesAndroid(PlatformConfig platform) {
        DesiredCapabilities desiredCapabilities = DesiredCapabilities.android();
        desiredCapabilities.setCapability(MobileCapabilityType.UDID, platform.getUdid());
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, platform.getPlatform());
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platform.getPlatformVersion());
        desiredCapabilities.setCapability(MobileCapabilityType.BROWSER_NAME, platform.getBrowser());
        desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, platform.getDeviceName());
        return desiredCapabilities;
    }

    private static DesiredCapabilities createCapabilitiesPhantom(PlatformConfig platform) {
        String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36";
        DesiredCapabilities capabilitiesPhantom = new DesiredCapabilities();
        capabilitiesPhantom.setCapability(CapabilityType.PROXY, platform.getProxy());
        String[] phantomArgs = new String[]{"--webdriver-loglevel=NONE"};
        capabilitiesPhantom.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomArgs);
        if (!platform.isRemote()) WebDriverManager.phantomjs().setup();
        capabilitiesPhantom.setCapability("phantomjs.page.settings.userAgent", userAgent);
        return capabilitiesPhantom;
    }
}
