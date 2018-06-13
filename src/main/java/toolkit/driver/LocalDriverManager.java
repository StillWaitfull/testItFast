package toolkit.driver;

import java.util.concurrent.ConcurrentHashMap;

public class LocalDriverManager {

    private static final ConcurrentHashMap<Thread, WebDriverController> webDrivers = new ConcurrentHashMap<>();

    public static WebDriverController getDriverController() {
        return webDrivers.get(Thread.currentThread());
    }

    public static void closeDriverController() {
        WebDriverController webDriverController = webDrivers.get(Thread.currentThread());
        if (webDriverController != null)
            webDriverController.shutdown();
    }

    static void removeWebDriverController() {
        webDrivers.remove(Thread.currentThread());
    }

    static void setWebDriverController(WebDriverController driver) {
        webDrivers.put(Thread.currentThread(), driver);
    }

    public static void cleanThreadPool() {
        webDrivers.values().forEach(WebDriverController::shutdown);
        webDrivers.keySet().forEach(Thread::interrupt);
        webDrivers.clear();
    }
}
