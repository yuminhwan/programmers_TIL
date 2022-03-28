package com.programmers.java.pattern;

import java.io.Serializable;

public class Singleton implements Serializable {
    private Singleton() {
    }

    public static Singleton getInstance() {
        return singletonHolder.INSTANCE;
    }

    private Object readResolve() {
        return getInstance();
    }

    private static class singletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }
}
