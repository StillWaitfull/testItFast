package tests.simpleTests;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import configs.PlatformConfig;
import io.qameta.allure.Feature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pages.GooglePage;
import toolkit.driver.ProxyHelper;

@Feature(value = "Google mock tests")
public class GoogleMockTests {
    private WireMockServer mockServer;

    @Before
    public void init() {
        mockServer = ProxyHelper.getMockServer();
        mockServer.stubFor(
                WireMock.get(
                        WireMock.urlMatching(".*google.*"))
                        .willReturn(
                                WireMock.aResponse().withBody("You've reached a valid WireMock endpoint")
                        ));
        PlatformConfig.determinePlatform().setProxy(ProxyHelper.createProxyForMock(mockServer));
    }

    @Test
    public void googleTest() {
        GooglePage googlePage = new GooglePage();
        googlePage.openPage();
    }

    @After
    public void stopServer() {
        mockServer.shutdown();
    }
}
