package com.programmers.java.pattern;

public class MultiThread {
    public static void main(String[] args) {
        new Thread(() -> {
            Singleton singleton = Singleton.getInstance();
            System.out.println(singleton);
        }).start();

        new Thread(() -> {
            Singleton singleton = Singleton.getInstance();
            System.out.println(singleton);
        }).start();
    }
}
