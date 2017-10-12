package tests.jbehaveTests;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.ScanningStepsFactory;
import org.junit.runner.RunWith;
import toolkit.runner.TestItRunner;

import java.util.List;

import static org.jbehave.core.reporters.Format.*;
@RunWith(TestItRunner.class)
public class JBehaveJunitStories extends JUnitStories {

    public JBehaveJunitStories() {
        configuredEmbedder()
                .embedderControls()
                .doGenerateViewAfterStories(true)
                .doIgnoreFailureInStories(false)
                .doIgnoreFailureInView(true)
                .doVerboseFailures(true)
                .useThreads(2);
    }

    @Override
    public Configuration configuration() {
        return new MostUsefulConfiguration()
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withCodeLocation(CodeLocations.codeLocationFromClass(GoogleSteps.class))
                        .withDefaultFormats()
                        .withFormats(CONSOLE, TXT, HTML_TEMPLATE, XML_TEMPLATE));
    }

    public InjectableStepsFactory stepsFactory() {
        return new ScanningStepsFactory(configuration(), GoogleSteps.class);
    }

    @Override
    protected List<String> storyPaths() {
        return new StoryFinder()
                .findPaths(CodeLocations
                                .codeLocationFromPath(
                                        "src/test/resources/stories/google"),
                        "**/*.story", "");
    }
}
