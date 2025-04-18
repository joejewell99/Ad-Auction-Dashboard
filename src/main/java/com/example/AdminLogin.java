package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class AdminLogin {
    private final Stage stage;
    private final Scene scene;
    private final LoginDatabase db = new LoginDatabase();
    private final Consumer<User> onSuccess;

    public AdminLogin(Stage stage, Consumer<User> onSuccess) {
        this.stage = stage;
        this.onSuccess = onSuccess;

        VBox formBox = new VBox();
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(20));

        Label header = new Label("Admin Login");
        TextField userField = new TextField();
        userField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Label msg = new Label();
        msg.setMinHeight(24);
        Button loginButton = new Button("Login as Admin");
        loginButton.setOnAction(event -> {
            User u = db.authenticateUser(userField.getText(), passwordField.getText());
            if (u != null && "admin".equals(u.getRole())) {
                onSuccess.accept(u);
            } else {
                msg.setText("Invalid Username/Password");
                msg.setTextFill(Color.RED);
            }
        });
        formBox.getChildren().addAll(header, userField, passwordField, msg, loginButton);

        Button backBtn = new Button("Back");
        backBtn.setOnAction(event -> {
            new Login(stage).show();
        });

        HBox backHBox = new HBox(backBtn);
        backHBox.setAlignment(Pos.CENTER_LEFT);
        backHBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(formBox);
        root.setBottom(backHBox);

        Scene current = stage.getScene();
        double w = current.getWidth();
        double h = current.getHeight();
        this.scene = new Scene(root, w, h);
    }

    public void show() {
        stage.setScene(scene);
        stage.setTitle("Admin Login");
        stage.show();
    }
}
