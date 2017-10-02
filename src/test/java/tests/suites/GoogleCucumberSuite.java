package tests.suites;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import tests.cucumberTests.GoogleCucumberTest;

public class GoogleCucumberSuite {

    @Test
    public void runParallel() {
        JUnitCore.runClasses(GoogleCucumberTest.class);
    }

}
