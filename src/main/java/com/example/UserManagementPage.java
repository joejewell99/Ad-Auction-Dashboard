package com.example;

import com.example.security.PasswordChecker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;



public class UserManagementPage {
    private Stage stage;
    private LoginDatabase db;
    private User loggedInUser;
    private boolean darkMode;
    public static final String DB_URL = "jdbc:sqlite:login.db";


    private final String BACKGROUND_COLOR = "#f5f5f7";
    private final String PRIMARY_COLOR = "#4285F4";
    private final String PRIMARY_DARK_COLOR = "#3367d6";
    private final String SECTION_BACKGROUND = "white";
    private final String HEADER_COLOR = "#333333";
    private final String TEXT_COLOR = "#555555";
    private final String ERROR_COLOR = "#F44336";
    private final String SUCCESS_COLOR = "#4CAF50";
    private final String LOGOUT_COLOUR = "#ff0000";
    private final String LOGOUT_HOVER_COLOUR = "#8b0000";
    private final String DARKMODE_SECTION = "#2b2b2b";
    private final String DARKMODE_BACKGROUND = "#1f1f1f";
    private final String DARKMODE_TEXT = "#fafafa";
    private final String SETTINGS = "#888888";
    private final String SETTINGS_HOVER = "#555555";

    public UserManagementPage(Stage stage, User currentUser,boolean darkMode) {
        this.stage = stage;
        this.db = new LoginDatabase();
        this.loggedInUser = currentUser;
        this.darkMode = darkMode;
    }

    public void show() {

        if (! "admin".equals(loggedInUser.getRole())) {
            Alert deny = new Alert(Alert.AlertType.ERROR,
                    "Access denied .\nOnly administrators can manage users.");
            deny.setHeaderText("Permission error");
            deny.showAndWait();

            new Login(stage,darkMode).show();
            return;
        }
        TabPane tabPane = new TabPane();
        if (!darkMode) {
            tabPane.setStyle("-fx-background-color: #f5f5f7;");
        } else {
            tabPane.setStyle("-fx-background-color: " + DARKMODE_BACKGROUND);
        }

        Tab registerTab = new Tab();
        if (!darkMode) {
            registerTab.setStyle("-fx-background-color: #f5f5f7;");
        } else {
            registerTab.setStyle("-fx-background-color: " + DARKMODE_BACKGROUND);
        }
        Label registerTabLabel = new Label("Register");
        registerTabLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        registerTabLabel.setStyle("-fx-padding: 20 20 20 20;" );
        registerTabLabel.setTextFill(Color.web(darkMode ? DARKMODE_TEXT : HEADER_COLOR));
        registerTab.setGraphic(registerTabLabel);

        VBox registerBox = new VBox(10);
        registerBox.setPadding(new Insets(15));
        registerBox.setAlignment(Pos.CENTER_LEFT);

        Label registerLabel = new Label("Register New User");
        registerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        registerLabel.setTextFill(Color.web(darkMode ? DARKMODE_TEXT : HEADER_COLOR));

        TextField regUsernameField = new TextField();
        regUsernameField.setPromptText("Username");
        regUsernameField.setPrefHeight(40);
        regUsernameField.setStyle("-fx-background-radius: 5; " +
                "-fx-border-radius: 5; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-width: 1px; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 8px;");
        PasswordField regPasswordField = new PasswordField();
        regPasswordField.setPromptText("Password");
        regPasswordField.setPrefHeight(40);
        regPasswordField.setStyle("-fx-background-radius: 5; " +
                "-fx-border-radius: 5; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-width: 1px; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 8px;");
        Button regButton = createStyledButton("Register", PRIMARY_COLOR,PRIMARY_DARK_COLOR);
        Label regMessage = new Label();

        regButton.setOnAction(e -> {
            String user = regUsernameField.getText();
            String pass = regPasswordField.getText();

            if (!PasswordChecker.validate(pass)){
                regMessage.setText(PasswordChecker.requirements());
                return;
            }

            boolean ok = db.registerUser(user, pass);
            regMessage.setText(ok
                    ? "User successfully registered! Scan this QR code with Google Authenticator App"
                    : "User not registered!");

            if (ok) {
                try {
                    // 1) fetch the Base64 secret by username
                    String encodedSecret = db.getMfaSecretForUser(user);
                    if (encodedSecret == null) {
                        regMessage.setText("No MFA secret found for user");
                        return;
                    }

                    // 2) decode Base64 → raw key bytes
                    byte[] raw = Base64.getDecoder().decode(encodedSecret);

                    // 3) encode to Base32 (strip padding)
                    String base32 = new org.apache.commons.codec.binary.Base32()
                            .encodeToString(raw)
                            .replace("=", "");
                    // print for manual entry
                    System.out.println("Your secret (enter in GA): " + base32);

                    // build the otpauth URL **using** the Base32 secret
                    String issuer      = "AdAuctionDashboard";
                    String accountName = user;
                    String otpAuthUrl = String.format(
                            "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                            issuer, accountName, base32, issuer
                    );

                    // show the larger QR
                    ShowQR qr = new ShowQR(otpAuthUrl, 400);
                    registerBox.getChildren().add(qr.getImageView());

                } catch (Exception qrEx) {
                    qrEx.printStackTrace();
                    regMessage.setText("Registered—but failed to generate QR");
                }
            }

        });

        registerBox.getChildren().addAll(registerLabel, regUsernameField, regPasswordField, regButton, regMessage);
        registerTab.setContent(registerBox);
        registerTab.setClosable(false);


        Tab updateTab = new Tab();
        if (!darkMode) {
            updateTab.setStyle("-fx-background-color: #f5f5f7;");
        } else {
            updateTab.setStyle("-fx-background-color: " + DARKMODE_BACKGROUND);
        }
        Label updateTabLabel = new Label("Update Password");
        updateTabLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        updateTabLabel.setStyle("-fx-padding: 20 20 20 20;" );
        updateTabLabel.setTextFill(Color.web(darkMode ? DARKMODE_TEXT : HEADER_COLOR));
        updateTab.setGraphic(updateTabLabel);

        VBox updateBox = new VBox(10);
        updateBox.setPadding(new Insets(15));
        updateBox.setAlignment(Pos.CENTER_LEFT);

        Label updateLabel = new Label("Update Password");
        updateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        updateLabel.setTextFill(Color.web(darkMode ? DARKMODE_TEXT :HEADER_COLOR));

        TextField updateUsernameField = new TextField();
        updateUsernameField.setPromptText("Username");
        updateUsernameField.setPrefHeight(40);
        updateUsernameField.setStyle("-fx-background-radius: 5; " +
                "-fx-border-radius: 5; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-width: 1px; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 8px;");
        PasswordField updatePasswordField = new PasswordField();
        updatePasswordField.setPromptText("New Password");
        updatePasswordField.setPrefHeight(40);
        updatePasswordField.setStyle("-fx-background-radius: 5; " +
                "-fx-border-radius: 5; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-width: 1px; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 8px;");
        Button updateButton = createStyledButton("Update",PRIMARY_COLOR,PRIMARY_DARK_COLOR);
        Label updateMessage = new Label();

        updateButton.setOnAction(e -> {
            String user = updateUsernameField.getText();
            String pass = updatePasswordField.getText();
            if (!PasswordChecker.validate(pass)){
                updateMessage.setText(PasswordChecker.requirements());
                return;
            }
            boolean ok = db.updateUserPassword(user, pass);
            updateMessage.setText(ok
                    ? "Password successfully updated!"
                    : "Password not updated!");
        });
        updateBox.getChildren().addAll(updateLabel, updateUsernameField, updatePasswordField, updateButton, updateMessage);
        updateTab.setContent(updateBox);
        updateTab.setClosable(false);

        Tab deleteTab = new Tab();
        if (!darkMode) {
            deleteTab.setStyle("-fx-background-color: #f5f5f7;");
        } else {
            deleteTab.setStyle("-fx-background-color: " + DARKMODE_BACKGROUND);
        }
        Label deleteTabLabel = new Label("Delete User");
        deleteTabLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        deleteTabLabel.setStyle("-fx-padding: 20 20 20 20;" );
        deleteTabLabel.setTextFill(Color.web(darkMode ? DARKMODE_TEXT : HEADER_COLOR));
        deleteTab.setGraphic(deleteTabLabel);

        VBox deleteBox = new VBox(10);
        deleteBox.setPadding(new Insets(15));
        deleteBox.setAlignment(Pos.CENTER_LEFT);

        Label deleteLabel = new Label("Delete User");
        deleteLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        deleteLabel.setTextFill(Color.web(darkMode ? DARKMODE_TEXT : HEADER_COLOR));

        TextField deleteUsernameField = new TextField();
        deleteUsernameField.setPromptText("Username");
        deleteUsernameField.setPrefHeight(40);
        deleteUsernameField.setStyle("-fx-background-radius: 5; " +
                "-fx-border-radius: 5; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-width: 1px; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 8px;");
        Button deleteButton = createStyledButton("Delete",PRIMARY_COLOR,PRIMARY_DARK_COLOR);
        Label deleteMessage = new Label();

        deleteButton.setOnAction(e -> {
            String username = deleteUsernameField.getText();
            boolean success = db.deleteUser(username);
            if (success) {
                deleteMessage.setText("You have been deleted successfully");
            } else {
                deleteMessage.setText("Delete failed, check if user exist or fields are empty");
            }
        });
        deleteBox.getChildren().addAll(deleteLabel, deleteUsernameField, deleteButton, deleteMessage);
        deleteTab.setContent(deleteBox);
        deleteTab.setClosable(false);

        Tab listTab = new Tab();
        if (!darkMode) {
            listTab.setStyle("-fx-background-color: #f5f5f7;");
        } else {
            listTab.setStyle("-fx-background-color: " + DARKMODE_BACKGROUND);
        }
        Label listTabLabel = new Label("List Users");
        listTabLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        listTabLabel.setStyle("-fx-padding: 20 20 20 20;" );
        listTabLabel.setTextFill(Color.web(darkMode ? DARKMODE_TEXT : HEADER_COLOR));
        listTab.setGraphic(listTabLabel);

        VBox listBox = new VBox(10);
        listBox.setPadding(new Insets(15));
        listBox.setAlignment(Pos.CENTER_LEFT);

        Label listLabel = new Label("List Users");
        listLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        listLabel.setTextFill(Color.web(darkMode ? DARKMODE_TEXT : HEADER_COLOR));

        Button refreshButton = createStyledButton("Refresh List", PRIMARY_COLOR,PRIMARY_DARK_COLOR);
        ListView<String> usersListView = new ListView<>();

        refreshButton.setOnAction(e -> {
            ArrayList<String[]> users = (ArrayList<String[]>) db.listUsers();
            ObservableList<String> items = FXCollections.observableArrayList();
            for (String[] user : users) {
                items.add("ID: " + user[0] + " | Username: " + user[1]);
            }
            usersListView.setItems(items);
        });

        listBox.getChildren().addAll(listLabel,refreshButton ,usersListView);
        listTab.setContent(listBox);
        listTab.setClosable(false);

        tabPane.getTabs().addAll(registerTab, updateTab, deleteTab, listTab);

        Button backButton = createStyledButton("Logout",LOGOUT_COLOUR,LOGOUT_HOVER_COLOUR);
        backButton.setOnAction(e -> {
            new Login(stage,darkMode).show();
        });
        Region spacer = new Region();
        HBox.setHgrow(spacer,Priority.ALWAYS);


        HBox logoutBox = new HBox(spacer,backButton);
        logoutBox.setAlignment(Pos.CENTER);
        logoutBox.setPadding(new Insets(10));
        if (!darkMode) {
            logoutBox.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        } else {
            logoutBox.setStyle("-fx-background-color: " + DARKMODE_SECTION + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        }

        HBox navBox= new HBox();
        navBox.setAlignment(Pos.CENTER);
        navBox.setPadding(new Insets(10));
        if (!darkMode) {
            navBox.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        } else {
            navBox.setStyle("-fx-background-color: " + DARKMODE_SECTION + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        }

        Button settingsButton = createStyledButton("Settings", SETTINGS,SETTINGS_HOVER);
        settingsButton.setOnAction(e -> {
            PageInfo pageInfo = new PageInfo();
            pageInfo.setDarkMode(darkMode);
            pageInfo.setLoggedInUser(loggedInUser);
            SettingsPage settingsPage = new SettingsPage(stage,"Manage",pageInfo,darkMode);
            settingsPage.show();
        });

        Label title = new Label("Manage Users");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web(darkMode ? DARKMODE_TEXT :HEADER_COLOR));

        Region rightSpacer = new Region();
        HBox.setHgrow(rightSpacer,Priority.ALWAYS);


        navBox.getChildren().addAll(title,rightSpacer,settingsButton);




        BorderPane root = new BorderPane();
        if (!darkMode) {
            root.setStyle("-fx-background-color: #f5f5f7;");
        } else {
            root.setStyle("-fx-background-color: " + DARKMODE_BACKGROUND);
        }
        root.setTop(navBox);
        root.setCenter(tabPane);
        root.setBottom(logoutBox);
        BorderPane.setMargin(tabPane,new Insets(0,0,20,0));
        BorderPane.setMargin(logoutBox, new Insets(0, 0, 20, 0));

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setTitle("User Management Page");
        stage.show();

    }

    private Button createStyledButton(String text, String bgColor, String hoverColor) {
        Button button = new Button(text);
        button.setPrefSize(120, 40);
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
    public String getMfaSecretForUser(String username){
        String sql = "SELECT mfa_secret FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("mfa_secret");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
