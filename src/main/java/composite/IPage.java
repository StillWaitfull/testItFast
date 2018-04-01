package composite;


import org.openqa.selenium.By;


public interface IPage {
    String getPageUrl();

    //ACTIONS
    IPage openUrl(String url);

    IPage openPage();

    IPage click(By by);

    //VALIDATES
    boolean validateElementVisible(By by);

    //PROPERTIES
    boolean isVisible(By by);

    boolean isElementPresent(By by);


    //ASSERTIONS
    void assertThat(Runnable... assertions);

    //CHECKS
    boolean checkDimensionIsLess(int width);


}
