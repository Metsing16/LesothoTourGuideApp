package com.example.demo2;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AboutScreen extends BorderPane {

    private Stage primaryStage;
    private List<Image> aboutImages = new ArrayList<>();
    private int currentImageIndex = 0;
    private Timeline slideshowTimeline;
    private ImageView slideshowImage;

    public AboutScreen(Stage primaryStage) {
        this.primaryStage = primaryStage;

        aboutImages.add(new Image(getClass().getResourceAsStream("/images/maseru sun hote.jpg")));
        aboutImages.add(new Image(getClass().getResourceAsStream("/images/malealea-lodge1.jpg")));
        aboutImages.add(new Image(getClass().getResourceAsStream("/images/avani.jpg")));
        aboutImages.add(new Image(getClass().getResourceAsStream("/images/lesotho.jpg")));
        aboutImages.add(new Image(getClass().getResourceAsStream("/images/afriski-ski-mountain.jpg")));
        aboutImages.add(new Image(getClass().getResourceAsStream("/images/makoloane.jpeg")));
        aboutImages.add(new Image(getClass().getResourceAsStream("/images/hikimh.jpg")));

        this.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a2a6c, #6a11cb, #2575fc);");

        createUI();
    }

    private void createUI() {
        Button backButton = new Button("Back to Home");
        backButton.getStyleClass().add("glow-button");
        backButton.setOnAction(e -> returnToHome());

        HBox header = new HBox(backButton);
        header.getStyleClass().add("header");
        this.setTop(header);


        VBox mainContent = new VBox(20);
        mainContent.setStyle("-fx-padding: 20;");


        slideshowImage = new ImageView(aboutImages.get(0));
        slideshowImage.setFitWidth(800);
        slideshowImage.setFitHeight(400);
        slideshowImage.setPreserveRatio(true);
        slideshowImage.getStyleClass().add("slideshow-image");


        slideshowImage.setOnMouseClicked(e -> {
            currentImageIndex = (currentImageIndex + 1) % aboutImages.size();
            Image nextImage = aboutImages.get(currentImageIndex);

            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), slideshowImage);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(ev -> {
                slideshowImage.setImage(nextImage);
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), slideshowImage);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();
        });


        Text aboutText = new Text(
                "Lesotho, officially the Kingdom of Lesotho, is a landlocked country completely surrounded " +
                        "by South Africa. It's known as the 'Kingdom in the Sky' due to its high altitude, with its " +
                        "lowest point being 1,400 meters above sea level - the highest in the world.\n\n" +
                        "Key Facts:\n" +
                        "- Capital: Maseru\n" +
                        "- Population: ~2.1 million\n" +
                        "- Official Languages: Sesotho and English\n" +
                        "- Currency: Lesotho Loti (LSL)\n\n" +
                        "Lesotho is known for its stunning mountain scenery, traditional Basotho culture, and unique " +
                        "heritage. The Basotho people are famous for their distinctive conical hats and colorful blankets."
        );
        aboutText.setWrappingWidth(800);
        aboutText.getStyleClass().add("about-text");
        aboutText.setStyle("-fx-fill: white;");


        Text galleryTitle = new Text("Cultural Highlights");
        galleryTitle.getStyleClass().add("section-title");
        galleryTitle.setStyle("-fx-fill: #00e5ff; -fx-font-size: 26px; -fx-font-weight: bold;");



        // Cultural gallery
        ScrollPane galleryScroll = createCulturalGallery();

        mainContent.getChildren().addAll(slideshowImage, aboutText, galleryTitle, galleryScroll);

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");

        this.setCenter(scrollPane);

        setupSlideshow();
    }

    private ScrollPane createCulturalGallery() {
        Map<String, List<GalleryItem>> categories = new HashMap<>();


        List<GalleryItem> clothesItems = new ArrayList<>();
        clothesItems.add(new GalleryItem("/images/men.jpg", "Traditional Basotho attire with colorful blankets"));
        clothesItems.add(new GalleryItem("/images/mosali.jpg", "Women in traditional Seshoeshoe dress"));
        clothesItems.add(new GalleryItem("/images/banyalani.jpg", "Basotho traditional wedding attire"));
        categories.put("Traditional Clothes", clothesItems);


        List<GalleryItem> foodItems = new ArrayList<>();
        foodItems.add(new GalleryItem("/images/papa.jpeg", "Papa (maize porridge) with meat and vegetables"));
        foodItems.add(new GalleryItem("/images/lihobe.jpg", "Mix of sorghum,beans,etc"));
        foodItems.add(new GalleryItem("/images/motoho.jpeg", "Motoho (fermented sorghum porridge)"));
        categories.put("Traditional Food", foodItems);


        List<GalleryItem> cultureItems = new ArrayList<>();
        cultureItems.add(new GalleryItem("/images/ntlamu.jpg", "Traditional dances and music performances"));
        cultureItems.add(new GalleryItem("/images/morabaraba.jpg", "Mokorotlo - the traditional Basotho dance"));
        cultureItems.add(new GalleryItem("/images/litolobonya.JPG", "Traditional musical instruments"));
        categories.put("Games", cultureItems);


        List<GalleryItem> landmarkItems = new ArrayList<>();
        landmarkItems.add(new GalleryItem("/images/Katse-Dam-Lesotho.jpg", "Katse Dam - one of Africa's largest dams"));
        landmarkItems.add(new GalleryItem("/images/maletsunyane.jpg", "Maletsunyane Falls - one of highest waterfalls"));
        landmarkItems.add(new GalleryItem("/images/thababosiu.jpg", "Thaba Bosiu - historical mountain fortress"));
        categories.put("Landmarks", landmarkItems);

        VBox galleryContainer = new VBox(30);
        galleryContainer.getStyleClass().add("gallery-container");

        for (Map.Entry<String, List<GalleryItem>> entry : categories.entrySet()) {
            Text categoryTitle = new Text(entry.getKey());
            categoryTitle.getStyleClass().add("section-title");
            categoryTitle.setStyle("-fx-fill: #00e5ff; -fx-font-size: 26px; -fx-font-weight: bold;");



            HBox categoryBox = new HBox(20);
            categoryBox.setStyle("-fx-padding: 10px;");

            for (GalleryItem item : entry.getValue()) {
                VBox galleryItem = createGalleryItem(item);
                categoryBox.getChildren().add(galleryItem);
            }

            ScrollPane categoryScroll = new ScrollPane(categoryBox);
            categoryScroll.setStyle("-fx-background-color: transparent;");
            categoryScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            categoryScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            categoryScroll.setFitToHeight(true);

            galleryContainer.getChildren().addAll(categoryTitle, categoryScroll);
        }

        ScrollPane galleryScroll = new ScrollPane(galleryContainer);
        galleryScroll.setFitToWidth(true);
        galleryScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        galleryScroll.setMaxHeight(600);

        return galleryScroll;
    }

    private VBox createGalleryItem(GalleryItem item) {
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(item.imagePath)));
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        imageView.getStyleClass().add("gallery-image");

        Text descText = new Text(item.description);
        descText.setWrappingWidth(180);
        descText.getStyleClass().add("gallery-desc");
        descText.setStyle("-fx-fill: lightgray;");

        VBox box = new VBox(10, imageView, descText);
        box.getStyleClass().add("gallery-category");

        return box;
    }

    private void setupSlideshow() {
        slideshowTimeline = new Timeline(
                new KeyFrame(Duration.seconds(8), event -> {
                    currentImageIndex = (currentImageIndex + 1) % aboutImages.size();
                    Image nextImage = aboutImages.get(currentImageIndex);

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

    private void returnToHome() {
        slideshowTimeline.stop();
        HomeScreen homeScreen = new HomeScreen(primaryStage);
        Scene scene = new Scene(homeScreen, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/about.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    private static class GalleryItem {
        String imagePath;
        String description;

        public GalleryItem(String imagePath, String description) {
            this.imagePath = imagePath;
            this.description = description;
        }
    }
}
