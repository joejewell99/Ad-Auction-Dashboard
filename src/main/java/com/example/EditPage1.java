package com.example;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class EditPage1 extends EditPage{

    private ArrayList<String> timeFlag;
    private ArrayList<String> currentChart;
    private ArrayList<String> gender;
    private ArrayList<ArrayList<String>> age;
    private ArrayList<String> income;
    private ArrayList<ArrayList<String>> context;

    private LogManager logManager;
    private Stage stage;
    public EditPage1(Stage stage, LogManager logManager, ArrayList<String> currentChart,
                     ArrayList<String> timeFlag, ArrayList<String> gender, ArrayList<String> income,
                     ArrayList<ArrayList<String>> context, ArrayList<ArrayList<String>> age) {
        this.currentChart = currentChart;
        this.timeFlag = timeFlag;
        this.gender = gender;
        this.income = income;
        this.context = context;
        this.age = age;
        this.logManager = logManager;
        this.stage = stage;
    }

    @Override
    public void show() {

        //Metric options
        ToggleGroup metricOptions = new ToggleGroup();

        RadioButton clickMetric = new RadioButton("Total Clicks");
        clickMetric.setToggleGroup(metricOptions);
        clickMetric.setSelected(true);
        clickMetric.setOnAction(e -> {
            this.currentChart.set(0,"Clicks");
        });
        RadioButton impressionMetric = new RadioButton("Total Impressions");
        impressionMetric.setToggleGroup(metricOptions);
        impressionMetric.setOnAction(e -> {
            this.currentChart.set(0,"Impressions");
        });
        RadioButton uniqueMetric = new RadioButton("Total uniques");
        uniqueMetric.setToggleGroup(metricOptions);
        uniqueMetric.setOnAction(e -> {
            this.currentChart.set(0,"Uniques");
        });
        RadioButton bouncesMetric = new RadioButton("Total bounces");
        bouncesMetric.setToggleGroup(metricOptions);
        bouncesMetric.setOnAction(e -> {
            this.currentChart.set(0,"Bounces");
        });
        RadioButton conversionMetric = new RadioButton("Total conversions");
        conversionMetric.setToggleGroup(metricOptions);
        conversionMetric.setOnAction(e -> {
            this.currentChart.set(0,"Conversions");
        });
        RadioButton costMetric = new RadioButton("Total cost");
        costMetric.setToggleGroup(metricOptions);
        costMetric.setOnAction(e -> {
            this.currentChart.set(0,"Cost");
        });
        RadioButton ctrMetric = new RadioButton("CTR");
        ctrMetric.setToggleGroup(metricOptions);
        ctrMetric.setOnAction(e -> {
            this.currentChart.set(0,"CTR");
        });
        RadioButton cpaMetric = new RadioButton("CPA");
        cpaMetric.setToggleGroup(metricOptions);
        cpaMetric.setOnAction(e -> {
            this.currentChart.set(0,"CPA");
        });
        RadioButton cpcMetric = new RadioButton("CPC");
        cpcMetric.setToggleGroup(metricOptions);
        cpcMetric.setOnAction(e -> {
            this.currentChart.set(0,"CPC");
        });
        RadioButton cpmMetric = new RadioButton("CPM");
        cpmMetric.setToggleGroup(metricOptions);
        cpmMetric.setOnAction(e -> {
            this.currentChart.set(0,"CPM");
        });
        RadioButton bounceRateMetric = new RadioButton("Bounce Rate");
        bounceRateMetric.setToggleGroup(metricOptions);
        bounceRateMetric.setOnAction(e -> {
            this.currentChart.set(0,"BounceRate");
        });
        var metricHolder = new VBox();
        var metricLabel = new Label("Metrics");
        metricHolder.getChildren().addAll(metricLabel,clickMetric,impressionMetric,uniqueMetric,bouncesMetric,conversionMetric,costMetric,ctrMetric,cpaMetric,cpcMetric,cpmMetric,bounceRateMetric);

        //Time granularity buttons
        ToggleGroup timeOptions = new ToggleGroup();

        RadioButton dailyButton = new RadioButton("Daily");
        dailyButton.setSelected(true);
        dailyButton.setToggleGroup(timeOptions);
        dailyButton.setOnAction(e -> {
            this.timeFlag.set(0,"Daily");
        });
        RadioButton weeklyButton = new RadioButton("Weekly");
        weeklyButton.setToggleGroup(timeOptions);
        weeklyButton.setOnAction(e -> {
            this.timeFlag.set(0,"Weekly");
        });
        RadioButton monthlyButton = new RadioButton("Monthly");
        monthlyButton.setToggleGroup(timeOptions);
        monthlyButton.setOnAction(e -> {
            this.timeFlag.set(0,"Monthly");
        });
        var timeHolder = new VBox();
        var timeTabel = new Label("Time");
        timeHolder.getChildren().addAll(timeTabel,dailyButton,weeklyButton,monthlyButton);

        //Gender Buttons
        ToggleGroup genderOptions = new ToggleGroup();

        RadioButton bothGenderButton = new RadioButton("Both");
        bothGenderButton.setSelected(true);
        bothGenderButton.setToggleGroup(genderOptions);
        bothGenderButton.setOnAction(e -> {
            this.gender.set(0,"");
        });
        RadioButton maleButton = new RadioButton("Male");
        maleButton.setToggleGroup(genderOptions);
        maleButton.setOnAction(e -> {
            this.gender.set(0,"Male");
        });
        RadioButton femaleButton = new RadioButton("Female");
        femaleButton.setToggleGroup(genderOptions);
        femaleButton.setOnAction(e -> {
            this.gender.set(0,"Female");
        });
        var genderHolder = new VBox();
        var genderLabel = new Label("Gender");
        genderHolder.getChildren().addAll(genderLabel,bothGenderButton,maleButton,femaleButton);

        // Income buttons
        ToggleGroup incomeOptions = new ToggleGroup();

        RadioButton anyIncomeButton = new RadioButton("Any");
        anyIncomeButton.setSelected(true);
        anyIncomeButton.setToggleGroup(incomeOptions);
        anyIncomeButton.setOnAction(e -> {
            this.income.set(0,"");
        });
        RadioButton lowButton = new RadioButton("Low");
        lowButton.setToggleGroup(incomeOptions);
        lowButton.setOnAction(e -> {
            this.income.set(0,"Low");
        });
        RadioButton mediumButton = new RadioButton("Medium");
        mediumButton.setToggleGroup(incomeOptions);
        mediumButton.setOnAction(e -> {
            this.income.set(0,"Medium");
        });
        RadioButton highButton = new RadioButton("High");
        highButton.setToggleGroup(incomeOptions);
        highButton.setOnAction(e -> {
            this.income.set(0,"High");
        });
        var incomeHolder = new VBox();
        var incomeLabel = new Label("Income");
        incomeHolder.getChildren().addAll(incomeLabel,anyIncomeButton,lowButton,mediumButton,highButton);

        // Context checkboxes
        CheckBox newsButton = new CheckBox("News");
        CheckBox shoppingButton = new CheckBox("Shopping");
        CheckBox socialButton = new CheckBox("Social Media");
        CheckBox blogButton = new CheckBox("Blog");
        CheckBox hobbyButton = new CheckBox("Hobbies");
        CheckBox travelButton = new CheckBox("Travel");

        newsButton.setOnAction(e -> {
            if (newsButton.isSelected()) {
                this.context.get(0).add("News");
            } else {
                this.context.get(0).remove("News");
            }
        });
        shoppingButton.setOnAction(e -> {
            if (shoppingButton.isSelected()) {
                this.context.get(0).add("Shopping");
            } else {
                this.context.get(0).remove("Shopping");
            }
        });
        socialButton.setOnAction(e -> {
            if (socialButton.isSelected()) {
                this.context.get(0).add("Social Media");
            } else {
                this.context.get(0).remove("Social Media");
            }
        });
        blogButton.setOnAction(e -> {
            if (blogButton.isSelected()) {
                this.context.get(0).add("Blog");
            } else {
                this.context.get(0).remove("Blog");
            }
        });
        hobbyButton.setOnAction(e -> {
            if (hobbyButton.isSelected()) {
                this.context.get(0).add("Hobby");
            } else {
                this.context.get(0).remove("Hobby");
            }
        });
        travelButton.setOnAction(e -> {
            if (travelButton.isSelected()) {
                this.context.get(0).add("Travel");
            } else {
                this.context.get(0).remove("Travel");
            }
        });
        var contextHolder = new VBox();
        var contextLabel = new Label("Context");
        contextHolder.getChildren().addAll(contextLabel,newsButton,socialButton,shoppingButton,blogButton,hobbyButton,travelButton);


        // Age checkboxes
        CheckBox age1Button = new CheckBox("<25");
        CheckBox age2Button = new CheckBox("25-34");
        CheckBox age3Button = new CheckBox("35-44");
        CheckBox age4Button = new CheckBox("45-54");
        CheckBox age5Button = new CheckBox(">54");

        age1Button.setOnAction(e -> {
            if (age1Button.isSelected()) {
                this.age.get(0).add("<25");
            } else {
                this.age.get(0).remove("<25");
            }
        });
        age2Button.setOnAction(e -> {
            if (age2Button.isSelected()) {
                this.age.get(0).add("25-34");
            } else {
                this.age.get(0).remove("25-34");
            }
        });
        age3Button.setOnAction(e -> {
            if (age3Button.isSelected()) {
                this.age.get(0).add("35-44");
            } else {
                this.age.get(0).remove("35-44");
            }
        });
        age4Button.setOnAction(e -> {
            if (age4Button.isSelected()) {
                this.age.get(0).add("45-54");
            } else {
                this.age.get(0).remove("45-54");
            }
        });
        age5Button.setOnAction(e -> {
            if (age5Button.isSelected()) {
                this.age.get(0).add(">54");
            } else {
                this.age.get(0).remove(">54");
            }
        });
        var ageHolder = new VBox();
        var ageLabel = new Label("Age");
        ageHolder.getChildren().addAll(ageLabel,age1Button,age2Button,age3Button,age4Button,age5Button);

        //Navigation Buttons
        var buttonHolder = new HBox();
        var backButton = new Button("Back");
        var logOutButton = new Button("Logout");
        var overallMetricsButton = new Button("Overall Metrics");
        buttonHolder.getChildren().addAll(backButton, overallMetricsButton, logOutButton);

        backButton.setOnAction(e -> {
            MultiChartPage multiChartPage = new MultiChartPage(stage,logManager,currentChart,timeFlag,gender,income,context,age);
            multiChartPage.show();
        });

        overallMetricsButton.setOnAction(e -> {
            OverallMetricsPage metricsPage = new OverallMetricsPage(stage, logManager);
            metricsPage.show();
        });

        logOutButton.setOnAction(e -> {
            Login login = new Login(stage);
            login.show();
        });


        var allOptionHolder = new HBox(20);
        allOptionHolder.getChildren().addAll(metricHolder,timeHolder,genderHolder,incomeHolder,contextHolder,ageHolder);
        BorderPane root = new BorderPane();
        root.setCenter(allOptionHolder);
        root.setTop(buttonHolder);
        Scene scene = new Scene(root,1300,800);
        stage.setScene(scene);
        stage.setTitle("Edit Chart 1");
        stage.show();


    }
}
