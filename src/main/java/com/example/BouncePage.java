package com.example;

import com.itextpdf.text.DocumentException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

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

        HBox navBar = createNavigationBar();
        VBox inputBar = createInputBar();
        HBox logoutBar = createLogoutBar();

        root.setTop(navBar);
        root.setCenter(inputBar);
        root.setBottom(logoutBar);

        BorderPane.setMargin(inputBar, new Insets(20, 20, 20, 20));
        BorderPane.setMargin(logoutBar, new Insets(0, 0, 30, 0));



        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        stage.setScene(scene);
        stage.setFullScreen(true);
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

    private HBox createNavigationBar() {
        HBox navBar = new HBox(15);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(15, 20, 15, 20));
        navBar.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label title = new Label("Bounce Definition");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web(HEADER_COLOR));

        // Button to go back to main charts
        Button backButton = createStyledButton("Back",PRIMARY_COLOR,PRIMARY_DARK_COLOR);
        backButton.setOnAction(e-> {
            ChartPage chartPage = new ChartPage(stage,logManager,chartCreator);
            chartPage.show();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Add to navigation bar
        navBar.getChildren().addAll(title,spacer,backButton);

        return navBar;
    }

    private VBox createInputBar() {
        VBox inputBar = new VBox(15);
        inputBar.setAlignment(Pos.CENTER_LEFT);
        inputBar.setPadding(new Insets(15, 20, 15, 20));
        inputBar.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        // Field for user to input bounce definition number
        TextField bounceInput = new TextField();
        bounceInput.setPromptText("Enter bounce requirement time");
        bounceInput.setPrefHeight(40);
        bounceInput.setStyle("-fx-background-radius: 5; " +
                "-fx-border-radius: 5; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-width: 1px; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 8px;");
        bounceInput.setOnAction(e ->{
            String bounceDef = bounceInput.getText();
            chartCreator.setTimeSpent(Integer.parseInt(bounceDef));
        });

        //Instruction label
        Label instruction = new Label("Enter time limit for bounce and click enter");
        instruction.setFont(Font.font("Arial", FontWeight.LIGHT, 14));
        instruction.setTextFill(Color.web(HEADER_COLOR));

        //Tip label
        Label tip = new Label("Tip: Type 0 to set bounce definition to one page viewed");
        tip.setFont(Font.font("Arial", FontWeight.LIGHT, 14));
        tip.setTextFill(Color.web(HEADER_COLOR));

        inputBar.getChildren().addAll(instruction,bounceInput,tip);

        return inputBar;
    }

    private HBox createLogoutBar() {
        HBox logoutBar = new HBox(15);
        logoutBar.setAlignment(Pos.CENTER_LEFT);
        logoutBar.setPadding(new Insets(15, 20, 15, 20));
        logoutBar.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");


        //Button to logout
        Button logoutButton = createStyledButton("Logout",LOGOUT_COLOUR,LOGOUT_HOVER_COLOUR);
        logoutButton.setOnAction(e -> {
            Login loginPage = new Login(stage);
            loginPage.show();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);


        // Add to navigation bar
        logoutBar.getChildren().addAll(spacer,logoutButton);

        return logoutBar;
    }



}
