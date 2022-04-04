package com.programmers.java.day5.baseball;

import java.util.stream.Stream;

import com.github.javafaker.Faker;
import com.programmers.java.day5.baseball.engine.io.NumberGenerator;
import com.programmers.java.day5.baseball.engine.model.Numbers;

public class FakerNumberGenerator implements NumberGenerator {
    private final Faker faker = new Faker();

    @Override
    public Numbers generate(int count) {
        Faker faker = new Faker();

        return new Numbers(
            Stream.generate(() -> faker.number().randomDigitNotZero())
                .distinct()
                .limit(count)
                .toArray(Integer[]::new)
        );
    }
}
