package common;

import configs.PlatformConfig;
import org.openqa.selenium.Dimension;

public class Platform {


    private Dimension dimension;
    private boolean isMobile;
    private String browser;
    private String deviceName;
    private String udid;
    private String address;
    private String platformVersion;
    private PLATFORM platform;
    private boolean remote;
    private boolean proxy;

    public Platform(PLATFORM platform,
                    String platformVersion,
                    String deviceName,
                    String browser,
                    String udid,
                    String address,
                    Dimension dimension) {
        this.platform = platform;
        this.deviceName = deviceName;
        this.browser = browser;
        this.dimension = dimension;
        this.udid = udid;
        this.address = address;
        this.platformVersion = platformVersion;
        isMobile = true;
    }

    public Platform(Dimension dimension,
                    String browser,
                    String address) {
        this.platform = PLATFORM.PC;
        this.dimension = dimension;
        this.address = address;
        this.browser = browser;
        isMobile = false;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public boolean isMobile() {
        return isMobile;
    }

    public PLATFORM getPlatform() {
        return platform;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getBrowser() {
        return browser;
    }

    public String getUdid() {
        return udid;
    }

    public String getAddress() {
        return address;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public boolean isRemote() {
        return remote;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }

    public void setProxy(boolean proxy) {
        this.proxy = proxy;
    }

    public boolean isProxy() {
        return proxy;
    }

    public enum PLATFORM {
        PC("pc"), ANDROID("android"), IOS("ios");

        private final String name;

        PLATFORM(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static Platform createPlatformFromConfig(PlatformConfig platformConfig) {
        if(platformConfig.getPlatform().equals(PLATFORM.PC))
            return new Platform(
                    new Dimension(platformConfig.getDimensionW(),platformConfig.getDimensionH()),
                    platformConfig.getBrowser(),
                    platformConfig.getAddressHub()
            );
        else return new Platform(
                platformConfig.getPlatform(),
                platformConfig.getPlatformVersion(),
                platformConfig.getDeviceName(),
                platformConfig.getBrowser(),
                platformConfig.getUdid(),
                platformConfig.getAddressHub(),
                new Dimension(platformConfig.getDimensionW(),platformConfig.getDimensionH())
        );
    }


}
