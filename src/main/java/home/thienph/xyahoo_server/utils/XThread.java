package home.thienph.xyahoo_server.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class XThread {

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}
