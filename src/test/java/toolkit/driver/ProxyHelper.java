package toolkit.driver;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import org.apache.log4j.Logger;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import toolkit.helpers.YamlConfigProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class ProxyHelper {

    private static Logger log = Logger.getLogger(ProxyHelper.class);
    private static final Integer proxyPort = Integer.valueOf(YamlConfigProvider.getAppParameters("proxyPort"));
    // private static ProxyServer server = new ProxyServer(proxyPort);
    private static BrowserMobProxy server = new BrowserMobProxyServer(proxyPort);
    private static boolean needProxy = Boolean.parseBoolean(YamlConfigProvider.getAppParameters("enableProxy"));
    private static Proxy proxy = new Proxy();

    static {
        if (needProxy) {
            server.start();
            server.setRequestTimeout(WebDriverController.TIMEOUT, TimeUnit.SECONDS);
            server.newHar(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));

        }
    }

    static void initProxy() {
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


    static void setCapabilities(DesiredCapabilities capabilities) {
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
