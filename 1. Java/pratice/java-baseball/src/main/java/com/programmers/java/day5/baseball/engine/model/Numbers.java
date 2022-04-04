package com.programmers.java.day5.baseball.engine.model;

import java.util.function.BiConsumer;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class Numbers {
    private Integer[] nums;

    public void indexedForEach(BiConsumer<Integer, Integer> consumer) {
        for (int i = 0; i < nums.length; i++) {
            consumer.accept(nums[i], i);
        }
    }

}
