package toolkit.driver;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static toolkit.helpers.Context.applicationConfig;


public class ProxyHelper {

    private static Logger log = LoggerFactory.getLogger(ProxyHelper.class);
    private static final int proxyPort = applicationConfig.PROXY_PORT;
    // private static ProxyServer server = new ProxyServer(proxyPort);
    private static BrowserMobProxy server = new BrowserMobProxyServer();
    private static boolean needProxy = applicationConfig.ENABLE_PROXY;
    private static Proxy proxy = new Proxy();

    static {
        if (needProxy) {
            server.start(proxyPort);
            server.setRequestTimeout(WebDriverController.TIMEOUT, TimeUnit.SECONDS);
            server.newHar(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));

        }
    }

    public static void initProxy() {
        try {
            if (!needProxy)
                proxy.setAutodetect(true);
            else {
                proxy = ClientUtil.createSeleniumProxy(server);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void stopProxy() {
        try {
            server.stop();
        } catch (Exception e) {
            log.error("There was a problem with shutdown proxy server");
            e.printStackTrace();
        }
    }


    public static void setCapabilities(DesiredCapabilities capabilities) {
        capabilities.setCapability(CapabilityType.PROXY, proxy);
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


}
