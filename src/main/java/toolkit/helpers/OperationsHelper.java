package toolkit.helpers;

public class OperationsHelper {


    public static String getRandomEmail() {
        long currentTime = System.nanoTime();
        String longNumber = Long.toString(currentTime);
        return "notifytest." + "1" + longNumber + "@test.ru";
    }

    public static String getRandomLogin() {
        long currentTime = System.nanoTime();
        String longNumber = String.valueOf(currentTime);
        return "login" + longNumber.substring(4, 9);
    }

    public static void sendPause(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException iex) {
            Thread.interrupted();
        }
    }
}
