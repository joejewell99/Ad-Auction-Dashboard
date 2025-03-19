package com.example;

import com.itextpdf.text.DocumentException;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.chart.fx.ChartViewer;


import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;



public class ChartPage {
    private Stage stage;
    private LogManager logManager;
    private ChartCreator chartCreator;
    private String timeFlag;
    private String currentChart;

    private String gender;
    private ArrayList<String> age;
    private String income;
    private ArrayList<String> context;


    public ChartPage(Stage stage, LogManager logManager) {
        this.stage = stage;
        this.logManager = logManager;
        this.chartCreator = new ChartCreator(logManager);
        this.timeFlag = "Daily";
        this.currentChart = "Clicks";

        this.gender = "";
        this.age = new ArrayList<>();
        this.income = "";
        this.context = new ArrayList<>();
    }

    public void show() {

        //Holder for chart
        ChartViewer chartViewer = new ChartViewer(chartCreator.updateChart(currentChart,timeFlag,gender,income,context,age));
        chartViewer.setMaxSize(800, 600);


        //Metric buttons
        ToggleGroup metricOptions = new ToggleGroup();

        RadioButton clickMetric = new RadioButton("Total Clicks");
        clickMetric.setToggleGroup(metricOptions);
        clickMetric.setSelected(true);
        clickMetric.setOnAction(e -> {
            this.currentChart = "Clicks";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag,gender,income,context,age));
        });
        RadioButton impressionMetric = new RadioButton("Total Impressions");
        impressionMetric.setToggleGroup(metricOptions);
        impressionMetric.setOnAction(e -> {
            this.currentChart = "Impressions";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag,gender,income,context,age));
        });
        RadioButton uniqueMetric = new RadioButton("Total uniques");
        uniqueMetric.setToggleGroup(metricOptions);
        uniqueMetric.setOnAction(e -> {
            this.currentChart = "Uniques";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag,gender,income,context,age));
        });
        RadioButton bouncesMetric = new RadioButton("Total bounces");
        bouncesMetric.setToggleGroup(metricOptions);
        bouncesMetric.setOnAction(e -> {
            this.currentChart = "Bounces";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag,gender,income,context,age));
        });
        RadioButton conversionMetric = new RadioButton("Total conversions");
        conversionMetric.setToggleGroup(metricOptions);
        conversionMetric.setOnAction(e -> {
            this.currentChart = "Conversions";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag,gender,income,context,age));
        });
        RadioButton costMetric = new RadioButton("Total cost");
        costMetric.setToggleGroup(metricOptions);
        costMetric.setOnAction(e -> {
            this.currentChart = "Cost";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag,gender,income,context,age));
        });
        RadioButton ctrMetric = new RadioButton("CTR");
        ctrMetric.setToggleGroup(metricOptions);
        ctrMetric.setOnAction(e -> {
            this.currentChart = "CTR";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag,gender,income,context,age));
        });
        RadioButton cpaMetric = new RadioButton("CPA");
        cpaMetric.setToggleGroup(metricOptions);
        cpaMetric.setOnAction(e -> {
            this.currentChart = "CPA";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag,gender,income,context,age));
        });
        RadioButton cpcMetric = new RadioButton("CPC");
        cpcMetric.setToggleGroup(metricOptions);
        cpcMetric.setOnAction(e -> {
            this.currentChart = "CPC";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag,gender,income,context,age));
        });
        RadioButton cpmMetric = new RadioButton("CPM");
        cpmMetric.setToggleGroup(metricOptions);
        cpmMetric.setOnAction(e -> {
            this.currentChart = "CPM";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag,gender,income,context,age));
        });
        RadioButton bounceRateMetric = new RadioButton("Bounce Rate");
        bounceRateMetric.setToggleGroup(metricOptions);
        bounceRateMetric.setOnAction(e -> {
            this.currentChart = "BounceRate";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag,gender,income,context,age));
        });

        //Time granularity buttons
        ToggleGroup timeOptions = new ToggleGroup();

        RadioButton dailyButton = new RadioButton("Daily");
        dailyButton.setSelected(true);
        dailyButton.setToggleGroup(timeOptions);
        dailyButton.setOnAction(e -> {
            this.timeFlag = "Daily";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag,gender,income,context,age));
        });
        RadioButton weeklyButton = new RadioButton("Weekly");
        weeklyButton.setToggleGroup(timeOptions);
        weeklyButton.setOnAction(e -> {
            this.timeFlag = "Weekly";
            chartViewer.setChart(chartCreator.updateChart(currentChart, timeFlag,gender,income,context,age));
        });
        RadioButton monthlyButton = new RadioButton("Monthly");
        monthlyButton.setToggleGroup(timeOptions);
        monthlyButton.setOnAction(e -> {
            this.timeFlag = "Monthly";
            chartViewer.setChart(chartCreator.updateChart(currentChart,timeFlag,gender,income,context,age));
        });

        //Gender Buttons
        ToggleGroup genderOptions = new ToggleGroup();

        RadioButton bothGenderButton = new RadioButton("Both");
        bothGenderButton.setSelected(true);
        bothGenderButton.setToggleGroup(genderOptions);
        bothGenderButton.setOnAction(e -> {
            this.gender = "";
            chartViewer.setChart(chartCreator.updateChart(currentChart,timeFlag,gender,income,context,age));
        });
        RadioButton maleButton = new RadioButton("Male");
        maleButton.setToggleGroup(genderOptions);
        maleButton.setOnAction(e -> {
            this.gender = "Male";
            chartViewer.setChart(chartCreator.updateChart(currentChart,timeFlag,gender,income,context,age));
        });
        RadioButton femaleButton = new RadioButton("Female");
        femaleButton.setToggleGroup(genderOptions);
        femaleButton.setOnAction(e -> {
            this.gender = "Female";
            chartViewer.setChart(chartCreator.updateChart(currentChart,timeFlag,gender,income,context,age));
        });

        // Income buttons
        ToggleGroup incomeOptions = new ToggleGroup();

        RadioButton anyIncomeButton = new RadioButton("Any");
        anyIncomeButton.setSelected(true);
        anyIncomeButton.setToggleGroup(incomeOptions);
        anyIncomeButton.setOnAction(e -> {
            this.income = "";
            chartViewer.setChart(chartCreator.updateChart(currentChart,timeFlag,gender,income,context,age));
        });
        RadioButton lowButton = new RadioButton("Low");
        lowButton.setToggleGroup(incomeOptions);
        lowButton.setOnAction(e -> {
            this.income = "Low";
            chartViewer.setChart(chartCreator.updateChart(currentChart,timeFlag,gender,income,context,age));
        });
        RadioButton mediumButton = new RadioButton("Medium");
        mediumButton.setToggleGroup(incomeOptions);
        mediumButton.setOnAction(e -> {
            this.income = "Medium";
            chartViewer.setChart(chartCreator.updateChart(currentChart,timeFlag,gender,income,context,age));
        });
        RadioButton highButton = new RadioButton("High");
        highButton.setToggleGroup(incomeOptions);
        highButton.setOnAction(e -> {
            this.income = "High";
            chartViewer.setChart(chartCreator.updateChart(currentChart,timeFlag,gender,income,context,age));
        });

        // Context checkboxes
        CheckBox newsButton = new CheckBox("News");
        CheckBox shoppingButton = new CheckBox("Shopping");
        CheckBox socialButton = new CheckBox("Social Media");
        CheckBox blogButton = new CheckBox("Blog");
        CheckBox hobbyButton = new CheckBox("Hobbies");
        CheckBox travelButton = new CheckBox("Travel");

        newsButton.setOnAction(e -> {
            if (newsButton.isSelected()) {
                this.context.add("News");
            } else {
                this.context.remove("News");
            } chartViewer.setChart(chartCreator.updateChart(currentChart,timeFlag,gender,income,context,age));
        });
        shoppingButton.setOnAction(e -> {
            if (shoppingButton.isSelected()) {
                this.context.add("Shopping");
            } else {
                this.context.remove("Shopping");
            } chartViewer.setChart(chartCreator.updateChart(currentChart,timeFlag,gender,income,context,age));
        });
        socialButton.setOnAction(e -> {
            if (socialButton.isSelected()) {
                this.context.add("Social Media");
            } else {
                this.context.remove("Social Media");
            } chartViewer.setChart(chartCreator.updateChart(currentChart,timeFlag,gender,income,context,age));
        });
        blogButton.setOnAction(e -> {
            if (blogButton.isSelected()) {
                this.context.add("Blog");
            } else {
                this.context.remove("Blog");
            } chartViewer.setChart(chartCreator.updateChart(currentChart,timeFlag,gender,income,context,age));
        });
        hobbyButton.setOnAction(e -> {
            if (hobbyButton.isSelected()) {
                this.context.add("Hobby");
            } else {
                this.context.remove("Hobby");
            } chartViewer.setChart(chartCreator.updateChart(currentChart,timeFlag,gender,income,context,age));
        });
        travelButton.setOnAction(e -> {
            if (travelButton.isSelected()) {
                this.context.add("Travel");
            } else {
                this.context.remove("Travel");
            } chartViewer.setChart(chartCreator.updateChart(currentChart,timeFlag,gender,income,context,age));
        });


        // Age checkboxes
        CheckBox age1Button = new CheckBox("<25");
        CheckBox age2Button = new CheckBox("25-34");
        CheckBox age3Button = new CheckBox("35-44");
        CheckBox age4Button = new CheckBox("45-54");
        CheckBox age5Button = new CheckBox(">54");



        age1Button.setOnAction(e -> {
            if (age1Button.isSelected()) {
                this.age.add("<25");
            } else {
                this.age.remove("<25");
            } chartViewer.setChart(chartCreator.updateChart(currentChart,timeFlag,gender,income,context,age));
        });
        age2Button.setOnAction(e -> {
            if (age2Button.isSelected()) {
                this.age.add("25-34");
            } else {
                this.age.remove("25-34");
            } chartViewer.setChart(chartCreator.updateChart(currentChart,timeFlag,gender,income,context,age));
        });
        age3Button.setOnAction(e -> {
            if (age3Button.isSelected()) {
                this.age.add("35-44");
            } else {
                this.age.remove("35-44");
            } chartViewer.setChart(chartCreator.updateChart(currentChart,timeFlag,gender,income,context,age));
        });
        age4Button.setOnAction(e -> {
            if (age4Button.isSelected()) {
                this.age.add("45-54");
            } else {
                this.age.remove("45-54");
            } chartViewer.setChart(chartCreator.updateChart(currentChart,timeFlag,gender,income,context,age));
        });
        age5Button.setOnAction(e -> {
            if (age5Button.isSelected()) {
                this.age.add(">54");
            } else {
                this.age.remove(">54");
            } chartViewer.setChart(chartCreator.updateChart(currentChart,timeFlag,gender,income,context,age));
        });

        //Save graph to PDF button
        var pdfButton = new Button("Save to pdf");

        pdfButton.setOnAction(e -> {
            try {
                // Convert JavaFX Chart to BufferedImage
                WritableImage writableImage = chartViewer.snapshot(null, null);
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);

                // Save image as PDF
                chartCreator.saveChartAsPdf(bufferedImage);

            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (DocumentException ex) {
                throw new RuntimeException(ex);
            }
        });

        //Labels for filters
        var metricsHeader = new Label("Metrics");
        var timeLabel = new Label("Time Granularity");
        var genderLabel = new Label("Gender");
        var incomeLabel = new Label("Income");
        var contextLabel = new Label("Context");
        var ageLabel = new Label("Age");

        //Holder for metric and filter options
        var metricOptionHolder = new VBox();
        metricOptionHolder.getChildren().addAll(metricsHeader, clickMetric, impressionMetric, uniqueMetric, bouncesMetric, conversionMetric, costMetric, ctrMetric, cpaMetric, cpcMetric, cpmMetric, bounceRateMetric);
        metricOptionHolder.getChildren().addAll(genderLabel,bothGenderButton,maleButton,femaleButton);
        metricOptionHolder.getChildren().addAll(incomeLabel,anyIncomeButton,lowButton,mediumButton,highButton);
        metricOptionHolder.getChildren().addAll(contextLabel,newsButton,shoppingButton,socialButton,blogButton,hobbyButton,travelButton);
        metricOptionHolder.getChildren().addAll(ageLabel,age1Button,age2Button,age3Button,age4Button,age5Button);
        metricOptionHolder.getChildren().add(pdfButton);

        //Holder for time granularity options
        var timeOptionHolder = new HBox();
        timeOptionHolder.setAlignment(Pos.CENTER);
        timeOptionHolder.getChildren().addAll(timeLabel, dailyButton, weeklyButton,monthlyButton);


        //Navigation Buttons
        var buttonHolder = new HBox();
        var backButton = new Button("Back");
        var logOutButton = new Button("Logout");
        var overallMetricsButton = new Button("Overall Metrics");
        var multiChartButton = new Button("MultiChart");
        buttonHolder.getChildren().addAll(backButton, overallMetricsButton,multiChartButton, logOutButton);

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

        multiChartButton.setOnAction(e -> {
            ArrayList<String> currentCharts = new ArrayList<>(List.of("Clicks","Clicks"));
            ArrayList<String> timeFlags = new ArrayList<>(List.of("Daily","Daily"));
            ArrayList<String> genders = new ArrayList<>(List.of("",""));
            ArrayList<String> incomes = new ArrayList<>(List.of("",""));

            ArrayList<String> contexts1 = new ArrayList<>();
            ArrayList<String> contexts2 = new ArrayList<>();
            ArrayList<ArrayList<String>> contexts = new ArrayList<>();
            contexts.add(contexts1);
            contexts.add(contexts2);

            ArrayList<String> ages1 = new ArrayList<>();
            ArrayList<String> ages2 = new ArrayList<>();
            ArrayList<ArrayList<String>> ages = new ArrayList<>();
            ages.add(ages1);
            ages.add(ages2);

            MultiChartPage multiChartPage = new MultiChartPage(stage,logManager,currentCharts,timeFlags,genders,incomes,contexts,ages);
            multiChartPage.show();

        });

        //Layout
        BorderPane root = new BorderPane();
        root.setTop(buttonHolder);
        root.setCenter(chartViewer);
        root.setRight(metricOptionHolder);
        root.setBottom(timeOptionHolder);
        BorderPane.setMargin(metricOptionHolder, new Insets(0, 30, 0, 0));
        BorderPane.setMargin(timeOptionHolder, new Insets(0,0,50,0));


        Scene scene = new Scene(root, 1300, 800);
        stage.setScene(scene);
        stage.setTitle("Graphs");
        stage.show();
    }
    // Method to save chart as PDF
}

