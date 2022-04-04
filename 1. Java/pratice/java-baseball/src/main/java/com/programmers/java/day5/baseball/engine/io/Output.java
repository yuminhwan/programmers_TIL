package com.programmers.java.day5.baseball.engine.io;

import com.programmers.java.day5.baseball.engine.model.BallCount;

public interface Output {
    void ballCount(BallCount ballCount);

    void inputError();

    void correct();
}
