package tests;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.testng.annotations.Test;
import toolkit.helpers.HtmlParser;

import static configs.GeneralConfig.applicationContext;
import static configs.GeneralConfig.stageConfig;


public class HtmlParserTests {


    @Test
    public void testGoogleParser() {
       Elements inputs= applicationContext.getBean(HtmlParser.class, stageConfig.BASE_URL).getElements("input").getElements();
        System.out.println(inputs.stream().map(Element::val).reduce("",(r,s)->r+=s+" "));
    }

}
