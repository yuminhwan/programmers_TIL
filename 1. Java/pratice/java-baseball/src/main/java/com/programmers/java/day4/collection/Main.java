package com.programmers.java.day4.collection;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        MyCollection<String> c1 = new MyCollection<>(Arrays.asList("A", "BC", "CAS", "DASD", "EAAAA"));
        MyCollection<Integer> c2 = c1.map(String::length);
        MyCollection<Integer> c3 = c2.filter(i -> i % 2 == 0);
        c3.foreach(System.out::println);

        //메서드 체이닝
        new MyCollection<>(Arrays.asList("A", "BC", "CAS", "DASD", "EAAAA"))
            .map(String::length)
            .filter(i -> i % 2 == 1)
            .foreach(System.out::println);

        int s = new MyCollection<>(Arrays.asList("A", "BC", "CAS", "DASD", "EAAAA"))
            .map(String::length)
            .filter(i -> i % 2 == 1)
            .size();

        System.out.println(s); // 3
    }
}
