package com.example.demo2;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.util.ArrayList;
import java.util.List;

public class QuizScreen extends BorderPane {

    private Stage primaryStage;
    private List<QuizQuestion> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private VBox quizContent;
    private VBox resultsContent;
    private List<String> userAnswers = new ArrayList<>();

    public QuizScreen(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeQuestions();
        createUI();
        showQuestion(0);
    }

    private void initializeQuestions() {
        questions = new ArrayList<>();

        questions.add(new QuizQuestion(
                "What is the capital of Lesotho?",
                new String[]{"Maseru", "Leribe", "Mokhotlong", "Quthing"},
                0
        ));

        questions.add(new QuizQuestion(
                "What is the traditional Basotho hat called?",
                new String[]{"Tlhakola", "Kobo", "Mokorotlo", "Seahlolo"},
                2
        ));

        questions.add(new QuizQuestion(
                "Which famous waterfall is in Lesotho?",
                new String[]{"Tugela Falls", "Victoria Falls", "Tugela Falls", "Maletsunyane Falls"},
                3
        ));

        questions.add(new QuizQuestion(
                "What is the main language spoken in Lesotho?",
                new String[]{"Xhosa", "Zulu", "Sesotho", "English"},
                2
        ));

        questions.add(new QuizQuestion(
                "Lesotho is completely surrounded by which country?",
                new String[]{"South Africa", "Botswana", "Zimbabwe", "Mozambique"},
                0
        ));
    }

    private void createUI() {
        String videoPath = getClass().getResource("/videos/istockphoto-1435230281-640_adpp_is.mp4").toExternalForm();
        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setMute(true);

        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(1200);
        mediaView.setFitHeight(800);


        StackPane rootPane = new StackPane();


        Button backButton = new Button("Back to Home");
        backButton.getStyleClass().add("glow-button");
        backButton.setOnAction(e -> returnToHome());

        HBox header = new HBox(backButton);
        header.setStyle("-fx-padding: 20; -fx-alignment: center-left;");


        quizContent = new VBox(20);
        quizContent.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: rgba(0,0,0,0.5);");
        quizContent.setMaxWidth(800);

        resultsContent = new VBox(20);
        resultsContent.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: rgba(0,0,0,0.7);");
        resultsContent.setVisible(false);
        resultsContent.setMaxWidth(800);


        StackPane centerPane = new StackPane();
        centerPane.getChildren().addAll(quizContent, resultsContent);
        StackPane.setAlignment(quizContent, javafx.geometry.Pos.CENTER);
        StackPane.setAlignment(resultsContent, javafx.geometry.Pos.CENTER);


        BorderPane contentPane = new BorderPane();
        contentPane.setTop(header);
        contentPane.setCenter(centerPane);
        contentPane.setStyle("-fx-background-color: rgba(0,0,0,0.3);");


        rootPane.getChildren().addAll(mediaView, contentPane);

        this.setCenter(rootPane);

        mediaPlayer.play();
    }


    private void showQuestion(int index) {
        if (index >= questions.size()) {
            showResults();
            return;
        }

        currentQuestionIndex = index;
        QuizQuestion question = questions.get(index);

        quizContent.getChildren().clear();

        Text questionText = new Text((index + 1) + ". " + question.getQuestion());
        questionText.setStyle("-fx-font-size: 20px; -fx-fill: white; -fx-font-weight: bold;");
        questionText.setWrappingWidth(700);

        ToggleGroup answerGroup = new ToggleGroup();
        VBox answersBox = new VBox(10);
        answersBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        for (int i = 0; i < question.getOptions().length; i++) {
            RadioButton option = new RadioButton(question.getOptions()[i]);
            option.setToggleGroup(answerGroup);
            option.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
            option.setUserData(i);
            answersBox.getChildren().add(option);
        }

        Button nextButton = new Button(index == questions.size() - 1 ? "Finish Quiz" : "Next Question");
        nextButton.getStyleClass().add("glow-button");
        nextButton.setOnAction(e -> {
            if (answerGroup.getSelectedToggle() != null) {
                int selectedIndex = (int) answerGroup.getSelectedToggle().getUserData();
                userAnswers.add(question.getOptions()[selectedIndex]);

                if (selectedIndex == question.getCorrectAnswerIndex()) {
                    score++;
                }

                showQuestion(currentQuestionIndex + 1);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Answer Selected");
                alert.setHeaderText(null);
                alert.setContentText("Please select an answer before continuing.");
                alert.showAndWait();
            }
        });

        VBox.setMargin(nextButton, new javafx.geometry.Insets(20, 0, 0, 0));
        quizContent.getChildren().addAll(questionText, answersBox, nextButton);
    }

    private void showResults() {
        quizContent.setVisible(false);
        resultsContent.setVisible(true);
        resultsContent.getChildren().clear();

        Text resultsTitle = new Text("Quiz Results");
        resultsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        resultsTitle.setFill(Color.LIGHTBLUE);
        resultsTitle.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 5, 0, 0, 0);");

        Text scoreText = new Text("Your Score: " + score + " out of " + questions.size());
        scoreText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        scoreText.setFill(score >= questions.size() / 2 ? Color.LIGHTGREEN : Color.ORANGE);

        VBox detailsBox = new VBox(15);
        detailsBox.setStyle("-fx-padding: 20; -fx-background-color: rgba(0,0,0,0.3); -fx-background-radius: 10;");

        for (int i = 0; i < questions.size(); i++) {
            QuizQuestion question = questions.get(i);
            String userAnswer = userAnswers.get(i);
            boolean isCorrect = userAnswer.equals(question.getOptions()[question.getCorrectAnswerIndex()]);

            Text questionText = new Text("Q" + (i + 1) + ": " + question.getQuestion());
            questionText.setStyle("-fx-font-weight: bold; -fx-fill: white; -fx-font-size: 16px;");

            Text userAnswerText = new Text("Your answer: " + userAnswer);
            userAnswerText.setStyle("-fx-fill: " + (isCorrect ? "lightgreen" : "lightcoral") + "; -fx-font-size: 14px;");

            Text correctAnswerText = new Text("Correct answer: " + question.getOptions()[question.getCorrectAnswerIndex()]);
            correctAnswerText.setStyle("-fx-fill: lightblue; -fx-font-size: 14px;");

            VBox questionBox = new VBox(5, questionText, userAnswerText, correctAnswerText);
            detailsBox.getChildren().add(questionBox);
        }

        Button restartButton = new Button("Take Quiz Again");
        restartButton.getStyleClass().add("glow-button");
        restartButton.setOnAction(e -> {
            currentQuestionIndex = 0;
            score = 0;
            userAnswers.clear();
            quizContent.setVisible(true);
            resultsContent.setVisible(false);
            showQuestion(0);
        });

        Button homeButton = new Button("Return to Home");
        homeButton.getStyleClass().add("glow-button");
        homeButton.setOnAction(e -> returnToHome());

        HBox buttonBox = new HBox(20, restartButton, homeButton);
        buttonBox.setStyle("-fx-alignment: center;");

        resultsContent.getChildren().addAll(resultsTitle, scoreText, detailsBox, buttonBox);
    }

    private void returnToHome() {
        HomeScreen homeScreen = new HomeScreen(primaryStage);
        Scene scene = new Scene(homeScreen, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    private static class QuizQuestion {
        private String question;
        private String[] options;
        private int correctAnswerIndex;

        public QuizQuestion(String question, String[] options, int correctAnswerIndex) {
            this.question = question;
            this.options = options;
            this.correctAnswerIndex = correctAnswerIndex;
        }

        public String getQuestion() { return question; }
        public String[] getOptions() { return options; }
        public int getCorrectAnswerIndex() { return correctAnswerIndex; }
    }
}