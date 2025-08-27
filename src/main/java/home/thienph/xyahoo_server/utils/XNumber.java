package home.thienph.xyahoo_server.utils;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

public final class XNumber {
    private static final SecureRandom RANDOM = new SecureRandom();

    private XNumber() {
        // private constructor để không cho new
    }

    /**
     * Random int trong [min, max]
     */
    public static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * Random long trong [min, max]
     */
    public static long randomLong(long min, long max) {
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }

    /**
     * Random double trong [min, max)
     */
    public static double randomDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    /**
     * Random boolean
     */
    public static boolean randomBoolean() {
        return RANDOM.nextBoolean();
    }

    /**
     * Random float trong [min, max)
     */
    public static float randomFloat(float min, float max) {
        return min + RANDOM.nextFloat() * (max - min);
    }

    /**
     * Random số trong 0..9
     */
    public static int randomDigit() {
        return RANDOM.nextInt(10);
    }
}
