package com.tetrisultimate.controller;

import com.jfoenix.controls.JFXCheckBox;
import com.tetrisultimate.game.*;
import com.tetrisultimate.util.BoardCube;
import com.tetrisultimate.util.Constants;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.tetrisultimate.util.Constants.*;

public class GameController implements Initializable {
    @FXML
    public Pane gameScene;
    @FXML
    public Label scoreLabel;
    @FXML
    public Pane nextItemView;
    @FXML
    public Label lineLabel;
    @FXML
    public Label lvlLabel;
    @FXML
    public JFXCheckBox gravityCheckBox;
    @FXML
    public Label gameOverLabel;
    private Cube[][] board;
    private TetrisItem activeItem = null;
    private Timeline timeline;
    public static AtomicBoolean isMove = new AtomicBoolean(false);
    private static final AtomicBoolean isLeft = new AtomicBoolean(false);
    private static final AtomicBoolean isRight = new AtomicBoolean(false);
    private  boolean game = false;
    private int nextType = new Random().nextInt(TetrisItemsCollection.count);
    private int score = 0;
    private int line = 0;
    private int lvl = 1;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameOverLabel.setVisible(false);
        gameScene.setPrefWidth(colCount*size);
        gameScene.setPrefHeight(rowCount*size);

        scoreLabel.setText(String.valueOf(score));
        lvlLabel.setText(String.valueOf(lvl));
        lineLabel.setText(String.valueOf(line));

        board = new Cube[colCount][rowCount];
        for (int col = 0; col < colCount; col++){
            for (int row = 0; row < rowCount; row++){
                String path = "";
                switch (new Random().nextInt(6)){
                    case 0 -> path = "blue.png";
                    case 1 -> path = "red.png";
                    case 2 -> path = "green.png";
                    case 3 -> path = "purple.png";
                    case 4 -> path = "yellow.png";
                    case 5 -> path = "";
                }
                board[col][row] = new Cube(path);
                board[col][row].setTranslateX(col*size);
                board[col][row].setTranslateY(row*size);
                gameScene.getChildren().add(board[col][row]);
            }
        }

        timeline = new Timeline(new KeyFrame(Duration.millis(time), this::game));
        timeline.setCycleCount(Timeline.INDEFINITE);

        gameScene.setFocusTraversable(true);
        gameScene.requestFocus();
        gameScene.addEventHandler(KeyEvent.KEY_PRESSED, this::onKeyPressed);
    }
    public void start(){
        gameScene.getChildren().clear();
        Constants.ini(gravityCheckBox.isSelected());
        gravityCheckBox.setDisable(true);
        for (int col = 0; col < colCount; col++){
            for (int row = 0; row < rowCount; row++){
                board[col][row] = null;
            }
        }

        spawn();
        timeline.play();
    }
    private void game(ActionEvent event){
        if(!isMove.get()){
            GameController.isMove.set(true);
            if(activeItem == null)
                spawn();
            if(check())
                activeItem.move(moveSpeed);
        }
    }
    private boolean check(){
        if(activeItem == null)
            return false;
        for (var cube: activeItem.get()){
            if(cube.row == rowCount-1) {
                stop();
                return checkExplode();
            }
            else if(board[cube.col][cube.row] != null){
                wasted();
                return false;
            }else if(board[cube.col][cube.row+1] != null){
                stop();
                return checkExplode();
            }
        }
        return true;
    }
    private void stop() {
        for (var cube: activeItem.get()) {
            cube.movePermanent(cube.col*size, cube.row * size);
            board[cube.col][cube.row] = cube;
        }
        activeItem = null;
    }
    private boolean checkExplode() {
        ArrayList<Cube> resultCubes = new ArrayList<>();
        int count = 0;
        int lastRow = 0;
        for (int row = rowCount-1; row >=0 ; row--){
            ArrayList<Cube> findedCubes = new ArrayList<>();
            for (int col = 0; col < colCount; col++){
                if(board[col][row] != null){
                    findedCubes.add(board[col][row]);
                }
            }
            if(findedCubes.size() == colCount){
                resultCubes.addAll(findedCubes);
                findedCubes.clear();
                count++;
                lastRow = row;
            }
        }
        if(resultCubes.isEmpty()){
            GameController.isMove.set(false);
        }else {
            for (var cube : resultCubes) {
                if (cube == resultCubes.get(resultCubes.size() - 1))
                    if(isGravity) cube.explode(this::fallFullGravity);
                    else {
                        int finalCount = count;
                        int finalLastRow = lastRow;
                        cube.explode(() -> fallStandart(finalCount, finalLastRow));
                    }
                else
                    cube.explode();
                if(count % 4 == 0)
                    score += cube.score*10;
                else
                    score += cube.score;
                scoreLabel.setText(String.valueOf(score));
            }
            if(score % 1000 == 0){
                lvl++;
                lvlLabel.setText(String.valueOf(lvl));
                Constants.speedUp();
                timeline.stop();
                timeline = new Timeline(new KeyFrame(Duration.millis(time), this::game));
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
            }

            line += resultCubes.size();
            lineLabel.setText(String.valueOf(line));
        }
        return false;
    }
    public void fallStandart(int count, int lastRow){
        ArrayList<BoardCube>falls = new ArrayList<>();
        for (int col = 0; col < colCount; col++) {
            for (int row = lastRow-1; row >= 0; row--) {
                if(board[col][row] != null){
                    var tmp = board[col][row];
                    board[col][row] = board[col][row+count];
                    board[col][row+count] = tmp;

                    falls.add(new BoardCube(col, row+count, board[col][row+count]));
                }
            }
        }
        for (int col = 0; col < colCount; col++) {
            for (int row = 0; row < rowCount; row++) {
                if(board[col][row] == null)
                    continue;
                if(!board[col][row].isVisible()) {
                    gameScene.getChildren().remove(board[col][row]);
                    board[col][row] = null;
                }
            }
        }
        for(var f: falls){
            if(f == falls.get(falls.size()-1))
                f.cube.move(f.col*size, f.row*size, () -> GameController.isMove.set(false), fallSpeed);
            else
                f.cube.move(f.col*size, f.row*size, fallSpeed);
        }
        activeItem = null;
    }
    public void fallFullGravity(){
        ArrayList<BoardCube>falls = new ArrayList<>();
        for (int col = 0; col < colCount; col++) {
            for (int row = rowCount-1; row >= 0; row--) {
                if(board[col][row] == null)
                    continue;
                if(board[col][row].isVisible()) {
                    int t = row + 1;
                    boolean fl = false;
                    while (t < rowCount) {
                        if(board[col][t] != null)
                            if (!board[col][t].isVisible()) {
                                fl = true;
                            } else {
                                var tmp = board[col][row];
                                board[col][row] = board[col][t-1];
                                board[col][t-1] = tmp;

                                falls.add(new BoardCube(col, t-1, board[col][t-1]));
                                fl = false;
                                break;
                            }
                        t++;
                    }
                    if(fl){
                        var tmp = board[col][row];
                        board[col][row] = board[col][rowCount-1];
                        board[col][rowCount-1] = tmp;

                        falls.add(new BoardCube(col, rowCount-1, board[col][rowCount-1]));
                    }
                }
            }
        }
        for (int col = 0; col < colCount; col++) {
            for (int row = 0; row < rowCount; row++) {
                if(board[col][row] == null)
                    continue;
                if(!board[col][row].isVisible()) {
                    gameScene.getChildren().remove(board[col][row]);
                    board[col][row] = null;
                }
            }
        }
        for(var f: falls){
            if(f == falls.get(falls.size()-1))
                f.cube.move(f.col*size, f.row*size, () -> GameController.isMove.set(false), fallSpeed);
            else
                f.cube.move(f.col*size, f.row*size, fallSpeed);
        }
        activeItem = null;
    }
    private void wasted() {
        gameOverLabel.setVisible(true);
        GameController.isMove.set(false);
        timeline.stop();

        gravityCheckBox.setDisable(false);
        score = 0;
        line = 0;
        lvl = 1;
        scoreLabel.setText(String.valueOf(score));
        lvlLabel.setText(String.valueOf(lvl));
        lineLabel.setText(String.valueOf(line));
        timeline = new Timeline(new KeyFrame(Duration.millis(time), this::game));
        timeline.setCycleCount(Timeline.INDEFINITE);
        game = false;
    }
    private void spawn() {
        activeItem = new TetrisItem(nextType, (colCount/2.0-1)*size, 0);
        gameScene.getChildren().addAll(activeItem.get());

        nextType = new Random().nextInt(TetrisItemsCollection.count);

        TetrisItem nextItem = new TetrisItem(nextType, 0, 0);
        for (var item: nextItem.get())
            item.movePermanent(100+size/2.0 - nextItem.width * size + item.getTranslateX(), 150-size/2.0 - nextItem.height*size + item.getTranslateY());
        nextItemView.getChildren().clear();
        nextItemView.getChildren().addAll(nextItem.get());
    }
    @FXML
    public void onKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.UP){
            if(activeItem != null) {
                activeItem.get().forEach(cube -> cube.timer.stop());
                activeItem.rotate();
                if (!rotateCheck()) {
                    activeItem.rotate();
                    activeItem.rotate();
                    activeItem.rotate();
                }
                GameController.isMove.set(false);
            }
        }else if(keyEvent.getCode() == KeyCode.DOWN){
            if(activeItem != null) {
                activeItem.get().forEach(cube -> cube.timer.stop());
                int downRow = finedDown();
                activeItem.moveDown(downRow * size);
                GameController.isMove.set(false);
            }
        }else if(keyEvent.getCode() == KeyCode.LEFT){
            rlCheck();
            if(!isLeft.get() && activeItem != null) {
                    activeItem.moveLeft();
            }
        }else if(keyEvent.getCode() == KeyCode.RIGHT){
            rlCheck();
            if(!isRight.get() && activeItem != null) {
                    activeItem.moveRight();
            }
        }
    }

    private boolean rotateCheck() {
        for (var cube: activeItem.get()){
            if(cube.row >= rowCount)
                return false;
            else if(cube.row < 0)
                return false;
            else if (cube.col < 0)
                return false;
            if (cube.col >= colCount)
                return false;
            else if(board[cube.col][cube.row] != null)
                return false;
        }
        return true;
    }

    private void rlCheck(){
        if(activeItem == null){
            isLeft.set(true);
            isRight.set(true);
            return;
        }
        isLeft.set(false);
        isRight.set(false);
        for (var cube: activeItem.get()) {
            if (cube.col == 0)
                isLeft.set(true);
            else if (board[cube.col - 1][cube.row] != null)
                isLeft.set(true);
            if (cube.col == colCount - 1)
                isRight.set(true);
            else if (board[cube.col + 1][cube.row] != null)
                isRight.set(true);

            if(isLeft.get() || isRight.get())
                break;
        }
    }
    private int finedDown() {
        for(int i = 1; i < rowCount; i++){
            boolean find = false;
            for (var cube : activeItem.get()) {
                if(i + cube.row >= rowCount) {
                    find = true;
                    break;
                }
                if (board[cube.col][i + cube.row] != null) {
                    find = true;
                    break;
                }
            }
            if(find)
                return i - 1;
        }
        return 0;
    }

    public void startOnAction(ActionEvent actionEvent) {
        if(!game) {
            gameOverLabel.setVisible(false);
            game = true;
            start();
        }
    }
}