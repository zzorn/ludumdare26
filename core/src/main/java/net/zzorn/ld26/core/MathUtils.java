package net.zzorn.ld26.core;

/**
 *
 */
public class MathUtils {

    public static double mix(double t, double a, double b) {
        return a + t * (b - a);
    }

}
