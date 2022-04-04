package com.programmers.java.day5.dependency.lombok.step1;

public class Main {
    public static void main(String[] args) {
        User user = new User(1, "a");
        User user2 = new User(1, "a");

        user.setName("zzz");
        System.out.println(user);   // User(age=1, name=zzz)
        System.out.println(user.equals(user2));  // false
        System.out.println(user.getAge() + user.getName());  // User(age=1, name=zzz)

        User user3 = new User();
        System.out.println(user3.getName());  // null
    }
}
