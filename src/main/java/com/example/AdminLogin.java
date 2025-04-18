package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class AdminLogin {
    private final Stage stage;
    private final LoginDatabase db = new LoginDatabase();
    private final Consumer<User> onSuccess;

    private final String BACKGROUND_COLOR = "#f5f5f7";
    private final String PRIMARY_COLOR = "#4285F4";
    private final String PRIMARY_DARK_COLOR = "#3367d6";
    private final String SECTION_BACKGROUND = "white";
    private final String HEADER_COLOR = "#333333";
    private final String TEXT_COLOR = "#555555";
    private final String LOGOUT_COLOUR = "#ff0000";
    private final String LOGOUT_HOVER_COLOUR = "#8b0000";

    public AdminLogin(Stage stage, Consumer<User> onSuccess) {
        this.stage = stage;
        this.onSuccess = onSuccess;

    }

    public void show() {
        BorderPane root = new BorderPane();

        HBox navBar = createNavigationBar();
        VBox loginBar = createInputBar();
        HBox logoutBar = createLogoutBar();

        root.setTop(navBar);
        root.setCenter(loginBar);
        root.setBottom(logoutBar);

        BorderPane.setMargin(loginBar, new Insets(20, 20, 20, 20));
        BorderPane.setMargin(logoutBar, new Insets(0, 0, 30, 0));

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setTitle("Admin Login");
        stage.show();
    }

    private HBox createNavigationBar() {
        HBox navBar = new HBox(15);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(15, 20, 15, 20));
        navBar.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label title = new Label("Admin Login");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web(HEADER_COLOR));


        // Add to navigation bar
        navBar.getChildren().addAll(title);

        return navBar;
    }

    private VBox createInputBar() {
        VBox inputBar = new VBox(15);
        inputBar.setAlignment(Pos.CENTER_LEFT);
        inputBar.setPadding(new Insets(15, 20, 15, 20));
        inputBar.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");


        TextField userField = new TextField();
        userField.setPromptText("Username");
        userField.setPrefHeight(40);
        userField.setStyle("-fx-background-radius: 5; " +
                "-fx-border-radius: 5; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-width: 1px; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 8px;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefHeight(40);
        passwordField.setStyle("-fx-background-radius: 5; " +
                "-fx-border-radius: 5; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-width: 1px; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 8px;");

        Label msg = new Label();
        msg.setMinHeight(24);
        Button loginButton = createStyledButton("Login as Admin",PRIMARY_COLOR,PRIMARY_DARK_COLOR);
        loginButton.setOnAction(event -> {
            User u = db.authenticateUser(userField.getText(), passwordField.getText());
            if (u != null && "admin".equals(u.getRole())) {
                onSuccess.accept(u);
            } else {
                msg.setText("Invalid Username/Password");
                msg.setTextFill(Color.RED);
            }
        });

        inputBar.getChildren().addAll(userField,passwordField,msg,loginButton);

        return inputBar;
    }

    private HBox createLogoutBar() {
        HBox logoutBar = new HBox(15);
        logoutBar.setAlignment(Pos.CENTER_LEFT);
        logoutBar.setPadding(new Insets(15, 20, 15, 20));
        logoutBar.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");


        //Button to logout
        Button logoutButton = createStyledButton("Back",LOGOUT_COLOUR,LOGOUT_HOVER_COLOUR);
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
