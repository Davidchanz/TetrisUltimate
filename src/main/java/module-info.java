module com.tetrisultimate.tetrisultimate {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires com.jfoenix;

    opens com.tetrisultimate to javafx.fxml;
    exports com.tetrisultimate;
    exports com.tetrisultimate.controller;
    opens com.tetrisultimate.controller to javafx.fxml;
}