package com.tetrisultimate.game;

public class Constants {
    public static int colCount = 10;
    public static int rowCount = 20;
    public static int size = 30;
    public static int score = 10;
    public static double moveSpeed = 300;
    public static double fallSpeed = 30;
    public static double explodeSpeed = 300;
    public static double time = 300;
    public static String sourcePath = "/home/katsitovlis/Documents/Project/JavaMaven/TetrisUltimate/src/main/resources/com/tetrisultimate/images/";
    public static boolean isGravity;

    public static void ini(boolean gravity){
        colCount = 10;
        rowCount = 20;
        size = 30;
        score = 10;
        moveSpeed = 300;
        fallSpeed = 30;
        explodeSpeed = 300;
        time = 300;
        sourcePath = "/home/katsitovlis/Documents/Project/JavaMaven/TetrisUltimate/src/main/resources/com/tetrisultimate/images/";
        isGravity = gravity;
    }
    public static void speedUp(){
        if(moveSpeed != 1) {
            moveSpeed -= 50;
            if (moveSpeed <= 0)
                moveSpeed = 1;
        }else {
            time -= 50;
            if(time <= 0)
                time = 1;
        }
    }
}
