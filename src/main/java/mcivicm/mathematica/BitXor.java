package mcivicm.mathematica;

/**
 * 按位异或
 */

public class BitXor {
    /**
     * 给出整数 Subscript[n, i] 的位异或.
     *
     * @param integers
     * @return
     */
    public static int bitXor(Integer... integers) {
        ObjectHelper.requireNonNull(integers, "integers");
        if (integers.length == 0) {
            return 0;
        } else if (integers.length == 1) {
            return integers[0];
        } else if (integers.length == 2) {
            return integers[0] ^ integers[1];
        } else {
            int result = integers[0];
            for (int i = 1; i < integers.length; i++) {
                result ^= integers[i];
            }
            return result;
        }
    }
}
