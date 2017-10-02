package toolkit.runner;

import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import toolkit.driver.LocalDriverManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static configs.GeneralConfig.applicationContext;

public class JUnitExecutionListener extends RunListener {

    private final Logger logger = LoggerFactory.getLogger(JUnitExecutionListener.class);
    static final JUnitExecutionListener J_UNIT_EXECUTION_LISTENER = new JUnitExecutionListener();

    static {
        applicationContext.refresh();
    }


    public void testRunFinished(Result result) throws Exception {
        LocalDriverManager.cleanThreadPool();
    }


    public void testFailure(Failure failure) throws Exception {
        makeScreenshot(failure.getDescription().getMethodName());
        logger.error(
                "Test FAILED! Method:" + failure.getDescription().getMethodName() + ". StackTrace is " + failure.getTrace());
    }


    @Attachment(type = "image/png")
    private void makeScreenshot(String methodName) {
        Calendar calendar = Calendar.getInstance();
        String path = "";
        SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
        new File("target" + File.separator + "failure_screenshots" + File.separator).mkdirs();
        try {
            if (LocalDriverManager.getDriverController() != null) {
                File scrFile = ((TakesScreenshot) LocalDriverManager.getDriverController()
                        .getDriver()).getScreenshotAs(OutputType.FILE);
                path = "target" + File.separator + "failure_screenshots" +
                        File.separator + methodName + "_" + formater.format(calendar.getTime())
                        + "_" + LocalDriverManager.getDriverController().getBrowser()
                        + "_" + LocalDriverManager.getDriverController().getDimension() + "_webdriver.png";
                FileUtils.copyFile(scrFile, new File(path));
                Files.readAllBytes(Paths.get(path));
                return;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        throw new RuntimeException("There is a problem with screenshot");

    }
}
