package com.tetrisultimate.game;

import java.util.ArrayList;

public class TetrisItemsCollection {
    private static final ArrayList<TetrisItemsCollectionElement> items = new ArrayList<>();
    public static int count;
    static {
        Integer[][] body = {{1,1}, {1,1}};
        add(body, "red.png");
    }
    static {
        Integer[][] body = {{1,1,1,1}};
        add(body, "blue.png");
    }
    static {
        Integer[][] body = {{0,1},{1,1},{1,0}};
        add(body, "green.png");
    }
    static {
        Integer[][] body = {{1,0},{1,1},{0,1}};
        add(body, "orange.png");
    }
    static {
        Integer[][] body = {{1,0},{1,0},{1,1}};
        add(body, "yellow.png");
    }
    static {
        Integer[][] body = {{0,1},{0,1},{1,1}};
        add(body, "purple.png");
    }
    static {
        Integer[][] body = {{0,1},{0,1},{1,1}};
        add(body, "purple.png");
    }
    static {
        Integer[][] body = {{0,1},{1,1},{0,1}};
        add(body, "white.png");
    }
    public static void add(Integer[][] body, String path){
        items.add(new TetrisItemsCollectionElement(count++, body, path));
    }
    public static Cube[][] get(int type){
        for(var item: items){
            if(item.type == type)
                return item.getBody();
        }
        return null;
    }
}
