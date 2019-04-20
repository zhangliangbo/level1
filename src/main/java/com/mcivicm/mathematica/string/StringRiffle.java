package com.mcivicm.mathematica.string;

import com.mcivicm.mathematica.ObjectHelper;

import java.util.Arrays;
import java.util.List;

public class StringRiffle {

    public static <T> String stringRiffle(List<T> l, String s) {
        ObjectHelper.requireNonNull(l, s);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < l.size() - 1; i++) {
            sb.append(l.get(i)).append(s);
        }
        sb.append(l.get(l.size() - 1));
        return sb.toString();
    }

    public static <T> String stringRiffle(T[] a, String s) {
        return stringRiffle(Arrays.asList(a), s);
    }

    public static void main(String[] args) {
        System.out.println(stringRiffle(new Integer[]{1, 2, 3}, ":"));
    }

}
