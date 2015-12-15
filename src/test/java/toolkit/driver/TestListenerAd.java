package toolkit.driver;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

/**
 * Created by restore on 15.12.15.
 */
public class TestListenerAd extends TestListenerAdapter {

    @Override
    public void onTestFailure(ITestResult result) {
        if (result.getMethod().getRetryAnalyzer() != null) {
            RetryListener retryAnalyzer = (RetryListener)result.getMethod().getRetryAnalyzer();

            if(retryAnalyzer.isRetryAvailable()) {
                result.setStatus(ITestResult.SKIP);
            } else {
                result.setStatus(ITestResult.FAILURE);
            }
            Reporter.setCurrentTestResult(result);
        }
    }
}
