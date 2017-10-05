package toolkit.runner;

import org.junit.runner.Computer;
import org.junit.runner.Runner;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.junit.runners.model.RunnerScheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelClassAndMethodsComputer extends Computer {
    private final boolean classes;
    private static int threadSizeForMethods;
    private static int threadSizeForClasses;
    private final boolean methods;

    public ParallelClassAndMethodsComputer(boolean classes, boolean methods, int threadSizeForMethods, int threadSizeForClasses) {
        this.classes = classes;
        this.methods = methods;
        ParallelClassAndMethodsComputer.threadSizeForMethods = threadSizeForMethods;
        ParallelClassAndMethodsComputer.threadSizeForClasses = threadSizeForClasses;
    }

    public static Computer classes() {
        return new org.junit.experimental.ParallelComputer(true, false);
    }

    public static Computer methods() {
        return new org.junit.experimental.ParallelComputer(false, true);
    }

    private static Runner parallelize(Runner runner, ExecutorService fService) {
        if (runner instanceof ParentRunner) {
            ((ParentRunner<?>) runner).setScheduler(new RunnerScheduler() {
                public void schedule(Runnable childStatement) {
                    fService.submit(childStatement);
                }

                public void finished() {
                    try {
                        fService.shutdown();
                        fService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace(System.err);
                    }
                }
            });
        }
        return runner;
    }

    @Override
    public Runner getSuite(RunnerBuilder builder, Class<?>[] classes)
            throws InitializationError {
        Runner suite = super.getSuite(builder, classes);
        return this.classes ? parallelize(suite, Executors.newFixedThreadPool(threadSizeForClasses)) : suite;
    }

    @Override
    protected Runner getRunner(RunnerBuilder builder, Class<?> testClass)
            throws Throwable {
        Runner runner = super.getRunner(builder, testClass);
        return methods ? parallelize(runner, Executors.newFixedThreadPool(threadSizeForMethods)) : runner;
    }
}