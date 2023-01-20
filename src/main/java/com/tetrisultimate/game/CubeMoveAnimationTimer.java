package com.tetrisultimate.game;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;

public class CubeMoveAnimationTimer extends AnimationTimer {
    private Func action;
    public double stepX;
    public double stepY;
    public CubeMoveAnimationTimer(double stepX, double stepY){
        this.stepX = stepX;
        this.stepY = stepY;
    }
    public void setAction(Func action){
        this.action = action;
    }
    @Override
    public void handle(long l) {
        action.apply();
    }
}
