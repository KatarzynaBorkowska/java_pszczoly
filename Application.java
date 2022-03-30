package com.example.projekt_kb;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public class Application extends javafx.application.Application {

    static SplitPane root;
    static AnchorPane anchor;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("hello-view.fxml"));
        root = fxmlLoader.load();
        anchor = (AnchorPane) root.getItems().get(1);
        Scene scene = new Scene(root,  1100,346);
        stage.setTitle("Ul");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}