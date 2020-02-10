package xxl.exercise;

/**
 * 报数
 */
public class CountOff {
  public static void main(String[] args) {

  }

  class Counter {
    int k = 0;
    int m = 0;
    int n = 0;

    public Counter(int n, int m, int k) {
      this.n = n;
      this.m = m;
      this.k = k;
    }

    public int getCount() {
      int count = 0;
      while (true) {
        ++count;
        int mod = count % n;
        int quo = count / n;

      }
    }
  }
}
