package com.github.ko4evneg.caloriesApp.util;

import java.util.Collection;

public interface AssertionsHelper<T> {
    void assertRecursiveEquals(T actual, T expected);

    void assertAllRecursiveEquals(Collection<T> actual, Collection<T> expected);
}
