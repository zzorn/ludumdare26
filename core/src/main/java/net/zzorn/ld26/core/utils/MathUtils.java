package net.zzorn.ld26.core.utils;

import com.badlogic.gdx.math.Vector3;

import java.util.Random;

import static java.lang.Math.PI;

/**
 *
 */
public final class MathUtils {

    public static final double Tau = PI * 2;

    public static float mix(float t, float a, float b) {
        return a + t * (b - a);
    }

    public static float mixClamp(float t, float a, float b) {
        return clamp(mix(t, a, b), a, b);
    }

    public static float map(float t, float srcStart, float srcEnd, float targetStart, float targetEnd) {
        float relPos = srcStart == srcEnd ? 0.5f : (t - srcStart) / (srcEnd - srcStart);
        return mix(relPos, targetStart, targetEnd);
    }

    public static float mapClamp(float t, float srcStart, float srcEnd, float targetStart, float targetEnd) {
        return clamp(map(t, srcStart, srcEnd, targetStart, targetEnd), targetStart, targetEnd);
    }

    public static float clamp0to1(float v) {
        if (v < 0) return 0;
        else if (v > 1) return 1;
        else return v;
    }

    public static float clampMinus1to1(float v) {
        if (v < -1) return -1;
        else if (v > 1) return 1;
        else return v;
    }

    public static float clamp(float v, float min, float max) {
        if (v < min) return min;
        else if (v > max) return max;
        else return v;
    }

    public static Vector3 mulAdd(Vector3 target, Vector3 addition, float factor) {
        float dx = addition.x * factor;
        float dy = addition.y * factor;
        float dz = addition.z * factor;
        return target.add(dx, dy, dz);
    }

    public static void clamp(Vector3 v, float maxValue) {
        v.x = clamp(v.x, -maxValue, maxValue);
        v.y = clamp(v.y, -maxValue, maxValue);
        v.z = clamp(v.z, -maxValue, maxValue);
    }

    public static Vector3 randomVector(Random random, float xScale, float yScale, float zScale, Vector3 out) {
        float x = xScale * (float) random.nextGaussian();
        float y = yScale * (float) random.nextGaussian();
        float z = zScale * (float) random.nextGaussian();
        out.set(x, y, z);
        return out;
    }
}
