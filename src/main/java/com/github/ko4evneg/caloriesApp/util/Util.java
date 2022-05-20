package com.github.ko4evneg.caloriesApp.util;

import org.springframework.lang.Nullable;

public class Util {
    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T reference, @Nullable T left, @Nullable T right) {
        return (left == null || left.compareTo(reference) <= 0) && (right == null || right.compareTo(reference) > 0);
    }
}

