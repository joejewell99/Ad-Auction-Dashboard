package com.example;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;



public class Login {
    /**
     * Reference to primary stage.
     * UI for login scene.
     */
    private Stage stage;
    private Scene loginScene;
    private Logger logger;
    private LoginDatabase db;
    private boolean darkMode;

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

    /**
     * Constructors for primary stage + initializes the login class.
     * @param stage
     */
    public Login(Stage stage, boolean darkMode) {
        this.stage = stage;
        this.db = new LoginDatabase();
        this.darkMode = darkMode;
        initialize();
    }

    /**
     * Initializing the UI component + Layout.
     */
    private void initialize() {
        // Create main layout
        BorderPane mainLayout = new BorderPane();
        if (!darkMode) {
            mainLayout.setStyle("-fx-background-color: #f5f5f7;");
        } else {
            mainLayout.setStyle("-fx-background-color: " + DARKMODE_BACKGROUND);
        }

        // Create title section
        HBox headerBox = new HBox(15);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(15, 20, 15, 20));
        if (!darkMode) {
            headerBox.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        } else {
            headerBox.setStyle("-fx-background-color: " + DARKMODE_SECTION + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        }

        Label titleLabel = new Label("Ad Auction Dashboard");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));

        if(!darkMode) {
            titleLabel.setTextFill(Color.web("#333333"));
        } else {
            titleLabel.setTextFill(Color.web(DARKMODE_TEXT));
        }


        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button settingsButton = createStyledButton("Settings", SETTINGS,SETTINGS_HOVER);
        settingsButton.setOnAction(e -> {
            PageInfo pageInfo = new PageInfo();
            pageInfo.setDarkMode(darkMode);
            SettingsPage settingsPage = new SettingsPage(stage,"Login",pageInfo,darkMode);
            settingsPage.show();
        });


        headerBox.getChildren().addAll(titleLabel,spacer,settingsButton);
        mainLayout.setTop(headerBox);

        // Create login form
        VBox loginBox = new VBox(20);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(30, 40, 40, 40));
        loginBox.setMaxWidth(400);
        if(!darkMode) {
            loginBox.setStyle("-fx-background-color: white; " +
                    "-fx-background-radius: 10; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        } else {
            loginBox.setStyle("-fx-background-color: " + DARKMODE_SECTION + "; " +
                    "-fx-background-radius: 10; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        }
        BorderPane.setMargin(loginBox, new Insets(20, 20, 20, 20));

        // Username input area
        VBox usernameBox = new VBox(8);
        Label userNameLabel = new Label("Username");
        userNameLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        if (!darkMode) {
            userNameLabel.setTextFill(Color.web("#333333"));
        } else {
            userNameLabel.setTextFill(Color.web(DARKMODE_TEXT));
        }

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
        if (!darkMode) {
            passwordLabel.setTextFill(Color.web("#333333"));
        } else {
            passwordLabel.setTextFill(Color.web(DARKMODE_TEXT));
        }

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
        loginBox.getChildren().addAll(usernameBox, passwordBox, message, loginButton);

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
            String usernameText = userName.getText();
            String passwordText = passwordField.getText();

            try {
                User user = db.authenticateUser(usernameText, passwordText);
                if (user == null) {
                    message.setText("Login failed!");
                    message.setTextFill(Color.web("#F44336"));
                    return;
                }

                // Bypass OTP for the built-ins
                if (!("user".equals(usernameText) || "admin".equals(usernameText))) {
                    // --- OTP block wrapped in its own try/catch ---
                    try {
                        TextInputDialog otpDialog = new TextInputDialog();
                        otpDialog.initOwner(stage);
                        otpDialog.setTitle("2FA Verification");
                        otpDialog.setHeaderText("Enter the 6-digit code from your Authenticator");
                        otpDialog.setContentText("Code: ");
                        Optional<String> otpResult = otpDialog.showAndWait();

                        if (otpResult.isEmpty() ||
                                !db.verifyOtp(usernameText, otpResult.get().trim())) {
                            message.setText("Invalid OTP");
                            message.setTextFill(Color.web(TEXT_COLOR));
                            return;
                        }
                    } catch (Exception otpEx) {
                        // anything going wrong in OTP gen/verify
                        showAlert("An error occurred while verifying your 2FA code.");
                        otpEx.printStackTrace();
                        return;
                    }
                }

                // If we reach here, either default user or OTP passed
                message.setText("Login successful!");
                message.setTextFill(Color.web("#4CAF50"));
                App.getInstance().setLoggedInUser(user);
                App.getInstance().showInputFilesPage(darkMode);

            } catch (Exception ex) {
                // catches DB errors, unexpected NPEs, etc.
                showAlert("An unexpected error occurred during login. Please try again.");
                ex.printStackTrace();
            }
        });




        // Set Enter key to trigger login button
        passwordField.setOnAction(loginButton.getOnAction());
        userName.setOnAction(loginButton.getOnAction());

        // Button to manage the users
        Button manageUsersButton = createStyledButton("Manage Users",PRIMARY_COLOR,PRIMARY_DARK_COLOR);
        // Add the manage users button to loginBox (it was originally commented out)
        loginBox.getChildren().add(manageUsersButton);


        manageUsersButton.setOnAction(event -> {
            new AdminLogin(stage, adminUser -> {
                new UserManagementPage(stage, adminUser,darkMode).show();
            },darkMode).show();
        });

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        loginScene = new Scene(mainLayout, screenBounds.getWidth(), screenBounds.getHeight());
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
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setTitle("Ad Auction Dashboard - Login");
        stage.show();
    }


    private Button createStyledButton(String text, String bgColor, String hoverColor) {
        Button button = new Button(text);
        button.setPrefSize(120, 35);
        button.setFont(Font.font("Arial", FontWeight.NORMAL, 13));

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
