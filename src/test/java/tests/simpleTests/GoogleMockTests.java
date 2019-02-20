package tests.simpleTests;

import io.qameta.allure.Feature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import pages.GooglePage;
import toolkit.driver.ProxyHelper;

@Feature(value = "Google mock tests")
public class GoogleMockTests {

    private static final String MOCK_TEXT = "Ok google";

    @Before
    public void init() {
        MockServerClient mockServer = ProxyHelper.createMockServer();
        mockServer.when(
                HttpRequest.request()
                        .withBody(".*google.*")
        ).respond(
                HttpResponse.response()
                        .withBody(MOCK_TEXT)
        );
    }

    @Test
    public void googleTest() {
        GooglePage googlePage = new GooglePage();
        googlePage.openUrl(GooglePage.PAGE_URL);
        Assert.assertTrue(googlePage.getPageSource().contains(MOCK_TEXT));
    }

}
