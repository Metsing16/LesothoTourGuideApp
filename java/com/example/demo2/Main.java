package com.example.demo2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        HomeScreen homeScreen = new HomeScreen(primaryStage);
        Scene scene = new Scene(homeScreen, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setTitle("Lesotho Tour App - The Mountain Kingdom");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}