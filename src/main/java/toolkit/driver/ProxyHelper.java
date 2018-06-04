package toolkit.driver;

import configs.ApplicationConfig;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import org.openqa.selenium.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;


public class ProxyHelper {

    private static final Logger log = LoggerFactory.getLogger(ProxyHelper.class);
    private static BrowserMobProxy server= new BrowserMobProxyServer();
    private static Proxy proxy;
    private static final ApplicationConfig APPLICATION_CONFIG = ApplicationConfig.getInstance();

    public static synchronized Proxy getInstance() {
        if (proxy == null) {
            proxy = new Proxy();
            if (APPLICATION_CONFIG.ENABLE_PROXY) {
                if (APPLICATION_CONFIG.REMOTE) {
                    String proxyStr = APPLICATION_CONFIG.REMOTE_PROXY_HOST + ":" + APPLICATION_CONFIG.PROXY_PORT;
                    proxy.setHttpProxy(proxyStr);
                    proxy.setSslProxy(proxyStr);
                } else {
                    setUpProxyServer();
                    proxy = ClientUtil.createSeleniumProxy(server);
                }
            } else proxy.setAutodetect(true);
        }
        return proxy;
    }


    private static void setUpProxyServer() {
        if (!server.isStarted()) {
            server.setTrustAllServers(true);
            server.setRequestTimeout(APPLICATION_CONFIG.TIMEOUT, TimeUnit.SECONDS);
            server.start(APPLICATION_CONFIG.PROXY_PORT);
        }
    }


    public static void saveHarToDisk(String name) {
        Har har = server.getHar();
        try {
            File file = new File("target" + File.separator + "hars" + File.separator + name + ".har");
            file.getParentFile().mkdirs();
            har.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
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

    public static Proxy createSeleniumProxy(InetSocketAddress connectableAddressAndPort) {
        Proxy proxy = new Proxy();
        proxy.setProxyType(Proxy.ProxyType.MANUAL);
        String proxyStr = String.format("%s:%d", connectableAddressAndPort.getHostString(), connectableAddressAndPort.getPort());
        proxy.setHttpProxy(proxyStr);
        proxy.setSslProxy(proxyStr);
        return proxy;
    }


}
