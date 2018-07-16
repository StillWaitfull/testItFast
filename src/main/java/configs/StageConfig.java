package configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;


public class StageConfig {

    private String baseUrl;

    private StageConfig() {
    }

    public String getBaseUrl() {
        String envBaseUrl = System.getenv("BASE_URL");
        baseUrl = (envBaseUrl == null) ? baseUrl : envBaseUrl;
        return StringUtils.removeEnd(baseUrl, "/");
    }

    private static StageConfig instance;

    public static synchronized StageConfig getInstance() {
        if (instance == null) {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            String stage = System.getenv("stage");
            if (stage == null) stage = ApplicationConfig.getInstance().CONFIG_NAME;
            String path = StageConfig.class.getClassLoader().getResource("configs" +File.separator+stage).getPath();
            try {
                instance = mapper.readValue(new File(path), StageConfig.class);
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при парсинге файла с конфигами " + path);
            }
        }
        return instance;
    }

}
