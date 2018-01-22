package tests.suites;

import configs.PlatformConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import tests.simpleTests.GoogleTest;
import toolkit.runner.ParallelClassAndMethodsComputer;

import static common.Platform.PLATFORM.PC;

public class SuiteWithComputer {


    @Test
    public void runParallel() {
        PlatformConfig fullScreen = new PlatformConfig("chrome",
                "800",
                "1920",
                PC);
        PlatformConfig.setConfigToThread(fullScreen);
        boolean result = JUnitCore.runClasses(new ParallelClassAndMethodsComputer(
                        true,
                        true,
                        5,
                        3),
                GoogleTest.class)
                .wasSuccessful();
        Assert.assertTrue(result);
    }


}
