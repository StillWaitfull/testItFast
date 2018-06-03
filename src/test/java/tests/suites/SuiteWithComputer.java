package tests.suites;

import configs.PlatformConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.openqa.selenium.Dimension;
import tests.simpleTests.GoogleTest;
import toolkit.runner.ParallelClassAndMethodsComputer;

import static common.Platform.PC;


public class SuiteWithComputer {


    @Test
    public void runParallel() {
        PlatformConfig fullScreen = new PlatformConfig("chrome",
                new Dimension(1920,800),
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
