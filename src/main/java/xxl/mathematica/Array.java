package xxl.mathematica;

import xxl.mathematica.function.Function;
import xxl.mathematica.function.BiFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * 数组
 */

public class Array {
    /**
     * 以0为起点
     *
     * @param function
     * @param n
     * @param <R>
     * @return
     */
    public static <R> List<R> array(Function<Integer, R> function, int n) {
        return array(function, n, 0);
    }

    /**
     * 以r为起点生成n个值
     *
     * @param function
     * @param n        生成n个值
     * @param r        以r为起点
     * @param <R>
     * @return
     */
    public static <R> List<R> array(Function<Integer, R> function, int n, int r) {
        ObjectHelper.requireNonNull(function);
        ObjectHelper.requireNonNegative(n);

        List<R> rs = new ArrayList<>(0);
        for (int i = 0; i < n; i++) {
            rs.add(function.apply(i + r));
        }
        return rs;
    }

    /**
     * 以r为起点生成n个值
     *
     * @param function
     * @param n        生成n个值
     * @param r        以r为起点
     * @param <R>
     * @return
     */
    public static <R> List<R> array(Function<Long, R> function, long n, long r) {
        ObjectHelper.requireNonNull(function, "function");
        ObjectHelper.requireNonNegative(n, "n");

        List<R> rs = new ArrayList<R>(0);
        for (int i = 0; i < n; i++) {
            rs.add(function.apply(i + r));
        }
        return rs;
    }

    /**
     * 以r为起点生成n个值
     *
     * @param function
     * @param n        生成n个值
     * @param r        以r为起点
     * @param <R>
     * @return
     */
    public static <R> List<R> array(Function<Float, R> function, int n, float r) {
        ObjectHelper.requireNonNull(function, "function");
        ObjectHelper.requireNonNegative(n, "n");

        List<R> rs = new ArrayList<R>(0);
        for (int i = 0; i < n; i++) {
            rs.add(function.apply(i + r));
        }
        return rs;
    }

    /**
     * 以r为起点生成n个值
     *
     * @param function
     * @param n        生成n个值
     * @param r        以r为起点
     * @param <R>
     * @return
     */
    public static <R> List<R> array(Function<Double, R> function, int n, double r) {
        ObjectHelper.requireNonNull(function, "function");
        ObjectHelper.requireNonNegative(n, "n");

        List<R> rs = new ArrayList<R>(0);
        for (int i = 0; i < n; i++) {
            rs.add(function.apply(i + r));
        }
        return rs;
    }


    /**
     * 将范围min和max分成n等分
     *
     * @param function
     * @param n
     * @param min
     * @param max
     * @param <R>
     * @return
     */
    public static <R> List<R> array(Function<Double, R> function, int n, int min, int max) {
        ObjectHelper.requireNonNull(function, "function");
        ObjectHelper.requireNonNegative(n, "n");
        ObjectHelper.requireAscend(min, max, "min", "max");

        List<R> rs = new ArrayList<R>(0);
        for (int i = 0; i < n; i++) {
            rs.add(function.apply(min + ((double) (max - min)) * i / n));
        }
        return rs;
    }

    /**
     * 将范围min和max分成n等分
     *
     * @param function
     * @param n
     * @param min
     * @param max
     * @param <R>
     * @return
     */
    public static <R> List<R> array(Function<Double, R> function, int n, long min, long max) {
        ObjectHelper.requireNonNull(function, "function");
        ObjectHelper.requireNonNegative(n, "n");
        ObjectHelper.requireAscend(min, max, "min", "max");

        List<R> rs = new ArrayList<R>(0);
        for (int i = 0; i < n; i++) {
            rs.add(function.apply(min + ((double) (max - min)) * i / n));
        }
        return rs;
    }


    /**
     * 将范围min和max分成n等分
     *
     * @param function
     * @param n
     * @param min
     * @param max
     * @param <R>
     * @return
     */
    public static <R> List<R> array(Function<Float, R> function, int n, float min, float max) {
        ObjectHelper.requireNonNull(function, "function");
        ObjectHelper.requireNonNegative(n, "n");
        ObjectHelper.requireAscend(min, max, "min", "max");

        List<R> rs = new ArrayList<R>(0);
        for (int i = 0; i < n; i++) {
            rs.add(function.apply(min + ((max - min)) * i / n));
        }
        return rs;
    }


    /**
     * 将范围min和max分成n等分
     *
     * @param function
     * @param n
     * @param min
     * @param max
     * @param <R>
     * @return
     */
    public static <R> List<R> array(Function<Double, R> function, int n, double min, double max) {
        ObjectHelper.requireNonNull(function, "function");
        ObjectHelper.requireNonNegative(n, "n");
        ObjectHelper.requireAscend(min, max, "min", "max");

        List<R> rs = new ArrayList<R>(0);
        for (int i = 0; i < n; i++) {
            rs.add(function.apply(min + ((max - min) * i / n)));
        }
        return rs;
    }

    /**
     * 根据函数生成n1*n2个结果的二维列表
     *
     * @param function
     * @param n1
     * @param n2
     * @param <R>
     * @return
     */
    public static <R> List<List<R>> array(BiFunction<Integer, Integer, R> function, int n1, int n2) {
        ObjectHelper.requireNonNull(function, "function");
        ObjectHelper.requireNonNegative(n1, "n1");
        ObjectHelper.requireNonNegative(n2, "n2");

        List<List<R>> rs = new ArrayList<>(0);
        for (int i = 0; i < n1; i++) {
            List<R> list = new ArrayList<>(0);
            for (int j = 0; j < n2; j++) {
                list.add(function.apply(i, j));
            }
            rs.add(list);
        }
        return rs;
    }

    /**
     * 根据函数生成n1*n2个结果的二维列表
     *
     * @param function
     * @param n1
     * @param n2
     * @param r1
     * @param r2
     * @param <R>
     * @return
     */
    public static <R> List<List<R>> array(BiFunction<Integer, Integer, R> function, int n1, int n2, int r1, int r2) {
        ObjectHelper.requireNonNull(function, "function");
        ObjectHelper.requireNonNegative(n1, "n1");
        ObjectHelper.requireNonNegative(n2, "n2");

        List<List<R>> rs = new ArrayList<>(0);
        for (int i = 0; i < n1; i++) {
            List<R> temp = new ArrayList<>();
            for (int j = 0; j < n2; j++) {
                temp.add(function.apply(i + r1, j + r2));
            }
            rs.add(temp);
        }
        return rs;
    }

    /**
     * 根据函数生成n1*n2个结果的二维列表
     *
     * @param function
     * @param n1
     * @param n2
     * @param r1
     * @param r2
     * @param <R>
     * @return
     */
    public static <R> List<List<R>> array(BiFunction<Long, Long, R> function, long n1, long n2, long r1, long r2) {
        ObjectHelper.requireNonNull(function, "function");
        ObjectHelper.requireNonNegative(n1, "n1");
        ObjectHelper.requireNonNegative(n2, "n2");

        List<List<R>> rs = new ArrayList<>(0);
        for (int i = 0; i < n1; i++) {
            List<R> temp = new ArrayList<>();
            for (int j = 0; j < n2; j++) {
                temp.add(function.apply(i + r1, j + r2));
            }
            rs.add(temp);
        }
        return rs;
    }

    /**
     * 根据函数生成n1*n2个结果的二维列表
     *
     * @param function
     * @param n1
     * @param n2
     * @param r1
     * @param r2
     * @param <R>
     * @return
     */
    public static <R> List<List<R>> array(BiFunction<Float, Float, R> function, int n1, int n2, float r1, float r2) {
        ObjectHelper.requireNonNull(function, "function");
        ObjectHelper.requireNonNegative(n1, "n1");
        ObjectHelper.requireNonNegative(n2, "n2");

        List<List<R>> rs = new ArrayList<>(0);
        for (int i = 0; i < n1; i++) {
            List<R> temp = new ArrayList<>();
            for (int j = 0; j < n2; j++) {
                temp.add(function.apply(i + r1, j + r2));
            }
            rs.add(temp);
        }
        return rs;
    }

    /**
     * 指定维度的最大值和最小值
     *
     * @param function
     * @param n1
     * @param n2
     * @param min1
     * @param max1
     * @param min2
     * @param max2
     * @param <R>
     * @return
     */
    public static <R> List<List<R>> array(BiFunction<Float, Float, R> function, int n1, int n2, float min1, float max1, float min2, float max2) {
        ObjectHelper.requireNonNull(function, "function");
        ObjectHelper.requireNonNegative(n1, "n1");
        ObjectHelper.requireAscend(min1, max1, "min1", "max1");
        ObjectHelper.requireNonNegative(n2, "n2");
        ObjectHelper.requireAscend(min2, max2, "min2", "max2");

        List<List<R>> rs = new ArrayList<>();
        float intervalI = (max1 - min1) / n1;
        float intervalJ = (max2 - min2) / n2;
        for (int i = 0; i < n1; i++) {
            List<R> temp = new ArrayList<>(0);
            for (int j = 0; j < n2; j++) {
                temp.add(function.apply(min1 + intervalI * i, min2 + intervalJ * j));
            }
            rs.add(temp);
        }
        return rs;
    }

    /**
     * 根据函数生成n1*n2个结果的二维列表
     *
     * @param function
     * @param n1
     * @param n2
     * @param r1
     * @param r2
     * @param <R>
     * @return
     */
    public static <R> List<List<R>> array(BiFunction<Double, Double, R> function, int n1, int n2, double r1, double r2) {
        ObjectHelper.requireNonNull(function, "function");
        ObjectHelper.requireNonNegative(n1, "n1");
        ObjectHelper.requireNonNegative(n2, "n2");

        List<List<R>> rs = new ArrayList<>(0);
        for (int i = 0; i < n1; i++) {
            List<R> temp = new ArrayList<>();
            for (int j = 0; j < n2; j++) {
                temp.add(function.apply(i + r1, j + r2));
            }
            rs.add(temp);
        }
        return rs;
    }

    /**
     * 指定维度的最大值和最小值
     *
     * @param function
     * @param n1
     * @param n2
     * @param min1
     * @param max1
     * @param min2
     * @param max2
     * @param <R>
     * @return
     */
    public static <R> List<List<R>> array(BiFunction<Double, Double, R> function, int n1, int n2, int min1, int max1, int min2, int max2) {
        ObjectHelper.requireNonNull(function, "function");
        ObjectHelper.requireNonNegative(n1, "n1");
        ObjectHelper.requireAscend(min1, max1, "min1", "max1");
        ObjectHelper.requireNonNegative(n2, "n2");
        ObjectHelper.requireAscend(min2, max2, "min2", "max2");

        List<List<R>> rs = new ArrayList<>();
        double intervalI = ((double) (max1 - min1)) / n1;
        double intervalJ = ((double) (max2 - min2)) / n2;
        for (int i = 0; i < n1; i++) {
            List<R> temp = new ArrayList<>(0);
            for (int j = 0; j < n2; j++) {
                temp.add(function.apply(min1 + intervalI * i, min2 + intervalJ * j));
            }
            rs.add(temp);
        }
        return rs;
    }

    /**
     * 指定维度的最大值和最小值
     *
     * @param function
     * @param n1
     * @param n2
     * @param min1
     * @param max1
     * @param min2
     * @param max2
     * @param <R>
     * @return
     */
    public static <R> List<List<R>> array(BiFunction<Double, Double, R> function, int n1, int n2, long min1, long max1, long min2, long max2) {
        ObjectHelper.requireNonNull(function, "function");
        ObjectHelper.requireNonNegative(n1, "n1");
        ObjectHelper.requireAscend(min1, max1, "min1", "max1");
        ObjectHelper.requireNonNegative(n2, "n2");
        ObjectHelper.requireAscend(min2, max2, "min2", "max2");

        List<List<R>> rs = new ArrayList<>();
        double intervalI = ((double) (max1 - min1)) / n1;
        double intervalJ = ((double) (max2 - min2)) / n2;
        for (int i = 0; i < n1; i++) {
            List<R> temp = new ArrayList<>(0);
            for (int j = 0; j < n2; j++) {
                temp.add(function.apply(min1 + intervalI * i, min2 + intervalJ * j));
            }
            rs.add(temp);
        }
        return rs;
    }





    /**
     * 指定维度的最大值和最小值
     *
     * @param function
     * @param n1
     * @param n2
     * @param min1
     * @param max1
     * @param min2
     * @param max2
     * @param <R>
     * @return
     */
    public static <R> List<List<R>> array(BiFunction<Double, Double, R> function, int n1, int n2, double min1, double max1, double min2, double max2) {
        ObjectHelper.requireNonNull(function, "function");
        ObjectHelper.requireNonNegative(n1, "n1");
        ObjectHelper.requireAscend(min1, max1, "min1", "max1");
        ObjectHelper.requireNonNegative(n2, "n2");
        ObjectHelper.requireAscend(min2, max2, "min2", "max2");

        List<List<R>> rs = new ArrayList<>();
        double intervalI = (max1 - min1) / n1;
        double intervalJ = (max2 - min2) / n2;
        for (int i = 0; i < n1; i++) {
            List<R> temp = new ArrayList<>(0);
            for (int j = 0; j < n2; j++) {
                temp.add(function.apply(min1 + intervalI * i, min2 + intervalJ * j));
            }
            rs.add(temp);
        }
        return rs;
    }
}
