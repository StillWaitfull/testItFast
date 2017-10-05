package tests.jbehaveTests;

import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.configuration.AnnotationBuilder;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.ParameterConverters;
import org.junit.Test;
import org.junit.runner.RunWith;
import toolkit.runner.TestItRunner;

import java.text.SimpleDateFormat;

import static org.jbehave.core.reporters.StoryReporterBuilder.Format.*;

@RunWith(TestItRunner.class)
@Configure(storyLoader = JBehaveSuite.MyStoryLoader.class, storyReporterBuilder = JBehaveSuite.MyReportBuilder.class,
        parameterConverters = {JBehaveSuite.MyDateConverter.class})
@UsingEmbedder(storyTimeoutInSecs = 100, threads = 2)
@UsingSteps(instances = {GoogleSteps.class})
public class JBehaveSuite {

    private Embedder embedder;

    public JBehaveSuite() {
        embedder = new AnnotationBuilder(JBehaveSuite.class).buildEmbedder();
    }

    @Test
    public void run() {
        embedder.runStoriesAsPaths(
                new StoryFinder()
                        .findPaths(CodeLocations
                                        .codeLocationFromPath(
                                                "src/test/resources/stories/google"),
                                "**/*.story", ""));
    }

    public static class MyReportBuilder extends StoryReporterBuilder {
        public MyReportBuilder() {
            this.withFormats(CONSOLE, TXT, HTML, XML).withDefaultFormats();
        }
    }

    public static class MyStoryLoader extends LoadFromClasspath {
        public MyStoryLoader() {
            super(JBehaveSuite.class.getClassLoader());
        }
    }

    public static class MyDateConverter extends ParameterConverters.DateConverter {
        public MyDateConverter() {
            super(new SimpleDateFormat("yyyy-MM-dd"));
        }
    }

}
