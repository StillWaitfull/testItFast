package testConfigs;

import common.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties
@PropertySource(value = "classpath:configs/someCollectionConfig.yml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties
public class CollectionConfig {

    private List<String> fileNames = new ArrayList<>();

    public List<String> getFileNames() {
        return fileNames;
    }
}
