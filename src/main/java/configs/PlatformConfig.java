package configs;

import common.Platform;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Proxy;
import toolkit.driver.ProxyHelper;

import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;

public class PlatformConfig {
    private static final WeakHashMap<Thread, PlatformConfig> PLATFORM_CONFIG_CONCURRENT_HASH_MAP = new WeakHashMap<>();
    private static final ApplicationConfig APPLICATION_CONFIG = ApplicationConfig.getInstance();

    private String browser;
    private Dimension dimension;
    private Platform platform;
    private String platformVersion;
    private String deviceName;
    private String udid;
    private String addressHub;
    private Proxy proxy;
    private boolean remote;
    private boolean isMobile;

    public PlatformConfig(String browser,
                          Dimension dimension,
                          Platform platform,
                          String platformVersion,
                          String deviceName,
                          String udid) {
        this.browser = browser;
        this.dimension = dimension;
        this.platform = platform;
        this.platformVersion = platformVersion;
        this.deviceName = deviceName;
        this.udid = udid;
        this.addressHub = APPLICATION_CONFIG.HUB_ADDRESS;
        this.proxy=ProxyHelper.getProxy();
        isMobile = true;
    }

    public PlatformConfig(String browser,
                          Dimension dimension,
                          Platform platform) {
        this.browser = browser;
        this.dimension = dimension;
        this.platform = platform;
        this.addressHub = APPLICATION_CONFIG.HUB_ADDRESS;
        this.proxy=ProxyHelper.getProxy();
        isMobile = false;
    }

    public static void setConfigToThread(PlatformConfig configToThread) {
        PLATFORM_CONFIG_CONCURRENT_HASH_MAP.put(Thread.currentThread(), configToThread);
    }

    public static PlatformConfig getConfigFromThread() {
        return PLATFORM_CONFIG_CONCURRENT_HASH_MAP.get(Thread.currentThread());
    }

    public static void removeConfig() {
        PLATFORM_CONFIG_CONCURRENT_HASH_MAP.remove(Thread.currentThread());
    }


    private static List<Thread> getFirstThreadFromGroup(final ThreadGroup group) {
        if (group == null)
            throw new NullPointerException("Null thread group");
        int nAlloc = group.activeCount();
        int n;
        Thread[] threads;
        do {
            nAlloc *= 2;
            threads = new Thread[nAlloc];
            n = group.enumerate(threads);
        } while (n == nAlloc);
        if (threads.length == 0 || threads[0] == null)
            throw new RuntimeException("Eror in threadGroup");
        return Arrays.asList(threads);
    }

    private static PlatformConfig getPlatformConfig() {
        Thread currentThread = Thread.currentThread();
        List<Thread> mainThreads = getFirstThreadFromGroup(currentThread.getThreadGroup());
        if (PLATFORM_CONFIG_CONCURRENT_HASH_MAP.containsKey(currentThread)) {
            return PLATFORM_CONFIG_CONCURRENT_HASH_MAP.get(currentThread);
        }
        for (Thread thread : mainThreads) {
            if (PLATFORM_CONFIG_CONCURRENT_HASH_MAP.containsKey(thread))
                return PLATFORM_CONFIG_CONCURRENT_HASH_MAP.get(thread);
        }
        return null;
    }


    private static PlatformConfig createPlatformConfig() {
        PlatformConfig configFromThread;
        Dimension dimension = determineDimension();
        Platform platform = Platform.valueOf(APPLICATION_CONFIG.PLATFORM.toUpperCase());
        if (!platform.equals(Platform.PC)) {
            configFromThread = new PlatformConfig(
                    determineBrowser(APPLICATION_CONFIG.BROWSER),
                    dimension,
                    platform,
                    APPLICATION_CONFIG.MOBILE_PLATFORM_VERSION,
                    APPLICATION_CONFIG.MOBILE_DEVICE_NAME,
                    APPLICATION_CONFIG.UDID);
        } else {
            configFromThread = new PlatformConfig(
                    determineBrowser(APPLICATION_CONFIG.BROWSER),
                    dimension,
                    platform);
        }
        setConfigToThread(configFromThread);
        return configFromThread;
    }

    public static PlatformConfig determinePlatform() {
        PlatformConfig platformConfig = getPlatformConfig();
        if (platformConfig == null) platformConfig = createPlatformConfig();
        String remoteEnv = System.getenv("remote");
        platformConfig.remote = (remoteEnv == null ? APPLICATION_CONFIG.REMOTE : Boolean.parseBoolean(remoteEnv));
        return platformConfig;
    }


    private static String determineBrowser(String browser) {
        String browserEnv = System.getenv("browser");
        if (browserEnv == null)
            browser = (browser == null) ? APPLICATION_CONFIG.BROWSER : browser;
        return browser;
    }


    private static Dimension determineDimension() {
        if (APPLICATION_CONFIG.DIMENSION_W.isEmpty() && APPLICATION_CONFIG.DIMENSION_H.isEmpty())
            throw new RuntimeException("You should set dimension for test in configs");
        else
            return new Dimension(Integer.parseInt(APPLICATION_CONFIG.DIMENSION_W), Integer.parseInt(APPLICATION_CONFIG.DIMENSION_H));

    }

    public Platform getPlatform() {
        return platform;
    }

    public String getBrowser() {
        return browser;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getUdid() {
        return udid;
    }

    public String getAddressHub() {
        return addressHub;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public boolean isRemote() {
        return remote;
    }

    public PlatformConfig setProxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public boolean isMobile() {
        return isMobile;
    }
}
