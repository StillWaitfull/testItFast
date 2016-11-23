package toolkit.driver;

import com.google.common.base.Throwables;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import ru.yandex.qatools.allure.annotations.Attachment;
import toolkit.IsKnownBug;
import toolkit.config.Platform;
import toolkit.helpers.OperationsHelper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

import static toolkit.helpers.Context.applicationConfig;
import static toolkit.helpers.Context.applicationContext;


public class WebDriverListener extends TestListenerAdapter implements IInvokedMethodListener {
    private Logger logger = LoggerFactory.getLogger(WebDriverListener.class);

    private static ConcurrentSkipListSet<Integer> invocateds = new ConcurrentSkipListSet<>();

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        String methodName = method.getTestMethod().getMethodName();
        if (method.isTestMethod()) {
            if (!RetryListener.get().getNameMethod().equals(methodName))
                RetryListener.get().count = new AtomicInteger(RetryListener.maxRetryCount);
            RetryListener.get().setNameMethod(methodName);
        }
        if (LocalDriverManager.getDriverController() == null && method.isTestMethod() && !methodName.contains("NotDriver")) {
            applicationContext.getBean(WebDriverController.class, determinePlatform(testResult));
        }
    }


    private Platform determinePlatform(ITestResult testResult) {
        Map<String, String> params = testResult.getTestClass().getXmlTest().getAllParameters();
        Platform platform4Test = new Platform();
        String browser = params.get("browser");
        String dimensionH = params.get("dimensionH");
        String dimensionW = params.get("dimensionW");
        String platform = params.get("platform");
        String platformVersion = params.get("platformVersion");
        String deviceName = params.get("deviceName");
        String mobileBrowser = params.get("mobileBrowser");
        String udid = params.get("udid");
        String address = params.get("address");
        Dimension dimension = determineDimension(dimensionH, dimensionW);
        if ((platform != null && deviceName != null && mobileBrowser != null) || applicationConfig.IS_MOBILE) {
            if (platform != null && udid != null)
                platform4Test.setMobile(platform, deviceName, mobileBrowser, udid, address, dimension);
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


    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (invocateds.add(System.identityHashCode(testResult))) {
            if (method.getTestMethod().getMethod().getAnnotation(IsKnownBug.class) != null && testResult.getStatus() == ITestResult.FAILURE) {
                IsKnownBug clazz = testResult.getMethod().getMethod().getAnnotation(IsKnownBug.class);
                Assert.fail(clazz.getBugUrl() + " " + clazz.getBugDescription());
            }
            if (!testResult.isSuccess() && method.isTestMethod() && testResult.getStatus() != ITestResult.SKIP && LocalDriverManager.getDriverController() != null) {
                method.getTestMethod().setRetryAnalyzer(RetryListener.get());
                if (!RetryListener.get().isRetryAvailable())
                    makeScreenshot(testResult.getName());
                logger.error(
                        "Test FAILED! Method:" + testResult.getName() + ". StackTrace is " + Throwables.getStackTraceAsString(
                                testResult.getThrowable()));
            }
        }
    }


    @Attachment(value = "{0}", type = "image/png")
    private byte[] makeScreenshot(String methodName) {
        Calendar calendar = Calendar.getInstance();
        String path = "";
        SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
        new File("target" + File.separator + "failure_screenshots" + File.separator).mkdirs();
        try {
            if (LocalDriverManager.getDriverController() != null) {
                File scrFile = ((TakesScreenshot) LocalDriverManager.getDriverController()
                        .getDriver()).getScreenshotAs(OutputType.FILE);
                path = "target" + File.separator + "failure_screenshots" +
                        File.separator + methodName + "_" + formater.format(calendar.getTime())
                        + "_" + LocalDriverManager.getDriverController().getBrowser()
                        + "_" + LocalDriverManager.getDriverController().getDimension() + "_webdriver.png";
                FileUtils.copyFile(scrFile, new File(path));
                return Files.readAllBytes(Paths.get(path));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        throw new RuntimeException("There is a problem with screenshot");

    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (result.getMethod().getRetryAnalyzer() != null) {
            RetryListener retryAnalyzer = (RetryListener) result.getMethod().getRetryAnalyzer();
            if (retryAnalyzer.isRetryAvailable()) {
                result.setStatus(ITestResult.SKIP);
            } else {
                result.setStatus(ITestResult.FAILURE);
            }
            Reporter.setCurrentTestResult(result);
        }
    }

}
