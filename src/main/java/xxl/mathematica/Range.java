package xxl.mathematica;

import java.util.ArrayList;

/**
 * 范围，严格以上不允许实例化
 */

public class Range<T> extends ArrayList<T> {

    private Range() {
    }

    /**
     * 从0（包括）到max（不包括）的列表
     *
     * @param max
     * @return
     */
    public static Range<Integer> range(int max) {
        if (max < 0) {
            max = 0;
        }
        Range<Integer> range = new Range<>();
        for (int i = 0; i < max; i++) {
            range.add(i);
        }
        return range;
    }

    /**
     * @param min 制定下界
     * @param max
     * @return
     */
    public static Range<Integer> range(int min, int max) {
        Range<Integer> range = new Range<>();
        if (min <= max) {
            for (int i = min; i < max; i++) {
                range.add(i);
            }
        }
        return range;
    }

    /**
     * @param min
     * @param max
     * @param step 指定步长，可大可小
     * @return
     */
    public static Range<Integer> range(int min, int max, int step) {
        Range<Integer> range = new Range<>();
        if (step >= 0) {
            if (min <= max) {
                for (int i = min; i < max; i += step) {
                    range.add(i);
                }
            }
        } else {
            if (min >= max) {
                for (int i = min; i > max; i += step) {
                    range.add(i);
                }
            }
        }
        return range;
    }

    /**
     * 从0（包括）到max（不包括）的列表
     *
     * @param max
     * @return
     */
    public static Range<Long> range(long max) {
        if (max < 0) {
            max = 0;
        }
        Range<Long> range = new Range<>();
        for (long i = 0; i < max; i++) {
            range.add(i);
        }
        return range;
    }

    /**
     * @param min 制定下界
     * @param max
     * @return
     */
    public static Range<Long> range(long min, long max) {
        Range<Long> range = new Range<>();
        if (min <= max) {
            for (long i = min; i < max; i++) {
                range.add(i);
            }
        }
        return range;
    }

    /**
     * @param min
     * @param max
     * @param step 指定步长，可大可小
     * @return
     */
    public static Range<Long> range(long min, long max, long step) {
        Range<Long> range = new Range<>();
        if (step >= 0) {
            if (min <= max) {
                for (long i = min; i < max; i += step) {
                    range.add(i);
                }
            }
        } else {
            if (min >= max) {
                for (long i = min; i > max; i += step) {
                    range.add(i);
                }
            }
        }
        return range;
    }

    /**
     * 从0（包括）到max（不包括）的列表
     *
     * @param max
     * @return
     */
    public static Range<Float> range(float max) {
        if (max < 0f) {
            max = 0f;
        }
        Range<Float> range = new Range<>();
        for (float i = 0f; i < max; i++) {
            range.add(i);
        }
        return range;
    }

    /**
     * @param min 制定下界
     * @param max
     * @return
     */
    public static Range<Float> range(float min, float max) {
        Range<Float> range = new Range<>();
        if (min <= max) {
            for (float i = min; i < max; i++) {
                range.add(i);
            }
        }
        return range;
    }

    /**
     * @param min
     * @param max
     * @param step 指定步长，可大可小
     * @return
     */
    public static Range<Float> range(float min, float max, float step) {
        Range<Float> range = new Range<>();
        if (step >= 0f) {
            if (min <= max) {
                for (float i = min; i < max; i += step) {
                    range.add(i);
                }
            }
        } else {
            if (min >= max) {
                for (float i = min; i > max; i += step) {
                    range.add(i);
                }
            }
        }
        return range;
    }


    /**
     * 从0（包括）到max（不包括）的列表
     *
     * @param max
     * @return
     */
    public static Range<Double> range(double max) {
        if (max < 0D) {
            max = 0D;
        }
        Range<Double> range = new Range<>();
        for (double i = 0D; i < max; i++) {
            range.add(i);
        }
        return range;
    }

    /**
     * @param min 制定下界
     * @param max
     * @return
     */
    public static Range<Double> range(double min, double max) {
        Range<Double> range = new Range<>();
        if (min <= max) {
            for (double i = min; i < max; i++) {
                range.add(i);
            }
        }
        return range;
    }

    /**
     * @param min
     * @param max
     * @param step 指定步长，可大可小
     * @return
     */
    public static Range<Double> range(double min, double max, double step) {
        Range<Double> range = new Range<>();
        if (step >= 0D) {
            if (min <= max) {
                for (double i = min; i < max; i += step) {
                    range.add(i);
                }
            }
        } else {
            if (min >= max) {
                for (double i = min; i > max; i += step) {
                    range.add(i);
                }
            }
        }
        return range;
    }
}
