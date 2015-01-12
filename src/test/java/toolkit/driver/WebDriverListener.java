package toolkit.driver;

import com.google.common.base.Throwables;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import toolkit.IsKnownBug;
import toolkit.helpers.OperationsHelper;

import java.util.concurrent.ConcurrentSkipListSet;


public class WebDriverListener implements IInvokedMethodListener {
    Logger log4j = Logger.getLogger(WebDriverListener.class);


    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (LocalDriverManager.getDriverController() == null && method.isTestMethod()) {
            LocalDriverManager.setWebDriverController(new WebDriverController());
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
            if (!testResult.isSuccess() && method.isTestMethod()) {
                OperationsHelper.makeScreenshot(testResult.getName());
                log4j.error(
                        "Test FAILED! Method:" + testResult.getName() + ". StackTrace is " + Throwables.getStackTraceAsString(
                                testResult.getThrowable()));
                OperationsHelper.logoutHook();
            }
        }
    }


}
