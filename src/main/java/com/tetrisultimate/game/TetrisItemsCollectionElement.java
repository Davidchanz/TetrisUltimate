package com.tetrisultimate.game;

import javafx.scene.paint.Color;

public class TetrisItemsCollectionElement {
    public final int type;
    private int col;
    private int row;
    private final Cube[][] body;
    TetrisItemsCollectionElement(int type, Integer[][] body, String path){
        this.type = type;
        this.col = body.length;
        this.row = body[0].length;
        this.body = new Cube[col][row];
        for (int i = 0; i < col; i++){
            for (int j = 0; j < row; j++){
                if(body[i][j] == 1)
                    this.body[i][j] = new Cube(path);
            }
        }
    }

    public Cube[][] getBody() {
        return body;
    }
}
