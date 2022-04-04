package com.programmers.java.day5.dependency.faker;

import java.util.Arrays;
import java.util.stream.Stream;

import com.github.javafaker.Faker;

public class Main {
    public static void main(String[] args) {
        Faker faker = new Faker();

        Integer[] nums = Stream.generate(() -> faker.number().randomDigitNotZero())
            .distinct()
            .limit(10)
            .toArray(Integer[]::new);

        System.out.println(Arrays.toString(nums));  // [6, 8, 3]

        String title = faker.name().title();
        System.out.println(title);  // Dynamic Tactics Technician
        System.out.println(faker.name().fullName());  // Abe Fadel
        System.out.println(faker.number().randomNumber());
    }
}
