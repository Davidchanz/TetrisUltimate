package com.tetrisultimate.game;

public class Constants {
    public static int colCount;
    public static int rowCount = 8;
    public static int size = 50;
    public static final int space = 17;
    public static int startX;
    public static int startY;

    public static void ini(int _colCount, int _rowCount, int _size){
        colCount = _colCount;
        rowCount = _rowCount;
        size = _size;
        startX = 600/colCount + space;
        startY = 800/rowCount + space;
    }
}
