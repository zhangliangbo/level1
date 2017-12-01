package com.mcivicm.mathematica;

import java.util.List;

/**
 * 对象助手
 */

public class ObjectHelper {
    /**
     * 如果对象为空，则抛出异常
     *
     * @param object
     * @param param
     */
    public static void requireNonNull(Object object, String param) {
        if (object == null) {
            throw new IllegalArgumentException(param + "==null");
        }
    }

    public static int verifyPositive(int value, String paramName) {
        if (value <= 0) {
            throw new IllegalArgumentException(paramName + " > 0 required but it was " + value);
        } else {
            return value;
        }
    }

    public static long verifyPositive(long value, String paramName) {
        if (value <= 0L) {
            throw new IllegalArgumentException(paramName + " > 0 required but it was " + value);
        } else {
            return value;
        }
    }

    public static int requireNonNegative(int value, String param) {
        if (value < 0) {
            throw new IllegalArgumentException(param + " >= 0 required but it was " + value);
        } else {
            return value;
        }
    }

    public static int requireNonZero(int value, String param) {
        if (value == 0) {
            throw new IllegalArgumentException(param + " != 0 required but it was " + value);
        } else {
            return value;
        }
    }

    public static float requireNonNegative(float value, String param) {
        if (value < 0f) {
            throw new IllegalArgumentException(param + " >= 0 required but it was " + value);
        } else {
            return value;
        }
    }

    public static double requireNonNegative(double value, String param) {
        if (value < 0D) {
            throw new IllegalArgumentException(param + " >= 0 required but it was " + value);
        } else {
            return value;
        }
    }

    public static long requireNonNegative(long value, String param) {
        if (value < 0L) {
            throw new IllegalArgumentException(param + " >= 0 required but it was " + value);
        } else {
            return value;
        }
    }

    public static void requireLengthNotLessThan(List list, int size, String param) {
        if (list.size() < size) {
            throw new IllegalArgumentException(param + " 's size must not be less than " + size);
        }
    }

    public static <T> void requireLengthNotLessThan(T[] array, int length, String param) {
        if (array.length < length) {
            throw new IllegalArgumentException(param + " 's size must not be less than " + length);
        }
    }

    public static void requireAscend(int min, int max, String minParam, String maxParam) {
        if (min > max) {
            throw new IllegalArgumentException(minParam + "can not be greater than " + maxParam);
        }
    }

    public static void requireAscend(double min, double max, String minParam, String maxParam) {
        if (min > max) {
            throw new IllegalArgumentException(minParam + "can not be greater than " + maxParam);
        }
    }
}
