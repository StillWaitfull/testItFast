package configs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;

import java.io.File;

import static common.OperationSystem.instance;


public class BrowserConfig {


    public static WebDriver getBrowser(String browser) {
        switch (browser) {
            case "firefox": {
                return getDriverFF();
            }
            case "chrome": {
                return getDriverChrome();
            }
            default:
                throw new RuntimeException("There is no such driver");
        }
    }

    private static WebDriver getDriverFF() {
        FirefoxOptions capabilitiesFF = createCapabilitiesFF();
        return new FirefoxDriver(GeckoDriverService.createDefaultService(), capabilitiesFF);
    }


    private static WebDriver getDriverChrome() {
        ChromeOptions capabilitiesChrome = createCapabilitiesChrome();
        return new ChromeDriver(capabilitiesChrome);
    }


    private static FirefoxOptions createCapabilitiesFF() {
        FirefoxOptions options = new FirefoxOptions();
        options.setCapability("marionette", true);
        System.setProperty("webdriver.gecko.driver", "lib" + File.separator + "geckodriver" + instance.getExecutableSuffix());
        return options;
    }

    private static ChromeOptions createCapabilitiesChrome() {
        ChromeOptions capabilitiesChrome = new ChromeOptions();
        capabilitiesChrome.setCapability("enableVNC", true);
        System.setProperty("webdriver.chrome.driver", "lib" + File.separator + "chromedriver" + instance.getExecutableSuffix());
        return capabilitiesChrome;
    }

}
