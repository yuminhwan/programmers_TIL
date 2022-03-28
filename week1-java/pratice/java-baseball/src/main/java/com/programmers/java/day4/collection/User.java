package com.programmers.java.day4.collection;

public class User {
    public static User EMPTY = new User(0, "");

    private final int age;
    private final String name;

    public User(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public boolean isOverAndSameAge(int age) {
        if (this == EMPTY) {
            return false;
        }
        return this.age >= age;
    }

    @Override
    public String toString() {
        return name + " (" + age + ")";
    }
}
