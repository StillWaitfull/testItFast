package configs;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = {"configs", "toolkit"})
public class GeneralConfig {
    public static final AnnotationConfigApplicationContext applicationContext;

    static {
        applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(GeneralConfig.class);
    }

    public static void scanPackage(String packageName) {
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner((BeanDefinitionRegistry) applicationContext, false);
        scanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
        scanner.scan(packageName);

    }

}
