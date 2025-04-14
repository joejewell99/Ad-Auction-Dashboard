package com.example;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class BouncePage {
    private Stage stage;
    private  LogManager logManager;
    private ChartCreator chartCreator;

    // Define style constants
    private final String BACKGROUND_COLOR = "#f5f5f7";
    private final String PRIMARY_COLOR = "#4285F4";
    private final String PRIMARY_DARK_COLOR = "#3367d6";
    private final String LOGOUT_COLOUR = "#ff0000";
    private final String LOGOUT_HOVER_COLOUR = "#8b0000";
    private final String SECTION_BACKGROUND = "white";
    private final String HEADER_COLOR = "#333333";
    private final String TEXT_COLOR = "#555555";

    public BouncePage(Stage stage, LogManager logManager, ChartCreator chartCreator){
        this.stage = stage;
        this.logManager = logManager;
        this.chartCreator = chartCreator;

    }

    public void show() {
        // Create main layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        // Field for user to input bounce definition number
        TextField bounceInput = new TextField();
        bounceInput.setOnAction(e ->{
            String bounceDef = bounceInput.getText();
            chartCreator.setTimeSpent(Integer.parseInt(bounceDef));
        });

        // Button to go back to main charts
        Button backButton = createStyledButton("Back",PRIMARY_COLOR,PRIMARY_DARK_COLOR);
        backButton.setOnAction(e-> {
            ChartPage chartPage = new ChartPage(stage,logManager,chartCreator);
            chartPage.show();
        });

        //Button to logout
        Button logoutButton = createStyledButton("Logout",LOGOUT_COLOUR,LOGOUT_HOVER_COLOUR);
        logoutButton.setOnAction(e -> {
            Login loginPage = new Login(stage);
            loginPage.show();
        });

        root.setCenter(bounceInput);
        root.setTop(backButton);
        root.setBottom(logoutButton);

        Scene scene = new Scene(root, 1300, 800);
        stage.setScene(scene);
        stage.setTitle("Ad Auction Dashboard - Bounce Definition");
        stage.show();

    }


    // Create styled button
    public Button createStyledButton(String text, String bgColor, String hoverColor) {
        Button button = new Button(text);
        button.setPrefSize(140, 35);
        button.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        String style = String.format(
                "-fx-background-color: %s; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: normal; " +
                        "-fx-background-radius: 5; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 2, 0, 0, 1); " +
                        "-fx-cursor: hand;", bgColor);

        String hoverStyle = String.format(
                "-fx-background-color: %s; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: normal; " +
                        "-fx-background-radius: 5; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 0, 2); " +
                        "-fx-cursor: hand;", hoverColor);

        button.setStyle(style);

        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(style));

        return button;
    }

}
