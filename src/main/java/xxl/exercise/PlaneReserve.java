package xxl.exercise;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 航班预定
 */
public class PlaneReserve {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] records = line.split(";");
            int[][] books = new int[records.length][3];
            for (int i = 0; i < books.length; i++) {
                String[] items = records[i].split(",");
                for (int j = 0; j < items.length; j++) {
                    books[i][j] = Integer.parseInt(items[j]);
                }
            }
            int[] ans = corpFlightBookings(books, 5);
            System.out.println(Arrays.toString(ans));
        }
    }

    static int[] corpFlightBookings(int[][] bookings, int n) {
        int[] ans = new int[n];
        for (int[] rec : bookings) {
            for (int j = rec[0]; j <= rec[1]; ++j) {
                ans[j - 1] += rec[2];
            }
        }
        return ans;
    }
}
