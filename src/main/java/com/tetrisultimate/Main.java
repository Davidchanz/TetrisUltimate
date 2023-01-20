package com.tetrisultimate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("gameView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 655);
        stage.setTitle("Menu");
        stage.setScene(scene);
        stage.show();
        stage.setMinHeight(670);
        stage.setMinWidth(600);
        stage.setMaxHeight(670);
        stage.setResizable(false);
    }

    public static void main(String[] args) {
        launch();
    }
}