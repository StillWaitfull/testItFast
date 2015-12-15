package toolkit.driver;

import org.apache.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by restore on 14.12.15.
 */
public class RetryListener implements IRetryAnalyzer {

    private int maxRetryCount = 2;
    AtomicInteger count = new AtomicInteger(maxRetryCount);

    Logger logger = Logger.getLogger(RetryListener.class);

    @Override
    public boolean retry(ITestResult result) {
        boolean retry = false;
        if (isRetryAvailable()) {
            logger.info("Going to retry test case: " + result.getMethod() + ", " + (maxRetryCount - count.intValue() + 1) + " out of " + maxRetryCount);
            retry = true;
            count.decrementAndGet();
        }
        return retry;

    }

    public boolean isRetryAvailable() {
        return (count.intValue() > 0);
    }

    static ThreadLocal<RetryListener> retryListenerThreadLocal = new ThreadLocal<>();

    public static RetryListener get() {
        if (retryListenerThreadLocal.get() == null) {
            retryListenerThreadLocal.set(new RetryListener());
        }
        return retryListenerThreadLocal.get();

    }
}

