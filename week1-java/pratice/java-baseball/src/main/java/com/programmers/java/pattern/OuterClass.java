package com.programmers.java.pattern;

public class OuterClass {

    private final String outerName = "홍길동";

    class Inner {
        public void printName() {
            System.out.println(outerName);
        }
    }
}
