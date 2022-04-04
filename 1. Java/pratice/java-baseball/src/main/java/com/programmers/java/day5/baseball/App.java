package com.programmers.java.day5.baseball;

import com.programmers.java.day5.baseball.engine.BaseBall;
import com.programmers.java.day5.baseball.engine.io.NumberGenerator;

public class App {
    public static void main(String[] args) {
        // NumberGenerator numberGenerator = new FakerNumberGenerator();
        NumberGenerator numberGenerator = new HackFakerNumberGenerator();
        Console console = new Console();

        new BaseBall(numberGenerator, console, console).run();
    }
}

