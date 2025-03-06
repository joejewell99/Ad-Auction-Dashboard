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
    /**
     * Reference to primary stage.
     * UI for login scene.
     */
    private Stage stage;
    private Scene loginScene;

    /**
     * Constructors for primary stage + initializes the login class.
     * @param stage
     */

    Login(Stage stage) {
        this.stage = stage;
        initialize();
    }

    /**
     * Initializing the UI component + Layout.
     */
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

        /**
         * Defining the action to be performed when clicking on the button.
         * Get the user + pass inputted by the user.
         * Checks if the credentials are correct, return successful. Otherwise, login failed.
         */
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

        /**
         * Creating a scene containing a grid layout.
         */
        loginScene = new Scene(grid, 600, 400);

    }

    /**
     * Method that checks the correct login info.
     * @param username set to empty.
     * @param password set to empty.
     * @return User & Password if true. Otherwise, false.
     * Kept the login info as empty for the time being.
     */
    private boolean authenticate(String username, String password) {
        return "".equals(username) && "".equals(password);
    }

    /**
     * Display login scene.
     */
        public void show(){
        stage.setScene(loginScene);
        stage.setTitle("Login");
        stage.show();
    }
}
