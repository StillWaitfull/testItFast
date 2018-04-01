package configs;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class ApplicationConfig {

    private ApplicationConfig() {
    }


    @JsonProperty("timeout")
    public int TIMEOUT;

    @JsonProperty("baseUrl")
    public String BASE_URL;

    @JsonProperty("browser")
    public String BROWSER;

    @JsonProperty("dimensionW")
    public int DIMENSION_W;

    @JsonProperty("dimensionH")
    public int DIMENSION_H;


    private static ApplicationConfig instance;

    public static synchronized ApplicationConfig getInstance() {
        if (instance == null) {
            String path = "application.yml";
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
