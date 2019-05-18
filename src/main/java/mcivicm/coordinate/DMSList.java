package mcivicm.coordinate;

import java.util.Arrays;
import java.util.List;

public class DMSList {
    /**
     * 转成度分秒
     *
     * @param degree
     * @return
     */
    public static List<Number> dmsList(double degree) {
        int d = (int) Math.floor(degree);
        double min = (degree - d) * 60;
        int m = (int) Math.floor(min);
        double s = (min - m) * 60;
        return Arrays.asList(d, m, s);
    }
}
