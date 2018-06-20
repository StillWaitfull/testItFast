package tests.suites;

import configs.PlatformConfig;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openqa.selenium.Dimension;
import tests.simpleTests.GoogleTest;
import toolkit.runner.ParallelClassAndMethodsSuite;

import static common.Platform.PC;


@RunWith(ParallelClassAndMethodsSuite.class)
@Suite.SuiteClasses({GoogleTest.class})
public class GoogleSuite {

    private final static PlatformConfig PLATFORM_CONFIG = new PlatformConfig(
            "chrome",
            new Dimension(1920,800),
            PC);

    @BeforeClass
    public static void setUp() {
        PlatformConfig.setConfigToThread(PLATFORM_CONFIG);
    }

}
