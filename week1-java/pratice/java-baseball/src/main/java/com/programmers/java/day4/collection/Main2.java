package com.programmers.java.day4.collection;

import java.util.Arrays;

public class Main2 {
    public static void main(String[] args) {
        new MyCollection<User>(
            Arrays.asList(
                new User(15, "AAA"),
                new User(16, "BBB"),
                new User(17, "CCC"),
                new User(18, "DDD"),
                new User(19, "EEE"),
                new User(20, "FFF"),
                new User(21, "GGG"),
                new User(22, "HHH"),
                new User(23, "III")
            )
        )
            .filter(user -> user.isOverAndSameAge(19))
            .foreach(System.out::println);
    }
}
