package com.programmers.java.day3;

@FunctionalInterface
public interface MyMapper<T, V> {
    V map(T s);
}
