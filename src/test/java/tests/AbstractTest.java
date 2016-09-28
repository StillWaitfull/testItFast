package tests;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.testng.Assert;
import org.testng.annotations.*;
import toolkit.CheckingDifferentImages;
import toolkit.config.ApplicationConfig;
import toolkit.config.Config;
import toolkit.config.StageConfig;
import toolkit.driver.LocalDriverManager;
import toolkit.driver.ProxyHelper;
import toolkit.driver.WebDriverListener;

import java.util.HashMap;
import java.util.stream.Collectors;


@Listeners({WebDriverListener.class})
public abstract class AbstractTest {

    public static ApplicationContext applicationContext;
    public static ApplicationConfig applicationConfig;
    public static StageConfig stageConfig;

    static {
        applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationConfig = applicationContext.getBean(ApplicationConfig.class);
        String stage = applicationConfig.getConfigName();
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(Config.class);
        if (System.getenv("stage") != null) stage = System.getenv("stage");
        String finalStage = stage;
        HashMap<String, Object> map = new HashMap<String, Object>() {{
            put("configName", finalStage);
        }};
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        environment.getPropertySources().addFirst(new MapPropertySource("configName", map));
        applicationContext.refresh();
        stageConfig = applicationContext.getBean(StageConfig.class);
    }

    @BeforeTest
    @Parameters({"browser"})
    public void setBrowser(@Optional String browser) {
        ProxyHelper.initProxy();
    }

    @BeforeTest
    @Parameters({"dimensionH", "dimensionW"})
    public void setDimension(@Optional String dimensionH, @Optional String dimensionW) {
    }

    @AfterMethod
    public void after() {
        if (LocalDriverManager.getDriverController() != null)
            LocalDriverManager.getDriverController().deleteAllCookies();
    }

    @AfterTest
    public void cleanPool() {
        LocalDriverManager.cleanThreadPool();
    }

    @AfterSuite
    public void stopServices() {
        if (!CheckingDifferentImages.failedTests.isEmpty())
            Assert.fail("There was errors in frontend tests \n" + CheckingDifferentImages.failedTests.stream().collect(Collectors.joining("\n")));
        ProxyHelper.stopProxy();
    }
}
