package toolkit.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by restore on 14.12.15.
 */
public class RetryListener implements IRetryAnalyzer {

    static int maxRetryCount = 1;
    AtomicInteger count = new AtomicInteger(maxRetryCount);
    private Logger logger = LoggerFactory.getLogger(RetryListener.class);
    private static ThreadLocal<RetryListener> retryListenerThreadLocal = new ThreadLocal<>();
    private String nameMethod = "";


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

    boolean isRetryAvailable() {
        return (count.intValue() > 0);
    }


    static RetryListener get() {
        if (retryListenerThreadLocal.get() == null) {
            retryListenerThreadLocal.set(new RetryListener());
        }
        return retryListenerThreadLocal.get();

    }

    String getNameMethod() {
        return nameMethod;
    }

    void setNameMethod(String nameMethod) {
        this.nameMethod = nameMethod;
    }
}

