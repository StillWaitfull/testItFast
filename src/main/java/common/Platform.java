package common;

import org.openqa.selenium.Dimension;

public class Platform {


    private Dimension dimension;
    private boolean isMobile;
    private String browser;
    private String deviceName;
    private String mobileBrowser;
    private String udid;
    private String address;
    private String platformVersion;
    private PLATFORM platform;

    private boolean remote;

    public enum PLATFORM {
        PC("pc"), ANDROID("android"), IOS("ios");

        PLATFORM(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        private String name;
    }


    public void setMobile(String platform, String platformVersion, String deviceName, String mobileBrowser, String udid, String address, Dimension dimension) {
        this.platform = PLATFORM.valueOf(platform.toUpperCase());
        this.deviceName = deviceName;
        this.mobileBrowser = mobileBrowser;
        this.dimension = dimension;
        this.udid = udid;
        this.address = address;
        this.platformVersion = platformVersion;
        isMobile = true;
    }

    public void setDesktop(Dimension dimension, String browser) {
        this.platform = PLATFORM.PC;
        this.dimension = dimension;
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

    public String getMobileBrowser() {
        return mobileBrowser;
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

}

