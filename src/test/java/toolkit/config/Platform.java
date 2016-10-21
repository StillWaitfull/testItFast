package toolkit.config;

import org.openqa.selenium.Dimension;

/**
 * Created by skashapov on 20.10.16.
 */

public class Platform {


    private Dimension dimension;
    private boolean isMobile;
    private String platform;
    private String browser;
    private String platformVersion;
    private String deviceName;
    private String mobileBrowser;
    private String udid;
    private String address;

    public void setMobile(String platform, String deviceName, String mobileBrowser, String platformVersion, String udid, String address, Dimension dimension) {
        this.platform = platform;
        this.platformVersion = platformVersion;
        this.deviceName = deviceName;
        this.mobileBrowser = mobileBrowser;
        this.dimension = dimension;
        this.udid = udid;
        this.address = address;
        isMobile = true;
    }

    public void setDesktop(Dimension dimension, String browser) {
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

    public String getPlatform() {
        return platform;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getMobileBrowser() {
        return mobileBrowser;
    }

    public String getPlatformVersion() {
        return platformVersion;
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
}

