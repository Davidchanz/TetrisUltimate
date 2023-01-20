package com.tetrisultimate.game;

import com.tetrisultimate.controller.GameController;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Random;

public class Cube extends ImageView implements Cloneable{
    private FadeTransition fadeTransition = new FadeTransition(Duration.millis(Constants.explodeSpeed), this);
    //private TranslateTransition translateTransition = new TranslateTransition(Duration.millis(Constants.fallSpeed), this);
    public int score;
    private String path;
    public int col;
    public int row;
    public CubeMoveAnimationTimer timer;
    public Cube(String path){
        File file = new File(Constants.sourcePath+path);
        ImageView imageView = null;
        try {
            imageView = new ImageView(file.toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        this.setImage(imageView.getImage());
        //imageView.fitWidthProperty().bind(this.widthProperty().divide(1.2));
        this.setFitHeight(Constants.size);
        this.setFitWidth(Constants.size);
        this.setPreserveRatio(true);

        //super(Constants.size, Constants.size, color);
        /*this.setStroke(Color.BLACK);
        this.setStrokeWidth(1);*/
        this.setManaged(false);

        this.path = path;
        this.score = Constants.score;
    }
    @Override
    public Cube clone() throws CloneNotSupportedException {
        return new Cube(this.path);
    }

    public void move(double xPos, double yPos, Func foo, double speed) {
        //new Thread(()->{
            timer = new CubeMoveAnimationTimer((xPos - getTranslateX()) / (speed), (yPos - getTranslateY()) / (speed));
            timer.setAction(() -> {
                //double delay = (yPos - getTranslateY()) / (Constants.moveSpeed * 100);
                this.setTranslateY(getTranslateY()+ timer.stepY);
                //this.setTranslateX(getTranslateX()+ timer.stepX);
                if(getTranslateY() >= yPos /*&& getTranslateX() >= xPos*/){
                    this.row = (int)(getTranslateY() / Constants.size);
                    this.col = (int)(getTranslateX() / Constants.size);
                    foo.apply();
                    timer.stop();
                }
                /*translateTransition.setToX(xPos);
                translateTransition.setToY(yPos);
                translateTransition.play();
                translateTransition.setOnFinished(actionEvent -> {
                    //System.out.println("move end");
                    foo.apply();
                });*/
            });
            timer.start();

        //}).start();
        /*this.setTranslateX(xPos);
        this.setTranslateY(yPos);*/
    }

    public void move(double xPos, double yPos, double speed) {
        /*new Thread(() -> {
            translateTransition.setToX(xPos);
            translateTransition.setToY(yPos);
            translateTransition.play();
        }).start();*/

        //new Thread(()->{
            timer = new CubeMoveAnimationTimer((xPos - getTranslateX()) / (speed), (yPos - getTranslateY()) / (speed));
            timer.setAction(() -> {
                //double delay = (yPos - getTranslateY()) / (Constants.moveSpeed * 100);
                this.setTranslateY(getTranslateY()+ timer.stepY);
                //this.setTranslateX(getTranslateX()+ timer.stepX);
                if(getTranslateY() >= yPos /*&& getTranslateX() >= xPos*/){
                    //foo.apply();
                    this.row = (int)(getTranslateY() / Constants.size);
                    this.col = (int)(getTranslateX() / Constants.size);
                    timer.stop();
                }
                /*translateTransition.setToX(xPos);
                translateTransition.setToY(yPos);
                translateTransition.play();
                translateTransition.setOnFinished(actionEvent -> {
                    //System.out.println("move end");
                    foo.apply();
                });*/
            });
            timer.start();

        //}).start();

        /*this.setTranslateX(xPos);
        this.setTranslateY(yPos);*/
    }

    public void movePermanent(double xPos, double yPos) {
        this.setTranslateX(xPos);
        this.setTranslateY(yPos);
        this.row = (int)(getTranslateY() / Constants.size);
        this.col = (int)(getTranslateX() / Constants.size);
    }
    public void movePermanent(double xPos, double yPos, Func foo) {
        this.setTranslateX(xPos);
        this.setTranslateY(yPos);
        this.row = (int)(getTranslateY() / Constants.size);
        this.col = (int)(getTranslateX() / Constants.size);
        foo.apply();
    }

    public void explode() {
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(actionEvent -> {
            this.setVisible(false);
        });
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
