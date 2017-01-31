package tests;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Listeners;
import toolkit.config.GeneralConfig;
import toolkit.driver.WebDriverListener;


@Listeners({WebDriverListener.class})
@ContextConfiguration(classes = GeneralConfig.class)
public abstract class AbstractTest extends AbstractTestNGSpringContextTests {


}
