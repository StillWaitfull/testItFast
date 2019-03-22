package elements;

import org.openqa.selenium.By;

public interface Component {

    By getBaseLocator();

    AbstractPage getPage();
}
