package composite;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


public interface IPage {
    String getPageUrl();


    //ACTIONS
    IPage openUrl(String url);

    IPage openPage();

    IPage click(By by);

    String getCurrentUrl();

    IPage refreshPage();

    String getText(By by);

    String getAttribute(By by, String nameAttribute);

    WebElement findElement(By by);

    //VALIDATES
    boolean validateElementVisible(By by);

    boolean validateElementAttributeNotEmpty(By by, String nameAttribute);

    boolean validateElementPresent(By by);

    boolean validateElementIsNotVisible(By by);

    boolean validateElementHasText(By by);

    boolean validateElementHasText(By by, String text);

    boolean validateUrlContains(String s);

    boolean validateTextEquals(By by, String text);

    //PROPERTIES
    boolean isVisible(By by);

    boolean isElementPresent(By by);


    //ASSERTIONS
    void assertThat(Runnable... assertions);

    //CHECKS
    boolean checkDimensionIsLess(int width);


}
