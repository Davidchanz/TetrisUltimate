package com.tetrisultimate.game;

public class TetrisItemsCollectionElement {
    public final int type;
    private final Cube[][] body;
    TetrisItemsCollectionElement(int type, Integer[][] body, String path){
        this.type = type;
        int col = body.length;
        int row = body[0].length;
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
