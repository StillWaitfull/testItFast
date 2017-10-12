package tests.suites;

import com.googlecode.junittoolbox.ParallelSuite;
import configs.PlatformConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.simpleTests.GoogleTest;

import static common.Platform.PLATFORM.PC;

@RunWith(ParallelSuite.class)
@Suite.SuiteClasses({GoogleTest.class})
public class GoogleSuite {

    private final static PlatformConfig CHROME_FULL_SCREEN = new PlatformConfig(
            "chrome",
            "1920",
            "800",
            PC,
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
