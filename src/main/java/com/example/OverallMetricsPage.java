package com.example;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;


public class OverallMetricsPage {
    private Stage stage;
    private Scene scene;

    public LogManager logManager;
    private ArrayList<String[]> clickData;
    private ArrayList<String[]> impressionData;
    private ArrayList<String[]> serverData;
    private OverallMetricsCalculator metricsCalculator = new OverallMetricsCalculator();

    OverallMetricsPage(Stage stage,LogManager logManager) {
        this.stage = stage;
        this.logManager = logManager;
        this.clickData = logManager.getClickData();
        this.impressionData = logManager.getImpressionData();
        this.serverData = logManager.getServerData();
        initialize();
    }

    private void initialize() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        //Title
        Label titleLabel = new Label("Overall Metrics");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        grid.add(titleLabel, 0, 0, 3, 1);
        GridPane.setHalignment(titleLabel, HPos.CENTER);

        var buttonHolder = new HBox();
        var logoutButton = new Button("Logout");
        var backButton = new Button("BACK");
        buttonHolder.getChildren().addAll(logoutButton,backButton);
        grid.add(buttonHolder,1,2);

        //Logout Button action
        logoutButton.setOnAction(e -> {
            System.out.println("Logout Button clicked");
            Login login = new Login(stage);
            login.show();
        });

        //Back Button action
        backButton.setOnAction(e -> {
            ChartPage chartPage = new ChartPage(stage, logManager);
            chartPage.show();
        });


        //Metrics
        var impressions = new Label("Total impressions: " + Integer.toString(metricsCalculator.calcImpressions(impressionData)));
        var clicks =  new Label("Total clicks: " + Integer.toString(metricsCalculator.calcClicks(clickData)));
        var uniques = new Label("Total uniques: " + Integer.toString(metricsCalculator.calcUniques(clickData)));
        var bounces = new Label("Total bounces: " + Integer.toString(metricsCalculator.calcBounces(serverData)));
        var conversions = new Label("Total conversions: " + Integer.toString(metricsCalculator.calcConversions(serverData)));
        var cost = new Label("Total cost: " + Float.toString(metricsCalculator.calcCost(clickData,impressionData)));
        var ctr = new Label("CTC: " + Float.toString(metricsCalculator.calcCTR(clickData,impressionData)));
        var cpa = new Label("CPA: " + Float.toString(metricsCalculator.calcCPA(clickData,impressionData,serverData)));
        var cpc = new Label("CPC: " + Float.toString(metricsCalculator.calcCPC(clickData,impressionData,serverData)));
        var cpm = new Label("CPM: " + Float.toString(metricsCalculator.calcCPM(clickData,impressionData)));
        var bounceRate = new Label("Bounce rate: " + Float.toString(metricsCalculator.calcBounceRate(clickData,serverData)));

        //Metrics Holder
        var metricsHolder = new VBox(10);
        grid.add(metricsHolder,1,1);
        metricsHolder.getChildren().addAll(impressions,clicks,uniques,bounces,conversions,cost,ctr,cpa,cpc,cpm,bounceRate);


        scene = new Scene(grid, 600, 400);

    }

    public void show() {
        stage.setScene(scene);
        stage.setTitle("Overall Metrics");
        stage.show();
    }
}
