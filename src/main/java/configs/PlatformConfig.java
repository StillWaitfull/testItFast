package configs;

import common.Platform;
import common.Platform.PLATFORM;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Proxy;
import toolkit.driver.ProxyHelper;

import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;

public class PlatformConfig {
    private static final WeakHashMap<Thread, PlatformConfig> PLATFORM_CONFIG_CONCURRENT_HASH_MAP = new WeakHashMap<>();
    private String browser;
    private String dimensionH;
    private String dimensionW;
    private PLATFORM platform;
    private String platformVersion;
    private String deviceName;
    private String mobileBrowser;
    private String udid;
    private String addressHub;
    private Proxy proxy;
    private static final ApplicationConfig applicationConfig = ApplicationConfig.getInstance();

    public PlatformConfig() {
    }

    public PlatformConfig(String browser,
                          String dimensionW,
                          String dimensionH,
                          PLATFORM platform,
                          String platformVersion,
                          String deviceName,
                          String udid) {
        this.browser = browser;
        this.dimensionH = dimensionH;
        this.dimensionW = dimensionW;
        this.platform = platform;
        this.platformVersion = platformVersion;
        this.deviceName = deviceName;
        this.udid = udid;
    }

    public PlatformConfig(String browser,
                          String dimensionW,
                          String dimensionH,
                          PLATFORM platform) {
        this.browser = browser;
        this.dimensionH = dimensionH;
        this.dimensionW = dimensionW;
        this.platform = platform;
    }

    //FOR CLONE
    private PlatformConfig(String browser,
                           int dimensionH,
                           int dimensionW,
                           PLATFORM platform,
                           String platformVersion,
                           String deviceName,
                           String mobileBrowser,
                           String udid,
                           String addressHub,
                           Proxy proxy) {
        this.browser = browser;
        this.dimensionH = String.valueOf(dimensionH);
        this.dimensionW = String.valueOf(dimensionW);
        this.platform = platform;
        this.platformVersion = platformVersion;
        this.deviceName = deviceName;
        this.mobileBrowser = mobileBrowser;
        this.udid = udid;
        this.addressHub = addressHub;
        this.proxy = proxy;
    }

    private static PlatformConfig cloneConfig(PlatformConfig platformConfig) {
        return new PlatformConfig(platformConfig.getBrowser(),
                platformConfig.getDimensionH(),
                platformConfig.getDimensionW(),
                platformConfig.getPlatform(),
                platformConfig.getPlatformVersion(),
                platformConfig.getDeviceName(),
                platformConfig.getMobileBrowser(),
                platformConfig.getUdid(),
                platformConfig.getAddressHub(),
                platformConfig.getProxy());
    }

    public static void setConfigToThread(PlatformConfig configToThread) {
        PLATFORM_CONFIG_CONCURRENT_HASH_MAP.put(Thread.currentThread(), configToThread);
    }

    public static PlatformConfig getConfigFromThread(Thread configToThread) {
        return PLATFORM_CONFIG_CONCURRENT_HASH_MAP.get(configToThread);
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


    public static PlatformConfig createOrGetPlatformConfig() {
        PlatformConfig configFromThread;
        if (getPlatformConfig() == null) {
            Dimension dimension = determineDimension();
            PLATFORM platform = PLATFORM.valueOf(applicationConfig.PLATFORM.toUpperCase());
            if (!platform.equals(PLATFORM.PC)) {
                configFromThread = new PlatformConfig(
                        applicationConfig.BROWSER,
                        String.valueOf(dimension.width),
                        String.valueOf(dimension.height),
                        platform,
                        applicationConfig.MOBILE_PLATFORM_VERSION,
                        applicationConfig.MOBILE_DEVICE_NAME,
                        applicationConfig.UDID);
            } else
                configFromThread = new PlatformConfig(
                        determineBrowser(applicationConfig.BROWSER),
                        String.valueOf(dimension.width),
                        String.valueOf(dimension.height),
                        platform);
            setConfigToThread(configFromThread);
        } else configFromThread = cloneConfig(getPlatformConfig());
        if (configFromThread.getProxy() == null) configFromThread.setProxy(ProxyHelper.getInstance());
        configFromThread.setAddressHub(applicationConfig.HUB_ADDRESS);
        return configFromThread;
    }

    public static Platform determinePlatform() {
        PlatformConfig platformConfig = createOrGetPlatformConfig();
        Platform platform = Platform.createPlatformFromConfig(platformConfig);
        String remoteEnv = System.getenv("remote");
        platform.setRemote(remoteEnv == null ? applicationConfig.REMOTE : Boolean.parseBoolean(remoteEnv));
        platform.setProxy(platformConfig.getProxy());
        return platform;
    }


    private static String determineBrowser(String browser) {
        String browserEnv = System.getenv("browser");
        if (browserEnv == null)
            browser = (browser == null) ? applicationConfig.BROWSER : browser;
        return browser;
    }


    private static Dimension determineDimension() {
        if (applicationConfig.DIMENSION_W.isEmpty() && applicationConfig.DIMENSION_H.isEmpty())
            throw new RuntimeException("You should set dimension for test in configs");
        else
            return new Dimension(Integer.parseInt(applicationConfig.DIMENSION_W), Integer.parseInt(applicationConfig.DIMENSION_H));

    }

    public PLATFORM getPlatform() {
        return platform;
    }

    public String getBrowser() {
        return browser;
    }

    public int getDimensionW() {
        return Integer.parseInt(dimensionW);
    }

    public int getDimensionH() {
        return Integer.parseInt(dimensionH);
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public String getDeviceName() {
        return deviceName;
    }

    private String getMobileBrowser() {
        return mobileBrowser;
    }

    public String getUdid() {
        return udid;
    }

    public String getAddressHub() {
        return addressHub;
    }

    private void setAddressHub(String addressHub) {
        this.addressHub = addressHub;
    }

    private Proxy getProxy() {
        return proxy;
    }

    public PlatformConfig setProxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }
}
