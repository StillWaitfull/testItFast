package toolkit.driver;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Created by restore on 14.12.15.
 */
public class RetryListener implements IRetryAnalyzer {

    private int retryCount = 0;
    private int maxRetryCount = 1;

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            return true;
        }
        return false;
    }
}

