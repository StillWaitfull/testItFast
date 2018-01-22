package configs;

import common.Platform;
import org.openqa.selenium.Dimension;

import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;

public class PlatformConfig {
    private static final WeakHashMap<Thread, PlatformConfig> PLATFORM_CONFIG_CONCURRENT_HASH_MAP = new WeakHashMap<>();
    private String browser;
    private String dimensionH;
    private String dimensionW;
    private Platform.PLATFORM platform;
    private String platformVersion;
    private String deviceName;
    private String mobileBrowser;
    private String udid;
    private String addressHub;
    private String proxy;
    private ApplicationConfig applicationConfig = ApplicationConfig.getInstance();

    public PlatformConfig() {
    }

    public PlatformConfig(String browser,
                          String dimensionW,
                          String dimensionH,
                          Platform.PLATFORM platform,
                          String platformVersion,
                          String deviceName,
                          String mobileBrowser,
                          String udid) {
        this.browser = browser;
        this.dimensionH = dimensionH;
        this.dimensionW = dimensionW;
        this.platform = platform;
        this.platformVersion = platformVersion;
        this.deviceName = deviceName;
        this.mobileBrowser = mobileBrowser;
        this.udid = udid;
    }

    public PlatformConfig(String browser,
                          String dimensionW,
                          String dimensionH,
                          Platform.PLATFORM platform) {
        this.browser = browser;
        this.dimensionH = dimensionH;
        this.dimensionW = dimensionW;
        this.platform = platform;
    }

    public static void setConfigToThread(PlatformConfig configToThread) {
        PLATFORM_CONFIG_CONCURRENT_HASH_MAP.put(Thread.currentThread(), configToThread);
    }

    public static PlatformConfig getConfigFromThread(Thread configToThread) {
        return PLATFORM_CONFIG_CONCURRENT_HASH_MAP.get(configToThread);
    }


    private List<Thread> getFirstThreadFromGroup(final ThreadGroup group) {
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

    private PlatformConfig getPlatformConfig() {
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


    public PlatformConfig createOrGetPlatformConfig(){
        PlatformConfig configFromThread = getPlatformConfig();
        if (configFromThread == null) {
            Dimension dimension = determineDimension(dimensionH, dimensionW);
            if (platform == null)
                platform = Platform.PLATFORM.valueOf(applicationConfig.PLATFORM.toUpperCase());
            if ((udid != null && deviceName != null) || !platform.equals(Platform.PLATFORM.PC)) {
                configFromThread = new PlatformConfig(
                        mobileBrowser = (mobileBrowser == null) ? applicationConfig.BROWSER : mobileBrowser,
                        String.valueOf(dimension.width),
                        String.valueOf(dimension.height),
                        platform,
                        platformVersion = (platformVersion == null) ? applicationConfig.MOBILE_PLATFORM_VERSION : platformVersion,
                        deviceName = (deviceName == null) ? applicationConfig.MOBILE_DEVICE_NAME : deviceName,
                        mobileBrowser,
                        udid = (udid == null) ? applicationConfig.UDID : udid);
            } else
                configFromThread = new PlatformConfig(determineBrowser(browser),
                        String.valueOf(dimension.width),
                        String.valueOf(dimension.height),
                        platform);
        }
        configFromThread.setAddressHub(applicationConfig.HUB_ADDRESS);
        setConfigToThread(configFromThread);
        return configFromThread;
    }

    public Platform determinePlatform() {
        PlatformConfig platformConfig= createOrGetPlatformConfig();
        Platform platform = Platform.createPlatformFromConfig(platformConfig);
        String remoteEnv = System.getenv("remote");
        platform.setRemote(remoteEnv == null ? applicationConfig.REMOTE : Boolean.parseBoolean(remoteEnv));
        platform.setProxy(platformConfig.proxy == null ? applicationConfig.ENABLE_PROXY : platformConfig.isProxy());
        return platform;
    }


    private String determineBrowser(String browser) {
        String browserEnv = System.getenv("browser");
        if (browserEnv == null)
            browser = (browser == null) ? applicationConfig.BROWSER : browser;
        return browser;
    }


    private Dimension determineDimension(String dimensionH, String dimensionW) {
        Dimension dimension;
        if (dimensionH != null && dimensionW != null)
            dimension = new Dimension(Integer.parseInt(dimensionW), Integer.parseInt(dimensionH));
        else {
            if (applicationConfig.DIMENSION_W.isEmpty() && applicationConfig.DIMENSION_H.isEmpty())
                throw new RuntimeException("You should set dimension for test in configs");
            else
                dimension = new Dimension(Integer.parseInt(applicationConfig.DIMENSION_W), Integer.parseInt(applicationConfig.DIMENSION_H));
        }
        return dimension;
    }

    public Platform.PLATFORM getPlatform() {
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

    public String getMobileBrowser() {
        return mobileBrowser;
    }

    public String getUdid() {
        return udid;
    }

    public String getAddressHub() {
        return addressHub;
    }

    public void setAddressHub(String addressHub) {
        this.addressHub = addressHub;
    }

    private boolean isProxy() {
        return Boolean.parseBoolean(proxy);
    }

    public PlatformConfig setProxy(boolean proxy) {
        this.proxy = String.valueOf(proxy);
        return this;
    }
}