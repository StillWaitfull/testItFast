package tests.suites;

import com.googlecode.junittoolbox.ParallelSuite;
import org.junit.runner.RunWith;
import tests.simpleTests.GoogleTest;
import toolkit.runner.ParallelClassAndMethodsSuite;

@RunWith(ParallelSuite.class)
@ParallelClassAndMethodsSuite.SuiteClasses({GoogleTest.class})
public class GoogleSuite {


}
