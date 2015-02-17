package toolkit.driver;

import com.opera.core.systems.OperaDriver;
import common.OperationSystem;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import toolkit.helpers.YamlConfigProvider;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class WebDriverController {

    private WebDriver driver;
    private static WebDriverWait waitDriver;
    private static Logger log = Logger.getLogger(WebDriverController.class);
    public static final int TIMEOUT = Integer.parseInt(YamlConfigProvider.getAppParameters("Timeout"));


    public WebDriverController() {
        ProxyHelper.initProxy();
        setBrowser();
        driver.manage().timeouts().setScriptTimeout(TIMEOUT, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(TIMEOUT, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.switchTo();
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }


    private void setBrowser() {
        String browser = System.getenv("browser");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        ProxyHelper.setCapabilities(capabilities);
        if (browser == null) {
            browser = YamlConfigProvider.getAppParameters("browser");
        }
        switch (browser) {
            case "firefox": {
                FirefoxProfile firefoxProfile = new FirefoxProfile();
                try {
                    String versionFirebug = YamlConfigProvider.getAppParameters("firebug-version");
                    if (YamlConfigProvider.getAppParameters("firebug").equals("true")) {
                        firefoxProfile.addExtension(new File("src" + File.separator + "test" + File.separator + "resources" + File.separator + "extensions" + File.separator + "firebug-" + versionFirebug + ".xpi"));
                        firefoxProfile.setPreference("extensions.firebug.currentVersion", versionFirebug); // Avoid startup screen
                    }
                    firefoxProfile.setAcceptUntrustedCertificates(true);
                    firefoxProfile.setAssumeUntrustedCertificateIssuer(false);
                    //HUGE SPEED IS HERE
                    //    firefoxProfile.setPreference("webdriver.load.strategy", "unstable");
                    firefoxProfile.setPreference("browser.download.folderList", 2);
                    firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false);
                    firefoxProfile.setPreference("intl.accept_languages", "ru");
                    firefoxProfile.setPreference("general.useragent.local", "ru");
                    firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/octet-stream");
                    capabilities.setBrowserName("firefox");
                    capabilities.setPlatform(Platform.ANY);
                    //capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                    capabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
                    driver = new FirefoxDriver(capabilities);

                } catch (Exception e) {
                    log.error("There was a problem with start firefox driver");
                }
                break;
            }
            case "ie":
                try {
                    System.setProperty("webdriver.ie.driver", "lib" + File.separator + "IEDriverServer64.exe");
                    DesiredCapabilities capabilitiesIe = DesiredCapabilities.internetExplorer();
                    if (ProxyHelper.needProxy) ProxyHelper.setCapabilities(capabilitiesIe);
                    capabilitiesIe.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                    driver = new InternetExplorerDriver(capabilitiesIe);
                } catch (Exception e) {
                    log.error("There was a problem with start ie driver");
                }
                break;
            case "chrome":
                try {
                    System.setProperty("webdriver.chrome.driver", OperationSystem.instance.isLinux() ? "lib" + File.separator + "chromedriver" : "lib" + File.separator + "chromedriver.exe");
                    driver = new ChromeDriver(capabilities);
                } catch (Exception e) {
                    log.error("There was a problem with start chrome driver");
                }
                break;
            case "opera": {
                capabilities.setCapability("opera.arguments", "-fullscreen");
                capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                try {
                    driver = new OperaDriver();
                } catch (Exception e) {
                    log.error("There was a problem with start opera driver");
                }
                break;
            }
            case "phantom":
                try {
                    DesiredCapabilities capabilitiesPhantom = DesiredCapabilities.phantomjs();
                    capabilitiesPhantom.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "lib" + File.separator + (OperationSystem.instance.isLinux() ? "phantomjs" : "phantomjs.exe"));
                    capabilitiesPhantom.setCapability("takesScreenshot", true);
                    driver = new PhantomJSDriver(capabilitiesPhantom);
                } catch (Exception e) {
                    log.error("There was a problem with start phantom driver");
                }
                break;
        }

    }


    public void waitForPageLoaded() {
        ExpectedCondition<Boolean> expectation = driver1 -> executeScript("return document.readyState").toString().equals("complete");
        try {
            getInstanceWaitDriver().until(expectation);
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for Page Load Request to complete.");
        }
    }


    public static WebDriverWait getInstanceWaitDriver() {
        if (waitDriver == null) {
            waitDriver = new WebDriverWait(LocalDriverManager.getDriverController().getDriver(), TIMEOUT);
        }
        return waitDriver;
    }

    public String getPageAddress() {
        return driver.getCurrentUrl();
    }

    public void goToUrl(String url) {
        driver.get(url);
        waitForPageLoaded();
    }

    /**
     * Returns WebDriver
     */
    public WebDriver getDriver() {
        return driver;
    }

    public org.openqa.selenium.Cookie getCookie(String key) {
        return driver.manage().getCookieNamed(key);
    }

    public Set<org.openqa.selenium.Cookie> getCookies() {
        return driver.manage().getCookies();
    }

    /**
     * Sends into a browser
     */
    public void navigationBack() {
        driver.navigate().back();
        waitForPageLoaded();
    }

    //Delete all cookies
    public void deleteAllCookies() {
        driver.manage().deleteAllCookies();
    }

    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    // Set a cookie
    public void addCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        driver.manage().addCookie(cookie);
    }

    public WebElement findElement(final By by) {
        return driver.findElement(by);
    }

    public List<WebElement> findElements(final By by) {
        return driver.findElements(by);
    }


    public void get(String url) {
        if (url.isEmpty()) {
            throw new IllegalArgumentException();
        }
        driver.get(url);
        waitForPageLoaded();
    }


    public void shutdown() {
        try {
            driver.quit();
            driver = null;
        } catch (Exception e) {
        }
    }

    /**
     * Executes JavaScript in the context of the currently selected frame or window. (See also {@link JavascriptExecutor})
     */
    public Object executeScript(String script, Object... args) {
        if (driver == null)
            throw new RuntimeException("Driver is null in method executeScript");
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    public Object executeAsyncScript(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeAsyncScript(script, args);
    }

    // Change the cookie
    public void changeCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        driver.manage().deleteCookieNamed(key);
        driver.manage().addCookie(cookie);
    }


    /**
     * Returns attribute value
     *
     * @param by        -
     * @param attribute -
     */
    public String getAttributeValue(By by, String attribute) {
        return driver.findElement(by).getAttribute(attribute);
    }


    /**
     * Refreshes current page
     */
    public void refresh() {
        driver.navigate().refresh();
        waitForPageLoaded();
    }

    /**
     * Switches to iFrame, if it needed
     *
     * @param iFrame - the name or id of iFrame
     */
    public void switchTo(String iFrame) {
        driver.switchTo().frame(iFrame);
    }

    public void switchToWindow(String iFrame) {
        driver.switchTo().window(iFrame);
    }

    public void closeWindow() {
        driver.close();
    }

    /**
     * Returns link to main content on the page
     */
    public void switchToMainContent() {
        driver.switchTo().defaultContent();
    }

    public class Cookie extends org.openqa.selenium.Cookie {

        public Cookie(String name, String value, String path, Date expiry) {
            super(name, value, path, expiry);
        }

        public Cookie(String name, String value, String domain, String path, Date expiry) {
            super(name, value, domain, path, expiry);
        }

        public Cookie(String name, String value, String domain, String path, Date expiry, boolean isSecure) {
            super(name, value, domain, path, expiry, isSecure);
        }

        public Cookie(String name, String value) {
            super(name, value);
        }

        public Cookie(String name, String value, String path) {
            super(name, value, path);
        }
    }


}
