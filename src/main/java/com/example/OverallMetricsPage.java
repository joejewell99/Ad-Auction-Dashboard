package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;



public class OverallMetricsPage {
    private Stage stage;
    private Scene scene;

    public LogManager logManager;
    private OverallMetricsCalculator metricsCalculator = new OverallMetricsCalculator();

    OverallMetricsPage(Stage stage,LogManager logManager) {
        this.stage = stage;
        this.logManager = logManager;
        initialize();
    }

    private void initialize() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25));

        Label titleLabel = new Label("Overall Metrics");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        grid.add(titleLabel, 0, 0, 2, 1);

        Button logoutButton = new Button("Logout");
        grid.add(logoutButton, 1, 4);

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
