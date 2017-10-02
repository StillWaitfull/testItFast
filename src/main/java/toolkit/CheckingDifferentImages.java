package toolkit;


import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import toolkit.driver.LocalDriverManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * 79684452367
 * I am gonna pass two images and I am gonna record only the differences
 * trying to catch if there is a different object or not int the scene
 */

public class CheckingDifferentImages {
    private static boolean isTest;

    private static final Logger log = LoggerFactory.getLogger(CheckingDifferentImages.class);

    private static final String PATH = "screenshots" + File.separator;
    private static final String ETALON_PATH = "etalon" + File.separator;
    private static final String TEST_PATH = "4test" + File.separator;
    private boolean result;


    public CheckingDifferentImages() {
        if (System.getenv("isTest") == null)
            isTest = false;
    }


    public void turnOnInTest() {
        isTest = true;
    }

    public void turnOffInTest() {
        isTest = false;
    }

    /**
     * Check difference.
     *
     * @param name     name of the method
     * @param accuracy the accuracy   1pixels
     */
    public void checkDifference(String name, int accuracy) {
        BufferedImage im1 = null;
        BufferedImage im2 = null;
        try {
            //loading the two pictures
            //read and load the image
            String firstPath = PATH + ETALON_PATH + name + ".png";
            String secondPath = PATH + TEST_PATH + name + ".png";
            BufferedImage input = ImageIO.read(new File(firstPath));
            //build an image with the same dimension of the file read
            im1 = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
            //object create to draw into the bufferedImage
            Graphics2D g2d = im1.createGraphics();
            //draw input into im
            g2d.drawImage(input, 0, 0, null);
            //making all again for the second image
            BufferedImage input2 = ImageIO.read(new File(secondPath));
            //build an image with the same dimension of the file read
            im2 = new BufferedImage(input2.getWidth(), input2.getHeight(), BufferedImage.TYPE_INT_ARGB);
            //object create to draw into the bufferedImage
            Graphics2D g2d2 = im2.createGraphics();
            //draw input into im
            g2d2.drawImage(input2, 0, 0, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        showDifference(im1, im2, name + "_diff", accuracy);

    }

    /**
     * Show difference.
     *
     * @param im1            the im 1
     * @param im2            the im 2
     * @param nameDifference the name difference
     * @param accuracy       the accuracy
     */
    private void showDifference(BufferedImage im1, BufferedImage im2, String nameDifference, int accuracy) {
        try {
            BufferedImage resultImage = new BufferedImage(im1.getWidth(), im2.getHeight(), BufferedImage.TYPE_INT_ARGB);
            double THR = 50;
            int area = 0;
            for (int h = 0; h < im1.getHeight(); h++) {
                for (int w = 0; w < im1.getWidth(); w++) {
                    try {
                        int red1 = 0xff & (im1.getRGB(w, h) >> 16);
                        int green1 = 0xff & (im1.getRGB(w, h) >> 8);
                        int blue1 = 0xff & im1.getRGB(w, h);


                        int red2 = 0xff & (im2.getRGB(w, h) >> 16);
                        int green2 = 0xff & (im2.getRGB(w, h) >> 8);
                        int blue2 = 0xff & im2.getRGB(w, h);


                        //euclidian distance to estimate the simil.
                        double dist = 0;
                        dist = Math.sqrt(Math.pow((double) (red1 - red2), 2.0)
                                + Math.pow((double) (green1 - green2), 2.0)
                                + Math.pow((double) (blue1 - blue2), 2.0));
                        if (dist > THR) {
                            resultImage.setRGB(w, h, im2.getRGB(w, h));
                            area++;
                        } else {
                            resultImage.setRGB(w, h, 0);
                        }
                        //2nd option
               /*     if (dist > THR) {
                        resultImage.setRGB(w, h,255);
	                    area++;
	                } else {
	                    resultImage.setRGB(w, h, im1.getRGB(w, h));
	                }*/
                    } catch (ArrayIndexOutOfBoundsException ignored) {
                    }
                } //w
            } //h
            if (accuracy < area) {
                log.info("Difference is more than " + accuracy + " pixels and it is " + area);
                File fileScreenshot = new File("target" + File.separator + "failure_diff_screenshots" + File.separator + nameDifference + ".png");
                fileScreenshot.getParentFile().mkdirs();
                ImageIO.write(resultImage, "PNG", fileScreenshot);
                log.info("Diff screen was saved in " + "target" + File.separator + "failure_diff_screenshots" + File.separator + nameDifference + ".png");
                result = false;
            } else {
                result = true;
                log.info("Everything is ok!");
            }
        } catch (Exception ignored) {
        }
    }

    public CheckingDifferentImages makeScreenshotForDiff(String name) {
        String path = isTest ? CheckingDifferentImages.TEST_PATH : CheckingDifferentImages.ETALON_PATH;
        File scrFile;
        log.info("Screen path " + path + " name is " + name);
        try {
            scrFile = ((TakesScreenshot) LocalDriverManager.getDriverController().getDriver()).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File("screenshots" +
                    File.separator + path + File.separator + name + ".png"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return this;
    }


    public boolean getResult() {
        return result;
    }
}