package tests.suites;

import configs.PlatformConfig;
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
                PC,
                null,
                null,
                null,
                null,
                null);
        PlatformConfig.setPlatformConfig(fullScreen);
        JUnitCore.runClasses(new ParallelClassAndMethodsComputer(true, true, 5, 3),
                GoogleTest.class);
        PlatformConfig.setPlatformConfig(null);
    }


}
