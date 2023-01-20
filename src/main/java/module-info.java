module com.tetrisultimate.tetrisultimate {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    opens com.tetrisultimate.tetrisultimate to javafx.fxml;
    exports com.tetrisultimate.tetrisultimate;
}