package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.chart.fx.ChartViewer;



public class ChartPage {
    private Stage stage;
    private LogManager logManager;
    private ChartCreator chartCreator;
    private String timeFlag;
    private String currentChart;

    public ChartPage(Stage stage, LogManager logManager) {
        this.stage = stage;
        this.logManager = logManager;
        this.chartCreator = new ChartCreator(logManager);
        this.timeFlag = "Daily";
        this.currentChart = "Clicks";
    }

    public void show() {

        //Holder for chart
        ChartViewer chartViewer = new ChartViewer(chartCreator.updateChart(currentChart,timeFlag));
        chartViewer.setMaxSize(600, 450);


        //Metric options
        ToggleGroup metricOptions = new ToggleGroup();

        RadioButton clickMetric = new RadioButton("Total Clicks");
        clickMetric.setToggleGroup(metricOptions);
        clickMetric.setSelected(true);
        clickMetric.setOnAction(e -> {
            this.currentChart = "Clicks";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag));
        });

        RadioButton impressionMetric = new RadioButton("Total Impressions");
        impressionMetric.setToggleGroup(metricOptions);
        impressionMetric.setOnAction(e -> {
            this.currentChart = "Impressions";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag));
        });

        RadioButton uniqueMetric = new RadioButton("Total uniques");
        uniqueMetric.setToggleGroup(metricOptions);
        uniqueMetric.setOnAction(e -> {
            this.currentChart = "Uniques";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag));
        });

        RadioButton bouncesMetric = new RadioButton("Total bounces");
        bouncesMetric.setToggleGroup(metricOptions);
        bouncesMetric.setOnAction(e -> {
            this.currentChart = "Bounces";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag));
        });

        RadioButton conversionMetric = new RadioButton("Total conversions");
        conversionMetric.setToggleGroup(metricOptions);
        conversionMetric.setOnAction(e -> {
            this.currentChart = "Conversions";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag));
        });

        RadioButton costMetric = new RadioButton("Total cost");
        costMetric.setToggleGroup(metricOptions);
        costMetric.setOnAction(e -> {
            this.currentChart = "Cost";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag));
        });

        RadioButton ctrMetric = new RadioButton("CTR");
        ctrMetric.setToggleGroup(metricOptions);
        ctrMetric.setOnAction(e -> {
            this.currentChart = "CTR";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag));
        });

        RadioButton cpaMetric = new RadioButton("CPA");
        cpaMetric.setToggleGroup(metricOptions);
        cpaMetric.setOnAction(e -> {
            this.currentChart = "CPA";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag));
        });

        RadioButton cpcMetric = new RadioButton("CPC");
        cpcMetric.setToggleGroup(metricOptions);
        cpcMetric.setOnAction(e -> {
            this.currentChart = "CPC";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag));
        });

        RadioButton cpmMetric = new RadioButton("CPM");
        cpmMetric.setToggleGroup(metricOptions);
        cpmMetric.setOnAction(e -> {
            this.currentChart = "CPM";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag));
        });

        RadioButton bounceRateMetric = new RadioButton("Bounce Rate");
        bounceRateMetric.setToggleGroup(metricOptions);
        bounceRateMetric.setOnAction(e -> {
            this.currentChart = "BounceRate";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag));
        });

        ToggleGroup timeOptions = new ToggleGroup();

        RadioButton dailyButton = new RadioButton("Daily");
        dailyButton.setSelected(true);
        dailyButton.setToggleGroup(timeOptions);
        dailyButton.setOnAction(e -> {
            this.timeFlag = "Daily";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag));
        });

        RadioButton weeklyButton = new RadioButton("Weekly");
        weeklyButton.setToggleGroup(timeOptions);
        weeklyButton.setOnAction(e -> {
            this.timeFlag = "Weekly";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag));
        });

        RadioButton monthlyButton = new RadioButton("Monthly");
        monthlyButton.setToggleGroup(timeOptions);
        monthlyButton.setOnAction(e -> {
            this.timeFlag = "Monthly";
            chartViewer.setChart(chartCreator.updateChart(currentChart,timeFlag));
        });


        var metricsHeader = new Label("Metrics");
        var timeLabel = new Label("Time Granularity");
        var metricOptionHolder = new VBox();
        metricOptionHolder.getChildren().addAll(metricsHeader, clickMetric, impressionMetric, uniqueMetric, bouncesMetric, conversionMetric, costMetric, ctrMetric, cpaMetric, cpcMetric, cpmMetric, bounceRateMetric);
        metricOptionHolder.getChildren().addAll(timeLabel, dailyButton, weeklyButton,monthlyButton);

        //Navigation Buttons
        var buttonHolder = new HBox();
        var backButton = new Button("Back");
        var logOutButton = new Button("Logout");
        var overallMetricsButton = new Button("Overall Metrics");
        buttonHolder.getChildren().addAll(backButton, overallMetricsButton, logOutButton);

        backButton.setOnAction(e -> {
            App.getInstance().showInputFilesPage();
        });

        overallMetricsButton.setOnAction(e -> {
            OverallMetricsPage metricsPage = new OverallMetricsPage(stage, logManager);
            metricsPage.show();
        });

        logOutButton.setOnAction(e -> {
            Login login = new Login(stage);
            login.show();
        });

        //Layout
        BorderPane root = new BorderPane();
        root.setTop(buttonHolder);
        root.setCenter(chartViewer);
        root.setRight(metricOptionHolder);
        BorderPane.setMargin(metricOptionHolder, new Insets(0, 10, 0, 0));


        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Graphs");
        stage.show();
    }
}

