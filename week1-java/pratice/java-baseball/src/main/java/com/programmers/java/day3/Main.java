package com.programmers.java.day3;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        new Main().loop(10, System.out::println);  // 호스트 코드

        new Main().filterNumbers(30,
            i -> i % 2 == 0,
            System.out::println);
    }

    void filterNumbers(int max, Predicate<Integer> p, Consumer<Integer> c) {
        for (int i = 0; i < max; i++) {
            if (p.test(i)) {
                c.accept(i);
            }
        }
    }

    void loop(int n, MyConsumer<Integer> consumer) {
        for (int i = 0; i < n; i++) {
            // 뭔가를 해라  ->  i를 주고 뭔가 해라
            // 입력은 있고, 출력은 따로 없어도 된다.
            // Consumer
            consumer.consume(i);
        }
    }
}
