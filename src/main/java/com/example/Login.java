package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;

public class Login {
    private Stage stage;
    private Scene loginScene;

    Login(Stage stage) {
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label userNameLabel = new Label("Username:");
        grid.add(userNameLabel, 0, 0);

        TextField userName = new TextField();
        grid.add(userName, 1, 0);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 1);

        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 1);


        Button loginButton = new Button("Login");
        grid.add(loginButton, 1, 2);

        Label message = new Label();
        grid.add(message, 1, 3);

        loginButton.setOnAction(event -> {
            String username = userName.getText();
            String password = passwordField.getText();
            if (authenticate(username, password)) {
                message.setText("Login Successful");
                App.getInstance().showInputFilesPage();
            } else {
                message.setText("Login Failed");
            }
        });

        loginScene = new Scene(grid, 600, 400);

    }
    // removed password and user just to make things simpler
    private boolean authenticate(String username, String password) {
        return "".equals(username) && "".equals(password);
    }
    public void show(){
        stage.setScene(loginScene);
        stage.setTitle("Login");
        stage.show();
    }
}
