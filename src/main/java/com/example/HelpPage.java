package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class HelpPage {
    private Stage stage;
    private String previousPage;
    private PageInfo pageInfo;
    private boolean darkMode;
    private int currentImage = 0;

    private final String BACKGROUND_COLOR = "#f5f5f7";
    private final String PRIMARY_COLOR = "#4285F4";
    private final String PRIMARY_DARK_COLOR = "#3367d6";
    private final String SECTION_BACKGROUND = "white";
    private final String HEADER_COLOR = "#333333";
    private final String TEXT_COLOR = "#555555";
    private final String LOGOUT_COLOUR = "#ff0000";
    private final String LOGOUT_HOVER_COLOUR = "#8b0000";
    private final String SETTINGS = "#888888";
    private final String SETTINGS_HOVER = "#555555";
    private final String DARKMODE_SECTION = "#2b2b2b";
    private final String DARKMODE_BACKGROUND = "#1f1f1f";
    private final String DARKMODE_TEXT = "#fafafa";

    public HelpPage(Stage stage,String previousPage, PageInfo pageInfo,boolean darkMode){
        this.stage = stage;
        this.previousPage = previousPage;
        this.pageInfo = pageInfo;
        this.darkMode = darkMode;
    }

    public void show() {
        BorderPane root = new BorderPane();
        if (!darkMode) {
            root.setStyle("-fx-background-color: #f5f5f7;");
        } else {
            root.setStyle("-fx-background-color: " + DARKMODE_BACKGROUND);
        }

        HBox navBar = createNavigationBar();
        VBox slideshowBar = createInputBar();
        HBox logoutBar = createLogoutBar();

        root.setTop(navBar);
        root.setCenter(slideshowBar);
        root.setBottom(logoutBar);

        BorderPane.setMargin(slideshowBar, new Insets(20, 20, 20, 20));
        BorderPane.setMargin(logoutBar, new Insets(0, 0, 30, 0));

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setTitle("Settings");
        stage.show();
    }

    private HBox createNavigationBar() {
        HBox navBar = new HBox(15);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(15, 20, 15, 20));
        if (!darkMode) {
            navBar.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        } else {
            navBar.setStyle("-fx-background-color: " + DARKMODE_SECTION + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        }

        Label title = new Label("Help");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setTextFill(Color.web(darkMode ? DARKMODE_TEXT : HEADER_COLOR));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = createStyledButton("Back",PRIMARY_COLOR,PRIMARY_DARK_COLOR);
        backButton.setOnAction(e -> {
            SettingsPage settingsPage = new SettingsPage(stage,previousPage,pageInfo,darkMode);
            settingsPage.show();
        });

        // Add to navigation bar
        navBar.getChildren().addAll(title,spacer,backButton);

        return navBar;
    }

    private VBox createInputBar() {
        //Main container
        VBox inputBar = new VBox(15);
        inputBar.setAlignment(Pos.CENTER);
        inputBar.setPadding(new Insets(15, 20, 15, 20));
        if (!darkMode) {
            inputBar.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        } else {
            inputBar.setStyle("-fx-background-color: " + DARKMODE_SECTION + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        }

        //Images to be displayed (Sample images for now)
        ArrayList<Image> imageList = new ArrayList<>();
        imageList.add(new Image(getClass().getResource("/cat1.jpg").toExternalForm()));
        imageList.add(new Image(getClass().getResource("/cat2.jpg").toExternalForm()));
        imageList.add(new Image(getClass().getResource("/cat3.jpg").toExternalForm()));

        //Slideshow component
        ImageView slideshow = new ImageView(imageList.get(0));
        slideshow.setPreserveRatio(true);
        slideshow.setFitWidth(500);
        slideshow.setFitHeight(500);

        //Slideshow Counter
        Label slideShowCounter = new Label();
        slideShowCounter.setFont(Font.font("Arial", FontWeight.LIGHT, 16));
        slideShowCounter.setTextFill(Color.web(darkMode ? DARKMODE_TEXT : HEADER_COLOR));
        slideShowCounter.setText((currentImage+1) + " / " + imageList.size());


        //Navigation buttons
        HBox navButtonBox = new HBox(15);
        navButtonBox.setAlignment(Pos.CENTER);
        Button backButton = createStyledButton("< Previous",PRIMARY_COLOR,PRIMARY_DARK_COLOR);
        Button nextButton = createStyledButton("Next >",PRIMARY_COLOR,PRIMARY_DARK_COLOR);
        backButton.setOnAction(e -> {
            currentImage = (currentImage - 1 + imageList.size()) % imageList.size();
            slideshow.setImage(imageList.get(currentImage));
            slideShowCounter.setText((currentImage+1) + " / " + imageList.size());
        });
        nextButton.setOnAction(e -> {
            currentImage = (currentImage + 1) % imageList.size();
            slideshow.setImage(imageList.get(currentImage));
            slideShowCounter.setText((currentImage+1) + " / " + imageList.size());
        });
        navButtonBox.getChildren().addAll(backButton,nextButton);



        inputBar.getChildren().addAll(slideshow,slideShowCounter,navButtonBox);


        return inputBar;
    }

    private HBox createLogoutBar() {
        HBox logoutBar = new HBox(15);
        logoutBar.setAlignment(Pos.CENTER_LEFT);
        logoutBar.setPadding(new Insets(15, 20, 15, 20));
        if (!darkMode) {
            logoutBar.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        } else {
            logoutBar.setStyle("-fx-background-color: " + DARKMODE_SECTION + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        }


        //Button to logout
        Button logoutButton = createStyledButton("Logout",LOGOUT_COLOUR,LOGOUT_HOVER_COLOUR);
        logoutButton.setOnAction(e -> {
            Login loginPage = new Login(stage,darkMode);
            loginPage.show();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);


        // Add to navigation bar
        logoutBar.getChildren().addAll(spacer,logoutButton);

        return logoutBar;
    }

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
