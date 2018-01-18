package configs;

import common.Platform;
import org.openqa.selenium.Dimension;

import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;

public class PlatformConfig {
    private String browser;
    private String dimensionH;
    private String dimensionW;
    private Platform.PLATFORM platform;
    private String platformVersion;
    private String deviceName;
    private String mobileBrowser;
    private String udid;
    private String addressHub;
    private ApplicationConfig applicationConfig = ApplicationConfig.getInstance();
    private static final WeakHashMap<Thread, PlatformConfig> PLATFORM_CONFIG_CONCURRENT_HASH_MAP = new WeakHashMap<>();

    public PlatformConfig() {
    }


    public PlatformConfig(String browser,
                          String dimensionW,
                          String dimensionH,
                          Platform.PLATFORM platform,
                          String platformVersion,
                          String deviceName,
                          String mobileBrowser,
                          String udid,
                          String address) {
        this.browser = browser;
        this.dimensionH = dimensionH;
        this.dimensionW = dimensionW;
        this.platform = platform;
        this.platformVersion = platformVersion;
        this.deviceName = deviceName;
        this.mobileBrowser = mobileBrowser;
        this.udid = udid;
        this.addressHub = address;
    }

    public PlatformConfig(String browser,
                          String dimensionW,
                          String dimensionH,
                          Platform.PLATFORM platform,
                          String address) {
        this.browser = browser;
        this.dimensionH = dimensionH;
        this.dimensionW = dimensionW;
        this.platform = platform;
        this.addressHub = address;
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


    public static void setConfigToThread(PlatformConfig configToThread) {
        PLATFORM_CONFIG_CONCURRENT_HASH_MAP.put(Thread.currentThread(), configToThread);
    }

    public static PlatformConfig getConfigOfThread(Thread configToThread) {
        return PLATFORM_CONFIG_CONCURRENT_HASH_MAP.get(configToThread);
    }


    public Platform determinePlatform() {
        PlatformConfig configFromThread = getPlatformConfig();
        Platform platform4Test = new Platform();
        if (configFromThread != null) {
            browser = configFromThread.browser;
            dimensionH = configFromThread.dimensionH;
            dimensionW = configFromThread.dimensionW;
            platform = configFromThread.platform;
            platformVersion = configFromThread.platformVersion;
            deviceName = configFromThread.deviceName;
            mobileBrowser = configFromThread.mobileBrowser;
            udid = configFromThread.udid;
            addressHub = configFromThread.addressHub;
        }
        //IsRemote
        String remoteEnv = System.getenv("remote");
        platform4Test.setRemote(remoteEnv == null ? applicationConfig.REMOTE : Boolean.parseBoolean(remoteEnv));

        //Dimension for assertions
        Dimension dimension = determineDimension(dimensionH, dimensionW);

        //Set Platform
        if (platform == null) platform = Platform.PLATFORM.valueOf(applicationConfig.PLATFORM.toUpperCase());

        if (udid != null && deviceName != null)
            platform4Test.setMobile(
                    platform,
                    platformVersion,
                    deviceName,
                    mobileBrowser,
                    udid,
                    addressHub,
                    dimension);

        else if (platform.equals(Platform.PLATFORM.ANDROID) || platform.equals(Platform.PLATFORM.IOS))
            platform4Test.setMobile(
                    platform,
                    applicationConfig.MOBILE_PLATFORM_VERSION,
                    applicationConfig.MOBILE_DEVICE_NAME,
                    applicationConfig.BROWSER,
                    applicationConfig.UDID,
                    applicationConfig.HUB_ADDRESS,
                    dimension);

        else platform4Test.setDesktop(dimension, determineBrowser(browser), applicationConfig.HUB_ADDRESS);
        return platform4Test;
    }


    private String determineBrowser(String browser) {
        String browserEnv = System.getenv("browser");
        if (browser == null && browserEnv == null)
            browser = applicationConfig.BROWSER;
        if (browserEnv != null)
            browser = browserEnv;
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
}
