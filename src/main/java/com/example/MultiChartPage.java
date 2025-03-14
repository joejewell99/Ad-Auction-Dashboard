package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;

import javax.swing.border.Border;
import java.util.ArrayList;

public class MultiChartPage {
    private Stage stage;
    private ArrayList<String> currentCharts;
    private ArrayList<String> timeFlags;
    private ArrayList<String> genders;
    private ArrayList<String> incomes;
    private ArrayList<ArrayList<String>> contexts;
    private ArrayList<ArrayList<String>> ages;

    private ChartCreator chartCreator;
    private LogManager logManager;



    public MultiChartPage(Stage stage,LogManager logManager,ArrayList<String> currentCharts, ArrayList<String> timeFlags,
                          ArrayList<String> genders, ArrayList<String> incomes, ArrayList<ArrayList<String>> contexts,
                          ArrayList<ArrayList<String>> ages) {
        this.stage = stage;
        this.logManager = logManager;
        this.chartCreator = new ChartCreator(logManager);

        this.currentCharts = currentCharts;
        this.timeFlags = timeFlags;
        this.genders = genders;
        this.incomes = incomes;
        this.contexts = contexts;
        this.ages = ages;
    }

    public void show() {
        //Chart Holder
        ChartViewer chartViewer1 = new ChartViewer(chartCreator.updateChart(currentCharts.get(0),timeFlags.get(0),
                genders.get(0),incomes.get(0),contexts.get(0),ages.get(0)));
        chartViewer1.setMinSize(500,300);
        chartViewer1.setMaxSize(500,300);
        ChartViewer chartViewer2 = new ChartViewer(chartCreator.updateChart(currentCharts.get(1),timeFlags.get(1),
                genders.get(1),incomes.get(1),contexts.get(1),ages.get(1)));
        chartViewer2.setMinSize(500,300);
        chartViewer2.setMaxSize(500,300);
        HBox chartHolder = new HBox(20);
        chartHolder.setAlignment(Pos.CENTER);
        chartHolder.getChildren().addAll(chartViewer1,chartViewer2);

        //Edit buttons holder
        var editChart1Button = new Button("Edit Chart 1");
        var editChart2Button = new Button("Edit Chart 2");
        var editHolder = new HBox(20);
        editHolder.setAlignment(Pos.CENTER);
        editHolder.getChildren().addAll(editChart1Button,editChart2Button);
        BorderPane.setMargin(editHolder,new Insets(0,0,100,0));

        editChart1Button.setOnAction(e -> {
            EditPage1 editPage = new EditPage1(stage,logManager,currentCharts,timeFlags,genders,incomes,contexts,ages);
            editPage.show();
        });

        editChart2Button.setOnAction(e -> {
            EditPage2 editPage2 = new EditPage2(stage,logManager,currentCharts,timeFlags,genders,incomes,contexts,ages);
            editPage2.show();
        });

        //Navigation Buttons
        var buttonHolder = new HBox();
        var backButton = new Button("Back");
        var logOutButton = new Button("Logout");
        var overallMetricsButton = new Button("Overall Metrics");
        buttonHolder.getChildren().addAll(backButton, overallMetricsButton, logOutButton);


        backButton.setOnAction(e -> {
            ChartPage chartPage = new ChartPage(stage,logManager);
            chartPage.show();
        });

        overallMetricsButton.setOnAction(e -> {
            OverallMetricsPage metricsPage = new OverallMetricsPage(stage, logManager);
            metricsPage.show();
        });

        logOutButton.setOnAction(e -> {
            Login login = new Login(stage);
            login.show();
        });



        BorderPane root = new BorderPane();
        root.setCenter(chartHolder);
        root.setBottom(editHolder);
        root.setTop(buttonHolder);
        Scene scene = new Scene(root,1300,800);
        stage.setScene(scene);
        stage.setTitle("Multicharts");
        stage.show();
    }
}
