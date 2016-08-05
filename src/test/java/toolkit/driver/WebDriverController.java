package toolkit.driver;

import common.OperationSystem;
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
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class WebDriverController {

    private WebDriver driver;
    public static final int TIMEOUT = Integer.parseInt(YamlConfigProvider.getAppParameters("Timeout"));
    private String browser = "";
    private Dimension dimension;

    WebDriverController(String browser, Dimension dimension) {
        this.browser = browser;
        ProxyHelper.initProxy();
        setBrowser(browser);
        driver.manage().timeouts().setScriptTimeout(TIMEOUT, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(TIMEOUT, TimeUnit.SECONDS);
        this.dimension = dimension;
        if (dimension == null) {
            driver.manage().window().maximize();
            this.dimension =  driver.manage().window().getSize();
        } else setWindowSize(dimension);
        driver.switchTo();
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }


    public void setWindowSize(Dimension dimension) {
        driver.manage().window().setSize(dimension);
    }

    public void maximizeWindow() {
        driver.manage().window().maximize();
    }

    String getBrowser() {
        return browser;
    }

    private void setBrowser(String browser) {
        if (browser == null) {
            browser = YamlConfigProvider.getAppParameters("browser");
            this.browser = browser;
        }

        switch (browser) {
            case "firefox": {
                try {
                    DesiredCapabilities capabilitiesFF = createCapabilitiesFF();
                    driver = new FirefoxDriver(capabilitiesFF);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("There was a problem with start firefox driver");
                }
                break;
            }
            case "ie":
                try {
                    DesiredCapabilities capabilitiesIe = DesiredCapabilities.internetExplorer();
                    ProxyHelper.setCapabilities(capabilitiesIe);
                    capabilitiesIe.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                    System.setProperty("webdriver.ie.driver", "lib" + File.separator + "IEDriverServer64.exe");
                    driver = new InternetExplorerDriver(capabilitiesIe);
                } catch (Exception e) {
                    throw new RuntimeException("There was a problem with start ie driver");

                }
                break;
            case "chrome":
                try {
                    DesiredCapabilities capabilitiesChrome = DesiredCapabilities.chrome();
                    ProxyHelper.setCapabilities(capabilitiesChrome);
                    System.setProperty("webdriver.chrome.driver", "lib" + File.separator + "chromedriver" + OperationSystem.instance.getExecutableSuffix());
                    driver = new ChromeDriver(capabilitiesChrome);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("There was a problem with start chrome driver");
                }
                break;
            case "opera": {
                try {
                    DesiredCapabilities capabilitiesOpera = DesiredCapabilities.operaBlink();
                    capabilitiesOpera.setCapability("opera.arguments", "-fullscreen");
                    capabilitiesOpera.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                    driver = new org.openqa.selenium.opera.OperaDriver(capabilitiesOpera);
                } catch (Exception e) {
                    throw new RuntimeException("There was a problem with start opera driver");
                }
                break;
            }
            case "phantom":
                try {
                    driver = new PhantomJSDriver(createCapabilitiesPhantom());
                } catch (Exception e) {
                    throw new RuntimeException("There was a problem with start phantom driver");
                }
                break;
        }

    }


    private static DesiredCapabilities createCapabilitiesFF() {
        DesiredCapabilities capabilitiesFF = new DesiredCapabilities();
        ProxyHelper.setCapabilities(capabilitiesFF);
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        System.setProperty("webdriver.gecko.driver", "lib" + File.separator + "geckodriver" + OperationSystem.instance.getExecutableSuffix());
        String versionFirebug = YamlConfigProvider.getAppParameters("firebug-version");
        if (YamlConfigProvider.getAppParameters("firebug").equals("true")) {
            try {
                firefoxProfile.addExtension(new File("src" + File.separator + "test" +
                        File.separator + "resources" + File.separator + "extensions" +
                        File.separator + "firebug-" + versionFirebug + ".xpi"));
                firefoxProfile.setPreference("extensions.firebug.currentVersion", versionFirebug); // Avoid startup screen
            } catch (IOException e) {
                e.printStackTrace();
            }
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


    private void waitForPageLoaded() {
        ExpectedCondition<Boolean> expectation = driver1 -> executeScript("return document.readyState").toString().equals("complete");
        try {
            getInstanceWaitDriver().until(expectation);
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for Page Load Request to complete.");
        }
    }


    public WebDriverWait getInstanceWaitDriver() {
        return new WebDriverWait(LocalDriverManager.getDriverController().getDriver(), TIMEOUT);
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


    void shutdown() {
        try {
            driver.quit();
            driver = null;
        } catch (Exception ignored) {
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

    Dimension getDimension() {
        return dimension;
    }

    String getStringDimension() {
        if (dimension != null)
            return dimension.getWidth() + "X" + dimension.getHeight();
        else return "";
    }
}
