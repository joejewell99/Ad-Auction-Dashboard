package com.example;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;

public class Login {
    /**
     * Reference to primary stage.
     * UI for login scene.
     */
    private Stage stage;
    private Scene loginScene;
    private Logger logger;
    private LoginDatabase db;

    /**
     * Constructors for primary stage + initializes the login class.
     * @param stage
     */
    public Login(Stage stage) {
        this.stage = stage;
        this.db = new LoginDatabase();
        initialize();
    }

    /**
     * Initializing the UI component + Layout.
     */
    private void initialize() {
        // Create main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f5f5f7;");

        // Create title section
        VBox headerBox = new VBox(15);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(40, 0, 30, 0));

        Label titleLabel = new Label("Ad Auction Dashboard");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.web("#333333"));

        Label subtitleLabel = new Label("Please login to access the system");
        subtitleLabel.setFont(Font.font("Arial", 16));
        subtitleLabel.setTextFill(Color.web("#666666"));

        headerBox.getChildren().addAll(titleLabel, subtitleLabel);
        mainLayout.setTop(headerBox);

        // Create login form
        VBox loginBox = new VBox(20);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(30, 40, 40, 40));
        loginBox.setMaxWidth(400);
        loginBox.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        // Username input area
        VBox usernameBox = new VBox(8);
        Label userNameLabel = new Label("Username");
        userNameLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        userNameLabel.setTextFill(Color.web("#333333"));

        TextField userName = new TextField();
        userName.setPromptText("Enter username");
        userName.setPrefHeight(40);
        userName.setStyle("-fx-background-radius: 5; " +
                "-fx-border-radius: 5; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-width: 1px; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 8px;");

        usernameBox.getChildren().addAll(userNameLabel, userName);

        // Password input area
        VBox passwordBox = new VBox(8);
        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        passwordLabel.setTextFill(Color.web("#333333"));

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setPrefHeight(40);
        passwordField.setStyle("-fx-background-radius: 5; " +
                "-fx-border-radius: 5; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-width: 1px; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 8px;");

        passwordBox.getChildren().addAll(passwordLabel, passwordField);

        // Message display area
        Label message = new Label();
        message.setFont(Font.font("Arial", 14));
        message.setTextAlignment(TextAlignment.CENTER);
        message.setAlignment(Pos.CENTER);
        message.setMinHeight(30);

        // Login button
        Button loginButton = new Button("Login");
        loginButton.setPrefSize(320, 45);
        loginButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        String buttonStyle = "-fx-background-color: #4285F4; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 1); " +
                "-fx-cursor: hand;";

        String buttonHoverStyle = "-fx-background-color: #3367d6; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2); " +
                "-fx-cursor: hand;";

        loginButton.setStyle(buttonStyle);
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(buttonHoverStyle));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(buttonStyle));

        // Tip information
        Label tipLabel = new Label("Tip: login with: 'user', 'pass'");
        tipLabel.setFont(Font.font("Arial", 12));
        tipLabel.setTextFill(Color.web("#888888"));
        tipLabel.setAlignment(Pos.CENTER);
        tipLabel.setPadding(new Insets(15, 0, 0, 0));

        // Add all components to login box
        loginBox.getChildren().addAll(usernameBox, passwordBox, message, loginButton, tipLabel);

        // Footer copyright information
        HBox footerBox = new HBox();
        footerBox.setAlignment(Pos.CENTER);
        footerBox.setPadding(new Insets(20, 0, 20, 0));

        Label footerLabel = new Label("©2025 Ad Auction Dashboard System");
        footerLabel.setTextFill(Color.web("#999999"));
        footerLabel.setFont(Font.font("Arial", 12));

        footerBox.getChildren().add(footerLabel);

        // Add components to main layout
        mainLayout.setCenter(loginBox);
        mainLayout.setBottom(footerBox);

        // Login button event handler
        loginButton.setOnAction(event -> {
            try {
                String usernameText = userName.getText();
                String passwordText = passwordField.getText();
                User user = db.authenticateUser(usernameText, passwordText);
                if (user != null) {
                    message.setText("Login successful!");
                    message.setTextFill(Color.web("#4CAF50"));
                    // Save the loggedin user and pass it to subsequent pages if needed.
                    App.getInstance().setLoggedInUser(user);
                    App.getInstance().showInputFilesPage();
                } else {
                    message.setText("Login failed!");
                    message.setTextFill(Color.web("#F44336"));
                    // Add slight shake effect for feedback (existing code)
                }
            } catch(Exception ex) {
                showAlert("An unexpected error occurred during login. Please try again.");
                logger.error("Error in login button action", ex);
            }
        });



        // Set Enter key to trigger login button
        passwordField.setOnAction(loginButton.getOnAction());
        userName.setOnAction(loginButton.getOnAction());

        // Button to manage the users
        Button manageUsersButton = new Button("Manage Users");
        // Add the manage users button to loginBox (it was originally commented out)
        loginBox.getChildren().add(manageUsersButton);


    manageUsersButton.setOnAction(event -> {
        new AdminLogin(stage, adminUser -> {
            new UserManagementPage(stage, adminUser).show();
        }).show();
    });


        loginScene = new Scene(mainLayout, 800, 600);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Method that checks the correct login info.
     * @param username set to empty.
     * @param password set to empty.
     * @return User & Password if true. Otherwise, false.
     * Kept the login info as empty for the time being.
     */
    /**
     private boolean authenticate(String username, String password) {

     if (username == null ||  username.isEmpty() || password == null || password.isEmpty()) {
     return false;
     }

     LoginDatabase db = new LoginDatabase();
     return db.authenticateUser(username, password);
     }


     /**
     * Display login scene.
     */
    public void show(){
        stage.setScene(loginScene);
        stage.setTitle("Ad Auction Dashboard - Login");
        stage.show();
    }
}
