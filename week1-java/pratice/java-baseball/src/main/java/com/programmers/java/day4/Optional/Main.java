package com.programmers.java.day4.Optional;

import practice.day4.collection.User;

public class Main {
    public static void main(String[] args) {
        User user = User.EMPTY;

        User user2 = getUser();

        //if(user2 == null) 이 방법을 아래 방법처럼
        if (user2 == User.EMPTY)

            System.out.println(user);
    }

    private static User getUser() {
        return User.EMPTY;
    }
}
