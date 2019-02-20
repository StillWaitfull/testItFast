package toolkit.runner;

import configs.PlatformConfig;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.junit.runner.Description;
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

class JUnitExecutionListener extends RunListener {

    private final Logger logger = LoggerFactory.getLogger(JUnitExecutionListener.class);


    public void testRunFinished(Result result) {
        LocalDriverManager.cleanThreadPool();
    }

    public void testFailure(Failure failure) {
        LocalDriverManager.getDriverController().stopLoading();
        makeScreenshot(failure.getDescription().getMethodName());
        logger.error("Test FAILED! Method:" + failure.getDescription().getMethodName() + ". StackTrace is " + failure.getTrace());
    }

    @Override
    public void testFinished(Description description) {
        PlatformConfig.removeConfig();
    }

    @Attachment(type = "image/png")
    private byte[] makeScreenshot(String methodName) {
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
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return new byte[0];
    }
}
