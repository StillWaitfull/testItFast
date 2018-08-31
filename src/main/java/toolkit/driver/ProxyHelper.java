package toolkit.driver;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import configs.ApplicationConfig;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.Har;
import org.openqa.selenium.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;


public class ProxyHelper {

    private static final Logger log = LoggerFactory.getLogger(ProxyHelper.class);
    private static BrowserMobProxy server;
    private static String host;
    private static int port;
    private static Proxy proxy;
    private static final ApplicationConfig APPLICATION_CONFIG = ApplicationConfig.getInstance();

    static {
        proxy = new Proxy();
        if (APPLICATION_CONFIG.ENABLE_PROXY) {
            if (APPLICATION_CONFIG.REMOTE) {
                host = APPLICATION_CONFIG.REMOTE_PROXY_HOST;
                port = APPLICATION_CONFIG.PROXY_PORT;
                String proxyStr = host + ":" + port;
                proxy.setHttpProxy(proxyStr);
                proxy.setSslProxy(proxyStr);
            } else {
                server = setUpProxyServer();
                InetSocketAddress socketAddress = getBrowserMobAddress(server);
                host = socketAddress.getHostString();
                port = socketAddress.getPort();
                proxy = createSeleniumProxy(socketAddress);
            }
        } else proxy.setAutodetect(true);
    }

    public static Proxy getProxy() {
        return proxy;
    }

    public static WireMockServer getMockServer() {
        WireMockServer wireMockServer = new WireMockServer(
                WireMockConfiguration.options()
                        .enableBrowserProxying(true)
                        .proxyVia(host, port)
                        .needClientAuth(true)
                        .dynamicPort()
                        .dynamicHttpsPort()
        );
        wireMockServer.start();
        return wireMockServer;
    }


    private static BrowserMobProxy setUpProxyServer() {
        BrowserMobProxy server = new BrowserMobProxyServer();
        server.setTrustAllServers(true);
        server.setRequestTimeout(APPLICATION_CONFIG.TIMEOUT, TimeUnit.SECONDS);
        server.start(APPLICATION_CONFIG.PROXY_PORT);
        return server;
    }


    public static void saveHarToDisk(String name) {
        Har har = server.getHar();
        try {
            File file = new File("build" + File.separator + "hars" + File.separator + name + ".har");
            file.getParentFile().mkdirs();
            har.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Proxy createProxyForMock(WireMockServer wireMockServer) {
        return createSeleniumProxy(getWireMockAddress(wireMockServer));
    }

    private static Proxy createSeleniumProxy(InetSocketAddress connectableAddressAndPort) {
        Proxy proxy = new Proxy();
        proxy.setProxyType(Proxy.ProxyType.MANUAL);
        String proxyStr = String.format("%s:%d", connectableAddressAndPort.getHostString(), connectableAddressAndPort.getPort());
        proxy.setHttpProxy(proxyStr);
        proxy.setSslProxy(proxyStr);
        return proxy;
    }

    private static InetSocketAddress getWireMockAddress(WireMockServer wireMockServer) {
        try {
            return new InetSocketAddress(InetAddress.getLocalHost(), wireMockServer.port());
        } catch (UnknownHostException e) {
            throw new RuntimeException("Could not resolve localhost", e);
        }
    }

    private static InetSocketAddress getBrowserMobAddress(BrowserMobProxy browserMobProxy) {
        try {
            return new InetSocketAddress(InetAddress.getLocalHost(), browserMobProxy.getPort());
        } catch (UnknownHostException e) {
            throw new RuntimeException("Could not resolve localhost", e);
        }
    }


    @PreDestroy
    public void stopProxy() {
        if (server.isStarted()) {
            try {
                server.stop();
            } catch (Exception e) {
                log.error("There was a problem with shutdown proxy server");
                e.printStackTrace();
            }
        }
    }


}
