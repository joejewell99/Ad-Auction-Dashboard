package com.example;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class InputFilesPage {
    private Stage stage;
    private Scene scene;

    private File impressionLogFile;
    private File clickLogFile;
    private File serverLogFile;

    // Log manager to be passed around the system (Use it in constructors for new scenes), essentially containing all the files and data
    public LogManager logManager = new LogManager();

    InputFilesPage(Stage stage) {
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        // Use BorderPane as main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f5f5f7;");
        
        // Create header section
        VBox headerBox = new VBox(15);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20, 0, 40, 0));
        
        Label titleLabel = new Label("Input Files");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#333333"));
        
        Label subtitleLabel = new Label("Please select data files for analysis");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setTextFill(Color.web("#666666"));
        
        headerBox.getChildren().addAll(titleLabel, subtitleLabel);
        mainLayout.setTop(headerBox);
        
        // File selection section using VBox
        VBox fileSelectionBox = new VBox(20);
        fileSelectionBox.setAlignment(Pos.CENTER);
        fileSelectionBox.setPadding(new Insets(10, 30, 30, 30));
        fileSelectionBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        // Initialize properties
        StringProperty impressionProperty = new SimpleStringProperty("");
        StringProperty clickProperty = new SimpleStringProperty("");
        StringProperty serverProperty = new SimpleStringProperty("");
        
        // Create file selection rows
        HBox impressionRow = createFileSelectionRow("Impression Log:", impressionProperty);
        HBox clickRow = createFileSelectionRow("Click Log:", clickProperty);
        HBox serverRow = createFileSelectionRow("Server Log:", serverProperty);
        
        // Create button area
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 10, 0));
        
        Button proceedButton = createStyledButton("Proceed", "#4285F4", "#3367d6");
        Button logoutButton = createStyledButton("Logout", "#757575", "#616161");
        
        buttonBox.getChildren().addAll(proceedButton, logoutButton);
        
        // Add all components to file selection box
        fileSelectionBox.getChildren().addAll(
                impressionRow, 
                createSeparator(), 
                clickRow, 
                createSeparator(), 
                serverRow, 
                createSeparator(), 
                buttonBox);
        
        mainLayout.setCenter(fileSelectionBox);
        
        // File chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Log File");
        
        // Get buttons and set click handlers
        Button impressionLogButton = (Button) impressionRow.getChildren().get(1);
        Button clickLogButton = (Button) clickRow.getChildren().get(1);
        Button serverLogButton = (Button) serverRow.getChildren().get(1);
        
        // Store the impression log in logManager
        impressionLogButton.setOnAction(e -> {
            impressionLogFile = fileChooser.showOpenDialog(stage);
            if (impressionLogFile != null) {
                impressionProperty.set(impressionLogFile.getName());
                logManager.assignImpressionLog(impressionLogFile);
                logManager.convertImpressionLog();
                System.out.println("Impression Log Selected: " + impressionLogFile.getAbsolutePath());
            }
        });

        // Store the click log in LogManager
        clickLogButton.setOnAction(e -> {
            clickLogFile = fileChooser.showOpenDialog(stage);
            if (clickLogFile != null) {
                clickProperty.set(clickLogFile.getName());
                logManager.assignClickLog(clickLogFile);
                logManager.convertClickLog();
                System.out.println("Click Log Selected: " + clickLogFile.getAbsolutePath());
            }
        });

        // Store the server log in LogManager
        serverLogButton.setOnAction(e -> {
            serverLogFile = fileChooser.showOpenDialog(stage);
            if (serverLogFile != null) {
                serverProperty.set(serverLogFile.getName());
                logManager.assignServerLog(serverLogFile);
                logManager.convertServerLog();
                System.out.println("Server Log Selected: " + serverLogFile.getAbsolutePath());
            }
        });

        proceedButton.setOnAction(e -> {
            System.out.println("Proceed Button clicked");
            ChartPage chartPage = new ChartPage(stage, logManager);
            chartPage.show();
        });

        logoutButton.setOnAction(e -> {
            System.out.println("Logout Button clicked");
            Login login = new Login(stage);
            login.show();
        });

        scene = new Scene(mainLayout, 800, 600);
    }
    
    // Create a file selection row
    private HBox createFileSelectionRow(String labelText, StringProperty fileNameProperty) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 0, 10, 0));
        
        Label label = new Label(labelText);
        label.setMinWidth(120);
        label.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        label.setTextFill(Color.web("#333333"));
        
        Button selectButton = new Button("Select File");
        selectButton.setPrefSize(120, 35);
        selectButton.setStyle(
                "-fx-background-color: #f0f0f0; " +
                "-fx-text-fill: #333333; " +
                "-fx-font-weight: normal; " +
                "-fx-font-size: 13px; " +
                "-fx-background-radius: 4; " +
                "-fx-border-radius: 4; " +
                "-fx-border-color: #d0d0d0; " +
                "-fx-border-width: 1px; " +
                "-fx-cursor: hand;");
        
        selectButton.setOnMouseEntered(e -> 
            selectButton.setStyle(
                "-fx-background-color: #e8e8e8; " +
                "-fx-text-fill: #333333; " +
                "-fx-font-weight: normal; " +
                "-fx-font-size: 13px; " +
                "-fx-background-radius: 4; " +
                "-fx-border-radius: 4; " +
                "-fx-border-color: #c0c0c0; " +
                "-fx-border-width: 1px; " +
                "-fx-cursor: hand;"));
        
        selectButton.setOnMouseExited(e -> 
            selectButton.setStyle(
                "-fx-background-color: #f0f0f0; " +
                "-fx-text-fill: #333333; " +
                "-fx-font-weight: normal; " +
                "-fx-font-size: 13px; " +
                "-fx-background-radius: 4; " +
                "-fx-border-radius: 4; " +
                "-fx-border-color: #d0d0d0; " +
                "-fx-border-width: 1px; " +
                "-fx-cursor: hand;"));
        
        Label fileNameLabel = new Label();
        fileNameLabel.textProperty().bind(fileNameProperty);
        fileNameLabel.setFont(Font.font("Arial", 13));
        fileNameLabel.setTextFill(Color.web("#666666"));
        fileNameLabel.setMinWidth(200);
        fileNameLabel.setMaxWidth(300);
        
        // File status indicator
        Region statusIndicator = new Region();
        statusIndicator.setPrefSize(8, 8);
        statusIndicator.setStyle("-fx-background-color: #cccccc; -fx-background-radius: 4;");
        
        // Update status indicator when file name changes
        fileNameProperty.addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                statusIndicator.setStyle("-fx-background-color: #4CAF50; -fx-background-radius: 4;");
            } else {
                statusIndicator.setStyle("-fx-background-color: #cccccc; -fx-background-radius: 4;");
            }
        });
        
        row.getChildren().addAll(label, selectButton, fileNameLabel, statusIndicator);
        return row;
    }
    
    // Create separator
    private Separator createSeparator() {
        Separator separator = new Separator();
        separator.setStyle("-fx-opacity: 0.3;");
        return separator;
    }
    
    // Create styled button
    private Button createStyledButton(String text, String bgColor, String hoverColor) {
        Button button = new Button(text);
        button.setPrefSize(120, 40);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        String style = String.format(
                "-fx-background-color: %s; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 1); " +
                "-fx-cursor: hand;", bgColor);
        
        button.setStyle(style);
        
        button.setOnMouseEntered(e -> 
            button.setStyle(String.format(
                "-fx-background-color: %s; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2); " +
                "-fx-cursor: hand;", hoverColor)));
        
        button.setOnMouseExited(e -> button.setStyle(style));
        
        return button;
    }
    
    public void show() {
        stage.setTitle("File Selection");
        stage.setScene(scene);
        stage.show();
    }
}
