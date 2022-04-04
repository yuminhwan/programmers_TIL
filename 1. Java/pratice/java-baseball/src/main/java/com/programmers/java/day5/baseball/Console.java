package com.programmers.java.day5.baseball;

import java.util.Scanner;

import com.programmers.java.day5.baseball.engine.io.Input;
import com.programmers.java.day5.baseball.engine.io.Output;
import com.programmers.java.day5.baseball.engine.model.BallCount;

public class Console implements Input, Output {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String input(String msg) {
        System.out.print(msg);
        return scanner.next();
    }

    @Override
    public void ballCount(BallCount ballCount) {
        System.out.println(ballCount.getStrike() + " Strikes, " + ballCount.getBall() + " Balls");
    }

    @Override
    public void inputError() {
        System.out.println("입력이 잘못되었습니다");
    }

    @Override
    public void correct() {
        System.out.println("정답입니다.");
    }
}
