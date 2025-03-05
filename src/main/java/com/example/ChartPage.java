package com.example;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class ChartPage {
    private Stage stage;
    private LogManager logManager;


    public ChartPage(Stage stage, LogManager logManager) {
        this.stage = stage;
        this.logManager = logManager;
    }

    public void show() {
        DefaultCategoryDataset dst = new DefaultCategoryDataset();
        OverallMetricsCalculator calc = new OverallMetricsCalculator();

        int impressions = calc.calcImpressions(logManager.getImpressionData());
        int clicks = calc.calcClicks(logManager.getClickData());
        int conversions = calc.calcConversions(logManager.getServerData());

        dst.addValue(impressions, "Metrics", "Impressions");
        dst.addValue(clicks, "Metrics", "Clicks");
        dst.addValue(conversions, "Metrics", "Conversions");

        JFreeChart chart = ChartFactory.createBarChart(
                "Campaign Metrics",
                "Metric",
                "Value",
                dst,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        ChartViewer chartViewer = new ChartViewer(chart);
        chartViewer.setMaxSize(400,300);


        var buttonHolder = new HBox();
        var backButton = new Button("Back");
        var logOutButton = new Button("Logout");
        var overallMetricsButton = new Button("Overall Metrics");
        buttonHolder.getChildren().addAll(backButton,overallMetricsButton,logOutButton);

        backButton.setOnAction(e -> {
            App.getInstance().showInputFilesPage();
        });

        overallMetricsButton.setOnAction(e -> {
            OverallMetricsPage metricsPage = new OverallMetricsPage(stage,logManager);
            metricsPage.show();
        });

        logOutButton.setOnAction(e -> {
            Login login = new Login(stage);
            login.show();
        });

        StackPane root = new StackPane(chartViewer);
        root.getChildren().add(buttonHolder);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();


    }
}
