package com.tetrisultimate.util;

import com.tetrisultimate.game.Cube;

public class GameUtil {
    public static void copy(Cube[][] orig, Cube[][] copy) throws CloneNotSupportedException {
        for(int i = 0; i < orig.length; i++){
            for(int j = 0; j < orig[0].length; j++){
                if(orig[i][j] == null)
                    copy[i][j] = null;
                else
                    copy[i][j] = orig[i][j].clone();
            }
        }
    }
    public static Cube[][] rotateRight(Cube[][] matrix) {
        int h = matrix[0].length;
        int w = matrix.length;
        Cube[][] mirror = new Cube[h][w];
        for(int i = 0 ; i < h; i++){
            for(int j = 0 ; j < w; j++){
                mirror[i][j] = matrix[j][h-i-1];
            }
        }
       return mirror;
    }
}
