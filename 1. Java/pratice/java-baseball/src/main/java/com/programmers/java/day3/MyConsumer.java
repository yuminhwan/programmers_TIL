package com.programmers.java.day3;

@FunctionalInterface
public interface MyConsumer<T> {
    void consume(T i);
}
