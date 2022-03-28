package com.programmers.java.day4.Optional;

import java.util.Optional;

import practice.day4.collection.User;

public class Main2 {
    public static void main(String[] args) {
        Optional<User> optionalUser = Optional.empty();  // null
        optionalUser = Optional.of(new User(1, "2")); // 데이터

        if (optionalUser.isPresent()) {
            // do 1
        } else {
            // do 2
        }

        if (optionalUser.isEmpty()) {
            // do 2
        } else {
            // do 1
        }

        optionalUser.ifPresent(user -> {
            // do 1
        });

        optionalUser.ifPresentOrElse(user -> {
            // do 1
        }, () -> {
            // do2
        });
    }
}
