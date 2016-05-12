package toolkit.helpers;

import org.yaml.snakeyaml.Yaml;
import tests.AbstractTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;



public class YamlConfigProvider extends AbstractTest {
    private static Yaml yaml = new Yaml();
    private static String configFilePath = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "configs" + File.separator;
    private static LinkedHashMap paramsMap = new LinkedHashMap();
    private static LinkedHashMap appParamsMap = new LinkedHashMap();

    static {

        try {
            String appConfigs = "application.yml";
            Iterable<Object> params = yaml.loadAll(new FileInputStream(new File(appConfigs)));
            appParamsMap = (LinkedHashMap) params.iterator().next();

        } catch (FileNotFoundException e) {
            String appConfigs = "application.yml";
            throw new RuntimeException("file with configs not found " + appConfigs);
        }

        String configName = System.getenv("config");
        if (configName == null) {
            configName = String.valueOf(appParamsMap.get("configName"));
        }

        try {
            Iterable<Object> params = yaml.loadAll(new FileInputStream(new File(configFilePath + configName)));
            paramsMap = (LinkedHashMap) params.iterator().next();

        } catch (FileNotFoundException e) {
            throw new RuntimeException("file with configs not found " + configFilePath + configName);
        }

    }

    public static String getStageParameters(String parameter) {
        if (!paramsMap.containsKey(parameter))
            throw new RuntimeException("There is no parameter " + parameter + " in stage config");
        return String.valueOf(paramsMap.get(parameter));
    }

    public static String getAppParameters(String parameter) {
        if (!appParamsMap.containsKey(parameter))
            throw new RuntimeException("There is no parameter " + parameter + " in application config");
        return String.valueOf(appParamsMap.get(parameter));
    }
}
