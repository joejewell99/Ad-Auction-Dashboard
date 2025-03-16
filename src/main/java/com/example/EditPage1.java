package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;

public class EditPage1 extends EditPage {

    private ArrayList<String> timeFlag;
    private ArrayList<String> currentChart;
    private ArrayList<String> gender;
    private ArrayList<ArrayList<String>> age;
    private ArrayList<String> income;
    private ArrayList<ArrayList<String>> context;

    private LogManager logManager;
    private Stage stage;
    
    // Define style constants
    private final String BACKGROUND_COLOR = "#f5f5f7";
    private final String PRIMARY_COLOR = "#4285F4";
    private final String PRIMARY_DARK_COLOR = "#3367d6";
    private final String SECTION_BACKGROUND = "white";
    private final String HEADER_COLOR = "#333333";
    private final String TEXT_COLOR = "#555555";
    
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
        // Create main layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        
        // Create top navigation bar
        HBox navBar = createNavigationBar();
        
        // Create central content area
        GridPane contentPane = createContentPane();
        
        // Set layout
        root.setTop(navBar);
        root.setCenter(contentPane);
        
        // Set margins
        BorderPane.setMargin(contentPane, new Insets(20, 20, 20, 20));
        
        Scene scene = new Scene(root, 1300, 800);
        stage.setScene(scene);
        stage.setTitle("Ad Auction Dashboard - Edit Chart 1");
        stage.show();
    }
    
    // Create top navigation bar
    private HBox createNavigationBar() {
        HBox navBar = new HBox(15);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(15, 20, 15, 20));
        navBar.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        Label title = new Label("Edit Chart 1");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web(HEADER_COLOR));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        
        Button backButton = createStyledButton("Back to Charts", "#757575", "#616161");
        Button overallMetricsButton = createStyledButton("Overall Metrics", PRIMARY_COLOR, PRIMARY_DARK_COLOR);
        Button logOutButton = createStyledButton("Logout", "#757575", "#616161");
        
        // Set button events
        backButton.setOnAction(e -> {
            MultiChartPage multiChartPage = new MultiChartPage(stage, logManager, currentChart, timeFlag, gender, income, context, age);
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
        
        // Add to navigation bar
        navBar.getChildren().addAll(title, spacer, backButton, overallMetricsButton, logOutButton);
        
        return navBar;
    }
    
    // Create central content area
    private GridPane createContentPane() {
        GridPane contentPane = new GridPane();
        contentPane.setHgap(20);
        contentPane.setVgap(20);
        contentPane.setAlignment(Pos.CENTER);
        
        // Create filter groups
        VBox metricsSection = createMetricsSection();
        VBox timeSection = createTimeSection();
        VBox genderSection = createGenderSection();
        VBox incomeSection = createIncomeSection();
        VBox contextSection = createContextSection();
        VBox ageSection = createAgeSection();
        
        // Add groups to grid
        contentPane.add(metricsSection, 0, 0);
        contentPane.add(timeSection, 1, 0);
        contentPane.add(genderSection, 2, 0);
        contentPane.add(incomeSection, 3, 0);
        contentPane.add(contextSection, 4, 0);
        contentPane.add(ageSection, 5, 0);
        
        return contentPane;
    }
    
    // Create metrics group
    private VBox createMetricsSection() {
        return createSectionBox("Metrics", createMetricsContent());
    }
    
    // Create time group
    private VBox createTimeSection() {
        return createSectionBox("Time Granularity", createTimeContent());
    }
    
    // Create gender group
    private VBox createGenderSection() {
        return createSectionBox("Gender", createGenderContent());
    }
    
    // Create income group
    private VBox createIncomeSection() {
        return createSectionBox("Income", createIncomeContent());
    }
    
    // Create context group
    private VBox createContextSection() {
        return createSectionBox("Context", createContextContent());
    }
    
    // Create age group
    private VBox createAgeSection() {
        return createSectionBox("Age", createAgeContent());
    }
    
    // Create a generic group box
    private VBox createSectionBox(String title, VBox content) {
        VBox sectionBox = new VBox(15);
        sectionBox.setPadding(new Insets(20));
        sectionBox.setMinWidth(200);
        sectionBox.setMaxWidth(200);
        sectionBox.setMinHeight(400);
        sectionBox.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                          "-fx-background-radius: 10; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLabel.setTextFill(Color.web(HEADER_COLOR));
        
        Separator separator = new Separator();
        separator.setStyle("-fx-opacity: 0.3;");
        
        sectionBox.getChildren().addAll(titleLabel, separator, content);
        
        return sectionBox;
    }
    
    // Create metrics content
    private VBox createMetricsContent() {
        VBox content = new VBox(10);
        ToggleGroup metricOptions = new ToggleGroup();
        
        // Create radio buttons for different metrics
        RadioButton clickMetric = createStyledRadioButton("Total Clicks", metricOptions);
        clickMetric.setSelected(true);
        clickMetric.setOnAction(e -> {
            this.currentChart.set(0, "Clicks");
        });
        
        RadioButton impressionMetric = createStyledRadioButton("Total Impressions", metricOptions);
        impressionMetric.setOnAction(e -> {
            this.currentChart.set(0, "Impressions");
        });
        
        RadioButton uniqueMetric = createStyledRadioButton("Unique Visitors", metricOptions);
        uniqueMetric.setOnAction(e -> {
            this.currentChart.set(0, "Uniques");
        });
        
        RadioButton bouncesMetric = createStyledRadioButton("Bounces", metricOptions);
        bouncesMetric.setOnAction(e -> {
            this.currentChart.set(0, "Bounces");
        });
        
        RadioButton conversionMetric = createStyledRadioButton("Conversions", metricOptions);
        conversionMetric.setOnAction(e -> {
            this.currentChart.set(0, "Conversions");
        });
        
        RadioButton costMetric = createStyledRadioButton("Total Cost", metricOptions);
        costMetric.setOnAction(e -> {
            this.currentChart.set(0, "Cost");
        });
        
        RadioButton ctrMetric = createStyledRadioButton("Click-Through Rate (CTR)", metricOptions);
        ctrMetric.setOnAction(e -> {
            this.currentChart.set(0, "CTR");
        });
        
        RadioButton cpaMetric = createStyledRadioButton("Cost Per Acquisition (CPA)", metricOptions);
        cpaMetric.setOnAction(e -> {
            this.currentChart.set(0, "CPA");
        });
        
        RadioButton cpcMetric = createStyledRadioButton("Cost Per Click (CPC)", metricOptions);
        cpcMetric.setOnAction(e -> {
            this.currentChart.set(0, "CPC");
        });
        
        RadioButton cpmMetric = createStyledRadioButton("Cost Per Mille (CPM)", metricOptions);
        cpmMetric.setOnAction(e -> {
            this.currentChart.set(0, "CPM");
        });
        
        RadioButton bounceRateMetric = createStyledRadioButton("Bounce Rate", metricOptions);
        bounceRateMetric.setOnAction(e -> {
            this.currentChart.set(0, "BounceRate");
        });
        
        content.getChildren().addAll(
            clickMetric, impressionMetric, uniqueMetric, bouncesMetric, 
            conversionMetric, costMetric, ctrMetric, cpaMetric, 
            cpcMetric, cpmMetric, bounceRateMetric
        );
        
        return content;
    }
    
    // Create time content
    private VBox createTimeContent() {
        VBox content = new VBox(10);
        ToggleGroup timeOptions = new ToggleGroup();
        
        RadioButton dailyButton = createStyledRadioButton("Daily", timeOptions);
        dailyButton.setSelected(true);
        dailyButton.setOnAction(e -> {
            this.timeFlag.set(0, "Daily");
        });
        
        RadioButton weeklyButton = createStyledRadioButton("Weekly", timeOptions);
        weeklyButton.setOnAction(e -> {
            this.timeFlag.set(0, "Weekly");
        });
        
        RadioButton monthlyButton = createStyledRadioButton("Monthly", timeOptions);
        monthlyButton.setOnAction(e -> {
            this.timeFlag.set(0, "Monthly");
        });
        
        content.getChildren().addAll(dailyButton, weeklyButton, monthlyButton);
        
        return content;
    }
    
    // Create gender content
    private VBox createGenderContent() {
        VBox content = new VBox(10);
        ToggleGroup genderOptions = new ToggleGroup();
        
        RadioButton bothGenderButton = createStyledRadioButton("All", genderOptions);
        bothGenderButton.setSelected(true);
        bothGenderButton.setOnAction(e -> {
            this.gender.set(0, "");
        });
        
        RadioButton maleButton = createStyledRadioButton("Male", genderOptions);
        maleButton.setOnAction(e -> {
            this.gender.set(0, "Male");
        });
        
        RadioButton femaleButton = createStyledRadioButton("Female", genderOptions);
        femaleButton.setOnAction(e -> {
            this.gender.set(0, "Female");
        });
        
        content.getChildren().addAll(bothGenderButton, maleButton, femaleButton);
        
        return content;
    }
    
    // Create income content
    private VBox createIncomeContent() {
        VBox content = new VBox(10);
        ToggleGroup incomeOptions = new ToggleGroup();
        
        RadioButton anyIncomeButton = createStyledRadioButton("All", incomeOptions);
        anyIncomeButton.setSelected(true);
        anyIncomeButton.setOnAction(e -> {
            this.income.set(0, "");
        });
        
        RadioButton lowButton = createStyledRadioButton("Low", incomeOptions);
        lowButton.setOnAction(e -> {
            this.income.set(0, "Low");
        });
        
        RadioButton mediumButton = createStyledRadioButton("Medium", incomeOptions);
        mediumButton.setOnAction(e -> {
            this.income.set(0, "Medium");
        });
        
        RadioButton highButton = createStyledRadioButton("High", incomeOptions);
        highButton.setOnAction(e -> {
            this.income.set(0, "High");
        });
        
        content.getChildren().addAll(anyIncomeButton, lowButton, mediumButton, highButton);
        
        return content;
    }
    
    // Create context content
    private VBox createContextContent() {
        VBox content = new VBox(10);
        
        CheckBox newsButton = createStyledCheckBox("News");
        newsButton.setOnAction(e -> {
            if (newsButton.isSelected()) {
                this.context.get(0).add("News");
            } else {
                this.context.get(0).remove("News");
            }
        });
        
        CheckBox shoppingButton = createStyledCheckBox("Shopping");
        shoppingButton.setOnAction(e -> {
            if (shoppingButton.isSelected()) {
                this.context.get(0).add("Shopping");
            } else {
                this.context.get(0).remove("Shopping");
            }
        });
        
        CheckBox socialButton = createStyledCheckBox("Social Media");
        socialButton.setOnAction(e -> {
            if (socialButton.isSelected()) {
                this.context.get(0).add("Social Media");
            } else {
                this.context.get(0).remove("Social Media");
            }
        });
        
        CheckBox blogButton = createStyledCheckBox("Blog");
        blogButton.setOnAction(e -> {
            if (blogButton.isSelected()) {
                this.context.get(0).add("Blog");
            } else {
                this.context.get(0).remove("Blog");
            }
        });
        
        CheckBox hobbyButton = createStyledCheckBox("Hobby");
        hobbyButton.setOnAction(e -> {
            if (hobbyButton.isSelected()) {
                this.context.get(0).add("Hobby");
            } else {
                this.context.get(0).remove("Hobby");
            }
        });
        
        CheckBox travelButton = createStyledCheckBox("Travel");
        travelButton.setOnAction(e -> {
            if (travelButton.isSelected()) {
                this.context.get(0).add("Travel");
            } else {
                this.context.get(0).remove("Travel");
            }
        });
        
        content.getChildren().addAll(newsButton, shoppingButton, socialButton, blogButton, hobbyButton, travelButton);
        
        return content;
    }
    
    // Create age content
    private VBox createAgeContent() {
        VBox content = new VBox(10);
        
        CheckBox age1Button = createStyledCheckBox("Under 25");
        age1Button.setOnAction(e -> {
            if (age1Button.isSelected()) {
                this.age.get(0).add("<25");
            } else {
                this.age.get(0).remove("<25");
            }
        });
        
        CheckBox age2Button = createStyledCheckBox("25-34");
        age2Button.setOnAction(e -> {
            if (age2Button.isSelected()) {
                this.age.get(0).add("25-34");
            } else {
                this.age.get(0).remove("25-34");
            }
        });
        
        CheckBox age3Button = createStyledCheckBox("35-44");
        age3Button.setOnAction(e -> {
            if (age3Button.isSelected()) {
                this.age.get(0).add("35-44");
            } else {
                this.age.get(0).remove("35-44");
            }
        });
        
        CheckBox age4Button = createStyledCheckBox("45-54");
        age4Button.setOnAction(e -> {
            if (age4Button.isSelected()) {
                this.age.get(0).add("45-54");
            } else {
                this.age.get(0).remove("45-54");
            }
        });
        
        CheckBox age5Button = createStyledCheckBox("Over 54");
        age5Button.setOnAction(e -> {
            if (age5Button.isSelected()) {
                this.age.get(0).add(">54");
            } else {
                this.age.get(0).remove(">54");
            }
        });
        
        content.getChildren().addAll(age1Button, age2Button, age3Button, age4Button, age5Button);
        
        return content;
    }
    
    // Create styled radio button
    private RadioButton createStyledRadioButton(String text, ToggleGroup group) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(group);
        radioButton.setFont(Font.font("Arial", 13));
        radioButton.setTextFill(Color.web(TEXT_COLOR));
        radioButton.setPadding(new Insets(3, 0, 3, 0));
        return radioButton;
    }
    
    // Create styled checkbox
    private CheckBox createStyledCheckBox(String text) {
        CheckBox checkBox = new CheckBox(text);
        checkBox.setFont(Font.font("Arial", 13));
        checkBox.setTextFill(Color.web(TEXT_COLOR));
        checkBox.setPadding(new Insets(3, 0, 3, 0));
        return checkBox;
    }
    
    // Create styled button
    private Button createStyledButton(String text, String bgColor, String hoverColor) {
        Button button = new Button(text);
        button.setPrefSize(120, 35);
        button.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        
        String style = String.format(
                "-fx-background-color: %s; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: normal; " +
                "-fx-background-radius: 5; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 2, 0, 0, 1); " +
                "-fx-cursor: hand;", bgColor);
        
        String hoverStyle = String.format(
                "-fx-background-color: %s; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: normal; " +
                "-fx-background-radius: 5; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 0, 2); " +
                "-fx-cursor: hand;", hoverColor);
        
        button.setStyle(style);
        
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(style));
        
        return button;
    }
}
