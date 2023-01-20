package com.tetrisultimate.game;

import com.tetrisultimate.controller.GameController;
import com.tetrisultimate.util.GameUtil;

import java.util.ArrayList;
import static com.tetrisultimate.util.Constants.size;

public class TetrisItem{
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
        for (Cube[] cubes : body) {
            for (int j = 0; j < body[0].length; j++) {
                if (cubes[j] != null)
                    result.add(cubes[j]);
            }
        }
        return result;
    }
    public void move(double speed) {
        //GameController.isMove.set(true);
        var cubes = get();
        for (var cube: cubes){
            if(cube == cubes.get(cubes.size()-1))
                cube.move(cube.getTranslateX(), cube.getTranslateY() + size, () -> GameController.isMove.set(false), speed);
            else
                cube.move(cube.getTranslateX(), cube.getTranslateY() + size, speed);
        }
        yPos+=size;
    }
    public void moveDown(double y) {
        var cubes = get();
        for (var cube: cubes){
            if(cube == cubes.get(cubes.size()-1))
                cube.movePermanent(cube.getTranslateX(), cube.row*size + y);
            else
                cube.movePermanent(cube.getTranslateX(), cube.row*size + y);
        }
        yPos=y;
    }
    public void moveLeft() {
        var cubes = get();
        for (var cube: cubes){
            if(cube == cubes.get(cubes.size()-1))
                cube.movePermanent(cube.getTranslateX() - size, cube.getTranslateY());
            else
                cube.movePermanent(cube.getTranslateX() - size, cube.getTranslateY());
        }
        xPos-=size;
    }
    public void moveRight() {
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
