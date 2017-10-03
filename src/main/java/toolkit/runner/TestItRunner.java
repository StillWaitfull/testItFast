package toolkit.runner;

import org.junit.AssumptionViolatedException;
import org.junit.Ignore;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import toolkit.driver.LocalDriverManager;

public class TestItRunner extends BlockJUnit4ClassRunner {
    private int failedAttempts = 0;


    public TestItRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.addListener(new JUnitExecutionListener());
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
                if (LocalDriverManager.getDriverController() != null)
                    LocalDriverManager.getDriverController().deleteAllCookies();
                failedAttempts++;
                caughtThrowable = t;
            }
        }
        notifier.addFailure(caughtThrowable);
    }
}
