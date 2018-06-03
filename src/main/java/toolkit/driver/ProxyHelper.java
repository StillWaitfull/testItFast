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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class ProxyHelper {

    private static final Logger log = LoggerFactory.getLogger(ProxyHelper.class);
    private static BrowserMobProxy server;
    private static Proxy proxy;


    public static synchronized Proxy getInstance() {
        if (proxy == null) {
            ApplicationConfig applicationConfig=ApplicationConfig.getInstance();
            proxy = new Proxy();
            if (applicationConfig.ENABLE_PROXY) {
                if (applicationConfig.REMOTE) {
                    String proxyStr = applicationConfig.REMOTE_PROXY_HOST + ":" + applicationConfig.PROXY_PORT;
                    proxy.setHttpProxy(proxyStr);
                    proxy.setSslProxy(proxyStr);
                } else {
                    server = new BrowserMobProxyServer();
                    server.setTrustAllServers(true);
                    server.setRequestTimeout(applicationConfig.TIMEOUT, TimeUnit.SECONDS);
                    server.newHar(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
                    server.start(applicationConfig.PROXY_PORT);
                    proxy = ClientUtil.createSeleniumProxy(server);
                }
            } else proxy.setAutodetect(true);
        }
        return proxy;

    }


    public static void saveHarToDisk(String name) {
        Har har = server.getHar();
        try {
            File file = new File("target" + File.separator + "hars" + File.separator + name + ".har");
            boolean mkDirs = file.getParentFile().mkdirs();
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
