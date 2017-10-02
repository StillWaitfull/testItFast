package tests.cucumberTests;

import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;
import toolkit.runner.CucumberRunner;

@RunWith(CucumberRunner.class)
@CucumberOptions(
        plugin = {
                "pretty",
                "json:target/cucumber-report/cucumber.json",
                "html:target/cucumber-report/cucumber.html"},
        features = "src/test/resources/features/google/")
public class GoogleCucumberTest {


}
