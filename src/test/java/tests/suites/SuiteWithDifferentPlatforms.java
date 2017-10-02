package tests.suites;

import configs.PlatformConfig;
import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;
import tests.simpleTests.GoogleTest;

public class SuiteWithDifferentPlatforms {


    @Test
    public void runParallel() {
        PlatformConfig fullScreen = new PlatformConfig("chrome",
                "800",
                "1920",
                null,
                null,
                null,
                null,
                null,
                null);
        PlatformConfig.setPlatformConfig(fullScreen);
        JUnitCore.runClasses(ParallelComputer.methods(), GoogleTest.class);
        PlatformConfig.setPlatformConfig(null);
    }

    @Test
    public void runParallel1() {
        PlatformConfig lowScreen = new PlatformConfig("chrome",
                "800",
                "800",
                null,
                null,
                null,
                null,
                null,
                null);
        PlatformConfig.setPlatformConfig(lowScreen);
        JUnitCore.runClasses(ParallelComputer.methods(), GoogleTest.class);
        PlatformConfig.setPlatformConfig(null);
    }


}
