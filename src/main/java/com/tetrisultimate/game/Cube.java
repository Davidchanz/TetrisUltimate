package com.tetrisultimate.game;

import com.tetrisultimate.util.Constants;
import com.tetrisultimate.util.Func;
import javafx.animation.FadeTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;
import java.net.MalformedURLException;

public class Cube extends ImageView implements Cloneable{
    private final FadeTransition fadeTransition = new FadeTransition(Duration.millis(Constants.explodeSpeed), this);
    public int score;
    private final String path;
    public int col;
    public int row;
    public CubeMoveAnimationTimer timer;
    public Cube(String path){
        File file = new File(Constants.sourcePath+path);
        ImageView imageView;
        try {
            imageView = new ImageView(file.toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        this.setImage(imageView.getImage());
        this.setFitHeight(Constants.size);
        this.setFitWidth(Constants.size);
        this.setPreserveRatio(true);

        this.setManaged(false);

        this.path = path;
        this.score = Constants.score;
    }
    @Override
    public Cube clone() {
        return new Cube(this.path);
    }
    public void move(double xPos, double yPos, Func foo, double speed) {
        timer = new CubeMoveAnimationTimer((xPos - getTranslateX()) / (speed), (yPos - getTranslateY()) / (speed));
        timer.setAction(() -> {
            this.setTranslateY(getTranslateY()+ timer.stepY);
            if(getTranslateY() >= yPos){
                this.row = (int)(getTranslateY() / Constants.size);
                this.col = (int)(getTranslateX() / Constants.size);
                foo.apply();
                timer.stop();
            }
        });
        timer.start();
    }
    public void move(double xPos, double yPos, double speed) {
        timer = new CubeMoveAnimationTimer((xPos - getTranslateX()) / (speed), (yPos - getTranslateY()) / (speed));
        timer.setAction(() -> {
            this.setTranslateY(getTranslateY()+ timer.stepY);
            if(getTranslateY() >= yPos){
                this.row = (int)(getTranslateY() / Constants.size);
                this.col = (int)(getTranslateX() / Constants.size);
                timer.stop();
            }
        });
        timer.start();
    }
    public void movePermanent(double xPos, double yPos) {
        this.setTranslateX(xPos);
        this.setTranslateY(yPos);
        this.row = (int)(getTranslateY() / Constants.size);
        this.col = (int)(getTranslateX() / Constants.size);
    }
    public void explode() {
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(actionEvent -> this.setVisible(false));
        fadeTransition.play();
    }
    public void explode(Func foo) {
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(actionEvent -> {
            this.setVisible(false);
            foo.apply();
        });
        fadeTransition.play();
    }
}
