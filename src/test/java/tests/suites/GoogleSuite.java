package tests.suites;

import configs.PlatformConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import tests.simpleTests.GoogleTest;

import java.util.ArrayList;
import java.util.Collection;

@RunWith(Parameterized.class)
public class GoogleSuite {

    private static PlatformConfig platformConfig;

    public GoogleSuite(PlatformConfig platformConfig) {
        GoogleSuite.platformConfig = platformConfig;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> setParameters() {
        Collection<Object[]> params = new ArrayList<>();
        PlatformConfig fullScreen = new PlatformConfig("chrome",
                "800",
                "1920",
                null,
                null,
                null,
                null,
                null,
                null);
        PlatformConfig lowScreen = new PlatformConfig("chrome",
                "800",
                "800",
                null,
                null,
                null,
                null,
                null,
                null);
        params.add(new Object[]{fullScreen});
        params.add(new Object[]{lowScreen});
        return params;
    }


    @BeforeClass
    public static void initPlatform() {
        PlatformConfig.setPlatformConfig(platformConfig);
    }

    @Test
    public void runParallel() {
        JUnitCore.runClasses(ParallelComputer.methods(), GoogleTest.class);
    }

    @AfterClass
    public static void deletePlatform() {
        PlatformConfig.setPlatformConfig(null);
    }

}
