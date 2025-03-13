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
    private String currentChart1;
    private String currentChart2;
    private String timeFlag1;
    private String timeFlag2;
    private String gender1;
    private String gender2;
    private String income1;
    private String income2;
    private ArrayList<String> context1;
    private ArrayList<String> context2;
    private ArrayList<String> age1;
    private ArrayList<String> age2;

    private ChartCreator chartCreator;
    private LogManager logManager;



    public MultiChartPage(Stage stage,LogManager logManager,ArrayList<String> currentCharts, ArrayList<String> timeFlags,
                          ArrayList<String> genders, ArrayList<String> incomes, ArrayList<ArrayList<String>> contexts,
                          ArrayList<ArrayList<String>> ages) {
        this.stage = stage;
        this.logManager = logManager;
        this.chartCreator = new ChartCreator(logManager);

        this.currentChart1 = currentCharts.get(0);
        this.currentChart2 = currentCharts.get(1);
        this.timeFlag1 = timeFlags.get(0);
        this.timeFlag2 = timeFlags.get(1);
        this.gender1 = genders.get(0);
        this.gender2 = genders.get(1);
        this.income1 = incomes.get(0);
        this.income2 = incomes.get(1);
        this.context1 = contexts.get(0);
        this.context2 = contexts.get(1);
        this.age1 = ages.get(0);
        this.age2 = ages.get(1);
    }

    public void show() {
        //Chart Holder
        ChartViewer chartViewer1 = new ChartViewer(chartCreator.updateChart(currentChart1,timeFlag1,gender1,income1,context1,age1));
        chartViewer1.setMinSize(500,300);
        chartViewer1.setMaxSize(500,300);
        ChartViewer chartViewer2 = new ChartViewer(chartCreator.updateChart(currentChart2,timeFlag2,gender2,income2,context2,age2));
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
