package configs;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class ApplicationConfig {

    private ApplicationConfig() {
    }

    @JsonProperty("configName")
    public String CONFIG_NAME;

    @JsonProperty("platform")
    public String PLATFORM;

    @JsonProperty("remote")
    public boolean REMOTE;

    @JsonProperty("browser")
    public String BROWSER;

    @JsonProperty("hubAddress")
    public String HUB_ADDRESS;

    @JsonProperty("Timeout")
    public int TIMEOUT;

    @JsonProperty("dimensionW")
    public String DIMENSION_W;

    @JsonProperty("dimensionH")
    public String DIMENSION_H;

    @JsonProperty("enableProxy")
    public boolean ENABLE_PROXY;

    @JsonProperty("remoteProxyHost")
    public String REMOTE_PROXY_HOST;

    @JsonProperty("proxyPort")
    public int PROXY_PORT;

    @JsonProperty("mobilePlatformVersion")
    public String MOBILE_PLATFORM_VERSION;

    @JsonProperty("mobileDeviceName")
    public String MOBILE_DEVICE_NAME;

    @JsonProperty("udid")
    public String UDID;


    private static ApplicationConfig instance;

    public static synchronized ApplicationConfig getInstance() {
        if (instance == null) {
            String path = StageConfig.class.getClassLoader().getResource("application.yml").getPath();
            try {
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                instance = mapper.readValue(new File(path), ApplicationConfig.class);
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при парсинге файла с конфигами " + path);
            }
        }
        return instance;
    }


}
