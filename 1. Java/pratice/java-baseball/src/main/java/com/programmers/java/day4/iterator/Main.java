package com.programmers.java.day4.iterator;

import java.util.Arrays;

import practice.day4.collection.MyCollection;

public class Main {
    public static void main(String[] args) {
        MyIterator<String> iter =
            new MyCollection<String>(Arrays.asList("A", "BC", "CAS", "DASD", "EAAAA"))
                .iterator();

        while (iter.hasNext()) {
            String s = iter.next();
            int len = s.length();
            if (len % 2 == 0) {
                continue;
            }

            System.out.println(s);
        }
    }
}
