package com.example;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

import static com.example.App.logger;

public class EditPage2 extends EditPage{
    private ArrayList<String> timeFlag;
    private ArrayList<String> currentChart;
    private ArrayList<String> gender;
    private ArrayList<ArrayList<String>> age;
    private ArrayList<String> income;
    private ArrayList<ArrayList<String>> context;

    private LogManager logManager;
    private Stage stage;
    public EditPage2(Stage stage, LogManager logManager, ArrayList<String> currentChart,
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
            try {
                this.currentChart.set(1, "Clicks");
            } catch (Exception ex) {
                showAlert("Error setting metric to 'Clicks'.");
                logger.error("Error in Click Metric selection", ex);
            }
        });

        RadioButton impressionMetric = new RadioButton("Total Impressions");
        impressionMetric.setToggleGroup(metricOptions);
        impressionMetric.setOnAction(e -> {
            try {
                this.currentChart.set(1, "Impressions");
            } catch (Exception ex) {
                showAlert("Error setting metric to 'Impressions'.");
                logger.error("Error in Impression Metric selection", ex);
            }
        });

        RadioButton uniqueMetric = new RadioButton("Total uniques");
        uniqueMetric.setToggleGroup(metricOptions);
        uniqueMetric.setOnAction(e -> {
            try {
                this.currentChart.set(1, "Uniques");
            } catch (Exception ex) {
                showAlert("Error setting metric to 'Uniques'.");
                logger.error("Error in Uniques Metric selection", ex);
            }
        });

        RadioButton bouncesMetric = new RadioButton("Total bounces");
        bouncesMetric.setToggleGroup(metricOptions);
        bouncesMetric.setOnAction(e -> {
            try {
                this.currentChart.set(1, "Bounces");
            } catch (Exception ex) {
                showAlert("Error setting metric to 'Bounces'.");
                logger.error("Error in Bounces Metric selection", ex);
            }
        });

        RadioButton conversionMetric = new RadioButton("Total conversions");
        conversionMetric.setToggleGroup(metricOptions);
        conversionMetric.setOnAction(e -> {
            try {
                this.currentChart.set(1, "Conversions");
            } catch (Exception ex) {
                showAlert("Error setting metric to 'Conversions'.");
                logger.error("Error in Conversions Metric selection", ex);
            }
        });

        RadioButton costMetric = new RadioButton("Total cost");
        costMetric.setToggleGroup(metricOptions);
        costMetric.setOnAction(e -> {
            try {
                this.currentChart.set(1, "Cost");
            } catch (Exception ex) {
                showAlert("Error setting metric to 'Cost'.");
                logger.error("Error in Cost Metric selection", ex);
            }
        });

        RadioButton ctrMetric = new RadioButton("CTR");
        ctrMetric.setToggleGroup(metricOptions);
        ctrMetric.setOnAction(e -> {
            try {
                this.currentChart.set(1, "CTR");
            } catch (Exception ex) {
                showAlert("Error setting metric to 'CTR'.");
                logger.error("Error in CTR Metric selection", ex);
            }
        });

        RadioButton cpaMetric = new RadioButton("CPA");
        cpaMetric.setToggleGroup(metricOptions);
        cpaMetric.setOnAction(e -> {
            try {
                this.currentChart.set(1, "CPA");
            } catch (Exception ex) {
                showAlert("Error setting metric to 'CPA'.");
                logger.error("Error in CPA Metric selection", ex);
            }
        });

        RadioButton cpcMetric = new RadioButton("CPC");
        cpcMetric.setToggleGroup(metricOptions);
        cpcMetric.setOnAction(e -> {
            try {
                this.currentChart.set(1, "CPC");
            } catch (Exception ex) {
                showAlert("Error setting metric to 'CPC'.");
                logger.error("Error in CPC Metric selection", ex);
            }
        });

        RadioButton cpmMetric = new RadioButton("CPM");
        cpmMetric.setToggleGroup(metricOptions);
        cpmMetric.setOnAction(e -> {
            try {
                this.currentChart.set(1, "CPM");
            } catch (Exception ex) {
                showAlert("Error setting metric to 'CPM'.");
                logger.error("Error in CPM Metric selection", ex);
            }
        });

        RadioButton bounceRateMetric = new RadioButton("Bounce Rate");
        bounceRateMetric.setToggleGroup(metricOptions);
        bounceRateMetric.setOnAction(e -> {
            try {
                this.currentChart.set(1, "BounceRate");
            } catch (Exception ex) {
                showAlert("Error setting metric to 'Bounce Rate'.");
                logger.error("Error in Bounce Rate Metric selection", ex);
            }
        });

        var metricHolder = new VBox();
        var metricLabel = new Label("Metrics");
        metricHolder.getChildren().addAll(metricLabel, clickMetric, impressionMetric,
          uniqueMetric, bouncesMetric, conversionMetric, costMetric,
          ctrMetric, cpaMetric, cpcMetric, cpmMetric, bounceRateMetric);

        //Time granularity buttons
        ToggleGroup timeOptions = new ToggleGroup();

        RadioButton dailyButton = new RadioButton("Daily");
        dailyButton.setSelected(true);
        dailyButton.setToggleGroup(timeOptions);
        dailyButton.setOnAction(e -> {
            try {
                this.timeFlag.set(1, "Daily");
            } catch (Exception ex) {
                showAlert("Error setting time granularity to 'Daily'.");
                logger.error("Error in Daily button", ex);
            }
        });
        RadioButton weeklyButton = new RadioButton("Weekly");
        weeklyButton.setToggleGroup(timeOptions);
        weeklyButton.setOnAction(e -> {
            try {
                this.timeFlag.set(1, "Weekly");
            } catch (Exception ex) {
                showAlert("Error setting time granularity to 'Weekly'.");
                logger.error("Error in Weekly button", ex);
            }
        });
        RadioButton monthlyButton = new RadioButton("Monthly");
        monthlyButton.setToggleGroup(timeOptions);
        monthlyButton.setOnAction(e -> {
            try {
                this.timeFlag.set(1, "Monthly");
            } catch (Exception ex) {
                showAlert("Error setting time granularity to 'Monthly'.");
                logger.error("Error in Monthly button", ex);
            }
        });
        var timeHolder = new VBox();
        var timeTabel = new Label("Time");
        timeHolder.getChildren().addAll(timeTabel, dailyButton, weeklyButton, monthlyButton);

        //Gender Buttons
        ToggleGroup genderOptions = new ToggleGroup();

        RadioButton bothGenderButton = new RadioButton("Both");
        bothGenderButton.setSelected(true);
        bothGenderButton.setToggleGroup(genderOptions);
        bothGenderButton.setOnAction(e -> {
            try {
                this.gender.set(1, "");
            } catch (Exception ex) {
                showAlert("Error setting gender to 'Both'.");
                logger.error("Error in Both gender button", ex);
            }
        });
        RadioButton maleButton = new RadioButton("Male");
        maleButton.setToggleGroup(genderOptions);
        maleButton.setOnAction(e -> {
            try {
                this.gender.set(1, "Male");
            } catch (Exception ex) {
                showAlert("Error setting gender to 'Male'.");
                logger.error("Error in Male gender button", ex);
            }
        });
        RadioButton femaleButton = new RadioButton("Female");
        femaleButton.setToggleGroup(genderOptions);
        femaleButton.setOnAction(e -> {
            try {
                this.gender.set(1, "Female");
            } catch (Exception ex) {
                showAlert("Error setting gender to 'Female'.");
                logger.error("Error in Female gender button", ex);
            }
        });
        var genderHolder = new VBox();
        var genderLabel = new Label("Gender");
        genderHolder.getChildren().addAll(genderLabel, bothGenderButton, maleButton, femaleButton);

        // Income buttons
        ToggleGroup incomeOptions = new ToggleGroup();

        RadioButton anyIncomeButton = new RadioButton("Any");
        anyIncomeButton.setSelected(true);
        anyIncomeButton.setToggleGroup(incomeOptions);
        anyIncomeButton.setOnAction(e -> {
            try {
                this.income.set(1, "");
            } catch (Exception ex) {
                showAlert("Error setting income to 'Any'.");
                logger.error("Error in Any income button", ex);
            }
        });
        RadioButton lowButton = new RadioButton("Low");
        lowButton.setToggleGroup(incomeOptions);
        lowButton.setOnAction(e -> {
            try {
                this.income.set(1, "Low");
            } catch (Exception ex) {
                showAlert("Error setting income to 'Low'.");
                logger.error("Error in Low income button", ex);
            }
        });
        RadioButton mediumButton = new RadioButton("Medium");
        mediumButton.setToggleGroup(incomeOptions);
        mediumButton.setOnAction(e -> {
            try {
                this.income.set(1, "Medium");
            } catch (Exception ex) {
                showAlert("Error setting income to 'Medium'.");
                logger.error("Error in Medium income button", ex);
            }
        });
        RadioButton highButton = new RadioButton("High");
        highButton.setToggleGroup(incomeOptions);
        highButton.setOnAction(e -> {
            try {
                this.income.set(1, "High");
            } catch (Exception ex) {
                showAlert("Error setting income to 'High'.");
                logger.error("Error in High income button", ex);
            }
        });
        var incomeHolder = new VBox();
        var incomeLabel = new Label("Income");
        incomeHolder.getChildren().addAll(incomeLabel, anyIncomeButton, lowButton, mediumButton, highButton);

        // Context checkboxes
        CheckBox newsButton = new CheckBox("News");
        newsButton.setOnAction(e -> {
            try {
                if (newsButton.isSelected()) {
                    this.context.get(1).add("News");
                } else {
                    this.context.get(1).remove("News");
                }
            } catch (Exception ex) {
                showAlert("Error updating News context.");
                logger.error("Error in News checkbox", ex);
            }
        });
        CheckBox shoppingButton = new CheckBox("Shopping");
        shoppingButton.setOnAction(e -> {
            try {
                if (shoppingButton.isSelected()) {
                    this.context.get(1).add("Shopping");
                } else {
                    this.context.get(1).remove("Shopping");
                }
            } catch (Exception ex) {
                showAlert("Error updating Shopping context.");
                logger.error("Error in Shopping checkbox", ex);
            }
        });
        CheckBox socialButton = new CheckBox("Social Media");
        socialButton.setOnAction(e -> {
            try {
                if (socialButton.isSelected()) {
                    this.context.get(1).add("Social Media");
                } else {
                    this.context.get(1).remove("Social Media");
                }
            } catch (Exception ex) {
                showAlert("Error updating Social Media context.");
                logger.error("Error in Social Media checkbox", ex);
            }
        });
        CheckBox blogButton = new CheckBox("Blog");
        blogButton.setOnAction(e -> {
            try {
                if (blogButton.isSelected()) {
                    this.context.get(1).add("Blog");
                } else {
                    this.context.get(1).remove("Blog");
                }
            } catch (Exception ex) {
                showAlert("Error updating Blog context.");
                logger.error("Error in Blog checkbox", ex);
            }
        });
        CheckBox hobbyButton = new CheckBox("Hobbies");
        hobbyButton.setOnAction(e -> {
            try {
                if (hobbyButton.isSelected()) {
                    this.context.get(1).add("Hobbies");
                } else {
                    this.context.get(1).remove("Hobbies");
                }
            } catch (Exception ex) {
                showAlert("Error updating Hobbies context.");
                logger.error("Error in Hobbies checkbox", ex);
            }
        });
        CheckBox travelButton = new CheckBox("Travel");
        travelButton.setOnAction(e -> {
            try {
                if (travelButton.isSelected()) {
                    this.context.get(1).add("Travel");
                } else {
                    this.context.get(1).remove("Travel");
                }
            } catch (Exception ex) {
                showAlert("Error updating Travel context.");
                logger.error("Error in Travel checkbox", ex);
            }
        });
        var contextHolder = new VBox();
        var contextLabel = new Label("Context");
        contextHolder.getChildren().addAll(contextLabel, newsButton, socialButton, shoppingButton, blogButton, hobbyButton, travelButton);

        // Age checkboxes
        CheckBox age1Button = new CheckBox("<25");
        age1Button.setOnAction(e -> {
            try {
                if (age1Button.isSelected()) {
                    this.age.get(1).add("<25");
                } else {
                    this.age.get(1).remove("<25");
                }
            } catch (Exception ex) {
                showAlert("Error updating age filter <25.");
                logger.error("Error in <25 checkbox", ex);
            }
        });
        CheckBox age2Button = new CheckBox("25-34");
        age2Button.setOnAction(e -> {
            try {
                if (age2Button.isSelected()) {
                    this.age.get(1).add("25-34");
                } else {
                    this.age.get(1).remove("25-34");
                }
            } catch (Exception ex) {
                showAlert("Error updating age filter 25-34.");
                logger.error("Error in 25-34 checkbox", ex);
            }
        });
        CheckBox age3Button = new CheckBox("35-44");
        age3Button.setOnAction(e -> {
            try {
                if (age3Button.isSelected()) {
                    this.age.get(1).add("35-44");
                } else {
                    this.age.get(1).remove("35-44");
                }
            } catch (Exception ex) {
                showAlert("Error updating age filter 35-44.");
                logger.error("Error in 35-44 checkbox", ex);
            }
        });
        CheckBox age4Button = new CheckBox("45-54");
        age4Button.setOnAction(e -> {
            try {
                if (age4Button.isSelected()) {
                    this.age.get(1).add("45-54");
                } else {
                    this.age.get(1).remove("45-54");
                }
            } catch (Exception ex) {
                showAlert("Error updating age filter 45-54.");
                logger.error("Error in 45-54 checkbox", ex);
            }
        });
        CheckBox age5Button = new CheckBox(">54");
        age5Button.setOnAction(e -> {
            try {
                if (age5Button.isSelected()) {
                    this.age.get(1).add(">54");
                } else {
                    this.age.get(1).remove(">54");
                }
            } catch (Exception ex) {
                showAlert("Error updating age filter >54.");
                logger.error("Error in >54 checkbox", ex);
            }
        });
        var ageHolder = new VBox();
        var ageLabel = new Label("Age");
        ageHolder.getChildren().addAll(ageLabel, age1Button, age2Button, age3Button, age4Button, age5Button);

        //Navigation Buttons
        var buttonHolder = new HBox();
        var backButton = new Button("Back");
        var logOutButton = new Button("Logout");
        var overallMetricsButton = new Button("Overall Metrics");
        buttonHolder.getChildren().addAll(backButton, overallMetricsButton, logOutButton);

        backButton.setOnAction(e -> {
            try {
                MultiChartPage multiChartPage = new MultiChartPage(stage, logManager, currentChart, timeFlag, gender, income, context, age);
                multiChartPage.show();
            } catch (Exception ex) {
                showAlert("Error navigating back to MultiChart Page.");
                logger.error("Error in Back button", ex);
            }
        });

        overallMetricsButton.setOnAction(e -> {
            try {
                OverallMetricsPage metricsPage = new OverallMetricsPage(stage, logManager);
                metricsPage.show();
            } catch (Exception ex) {
                showAlert("Error navigating to Overall Metrics Page.");
                logger.error("Error in Overall Metrics button", ex);
            }
        });

        logOutButton.setOnAction(e -> {
            try {
                Login login = new Login(stage);
                login.show();
            } catch (Exception ex) {
                showAlert("Error logging out.");
                logger.error("Error in Logout button", ex);
            }
        });

        var allOptionHolder = new HBox(20);
        allOptionHolder.getChildren().addAll(metricHolder, timeHolder, genderHolder, incomeHolder, contextHolder, ageHolder);
        BorderPane root = new BorderPane();
        root.setCenter(allOptionHolder);
        root.setTop(buttonHolder);
        Scene scene = new Scene(root, 1300, 800);
        stage.setScene(scene);
        stage.setTitle("Edit Chart 2");
        stage.show();
    }

    // Helper method to show an alert
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
