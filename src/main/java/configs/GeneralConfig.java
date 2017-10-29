package configs;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = {"configs", "toolkit", "testConfigs"})
public class GeneralConfig {
    public static final AnnotationConfigApplicationContext applicationContext;


    static {
        applicationContext = new AnnotationConfigApplicationContext(GeneralConfig.class);
    }

    public static void scanPackage(String packageName) {
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(applicationContext, false);
        scanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
        scanner.scan(packageName);

    }

}
