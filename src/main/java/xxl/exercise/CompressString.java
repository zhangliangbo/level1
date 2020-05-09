package xxl.exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 压缩字符串
 */
public class CompressString {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            char[] chars = line.toCharArray();
            System.out.println(compress2(chars));
        }
    }

    static int compress1(char[] chars) {
        int write = 0;
        int start = 0;
        for (int read = 0; read < chars.length; read++) {
            if (read + 1 == chars.length || chars[read + 1] != chars[read]) {
                chars[write++] = chars[read];
                if (read > start) {
                    char[] len = String.valueOf(read - start + 1).toCharArray();
                    for (char c : len) {
                        chars[write++] = c;
                    }
                }
                start = read + 1;
            }
        }
        return write;
    }

    static int compress2(char[] chars) {
        List<Character> res = new ArrayList<>();
        int start = 0;
        for (int read = 0; read < chars.length; read++) {
            if (read + 1 == chars.length || chars[read + 1] != chars[read]) {
                res.add(chars[read]);
                if (read > start) {
                    char[] len = String.valueOf(read - start + 1).toCharArray();
                    for (char c : len) {
                        res.add(c);
                    }
                }
                start = read + 1;
            }
        }
        for (int i = 0; i < res.size(); i++) {
            chars[i] = res.get(i);
        }
        return res.size();
    }
}
