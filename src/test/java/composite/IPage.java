package composite;


import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

public interface IPage {
    public String getPageUrl();

    IPage openPage();

    IPage pressEnter();

    WebElement findElement(By by);

    IPage windowSetSize(Dimension windowSize);

    String getAlertText();

    IPage clickOkInAlert();

    void waitForElementPresent(By by);

    IPage clickOnStalenessElement(By by);

    IPage clickCancelInAlert();

    IPage waitForNotAttribute(By by, String attribute, String value);

    IPage waitForTextPresent(String text);

    java.util.List<WebElement> findElements(By by);

    void waitForVisible(By by);

    void waitForNotVisible(By by);

    String getSrcOfElement(By by);

    String getCurrentUrl();

    IPage selectValueInDropDown(By by, String optionValue);

    IPage submit(By by);

    IPage navigateBack();

    IPage moveToElement(By by);

    void highlightTheElement(By by);

    IPage click(By by);

    IPage clickWithJs(By by);

    IPage simpleClick(By by);

    void assertThat(Runnable... assertions);

    String getText(By by);

    String getAttribute(By by, String nameAttribute);

    String getPageSource();

    boolean isElementPresent(By by);

    boolean isVisible(By by);

    IPage type(By by, String someText);

    IPage openUrl(String url);

    IPage openTab(String url);

    boolean validateElementPresent(By by);

    boolean validateElementIsNotVisible(By by);

    boolean validateElementVisible(By by);

    boolean validateUrlContains(String s);

    boolean validateElementEnable(By by);

    boolean validateTextEquals(By by, String text);

    IPage refreshPage();

    // Set a cookie
    IPage addCookie(String key, String value);

    IPage hoverOn(By by);

    IPage scrollOnTop();

    int getCountElements(By by);

    void makeScreenshotForDiff(String name);

}
