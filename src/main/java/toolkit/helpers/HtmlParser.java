package toolkit.helpers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HtmlParser {

    private Logger log = LoggerFactory.getLogger(HtmlParser.class);
    private Elements elements;
    private Document document;
    private String url;
    private final int TIMEOUT = 30;


    public HtmlParser(String url) {
        try {
            this.url = url;
            document = getDocument();
            elements = document.getAllElements();
        } catch (Exception e) {
            log.error("Cant connect to " + url);
        }
    }


    public HtmlParser(String url, Map<String, String> cookies) {
        try {
            this.url = url;
            document = getDocument(cookies);
            elements = document.getAllElements();
        } catch (Exception e) {
            log.error("Cant connect to " + url);
        }
    }


    private Document getDocument() {
        Document document = null;
        try {
            document =
                    Jsoup.connect(url)
                            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                            .timeout(TIMEOUT * 1000)
                            .get();
        } catch (Exception e) {
            log.error("Cant connect to " + url);
        }
        return document;
    }


    private Document getDocument(Map<String, String> cookies) {
        Document document = null;
        try {
            document =
                    Jsoup.connect(url)
                            .cookies(cookies)
                            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                            .timeout(TIMEOUT * 1000)
                            .get();
        } catch (Exception e) {
            log.error("Cant connect to " + url);
        }
        return document;
    }

    public Elements getElements() {
        return elements;
    }

    public HtmlParser getElements(String selector) {
        elements = document.getAllElements().select(selector);
        return this;

    }


    public Element getFirstElement(String selector) {
        try {
            return document.getAllElements().select(selector).first();
        } catch (Exception e) {
            return null;
        }

    }

    public HtmlParser getElementsWhichContainsText(String text) {
        elements = elements.first().getElementsContainingText(text);
        return this;
    }

    public String getHtmlCodeOfElements() {
        return elements.outerHtml();
    }

    public int numberOfOccurrencesInHtml(String patternString, String html) {
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(html);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    public HtmlParser getElementsMatchingText(String regex) {
        elements = document.getAllElements().first().getElementsMatchingText(regex);
        return this;
    }

    public String getText() {
        return document.getAllElements().text();
    }

}
