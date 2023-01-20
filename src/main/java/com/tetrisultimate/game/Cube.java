package com.treeinrow.game;

import com.treeinrow.Func;
import com.treeinrow.Observer;
import com.treeinrow.control.GameController;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Random;
import static com.treeinrow.Constants.*;

public class Cube extends ImageView {
    public int row, col;
    public double homePosX, homePosY;
    private static Cube[][] board;
    TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), this);
    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.2), this);
    private int type;
    private int score;
    private ArrayList<Cube> falls;
    public Cube(Cube[][] boardd, int coll, int roww, double xx, double yy, int size){
        reload();
        this.setManaged(false);

        this.setTranslateX(xx);
        this.setTranslateY(yy);

        this.homePosX = xx;
        this.homePosY = yy;
        this.row = roww;
        this.col = coll;
        board = boardd;
        this.score = 1;
    }
    public static boolean isSwaped(Cube c1, Cube c2, char start_dir) {
        switch (start_dir){
            case 'r' -> {
                if(c1.getTranslateX() > c2.getTranslateX())
                    return true;
                else
                    return false;
            }
            case 'l' -> {
                if(c1.getTranslateX() < c2.getTranslateX())
                    return true;
                else
                    return false;
            }
            case 'u' -> {
                if(c1.getTranslateY() < c2.getTranslateY())
                    return true;
                else
                    return false;
            }
            case 'd' -> {
                if(c1.getTranslateY() > c2.getTranslateY())
                    return true;
                else
                    return false;
            }
            default -> {
                return false;
            }
        }
    }
    public void moveHor(double x, double min, double max){
        if(x > max)
            setTranslateX(max);
        else if(x < min)
            setTranslateX(min);
        else
            setTranslateX(x);
    }
    public void moveVer(double y, double min, double max){
        if(y > max)
            setTranslateY(max);
        else if(y < min)
            setTranslateY(min);
        else
            setTranslateY(y);
    }
    public void returnToHome(boolean flag, Func foo){
        translateTransition.setByX(homePosX - getTranslateX());
        translateTransition.setByY(homePosY - getTranslateY());
        translateTransition.setInterpolator(Interpolator.LINEAR);
        translateTransition.setOnFinished(actionEvent -> {
            if(flag) {
                explouseCheck(this);
            }
            foo.apply();
        });
        translateTransition.play();
    }
    public void returnToHome(Cube... cubes){
        translateTransition.setByX(homePosX - getTranslateX());
        translateTransition.setByY(homePosY - getTranslateY());
        translateTransition.setInterpolator(Interpolator.LINEAR);
        translateTransition.setOnFinished(actionEvent -> {
            explouseCheck(cubes);
        });
        translateTransition.play();
    }
    public static void swap(Cube v1, Cube v2){
        double tmpHomePosX =  board[v1.col][v1.row].homePosX;
        double tmpHomePosY =  board[v1.col][v1.row].homePosY;
        int tmpRow = board[v1.col][v1.row].row;
        int tmpCol = board[v1.col][v1.row].col;

        board[v1.col][v1.row].homePosX = board[v2.col][v2.row].homePosX;
        board[v2.col][v2.row].homePosX = tmpHomePosX;

        board[v1.col][v1.row].homePosY = board[v2.col][v2.row].homePosY;
        board[v2.col][v2.row].homePosY = tmpHomePosY;

        board[v1.col][v1.row].col = board[v2.col][v2.row].col;
        board[v2.col][v2.row].col = tmpCol;

        board[v1.col][v1.row].row = board[v2.col][v2.row].row;
        board[v2.col][v2.row].row = tmpRow;

        Cube tmpCube = board[v1.col][v1.row];
        board[v1.col][v1.row] = board[v2.col][v2.row];
        board[v2.col][v2.row] = tmpCube;
    }
    public void explouseCheck(Cube... cubes) {
        ArrayList<Cube> result = new ArrayList<>();
        for (var cube: cubes){
            int lastType = -1;
            ArrayList<Cube> findedCubes = new ArrayList<>();
            //check rows
            for (int col = 0; col < colCount; col++) {
                if (lastType == board[col][cube.row].type) {
                    findedCubes.add(board[col][cube.row]);
                } else {
                    if (findedCubes.size() >= 3 && findedCubes.contains(cube)) {
                        result.addAll(findedCubes);
                        break;
                    }
                    lastType = board[col][cube.row].type;
                    findedCubes.clear();
                    findedCubes.add(board[col][cube.row]);
                }
            }
            if (findedCubes.size() >= 3 && findedCubes.contains(cube)) {
                result.addAll(findedCubes);
            }

            lastType = -1;
            findedCubes = new ArrayList<>();
            for (int row = 0; row < rowCount; row++) {
                if (lastType == board[cube.col][row].type) {
                    findedCubes.add(board[cube.col][row]);
                } else {
                    if (findedCubes.size() >= 3 && findedCubes.contains(cube)) {
                        result.addAll(findedCubes);
                        break;
                    }
                    lastType = board[cube.col][row].type;
                    findedCubes.clear();
                    findedCubes.add(board[cube.col][row]);
                }
            }
            if (findedCubes.size() >= 3 && findedCubes.contains(cube)) {
                result.addAll(findedCubes);
            }
        }

        for(var f: result){
            if(f == result.get(result.size()-1))
                f.explouse(this::fall);
            else
                f.explouse(() -> {});
        }

        if(result.isEmpty()) {
            GameController.flag1.set(true);
            GameController.firstClick.set(true);
        }/*else {
            fall();
        }*/
    }
    public void fall(){
        falls = new ArrayList<>();
        for (int col = 0; col < colCount; col++) {
            for (int row = rowCount-1; row >= 0; row--) {
                if(board[col][row].isVisible()) {
                    int t = row + 1;
                    boolean fl = false;
                    while (t < rowCount) {
                        if (!board[col][t].isVisible()) {
                            fl = true;
                        } else {
                            swap(board[col][row], board[col][t-1]);
                            falls.add(board[col][t-1]);
                            fl = false;
                            break;
                        }
                        t++;
                    }
                    if(fl){
                        swap(board[col][row], board[col][rowCount-1]);
                        falls.add(board[col][rowCount-1]);
                    }
                }
            }
        }
        for(var f: falls){
            if(f == falls.get(falls.size()-1))
                f.returnToHome(false, this::sammon);
            else
                f.returnToHome(false, () -> {});
        }
    }
    public void sammon(){
        falls = new ArrayList<>();
        for (int col = 0; col < colCount; col++) {
            int count = -1;
            for (int row = rowCount-1; row >= 0; row--) {
                if(!board[col][row].isVisible()){
                    count--;
                    board[col][row].reload();
                    board[col][row].setTranslateY((count-1)*size + startY + (count-1)*space);
                    falls.add(board[col][row]);
                }
            }
        }
        for(var f: falls){
            if(f == falls.get(falls.size()-1)) {
                f.setVisible(true);
                f.returnToHome(false, () -> {
                    explouseCheck(falls.toArray(new Cube[0]));
                    /*GameController.flag1.set(true);
                    GameController.firstClick.set(true);*/
                });
            }
            else {
                f.setVisible(true);
                f.returnToHome(false, () -> {});
            }
        }
    }
    public void reload(){
        this.type = new Random().nextInt(4);
        File file = null;
        switch (type){
            case 0 -> file = new File("src/main/resources/com/treeinrow/images/blue.png");
            case 1 -> file = new File("src/main/resources/com/treeinrow/images/green.png");
            case 2 -> file = new File("src/main/resources/com/treeinrow/images/red.png");
            case 3 -> file = new File("src/main/resources/com/treeinrow/images/yellow.png");
        }
        ImageView imageView = null;
        try {
            imageView = new ImageView(file.toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        this.setImage(imageView.getImage());
        //imageView.fitWidthProperty().bind(this.widthProperty().divide(1.2));
        this.setFitHeight(size*2);
        this.setFitWidth(size*2);
        this.setPreserveRatio(true);
    }
    public void explouse(Func foo){
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(actionEvent -> {
            this.setOpacity(1);
            board[this.col][this.row].setVisible(false);
            foo.apply();
        });
        fadeTransition.play();
        GameController.score += score;
        Observer.push();
    }
}
