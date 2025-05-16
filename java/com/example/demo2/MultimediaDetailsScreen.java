package com.example.demo2;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import java.io.InputStream;

public class MultimediaDetailsScreen extends BorderPane {

    private MediaPlayer mediaPlayer;
    private MediaPlayer audioBackgroundPlayer;
    private final String landmarkId;
    private final String landmarkName;
    private final String description;
    private final JSONArray images;
    private final String videoFile;
    private final String audioFile;

    public MultimediaDetailsScreen(String landmarkId, String landmarkName, String description,
                                   JSONArray images, String videoFile, String audioFile) {
        this.landmarkId = landmarkId;
        this.landmarkName = landmarkName;
        this.description = description;
        this.images = images;
        this.videoFile = videoFile;
        this.audioFile = audioFile;
        createUI();
    }

    private void createUI() {

        this.setStyle("-fx-background-color: linear-gradient(to bottom, #2c3e50, #4ca1af);");


        Label titleLabel = new Label(landmarkName);
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0, 0, 1);");

        Button closeButton = new Button("Close");
        closeButton.getStyleClass().add("glow-button");
        closeButton.setStyle("-fx-font-size: 14px; -fx-padding: 8 16;");
        closeButton.setOnAction(e -> ((Stage) this.getScene().getWindow()).close());

        HBox header = new HBox(20, titleLabel, closeButton);
        header.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: rgba(0,0,0,0.3);");
        this.setTop(header);

        // TabPane for different media types
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: transparent;");
        tabPane.setTabMinWidth(100);
        tabPane.setTabMaxWidth(150);

        // Images tab with ScrollPane
        Tab imagesTab = new Tab("Images");
        imagesTab.setClosable(false);
        imagesTab.setContent(createImagesTab());

        // Video tab
        Tab videoTab = new Tab("Video");
        videoTab.setClosable(false);
        videoTab.setContent(createVideoTab());

        // Audio tab with video background
        Tab audioTab = new Tab("Audio Guide");
        audioTab.setClosable(false);
        audioTab.setContent(createAudioTab());

        // Info tab
        Tab infoTab = new Tab("Information");
        infoTab.setClosable(false);
        infoTab.setContent(createInfoTab());

        tabPane.getTabs().addAll(infoTab, imagesTab, videoTab, audioTab);
        this.setCenter(tabPane);
    }

    private ScrollPane createImagesTab() {
        VBox imagesBox = new VBox(20);
        imagesBox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        try {
            for (int i = 0; i < images.length(); i++) {
                String imageFile = images.getString(i);
                InputStream is = getClass().getResourceAsStream("/images/" + imageFile);
                if (is != null) {
                    ImageView imageView = new ImageView(new Image(is));
                    imageView.setFitWidth(600);
                    imageView.setFitHeight(400);
                    imageView.setPreserveRatio(true);
                    imageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 10, 0, 0, 0);");
                    imagesBox.getChildren().add(imageView);
                }
            }

            if (imagesBox.getChildren().isEmpty()) {
                Text noImagesText = new Text("No images available for this landmark");
                noImagesText.setStyle("-fx-fill: white; -fx-font-size: 16px;");
                imagesBox.getChildren().add(noImagesText);
            }
        } catch (Exception e) {
            Text errorText = new Text("Error loading images");
            errorText.setStyle("-fx-fill: white; -fx-font-size: 16px;");
            imagesBox.getChildren().add(errorText);
        }

        ScrollPane scrollPane = new ScrollPane(imagesBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return scrollPane;
    }

    private VBox createVideoTab() {
        VBox videoBox = new VBox(20);
        videoBox.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: rgba(0,0,0,0.2);");

        try {
            Media media = new Media(getClass().getResource("/videos/" + videoFile).toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);
            mediaView.setFitWidth(600);
            mediaView.setFitHeight(400);
            mediaView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 10, 0, 0, 0);");

            Button playButton = new Button("Play");
            playButton.getStyleClass().add("glow-button");
            playButton.setStyle("-fx-font-size: 14px; -fx-padding: 8 16;");
            playButton.setOnAction(e -> mediaPlayer.play());

            Button pauseButton = new Button("Pause");
            pauseButton.getStyleClass().add("glow-button");
            pauseButton.setStyle("-fx-font-size: 14px; -fx-padding: 8 16;");
            pauseButton.setOnAction(e -> mediaPlayer.pause());

            HBox videoControls = new HBox(20, playButton, pauseButton);
            videoControls.setStyle("-fx-alignment: center; -fx-padding: 10;");

            videoBox.getChildren().addAll(mediaView, videoControls);
        } catch (Exception e) {
            Text errorText = new Text("Video not available for this landmark");
            errorText.setStyle("-fx-fill: white; -fx-font-size: 16px;");
            videoBox.getChildren().add(errorText);
        }

        return videoBox;
    }

    private StackPane createAudioTab() {
        StackPane audioStack = new StackPane();

        // Video background for audio tab
        try {
            Media videoMedia = new Media(getClass().getResource("/videos/koriana.mp4").toExternalForm());
            audioBackgroundPlayer = new MediaPlayer(videoMedia);
            audioBackgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            audioBackgroundPlayer.setMute(true);

            MediaView bgMediaView = new MediaView(audioBackgroundPlayer);
            bgMediaView.setFitWidth(800);
            bgMediaView.setFitHeight(600);
            audioStack.getChildren().add(bgMediaView);
            audioBackgroundPlayer.play();
        } catch (Exception e) {
            Pane fallbackBg = new Pane();
            fallbackBg.setStyle("-fx-background-color: linear-gradient(to bottom, #1e3c72, #2a5298);");
            audioStack.getChildren().add(fallbackBg);
        }


        Pane overlay = new Pane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");

        // Audio content
        VBox audioBox = new VBox(30);
        audioBox.setStyle("-fx-padding: 40; -fx-alignment: center;");
        audioBox.setMaxWidth(600);

        try {
            Media audioMedia = new Media(getClass().getResource("/audio/" + audioFile).toExternalForm());
            MediaPlayer audioPlayer = new MediaPlayer(audioMedia);

            Text audioTitle = new Text("Audio: " + landmarkName);
            audioTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: white; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0, 0, 1);");

            Text audioDescription = new Text("Listen to an immersive audio experience about this location");
            audioDescription.setStyle("-fx-font-size: 16px; -fx-fill: #eeeeee; -fx-font-style: italic;");
            audioDescription.setWrappingWidth(500);

            Button playButton = new Button("▶ Play Audio Guide");
            playButton.getStyleClass().add("glow-button");
            playButton.setStyle("-fx-font-size: 16px; -fx-padding: 12 24;");
            playButton.setOnAction(e -> audioPlayer.play());

            Button pauseButton = new Button("⏸ Pause");
            pauseButton.getStyleClass().add("glow-button");
            pauseButton.setStyle("-fx-font-size: 16px; -fx-padding: 12 24;");
            pauseButton.setOnAction(e -> audioPlayer.pause());

            HBox audioControls = new HBox(30, playButton, pauseButton);
            audioControls.setStyle("-fx-alignment: center; -fx-padding: 20;");

            audioBox.getChildren().addAll(audioTitle, audioDescription, audioControls);
        } catch (Exception e) {
            Text errorText = new Text("Audio guide not available for this landmark");
            errorText.setStyle("-fx-fill: white; -fx-font-size: 16px;");
            audioBox.getChildren().add(errorText);
        }

        audioStack.getChildren().addAll(overlay, audioBox);
        return audioStack;
    }

    private VBox createInfoTab() {
        VBox infoBox = new VBox(20);
        infoBox.setStyle("-fx-padding: 30; -fx-alignment: top-center; -fx-background-color: rgba(0,0,0,0.2);");

        Text titleText = new Text("About " + landmarkName);
        titleText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: white;");

        Text descriptionText = new Text(description);
        descriptionText.setWrappingWidth(700);
        descriptionText.setStyle("-fx-font-size: 16px; -fx-fill: #eeeeee; -fx-line-spacing: 5px;");

        infoBox.getChildren().addAll(titleText, descriptionText);
        return infoBox;
    }

    public void stopMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        if (audioBackgroundPlayer != null) {
            audioBackgroundPlayer.stop();
        }
    }
}