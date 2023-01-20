package com.tetrisultimate.game;

import java.util.ArrayList;

public class Observer {
    static ArrayList<Listener> listeners = new ArrayList<>();
    Observer(){

    }
    public static void addListener(Listener listener){
        listeners.add(listener);
    }
    public static void push(){
        listeners.forEach(Listener::action);
    }
}
