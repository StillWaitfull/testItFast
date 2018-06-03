package tests.suites;

import com.googlecode.junittoolbox.ParallelSuite;
import configs.PlatformConfig;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openqa.selenium.Dimension;
import tests.simpleTests.GoogleTest;

import static common.Platform.PC;


@RunWith(ParallelSuite.class)
@Suite.SuiteClasses({GoogleTest.class})
public class GoogleSuite {

    private final static PlatformConfig CHROME_FULL_SCREEN = new PlatformConfig(
            "chrome",
            new Dimension(1920,800),
            PC);

    @BeforeClass
    public static void setUp() {
        PlatformConfig.setConfigToThread(CHROME_FULL_SCREEN);
    }

}
