package com.mcivicm.mathematica.exception;

/**
 * 没有足够多的计算结果异常
 */

public class NoEnoughResultException extends Exception {
    public NoEnoughResultException(int has, int require) {
        super("the result size is " + has + ", can not get " + String.valueOf(require) + " item");
    }
}
