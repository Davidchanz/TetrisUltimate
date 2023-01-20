package com.treeinrow.control;

import com.treeinrow.Listener;
import com.treeinrow.Observer;
import com.treeinrow.game.Cube;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.treeinrow.Constants.*;

public class GameController implements Initializable, Listener {
    @FXML
    public Pane gameScene;
    @FXML
    public Label scoreLabel;
    private Cube[][] board;
    private double mouseX, mouseY;
    private double prevMouseX, prevMouseY;
    public static final AtomicBoolean firstClick = new AtomicBoolean();
    private boolean dirHorizontal;
    public static AtomicBoolean flag1 = new AtomicBoolean(true);
    private Cube secondCube = null;
    private char dir;
    private char start_dir;
    public static int score;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BackgroundImage myBI;
        try {
            myBI = new BackgroundImage(new Image(new File("src/main/resources/com/treeinrow/images/background.png").toURI().toURL().toExternalForm(),600, 800,false,true),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        gameScene.setBackground(new Background(myBI));

        board = new Cube[colCount][rowCount];
        for (int col = 0; col < colCount; col++){
            for (int row = 0; row < rowCount; row++){
                board[col][row] = new Cube(board, col, row, col*size + startX + (col-1)*space, row*size + startY + (row-1)*space, size);
                gameScene.getChildren().add(board[col][row]);
            }
        }
    }

    public void start(){
        for (int col = 0; col < colCount; col++) {
            for (int row = 0; row < rowCount; row++) {
                board[col][row].setTranslateY(board[col][row].getTranslateY() - (2*rowCount) * size + startY + (2*rowCount - 1) * space);
                ini(board[col][row]);
                board[col][row].returnToHome(false, () -> {});
            }
        }

        firstClick.set(true);
        Observer.addListener(this);
    }

    public void ini(Cube cube){
        cube.setOnMousePressed(event -> {
            if(firstClick.get()) {
                mouseX = event.getSceneX() - cube.getTranslateX();
                mouseY = event.getSceneY() - cube.getTranslateY();
            }
        });

        cube.setOnMouseDragged(event -> {
            if(flag1.get()) {
                if (firstClick.get()) {
                    firstClick.set(false);
                    if (Math.abs(event.getSceneX() - mouseX - cube.getTranslateX()) > Math.abs(event.getSceneY() - mouseY - cube.getTranslateY())) {
                        dirHorizontal = true;
                    } else
                        dirHorizontal = false;

                    double divX = event.getSceneX() - mouseX;
                    double divY = event.getSceneY() - mouseY;
                    if (divX - cube.getTranslateX() > 0) { //right
                        if(cube.col == board.length-1) {
                            start_dir = '0';
                            secondCube = null;
                        }else {
                            start_dir = 'r';
                            secondCube = board[cube.col + 1][cube.row];
                        }
                    }
                    else if (divX - cube.getTranslateX() < 0) { //left
                        if(cube.col == 0) {
                            start_dir = '0';
                            secondCube = null;
                        }else {
                            start_dir = 'l';
                            secondCube = board[cube.col - 1][cube.row];
                        }
                    }
                    else if (divY - cube.getTranslateY() < 0) { //up
                        if(cube.row == 0) {
                            start_dir = '0';
                            secondCube = null;
                        }else {
                            start_dir = 'u';
                            secondCube = board[cube.col][cube.row - 1];
                        }
                    }
                    else if (divY - cube.getTranslateY() > 0) { //down
                        if(cube.row == board[0].length-1) {
                            start_dir = '0';
                            secondCube = null;
                        }else {
                            start_dir = 'd';
                            secondCube = board[cube.col][cube.row + 1];
                        }
                    }
                    else {
                        start_dir = '0';
                        secondCube = null;
                        System.out.println("error");
                    }
                    System.out.println(start_dir);
                    if(start_dir == '0'){
                        firstClick.set(true);
                    }
                    prevMouseX = event.getSceneX();
                    prevMouseY = event.getSceneY();
                }
                double divX = event.getSceneX() - mouseX;
                double divY = event.getSceneY() - mouseY;
                if(dirHorizontal) {
                    if (prevMouseX < event.getSceneX()) //right
                        dir = 'r';
                    else if (prevMouseX > event.getSceneX()) //left
                        dir = 'l';
                    if (dir == 'r') {//right
                        if (cube.col == board.length - 1 && start_dir == 'r')
                            return;
                        //if (getTranslateX() >= homePosX && getTranslateX() <= secondCube.homePosX) {
                        //dir = 'r';
                        if(start_dir == 'r') {
                            secondCube.moveHor(secondCube.homePosX + (cube.homePosX - divX), cube.homePosX, secondCube.homePosX);
                            cube.moveHor(divX, cube.homePosX, secondCube.homePosX);
                        } else if(start_dir == 'l'){
                            secondCube.moveHor(secondCube.homePosX + (cube.homePosX - divX), secondCube.homePosX, cube.homePosX);
                            cube.moveHor(divX, secondCube.homePosX, cube.homePosX);
                        }
                    } else if (dir == 'l') {//left
                        if (cube.col == 0 && start_dir == 'l')
                            return;
                        if(start_dir == 'l') {
                            secondCube.moveHor(secondCube.homePosX + (cube.homePosX - divX), secondCube.homePosX, cube.homePosX);
                            cube.moveHor(divX, secondCube.homePosX, cube.homePosX);
                        }else if(start_dir == 'r'){
                            secondCube.moveHor(secondCube.homePosX + (cube.homePosX - divX), cube.homePosX, secondCube.homePosX);
                            cube.moveHor(divX,  cube.homePosX, secondCube.homePosX);
                        }
                    }
                } else {
                    if (prevMouseY > event.getSceneY()) //up
                        dir = 'u';
                    else if (prevMouseY < event.getSceneY()) //down
                        dir = 'd';
                    if (dir == 'u') {//up
                        if (cube.row == 0 && start_dir == 'u')
                            return;
                        if(start_dir == 'u') {
                            secondCube.moveVer(secondCube.homePosY + (cube.homePosY - divY), secondCube.homePosY, cube.homePosY);
                            cube.moveVer(divY, secondCube.homePosY, cube.homePosY);
                        }else if(start_dir == 'd'){
                            secondCube.moveVer(secondCube.homePosY + (cube.homePosY - divY), cube.homePosY, secondCube.homePosY);
                            cube.moveVer(divY, cube.homePosY, secondCube.homePosY);
                        }
                    } else if (dir == 'd') {//down
                        if (cube.row == board[0].length-1 && start_dir == 'd')
                            return;
                        if(start_dir == 'd')  {
                            secondCube.moveVer(secondCube.homePosY + (cube.homePosY - divY), cube.homePosY, secondCube.homePosY);
                            cube.moveVer(divY, cube.homePosY, secondCube.homePosY);
                        }else if(start_dir == 'u'){
                            secondCube.moveVer(secondCube.homePosY + (cube.homePosY - divY), secondCube.homePosY, cube.homePosY);
                            cube.moveVer(divY, secondCube.homePosY, cube.homePosY);
                        }
                    }
                }
                prevMouseX = event.getSceneX();
                prevMouseY = event.getSceneY();
            }
        });

        cube.setOnMouseReleased(event -> {
            if(flag1.get()) {
                if(secondCube == null)
                    return;
                flag1.set(false);

                if(Cube.isSwaped(cube, secondCube, start_dir)) {
                    Cube.swap(cube, secondCube);
                    secondCube.returnToHome(false, () -> {});
                    cube.returnToHome(cube, secondCube);
                }else {
                    secondCube.returnToHome(false, () -> {});
                    cube.returnToHome(false, () -> {
                        flag1.set(true);
                        firstClick.set(true);});
                }
            }
        });
    }

    @Override
    public void action() {
        scoreLabel.setText("Score: " + score);
    }
}