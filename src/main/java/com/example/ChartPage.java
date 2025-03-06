package com.example;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ChartPage {
    private Stage stage;
    private LogManager logManager;


    public ChartPage(Stage stage, LogManager logManager) {
        this.stage = stage;
        this.logManager = logManager;
    }

    public void show() {
        /**
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

         */

        //Holder for chart
        ChartViewer chartViewer = new ChartViewer(genClickChart());
        chartViewer.setMaxSize(700,450);

        //Metric options
        ToggleGroup metricOptions = new ToggleGroup();
        RadioButton clickMetric = new RadioButton("Total Clicks");
        clickMetric.setToggleGroup(metricOptions);
        clickMetric.setSelected(true);
        clickMetric.setOnAction(e -> {
            chartViewer.setChart(genClickChart());
        });
        RadioButton impressionMetric = new RadioButton("Total Impressions");
        impressionMetric.setToggleGroup(metricOptions);
        impressionMetric.setOnAction(e -> {
            chartViewer.setChart(genImpressionChart());
        });
        var metricOptionHolder = new HBox();
        metricOptionHolder.getChildren().addAll(clickMetric,impressionMetric);


        //Navigation Buttons
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

        //Layout
        StackPane root = new StackPane();
        root.getChildren().add(chartViewer);
        StackPane.setAlignment(chartViewer,Pos.CENTER);
        var box = new VBox();
        box.getChildren().add(buttonHolder);
        box.getChildren().add(metricOptionHolder);
        root.getChildren().add(box);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Graphs");
        stage.show();


    }

    /**
     * Generates chart for total clicks
     */
    public JFreeChart genClickChart() {
        ArrayList<String[]> clicks = logManager.getClickData();
        Map<String,Integer> clickMap = new TreeMap<>();

        //Get the number of clicks that appeared on each day
        for (String[] click : clicks){
            String dateTime = click[0];
            String[] dateTimeSplit = dateTime.split(" ");
            String date = dateTimeSplit[0];
            clickMap.put(date, clickMap.getOrDefault(date, 1) + 1);
        }

        // Create the dataset to feed to the chart creation
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int cumulativeClicks = 0;
        for (Map.Entry<String, Integer> entry : clickMap.entrySet()) {
            cumulativeClicks += entry.getValue();
            dataset.addValue(cumulativeClicks, "Rows", entry.getKey());
        }

        //Generate the chart
        JFreeChart clickChart = ChartFactory.createLineChart(
                "Total Clicks",
                "Date",
                "Clicks",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        // Adjusting font
        CategoryPlot plot = clickChart.getCategoryPlot();
        CategoryAxis xAxis = plot.getDomainAxis();
        xAxis.setTickLabelFont(new Font("Arial", Font.PLAIN, 6));  // Change font size here

        return clickChart;

    }

    /**
     * Generates chart for total impressions
     * @return
     */
    public JFreeChart genImpressionChart() {
        ArrayList<String[]> impressions = logManager.getImpressionData();
        Map<String,Integer> impressionMap = new TreeMap<>();

        //Get the number of impressions that appeared on each day
        for (String[] impression : impressions){
            String dateTime = impression[0];
            String[] dateTimeSplit = dateTime.split(" ");
            String date = dateTimeSplit[0];
            impressionMap.put(date, impressionMap.getOrDefault(date, 1) + 1);
        }

        // Create the dataset to feed to the chart creation
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int cumulativeImpressions = 0;
        for (Map.Entry<String, Integer> entry : impressionMap.entrySet()) {
            cumulativeImpressions += entry.getValue();
            dataset.addValue(cumulativeImpressions, "Rows", entry.getKey());
        }

        //Generate the chart
        JFreeChart impressionChart = ChartFactory.createLineChart(
                "Total Impressions",
                "Date",
                "Impressions",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        // Adjusting font
        CategoryPlot plot = impressionChart.getCategoryPlot();
        CategoryAxis xAxis = plot.getDomainAxis();
        xAxis.setTickLabelFont(new Font("Arial", Font.PLAIN, 6));  // Change font size here

        return impressionChart;

    }
}
