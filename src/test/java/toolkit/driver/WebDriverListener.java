package toolkit.driver;

import com.google.common.base.Throwables;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.*;
import toolkit.IsKnownBug;
import toolkit.helpers.OperationsHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;


public class WebDriverListener implements IInvokedMethodListener {
    private Logger log4j = Logger.getLogger(WebDriverListener.class);
    private ThreadLocal<String> browser = new ThreadLocal<>();

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        String methodName = method.getTestMethod().getMethodName();
        if (method.isTestMethod()) {
            log4j.info(methodName);
            if (!RetryListener.get().getNameMethod().equals(methodName))
                RetryListener.get().count = new AtomicInteger(RetryListener.maxRetryCount);
            RetryListener.get().setNameMethod(methodName);
        } else {
            if (testResult.getParameters().length != 0) {
                Object br = testResult.getParameters()[0];
                if (br != null) browser.set(br.toString());
            }
        }
        if (LocalDriverManager.getDriverController() == null && method.isTestMethod()) {
            LocalDriverManager.setWebDriverController(new WebDriverController(browser.get()));
        }
    }


    private static ConcurrentSkipListSet<Integer> invocateds = new ConcurrentSkipListSet<>();

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (invocateds.add(System.identityHashCode(testResult))) {
            if (method.getTestMethod().getMethod().getAnnotation(IsKnownBug.class) != null && testResult.getStatus() == ITestResult.FAILURE) {
                IsKnownBug clazz = testResult.getMethod().getMethod().getAnnotation(IsKnownBug.class);
                Assert.fail(clazz.getBugUrl() + " " + clazz.getBugDescription());
            }
            if (!testResult.isSuccess() && method.isTestMethod() && testResult.getStatus() != 3) {
                ITestNGMethod method1 = testResult.getMethod();
                method1.setRetryAnalyzer(RetryListener.get());
                method1.getRetryAnalyzer().retry(testResult);
                makeScreenshot(testResult.getName());
                log4j.error(
                        "Test FAILED! Method:" + testResult.getName() + ". StackTrace is " + Throwables.getStackTraceAsString(
                                testResult.getThrowable()));
                OperationsHelper.logoutHook();
            }
        }
    }


    public void makeScreenshot(String methodName) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
        new File("target" + File.separator + "failure_screenshots" + File.separator).mkdirs();
        try {
            if (LocalDriverManager.getDriverController() != null) {
                File scrFile = ((TakesScreenshot) LocalDriverManager.getDriverController()
                        .getDriver()).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scrFile, new File("target" + File.separator + "failure_screenshots" +
                        File.separator + methodName + "_" + formater.format(calendar.getTime()) + "_webdriver.png"));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

}
