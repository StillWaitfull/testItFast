package toolkit.driver;

import com.google.common.base.Throwables;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.*;
import ru.yandex.qatools.allure.annotations.Attachment;
import toolkit.IsKnownBug;
import toolkit.helpers.OperationsHelper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

import static tests.AbstractTest.applicationConfig;


public class WebDriverListener extends TestListenerAdapter implements IInvokedMethodListener {
    private Logger log4j = Logger.getLogger(WebDriverListener.class);
    private static ThreadLocal<String> browser = new ThreadLocal<>();
    private Dimension dimension = null;
    private static ConcurrentSkipListSet<Integer> invocateds = new ConcurrentSkipListSet<>();

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        String methodName = method.getTestMethod().getMethodName();
        if (method.isTestMethod()) {
            if (!RetryListener.get().getNameMethod().equals(methodName))
                RetryListener.get().count = new AtomicInteger(RetryListener.maxRetryCount);
            RetryListener.get().setNameMethod(methodName);
        } else {
            if (methodName.equals("setBrowser") && testResult.getParameters()[0] != null) {
                browser.set(testResult.getParameters()[0].toString());
            }
            if (methodName.equals("setDimension") && testResult.getParameters()[0] != null && testResult.getParameters()[1] != null)
                dimension = new Dimension(Integer.valueOf((String) testResult.getParameters()[0]), Integer.valueOf((String) testResult.getParameters()[1]));
            else dimension = setDimensionFromAppConfig();
        }
        if (LocalDriverManager.getDriverController() == null && method.isTestMethod() && !methodName.contains("NotDriver")) {
            LocalDriverManager.setWebDriverController(new WebDriverController(browser.get(), dimension));
        }
    }


    private Dimension setDimensionFromAppConfig() {
        String dimensionW = applicationConfig.getDimensionW();
        String dimensionH = applicationConfig.getDimensionH();
        if (dimensionH.isEmpty() && dimensionW.isEmpty())
            return null;
        else return new Dimension(Integer.parseInt(dimensionW), Integer.parseInt(dimensionH));
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (invocateds.add(System.identityHashCode(testResult))) {
            if (method.getTestMethod().getMethod().getAnnotation(IsKnownBug.class) != null && testResult.getStatus() == ITestResult.FAILURE) {
                IsKnownBug clazz = testResult.getMethod().getMethod().getAnnotation(IsKnownBug.class);
                Assert.fail(clazz.getBugUrl() + " " + clazz.getBugDescription());
            }
            if (!testResult.isSuccess() && method.isTestMethod() && testResult.getStatus() != ITestResult.SKIP) {
                method.getTestMethod().setRetryAnalyzer(RetryListener.get());
                if (!RetryListener.get().isRetryAvailable())
                    makeScreenshot(testResult.getName());
                log4j.error(
                        "Test FAILED! Method:" + testResult.getName() + ". StackTrace is " + Throwables.getStackTraceAsString(
                                testResult.getThrowable()));
                OperationsHelper.logoutHook();
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
