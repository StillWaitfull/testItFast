package tests;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.testng.annotations.Test;
import toolkit.helpers.HtmlParser;


public class HtmlParserTests {


    @Test
    public void testGoogleParser() {
        Elements inputs = new HtmlParser("https://google.com").getElements("input").getElements();
        System.out.println(inputs.stream().map(Element::val).reduce("", (r, s) -> r += s + " "));
    }

}
