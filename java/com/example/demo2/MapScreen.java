package com.example.demo2;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.json.JSONObject;

public class MapScreen extends BorderPane {

    private Stage primaryStage;
    private WebView mapWebView;

    public MapScreen(Stage primaryStage) {
        this.primaryStage = primaryStage;
        createUI();
    }

    private void createUI() {

        Button backButton = new Button("Back to Home");
        backButton.getStyleClass().add("glow-button");
        backButton.setOnAction(e -> returnToHome());

        HBox header = new HBox(backButton);
        header.setStyle("-fx-padding: 20; -fx-alignment: center-left;");
        this.setTop(header);


        mapWebView = new WebView();
        WebEngine engine = mapWebView.getEngine();


        engine.load(getClass().getResource("/map.html").toExternalForm());

        // JavaScript-Java bridge
        engine.setPromptHandler(data -> {
            if (data != null && data.getMessage() != null && data.getMessage().startsWith("LANDMARK_DATA:")) {
                String jsonData = data.getMessage().substring("LANDMARK_DATA:".length());
                Platform.runLater(() -> showMultimediaDetails(jsonData));
                return "OK";
            }
            return null;
        });

        this.setCenter(mapWebView);
    }

    private void showMultimediaDetails(String jsonData) {
        try {
            JSONObject landmarkData = new JSONObject(jsonData);
            String landmarkId = landmarkData.getString("id");
            String landmarkName = landmarkData.getString("name");
            String description = landmarkData.getString("description");

            MultimediaDetailsScreen detailsScreen = new MultimediaDetailsScreen(
                    landmarkId,
                    landmarkName,
                    description,
                    landmarkData.getJSONArray("images"),
                    landmarkData.getString("video"),
                    landmarkData.getString("audio")
            );

            Scene scene = new Scene(detailsScreen, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle(landmarkName + " - Multimedia Details");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void returnToHome() {
        HomeScreen homeScreen = new HomeScreen(primaryStage);
        Scene scene = new Scene(homeScreen, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
    }
}