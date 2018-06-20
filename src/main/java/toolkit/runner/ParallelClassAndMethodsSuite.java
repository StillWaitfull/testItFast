package toolkit.runner;

import configs.PlatformConfig;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runners.ParentRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.junit.runners.model.RunnerScheduler;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelClassAndMethodsSuite extends Suite {

    private static final int numberOfThreads = (System.getenv("numberOfThreads") == null ? 10 : Integer.parseInt(System.getenv("numberOfThreads")));
    public static final int PARALLEL_METHODS = 3;
    private final ExecutorService fService = Executors.newFixedThreadPool(numberOfThreads / PARALLEL_METHODS);


    private RunnerScheduler getSchedulerForClasses(ExecutorService executorService) {
        return new RunnerScheduler() {
            public void schedule(Runnable childStatement) {
                executorService.submit(childStatement);
            }

            public void finished() {
                try {
                    executorService.shutdown();
                    executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
            }
        };
    }

    public ParallelClassAndMethodsSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(klass, builder);
        setScheduler(getSchedulerForClasses(fService));
        List<Runner> children = getChildren();
        children.forEach(this::parallelize);
    }

    private Runner parallelize(Runner runner) {
        if (runner instanceof ParentRunner) {
            ((ParentRunner<?>) runner).setScheduler(getSchedulerForClasses(Executors.newFixedThreadPool(PARALLEL_METHODS)));
        }
        return runner;
    }

    @Override
    public Description getDescription() {
        try {
            Class currentClass = getTestClass().getJavaClass();
            PlatformConfig platformConfig = (PlatformConfig) currentClass.getField("PLATFORM_CONFIG").get(currentClass);
            PlatformConfig.setConfigToThread(platformConfig);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return super.getDescription();
    }
}
