package tests.suites;

import configs.PlatformConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.simpleTests.GoogleTest;
import toolkit.runner.ParallelSuite;

@RunWith(ParallelSuite.class)
@Suite.SuiteClasses({GoogleTest.class})
public class GoogleSuite {

    private final static PlatformConfig CHROME_FULL_SCREEN = new PlatformConfig(
            "chrome",
            "1920",
            "800",
            null,
            null,
            null,
            null,
            null,
            null);

    @BeforeClass
    public static void setUp() {
        PlatformConfig.setPlatformConfig(CHROME_FULL_SCREEN);
    }

    @AfterClass
    public static void tearDown() {
        PlatformConfig.setPlatformConfig(null);
    }
}
