package tests.s;

import composite.IPage;
import composite.pages.GooglePage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import toolkit.config.BrowserConfig;
import toolkit.driver.WebDriverListener;

/**
 * Created by sergi on 30.01.2017.
 */

@Listeners({WebDriverListener.class})
@ContextConfiguration(classes = BrowserConfig.class)
public class Ex extends AbstractTestNGSpringContextTests {


    @Test
    public void googleTest() {
        IPage iPage = new GooglePage();
        iPage.openPage();
    }
}
