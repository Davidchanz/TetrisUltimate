package com.tetrisultimate.game;

import com.tetrisultimate.controller.GameController;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import static com.tetrisultimate.game.Constants.size;

public class TetrisItem{
    private int type;
    private Cube[][] body;
    public double xPos, yPos;
    public int width, height;
    public TetrisItem(int type, double x, double y){
        var tmp = TetrisItemsCollection.get(type);
        if(tmp == null){
            System.err.println("type no exist error!");
            System.exit(1);
        }
        width = tmp.length;
        height = tmp[0].length;
        body = new Cube[tmp.length][tmp[0].length];
        try {
            GameUtil.copy(tmp, body);
        } catch (CloneNotSupportedException e) {
            System.err.println("copy error");
            System.exit(1);
        }
        this.type = type;
        this.xPos = x;
        this.yPos = y;
        iniBody();
    }
    private void iniBody() {
        for(int i = 0; i < body.length; i++){
            for(int j = 0; j < body[0].length; j++){
                if(body[i][j] != null)
                    body[i][j].movePermanent(xPos + i*size, yPos + j*size);
            }
        }
    }
    public ArrayList<Cube> get(){
        ArrayList<Cube> result = new ArrayList<>();
        for(int i = 0; i < body.length; i++){
            for(int j = 0; j < body[0].length; j++){
                if(body[i][j] != null)
                    result.add(body[i][j]);
            }
        }
        return result;
    }
    public void move(double speed) {
        //GameController.isMove.set(true);
        var cubes = get();
        for (var cube: cubes){
            if(cube == cubes.get(cubes.size()-1))
                cube.move(cube.getTranslateX(), cube.getTranslateY() + size, () -> {GameController.isMove.set(false);}, speed);
            else
                cube.move(cube.getTranslateX(), cube.getTranslateY() + size, speed);
        }
        yPos+=size;
    }
    public void moveDown(double y/*, Func foo*/) {
        var cubes = get();
        for (var cube: cubes){
            if(cube == cubes.get(cubes.size()-1))
                cube.movePermanent(cube.getTranslateX(), cube.row*size + y/*, foo*/);
            else
                cube.movePermanent(cube.getTranslateX(), cube.row*size + y);
        }
        yPos=y;
    }
    public void moveLeft(/*Func foo*/) {
        var cubes = get();
        for (var cube: cubes){
            if(cube == cubes.get(cubes.size()-1))
                cube.movePermanent(cube.getTranslateX() - size, cube.getTranslateY()/*, foo*/);
            else
                cube.movePermanent(cube.getTranslateX() - size, cube.getTranslateY());
        }
        xPos-=size;
    }
    public void moveRight(/*Func foo*/) {
        var cubes = get();
        for (var cube: cubes){
            if(cube == cubes.get(cubes.size()-1))
                cube.movePermanent(cube.getTranslateX()+size, cube.getTranslateY()/*, foo*/);
            else
                cube.movePermanent(cube.getTranslateX()+size, cube.getTranslateY());
        }
        xPos+=size;
    }

    public void rotate() {
        int tmp = width;
        width = height;
        height = tmp;
        body = GameUtil.rotateRight(body);
        for(int i = 0; i < body.length; i++){
            for(int j = 0; j < body[0].length; j++){
                if(body[i][j] != null)
                    body[i][j].movePermanent(xPos + i*size, yPos + j*size);
            }
        }
    }
}
