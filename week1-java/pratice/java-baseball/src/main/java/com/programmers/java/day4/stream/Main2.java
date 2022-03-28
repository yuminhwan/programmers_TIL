package com.programmers.java.day4.stream;

import java.util.Random;
import java.util.stream.Stream;

public class Main2 {
    public static void main(String[] args) {
        Random r = new Random();
        Stream.generate(r::nextInt)
            .limit(10)
            .forEach(System.out::println);  // 랜덤 값 10개 출력

        Stream.iterate(0, (i) -> i + 2) // 초기값 , 다음 값에 어떤 로직을 수행할 지
            .limit(10)
            .forEach(System.out::println); // 0 2 4 6 .... 16 18

    }
}
