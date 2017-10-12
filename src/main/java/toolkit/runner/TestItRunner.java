package toolkit.runner;

import com.googlecode.junittoolbox.ParallelRunner;
import org.junit.AssumptionViolatedException;
import org.junit.Ignore;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import toolkit.driver.LocalDriverManager;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestItRunner extends ParallelRunner {
    private int failedAttempts = 0;
    private static final AtomicBoolean ADDED = new AtomicBoolean(false);
    private static final CountDownLatch COMPLETE_LATCH = new CountDownLatch(1);

    public TestItRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    private void addListener(RunNotifier notifier) {
        if (!ADDED.getAndSet(true)) {
            notifier.addFirstListener(new JUnitExecutionListener());
            COMPLETE_LATCH.countDown();
        } else {
            try {
                COMPLETE_LATCH.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run(RunNotifier notifier) {
        addListener(notifier);
        EachTestNotifier testNotifier = new EachTestNotifier(notifier,
                getDescription());
        Statement statement = classBlock(notifier);
        try {
            statement.evaluate();
        } catch (AssumptionViolatedException e) {
            testNotifier.fireTestIgnored();
        } catch (StoppedByUserException e) {
            throw e;
        } catch (Throwable e) {
            retry(testNotifier, statement, e);
        }

    }


    @Override
    protected void runChild(final FrameworkMethod method, RunNotifier notifier) {
        Description description = describeChild(method);
        if (method.getAnnotation(Ignore.class) != null) {
            notifier.fireTestIgnored(description);
        } else {
            runTestUnit(methodBlock(method), description, notifier);
        }
    }


    private void runTestUnit(Statement statement, Description description,
                             RunNotifier notifier) {
        EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);
        eachNotifier.fireTestStarted();
        try {
            statement.evaluate();
        } catch (AssumptionViolatedException e) {
            eachNotifier.addFailedAssumption(e);
        } catch (Throwable e) {
            if (LocalDriverManager.getDriverController() != null)
                LocalDriverManager.getDriverController().deleteAllCookies();
            retry(eachNotifier, statement, e);
        } finally {
            eachNotifier.fireTestFinished();
        }
    }

    private void retry(EachTestNotifier notifier, Statement statement, Throwable currentThrowable) {
        Throwable caughtThrowable = currentThrowable;
        int retryCount = 1;
        while (retryCount > failedAttempts) {
            try {
                statement.evaluate();
                return;
            } catch (Throwable t) {
                failedAttempts++;
                caughtThrowable = t;
            }
        }
        notifier.addFailure(caughtThrowable);
    }
}
