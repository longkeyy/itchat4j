package org.solrcn.itchat4j.utils;

import java.util.Random;
import java.util.UUID;

/**
 * Created by longkeyy on 16/9/1.
 */
public class RadmonUtil {

    private static Random random = new Random();

    public static int randomnum(int min, int max) {
        if (min == max) return min;
        return random.nextInt(max) % (max - min + 1) + min;
    }

    public static long randomnumlong(long min, long max) {
        if (min == max) return min;
        long l = random.nextLong() % (max - min + 1) + min;
        return l;
    }

    public static String getIp() {
        return randomnum(1, 254) + "." + randomnum(1, 254) + "." + randomnum(1, 254) + "." + randomnum(1, 254);
    }

    public static String getDeviceId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().toUpperCase();
    }
}
