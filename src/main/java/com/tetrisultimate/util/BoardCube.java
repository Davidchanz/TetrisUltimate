package com.tetrisultimate.util;

import com.tetrisultimate.game.Cube;

public class BoardCube {
    public Cube cube;
    public int row;
    public int col;

    public BoardCube(int col, int row, Cube cube){
        this.col = col;
        this.row = row;
        this.cube = cube;
    }
}
