package toolkit.driver;

import com.google.common.base.Throwables;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import ru.yandex.qatools.allure.annotations.Attachment;
import toolkit.CheckingDifferentImages;
import toolkit.IsKnownBug;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static configs.GeneralConfig.applicationContext;


public class WebDriverListener extends TestListenerAdapter implements IInvokedMethodListener, ITestListener, ISuiteListener {
    private final Logger logger = LoggerFactory.getLogger(WebDriverListener.class);
    public static final ThreadLocal<ITestResult> testResultThreadLocal = new ThreadLocal<>();
    private static final ConcurrentSkipListSet<Integer> invocateds = new ConcurrentSkipListSet<>();


    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        testResultThreadLocal.set(testResult);
        String methodName = method.getTestMethod().getMethodName();
        if (method.isTestMethod()) {
            if (!RetryListener.get().getNameMethod().equals(methodName))
                RetryListener.get().setCount(new AtomicInteger(RetryListener.maxRetryCount));
            RetryListener.get().setNameMethod(methodName);
        }
        if (LocalDriverManager.getDriverController() == null && method.isTestMethod() && !methodName.contains("NotDriver")) {
            applicationContext.getBean(WebDriverController.class);
        }
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
    private void makeScreenshot(String methodName) {
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
                Files.readAllBytes(Paths.get(path));
                return;
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

    @Override
    public void onFinish(ITestContext var1) {
        LocalDriverManager.cleanThreadPool();
    }

    @Override
    public void onStart(ISuite iSuite) {

    }

    @Override
    public void onFinish(ISuite iSuite) {
        if (!CheckingDifferentImages.failedTests.isEmpty())
            Assert.fail("There was errors in frontend tests \n" + CheckingDifferentImages.failedTests.stream().collect(Collectors.joining("\n")));

    }

}
