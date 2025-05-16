package com.example.demo2;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends StackPane {

    private Stage primaryStage;
    private List<Image> bannerImages = new ArrayList<>();
    private int currentImageIndex = 0;
    private Timeline slideshowTimeline;
    private ImageView slideshowImage;

    public HomeScreen(Stage primaryStage) {
        this.primaryStage = primaryStage;


        bannerImages.add(new Image(getClass().getResourceAsStream("/images/kome2.jpg")));
        bannerImages.add(new Image(getClass().getResourceAsStream("/images/lesotho.jpg")));
        bannerImages.add(new Image(getClass().getResourceAsStream("/images/madiba.jpg")));
        bannerImages.add(new Image(getClass().getResourceAsStream("/images/horse.jpg")));
        bannerImages.add(new Image(getClass().getResourceAsStream("/images/afriski-from-above.jpg")));
        bannerImages.add(new Image(getClass().getResourceAsStream("/images/Katse-Dam-Lesotho.jpg")));


        createUI();
    }

    private void createUI() {
        slideshowImage = new ImageView(bannerImages.get(0));
        slideshowImage.setFitWidth(1200);
        slideshowImage.setFitHeight(800);
        slideshowImage.setPreserveRatio(false);


        Text titleText = new Text("LESOTHO TOUR APP");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        titleText.setFill(Color.WHITE);
        titleText.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

        Text subtitleText = new Text("The Mountain Kingdom - Where Heaven Meets Earth");
        subtitleText.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        subtitleText.setFill(Color.LIGHTBLUE);
        subtitleText.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 5, 0, 0, 0);");


        Button exploreBtn = new Button("Explore Map");
        exploreBtn.getStyleClass().add("glow-button");
        exploreBtn.setOnAction(e -> showMapScreen());

        Button aboutBtn = new Button("About Lesotho");
        aboutBtn.getStyleClass().add("glow-button");
        aboutBtn.setOnAction(e -> showAboutScreen());

        Button quizBtn = new Button("Take Quiz");
        quizBtn.getStyleClass().add("glow-button");
        quizBtn.setOnAction(e -> showQuizScreen());


        VBox contentBox = new VBox(20, titleText, subtitleText);
        contentBox.setAlignment(javafx.geometry.Pos.CENTER);

        HBox buttonBox = new HBox(20, exploreBtn, aboutBtn, quizBtn);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);

        VBox mainContent = new VBox(40, contentBox, buttonBox);
        mainContent.setAlignment(javafx.geometry.Pos.CENTER);

        this.getChildren().addAll(slideshowImage, mainContent);

        setupSlideshow();
    }

    private void setupSlideshow() {
        slideshowTimeline = new Timeline(
                new KeyFrame(Duration.seconds(5), event -> {
                    currentImageIndex = (currentImageIndex + 1) % bannerImages.size();
                    Image nextImage = bannerImages.get(currentImageIndex);

                    FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), slideshowImage);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.setOnFinished(e -> {
                        slideshowImage.setImage(nextImage);
                        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), slideshowImage);
                        fadeIn.setFromValue(0.0);
                        fadeIn.setToValue(1.0);
                        fadeIn.play();
                    });
                    fadeOut.play();
                })
        );
        slideshowTimeline.setCycleCount(Timeline.INDEFINITE);
        slideshowTimeline.play();
    }

    private void showMapScreen() {
        slideshowTimeline.stop();
        MapScreen mapScreen = new MapScreen(primaryStage);
        Scene scene = new Scene(mapScreen, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    private void showAboutScreen() {
        slideshowTimeline.stop();
        AboutScreen aboutScreen = new AboutScreen(primaryStage);
        Scene scene = new Scene(aboutScreen, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    private void showQuizScreen() {
        slideshowTimeline.stop();
        QuizScreen quizScreen = new QuizScreen(primaryStage);
        Scene scene = new Scene(quizScreen, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
    }
}
