package com.programmers.java.day4.stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Stream;

public class Main3 {
    public static void main(String[] args) {
        // 0 ~ 200 사이의 값 중에서 랜덤값 5개를 뽑아 큰 순서대로 표시하시오.
        Random r = new Random();
        int[] arr = Stream.generate(() -> r.nextInt(200) + 1)
            .limit(5)
            .sorted(Comparator.reverseOrder()) // 역순
            .mapToInt(i -> i)
            .toArray();

        System.out.println(Arrays.toString(arr));  // [91, 59, 50, 35, 5]
    }
}
