package com.programmers.java.day5.baseball.engine;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import com.programmers.java.day5.baseball.engine.io.Input;
import com.programmers.java.day5.baseball.engine.io.NumberGenerator;
import com.programmers.java.day5.baseball.engine.io.Output;
import com.programmers.java.day5.baseball.engine.model.BallCount;
import com.programmers.java.day5.baseball.engine.model.Numbers;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BaseBall implements Runnable {
    private final int COUNT_OF_NUMBERS = 5;

    private NumberGenerator numberGenerator;
    private Input input;
    private Output output;

    @Override
    public void run() {
        Numbers answer = numberGenerator.generate(COUNT_OF_NUMBERS);

        while (true) {
            String inputString = input.input("숫자를 맞춰보세요. :");
            Optional<Numbers> inputNumbers = parse(inputString);
            if (inputNumbers.isEmpty()) {
                output.inputError();
                continue;
            }

            BallCount ballCount = ballCount(answer, inputNumbers.get());
            output.ballCount(ballCount);
            if (ballCount.getStrike() == COUNT_OF_NUMBERS) {
                output.correct();
                break;
            }
        }
    }

    private BallCount ballCount(Numbers answer, Numbers inputNumbers) {
        AtomicInteger strike = new AtomicInteger();  // 동기화 기능을 추가
        AtomicInteger ball = new AtomicInteger();

        answer.indexedForEach((a, i) -> {
            inputNumbers.indexedForEach((n, j) -> {
                if (!a.equals(n)) {
                    return;
                }

                if (i.equals(j)) {
                    strike.addAndGet(1);
                } else {
                    ball.addAndGet(1);
                }
            });
        });
        return new BallCount(strike.get(), ball.get());
    }

    private Optional<Numbers> parse(String inputString) {
        if (inputString.length() != COUNT_OF_NUMBERS) {
            return Optional.empty();
        }

        long count = inputString.chars()
            .filter(Character::isDigit)
            .map(Character::getNumericValue)
            .filter(i -> i > 0)
            .distinct()
            .count();

        if (count != COUNT_OF_NUMBERS) {
            return Optional.empty();
        }

        return Optional.of(
            new Numbers(
                inputString.chars()
                    .map(Character::getNumericValue)
                    .boxed()
                    .toArray(Integer[]::new)
            ));
    }
}
