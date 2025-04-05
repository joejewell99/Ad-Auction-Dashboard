package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class UserManagementPage {
    private Stage stage;
    private LoginDatabase db;

    public UserManagementPage(Stage stage) {
        this.stage = stage;
        this.db = new LoginDatabase();
    }

    public void show() {
        TabPane tabPane = new TabPane();

        Tab registerTab = new Tab("Register");
        VBox registerBox = new VBox(10);
        registerBox.setPadding(new Insets(15));
        registerBox.setAlignment(Pos.CENTER_LEFT);

        Label registerLabel = new Label("Register New User");
        TextField regUsernameField = new TextField();
        regUsernameField.setPromptText("Username");
        PasswordField regPasswordField = new PasswordField();
        regPasswordField.setPromptText("Password");
        Button regButton = new Button("Register");
        Label regMessage = new Label();

        regButton.setOnAction(e -> {
            String username = regUsernameField.getText();
            String password = regPasswordField.getText();
            boolean success = db.registerUser(username, password);
            if (success) {
                regMessage.setText("You have been registered successfully");
            } else {
                regMessage.setText("Something went wrong, User may already exist or fields are empty");
            }
        });

        registerBox.getChildren().addAll(registerLabel, regUsernameField, regPasswordField, regButton, regMessage);
        registerTab.setContent(registerBox);
        registerTab.setClosable(false);

        Tab updateTab = new Tab("Update Password");
        VBox updateBox = new VBox(10);
        updateBox.setPadding(new Insets(15));
        updateBox.setAlignment(Pos.CENTER_LEFT);

        Label updateLabel = new Label("Update Password");
        TextField updateUsernameField = new TextField();
        updateUsernameField.setPromptText("Username");
        PasswordField updatePasswordField = new PasswordField();
        updatePasswordField.setPromptText("New Password");
        Button updateButton = new Button("Update");
        Label updateMessage = new Label();

        updateButton.setOnAction(e -> {
            String username = updateUsernameField.getText();
            String password = updatePasswordField.getText();
            boolean success = db.updateUserPassword(username, password);
            if (success) {
                updateMessage.setText("You have been updated successfully");
            } else {
                updateMessage.setText("Update failed, check if user already exist or fields are empty");
            }
        });
        updateBox.getChildren().addAll(updateLabel, updateUsernameField, updatePasswordField, updateButton, updateMessage);
        updateTab.setContent(updateBox);
        updateTab.setClosable(false);

        Tab deleteTab = new Tab("Delete User");
        VBox deleteBox = new VBox(10);
        deleteBox.setPadding(new Insets(15));
        deleteBox.setAlignment(Pos.CENTER_LEFT);

        Label deleteLabel = new Label("Delete User");
        TextField deleteUsernameField = new TextField();
        deleteUsernameField.setPromptText("Username");
        Button deleteButton = new Button("Delete");
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

        Tab listTab = new Tab("List Users");
        VBox listBox = new VBox(10);
        listBox.setPadding(new Insets(15));
        listBox.setAlignment(Pos.CENTER_LEFT);

        Label listLabel = new Label("List Users");
        Button refreshButton = new Button("Refresh List");
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

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            new Login(stage).show();
        });

        HBox navBox = new HBox(10, backButton);
        navBox.setAlignment(Pos.CENTER);
        navBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(tabPane);
        root.setBottom(navBox);

        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.setTitle("User Management Page");
        stage.show();

    }

}

