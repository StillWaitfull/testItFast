package tests.cucumberTests;

import cucumber.api.CucumberOptions;
import org.testng.annotations.Listeners;
import toolkit.driver.WebDriverListener;

@Listeners({WebDriverListener.class})
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber-html-report", "json:target/cucumber-report.json"},
        features = "src/test/resources/features/google/")
public class GoogleTests extends AbstractTestCucumber {
}
