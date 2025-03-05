package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
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
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25));

        Label titleLabel = new Label("Input Files");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        grid.add(titleLabel, 0, 0, 2, 1);

        Label impressionLogLabel = new Label("Impression Log:");
        grid.add(impressionLogLabel, 0, 1);
        Button impressionLogButton = new Button("Select File");
        grid.add(impressionLogButton, 1, 1);

        Label clickLogLabel = new Label("Click Log:");
        grid.add(clickLogLabel, 0, 2);
        Button clickLogButton = new Button("Select File");
        grid.add(clickLogButton, 1, 2);

        Label serverLogLabel = new Label("Server Log:");
        grid.add(serverLogLabel, 0, 3);
        Button serverLogButton = new Button("Select File");
        grid.add(serverLogButton, 1, 3);

        Button proceedButton = new Button("Proceed");
        grid.add(proceedButton, 0, 4);
        Button logoutButton = new Button("Logout");
        grid.add(logoutButton, 1, 4);
        var overallMetricsButton = new Button("OverallMetrics");
        grid.add(overallMetricsButton,2,4);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Log File");

        // Store the impression log in logManager
        impressionLogButton.setOnAction(e -> {
            impressionLogFile = fileChooser.showOpenDialog(stage);
            logManager.assignImpressionLog(impressionLogFile);
            logManager.convertImpressionLog();
            logManager.printImpressionData();
            if (impressionLogFile != null) {
                System.out.println("Impression Log Selected: " + impressionLogFile.getAbsolutePath());
            }
        });

        // Store the click log in LogManager
        clickLogButton.setOnAction(e -> {
            clickLogFile = fileChooser.showOpenDialog(stage);
            logManager.assignClickLog(clickLogFile);
            logManager.convertClickLog();
            logManager.printClickData();
            if (clickLogFile != null) {
                System.out.println("Click Log Selected: " + clickLogFile.getAbsolutePath());
            }
        });

        // Store the server log in LogManager
        serverLogButton.setOnAction(e -> {
            serverLogFile = fileChooser.showOpenDialog(stage);
            logManager.assignServerLog(serverLogFile);
            logManager.convertServerLog();
            logManager.printServerData();
            if (serverLogFile != null) {
                System.out.println("Server Log Selected: " + serverLogFile.getAbsolutePath());
            }
        });

        proceedButton.setOnAction(e -> {
            System.out.println("Proceed Button clicked");
        });

        overallMetricsButton.setOnAction(e -> {
            OverallMetricsPage metricsPage = new OverallMetricsPage(stage,logManager);
            metricsPage.show();
        });

        logoutButton.setOnAction(e -> {
            System.out.println("Logout Button clicked");
            Login login = new Login(stage);
            login.show();
        });

        scene = new Scene(grid, 600, 400);

    }
    public void show() {
        stage.setScene(scene);
        stage.show();
    }
}
