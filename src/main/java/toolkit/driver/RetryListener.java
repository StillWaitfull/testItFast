package toolkit.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.concurrent.atomic.AtomicInteger;


class RetryListener implements IRetryAnalyzer {

    static final int maxRetryCount = 1;
    private AtomicInteger count = new AtomicInteger(maxRetryCount);
    private final Logger logger = LoggerFactory.getLogger(RetryListener.class);
    private static final ThreadLocal<RetryListener> retryListenerThreadLocal = new ThreadLocal<>();
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

    public AtomicInteger getCount() {
        return count;
    }

    public void setCount(AtomicInteger count) {
        this.count = count;
    }
}

