package com.example;

import com.itextpdf.awt.geom.Dimension;
import com.itextpdf.text.DocumentException;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.App.logger;

public class ChartPage {
    private Stage stage;
    private LogManager logManager;
    private ChartCreator chartCreator;
    private String timeFlag;
    private String currentChart;
    ChartViewer chartViewer = null;

    private String gender;
    private ArrayList<String> age;
    private String income;
    private ArrayList<String> context;

    // Define style constants
    private final String BACKGROUND_COLOR = "#f5f5f7";
    private final String PRIMARY_COLOR = "#4285F4";
    private final String PRIMARY_DARK_COLOR = "#3367d6";
    private final String SECTION_BACKGROUND = "white";
    private final String HEADER_COLOR = "#333333";
    private final String TEXT_COLOR = "#555555";

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
        // Create main layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        // Chart display area
        try {
            // Create chart using current settings
            chartViewer = new ChartViewer(chartCreator.updateChart(currentChart, timeFlag, gender, income, context, age));
            // Check for empty dataset and set no-data message if needed
            if (chartViewer.getChart() != null && chartViewer.getChart().getCategoryPlot() != null) {
                if (chartViewer.getChart().getCategoryPlot().getDataset() == null ||
                  chartViewer.getChart().getCategoryPlot().getDataset().getRowCount() == 0 ||
                  chartViewer.getChart().getCategoryPlot().getDataset().getColumnCount() == 0) {
                    chartViewer.getChart().getCategoryPlot().setNoDataMessage("No data available");
                }
            }
        } catch (Exception ex) {
            showAlert("Error loading chart.");
            logger.error("Error loading chart", ex);
            // Fallback: create an empty ChartViewer
            chartViewer = new ChartViewer();
        }
        chartViewer.setMaxSize(800, 600);

        // Create a container for the chart, add styling
        BorderPane chartContainer = new BorderPane();
        chartContainer.setCenter(chartViewer);
        chartContainer.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
          "-fx-background-radius: 10; " +
          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        chartContainer.setPadding(new Insets(15));

        // ===== Top navigation bar =====
        HBox navBar = null;
        try {
            navBar = createNavigationBar();
        } catch (Exception ex) {
            showAlert("Error creating navigation bar.");
            logger.error("Error in createNavigationBar", ex);
            navBar = new HBox();
        }

        // ===== Right side metrics and filter panel =====
        ScrollPane filterPanel = null;
        try {
            filterPanel = createFilterPanel(chartViewer);
        } catch (Exception ex) {
            showAlert("Error creating filter panel.");
            logger.error("Error in createFilterPanel", ex);
            filterPanel = new ScrollPane();
        }

        // ===== Bottom time granularity selector =====
        HBox timeGranularityBar = null;
        try {
            timeGranularityBar = createTimeGranularityBar(chartViewer);
        } catch (Exception ex) {
            showAlert("Error creating time granularity selector.");
            logger.error("Error in createTimeGranularityBar", ex);
            timeGranularityBar = new HBox();
        }

        // Set layout positions
        root.setTop(navBar);
        root.setCenter(chartContainer);
        root.setRight(filterPanel);
        root.setBottom(timeGranularityBar);

        // Set margins
        BorderPane.setMargin(chartContainer, new Insets(10, 10, 10, 10));
        BorderPane.setMargin(filterPanel, new Insets(10, 10, 10, 5));
        BorderPane.setMargin(timeGranularityBar, new Insets(5, 10, 15, 10));

        // Create scene
        Scene scene = new Scene(root, 1300, 800);
        stage.setScene(scene);
        stage.setTitle("Ad Auction Dashboard - Data Charts");
        stage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    // Create top navigation bar
    private HBox createNavigationBar() {
        HBox navBar = new HBox(15);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(15, 20, 15, 20));
        navBar.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        Label title = new Label("Data Visualization");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.web(HEADER_COLOR));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        // Button to access overall metrics
        Button histogramButton = createStyledButton("Histogram View", PRIMARY_COLOR, PRIMARY_DARK_COLOR);
        
        // Button to access overall metrics
        Button overallMetricsButton = createStyledButton("Overall Metrics", PRIMARY_COLOR, PRIMARY_DARK_COLOR);
        
        // Button to compare multiple charts
        Button compareChartsButton = createStyledButton("Compare Charts", PRIMARY_COLOR, PRIMARY_DARK_COLOR);
        
        // Logout button
        Button logOutButton = createStyledButton("Logout", "#757575", "#616161");
        
        // File selection button
        Button fileSelectionButton = createStyledButton("Select Files", "#757575", "#616161");

        Button saveToPdfButton = createStyledButton("Save To Pdf", "#757575", "#616161");

        
        // Set button actions
        overallMetricsButton.setOnAction(e -> {
            OverallMetricsPage metricsPage = new OverallMetricsPage(stage, logManager);
            metricsPage.show();
        });

        histogramButton.setOnAction(e -> {
            HistogramChart histogramChart = new HistogramChart(stage,this);
            histogramChart.show();
        });
        
        compareChartsButton.setOnAction(e -> {
            ArrayList<String> currentCharts = new ArrayList<>();
            currentCharts.add(this.currentChart);
            currentCharts.add("Impressions"); // Default second chart
            
            ArrayList<String> timeFlags = new ArrayList<>();
            timeFlags.add(this.timeFlag);
            timeFlags.add("Daily"); // Default time flag for second chart
            
            ArrayList<String> genders = new ArrayList<>();
            genders.add(this.gender);
            genders.add(""); // Default gender for second chart
            
            ArrayList<String> incomes = new ArrayList<>();
            incomes.add(this.income);
            incomes.add(""); // Default income for second chart
            
            ArrayList<ArrayList<String>> contexts = new ArrayList<>();
            contexts.add(this.context);
            contexts.add(new ArrayList<>()); // Default context for second chart
            
            ArrayList<ArrayList<String>> ages = new ArrayList<>();
            ages.add(this.age);
            ages.add(new ArrayList<>()); // Default age for second chart
            
            MultiChartPage multiChartPage = new MultiChartPage(stage, logManager, currentCharts, timeFlags, genders, incomes, contexts, ages);
            multiChartPage.show();
        });
        
        logOutButton.setOnAction(e -> {
            Login login = new Login(stage);
            login.show();
        });
        
        fileSelectionButton.setOnAction(e -> {
            InputFilesPage inputFilesPage = new InputFilesPage(stage);
            inputFilesPage.show();
        });

        saveToPdfButton.setOnAction(e -> {
            try {
                // Convert JavaFX Chart to image
                WritableImage writableImage = chartViewer.snapshot(null, null);

                // Save image as PDF
                chartCreator.saveChartAsPdf(writableImage,stage);

            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (DocumentException ex) {
                throw new RuntimeException(ex);
            }
        });
        
        navBar.getChildren().addAll(title, spacer, histogramButton, fileSelectionButton,saveToPdfButton, overallMetricsButton, compareChartsButton, logOutButton);
        return navBar;
    }
    
    // Create filter panel
    private ScrollPane createFilterPanel(ChartViewer chartViewer) {
        VBox filterPanel = new VBox(20);
        filterPanel.setPadding(new Insets(20));
        filterPanel.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                "-fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        // Title for filter panel
        Label filterTitle = new Label("Chart Settings");
        filterTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        filterTitle.setTextFill(Color.web(HEADER_COLOR));
        
        // Add different filter sections
        VBox metricsSection = createMetricsOptions(chartViewer);
        VBox genderSection = createGenderOptions(chartViewer);
        VBox incomeSection = createIncomeOptions(chartViewer);
        VBox contextSection = createContextOptions(chartViewer);
        VBox ageSection = createAgeOptions(chartViewer);
        
        // Add sections to filter panel
        filterPanel.getChildren().addAll(
            filterTitle,
            new Separator(),
            metricsSection,
            new Separator(),
            genderSection,
            new Separator(),
            incomeSection,
            new Separator(),
            contextSection,
            new Separator(),
            ageSection
        );
        
        // Create a scroll pane to hold the filter panel
        ScrollPane scrollPane = new ScrollPane(filterPanel);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefWidth(300);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("edge-to-edge");
        
        return scrollPane;
    }
    
    // Create time granularity bar
    private HBox createTimeGranularityBar(ChartViewer chartViewer) {
        HBox timeBar = new HBox(20);
        timeBar.setAlignment(Pos.CENTER);
        timeBar.setPadding(new Insets(15, 20, 15, 20));
        timeBar.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                "-fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        Label timeLabel = new Label("Time Granularity:");
        timeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        timeLabel.setTextFill(Color.web(HEADER_COLOR));
        
        ToggleGroup timeToggleGroup = new ToggleGroup();
        
        RadioButton dailyButton = createStyledRadioButton("Daily", timeToggleGroup);
        dailyButton.setSelected(true);
        
        RadioButton weeklyButton = createStyledRadioButton("Weekly", timeToggleGroup);
        RadioButton monthlyButton = createStyledRadioButton("Monthly", timeToggleGroup);
        
        // Set action handlers for time granularity buttons
        dailyButton.setOnAction(e -> {
            timeFlag = "Daily";
            updateChart(chartViewer);
        });
        
        weeklyButton.setOnAction(e -> {
            timeFlag = "Weekly";
            updateChart(chartViewer);
        });
        
        monthlyButton.setOnAction(e -> {
            timeFlag = "Monthly";
            updateChart(chartViewer);
        });
        
        timeBar.getChildren().addAll(timeLabel, dailyButton, weeklyButton, monthlyButton);
        
        return timeBar;
    }
    
    // Create metrics options
    private VBox createMetricsOptions(ChartViewer chartViewer) {
        VBox metricsBox = new VBox(10);
        
        Label metricsLabel = new Label("Metrics");
        metricsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        metricsLabel.setTextFill(Color.web(HEADER_COLOR));
        
        ToggleGroup metricsToggleGroup = new ToggleGroup();
        
        RadioButton clicksButton = createStyledRadioButton("Total Clicks", metricsToggleGroup);
        clicksButton.setSelected(true);
        
        RadioButton impressionsButton = createStyledRadioButton("Total Impressions", metricsToggleGroup);
        RadioButton uniquesButton = createStyledRadioButton("Unique Visitors", metricsToggleGroup);
        RadioButton bouncesButton = createStyledRadioButton("Bounces", metricsToggleGroup);
        RadioButton conversionsButton = createStyledRadioButton("Conversions", metricsToggleGroup);
        RadioButton costButton = createStyledRadioButton("Total Cost", metricsToggleGroup);
        
        // Create a separator for ratio metrics
        Separator separator = new Separator();
        separator.setPadding(new Insets(5, 0, 5, 0));
        
        Label ratioLabel = new Label("Ratio Metrics");
        ratioLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        ratioLabel.setTextFill(Color.web(HEADER_COLOR));
        ratioLabel.setPadding(new Insets(5, 0, 5, 0));
        
        RadioButton ctrButton = createStyledRadioButton("Click-Through Rate (CTR)", metricsToggleGroup);
        RadioButton cpaButton = createStyledRadioButton("Cost Per Acquisition (CPA)", metricsToggleGroup);
        RadioButton cpcButton = createStyledRadioButton("Cost Per Click (CPC)", metricsToggleGroup);
        RadioButton cpmButton = createStyledRadioButton("Cost Per Mille (CPM)", metricsToggleGroup);
        RadioButton bounceRateButton = createStyledRadioButton("Bounce Rate", metricsToggleGroup);
        
        // Set action handlers for metrics buttons
        clicksButton.setOnAction(e -> {
            currentChart = "Clicks";
            updateChart(chartViewer);
        });
        
        impressionsButton.setOnAction(e -> {
            currentChart = "Impressions";
            updateChart(chartViewer);
        });
        
        uniquesButton.setOnAction(e -> {
            currentChart = "Uniques";
            updateChart(chartViewer);
        });
        
        bouncesButton.setOnAction(e -> {
            currentChart = "Bounces";
            updateChart(chartViewer);
        });
        
        conversionsButton.setOnAction(e -> {
            currentChart = "Conversions";
            updateChart(chartViewer);
        });
        
        costButton.setOnAction(e -> {
            currentChart = "Cost";
            updateChart(chartViewer);
        });
        
        ctrButton.setOnAction(e -> {
            currentChart = "CTR";
            updateChart(chartViewer);
        });
        
        cpaButton.setOnAction(e -> {
            currentChart = "CPA";
            updateChart(chartViewer);
        });
        
        cpcButton.setOnAction(e -> {
            currentChart = "CPC";
            updateChart(chartViewer);
        });
        
        cpmButton.setOnAction(e -> {
            currentChart = "CPM";
            updateChart(chartViewer);
        });
        
        bounceRateButton.setOnAction(e -> {
            currentChart = "BounceRate";
            updateChart(chartViewer);
        });
        
        metricsBox.getChildren().addAll(
            metricsLabel,
            clicksButton,
            impressionsButton,
            uniquesButton,
            bouncesButton,
            conversionsButton,
            costButton,
            separator,
            ratioLabel,
            ctrButton,
            cpaButton,
            cpcButton,
            cpmButton,
            bounceRateButton
        );
        
        return metricsBox;
    }
    
    // Create gender options
    private VBox createGenderOptions(ChartViewer chartViewer) {
        VBox genderBox = new VBox(10);
        
        Label genderLabel = new Label("Gender");
        genderLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        genderLabel.setTextFill(Color.web(HEADER_COLOR));
        
        ToggleGroup genderToggleGroup = new ToggleGroup();
        
        RadioButton allGenderButton = createStyledRadioButton("All", genderToggleGroup);
        allGenderButton.setSelected(true);
        
        RadioButton maleButton = createStyledRadioButton("Male", genderToggleGroup);
        RadioButton femaleButton = createStyledRadioButton("Female", genderToggleGroup);
        
        // Set action handlers for gender buttons
        allGenderButton.setOnAction(e -> {
            gender = "";
            updateChart(chartViewer);
        });
        
        maleButton.setOnAction(e -> {
            gender = "Male";
            updateChart(chartViewer);
        });
        
        femaleButton.setOnAction(e -> {
            gender = "Female";
            updateChart(chartViewer);
        });
        
        genderBox.getChildren().addAll(genderLabel, allGenderButton, maleButton, femaleButton);
        
        return genderBox;
    }
    
    // Create income options
    private VBox createIncomeOptions(ChartViewer chartViewer) {
        VBox incomeBox = new VBox(10);
        
        Label incomeLabel = new Label("Income");
        incomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        incomeLabel.setTextFill(Color.web(HEADER_COLOR));
        
        ToggleGroup incomeToggleGroup = new ToggleGroup();
        
        RadioButton allIncomeButton = createStyledRadioButton("All", incomeToggleGroup);
        allIncomeButton.setSelected(true);
        
        RadioButton lowButton = createStyledRadioButton("Low", incomeToggleGroup);
        RadioButton mediumButton = createStyledRadioButton("Medium", incomeToggleGroup);
        RadioButton highButton = createStyledRadioButton("High", incomeToggleGroup);
        
        // Set action handlers for income buttons
        allIncomeButton.setOnAction(e -> {
            income = "";
            updateChart(chartViewer);
        });
        
        lowButton.setOnAction(e -> {
            income = "Low";
            updateChart(chartViewer);
        });
        
        mediumButton.setOnAction(e -> {
            income = "Medium";
            updateChart(chartViewer);
        });
        
        highButton.setOnAction(e -> {
            income = "High";
            updateChart(chartViewer);
        });
        
        incomeBox.getChildren().addAll(incomeLabel, allIncomeButton, lowButton, mediumButton, highButton);
        
        return incomeBox;
    }
    
    // Create context options
    private VBox createContextOptions(ChartViewer chartViewer) {
        VBox contextBox = new VBox(10);
        
        Label contextLabel = new Label("Context");
        contextLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        contextLabel.setTextFill(Color.web(HEADER_COLOR));
        
        CheckBox newsCheck = createStyledCheckBox("News");
        CheckBox shoppingCheck = createStyledCheckBox("Shopping");
        CheckBox socialMediaCheck = createStyledCheckBox("Social Media");
        CheckBox blogCheck = createStyledCheckBox("Blog");
        CheckBox hobbyCheck = createStyledCheckBox("Hobby");
        CheckBox travelCheck = createStyledCheckBox("Travel");
        
        // Button to clear all context filters
        Button clearContextButton = new Button("Clear All");
        clearContextButton.setFont(Font.font("Arial", 12));
        clearContextButton.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #333333;");
        clearContextButton.setOnAction(e -> {
            newsCheck.setSelected(false);
            shoppingCheck.setSelected(false);
            socialMediaCheck.setSelected(false);
            blogCheck.setSelected(false);
            hobbyCheck.setSelected(false);
            travelCheck.setSelected(false);
            context.clear();
            updateChart(chartViewer);
        });
        
        // Set action handlers for context checkboxes
        newsCheck.setOnAction(e -> {
            if (newsCheck.isSelected()) {
                context.add("News");
            } else {
                context.remove("News");
            }
            updateChart(chartViewer);
        });
        
        shoppingCheck.setOnAction(e -> {
            if (shoppingCheck.isSelected()) {
                context.add("Shopping");
            } else {
                context.remove("Shopping");
            }
            updateChart(chartViewer);
        });
        
        socialMediaCheck.setOnAction(e -> {
            if (socialMediaCheck.isSelected()) {
                context.add("Social Media");
            } else {
                context.remove("Social Media");
            }
            updateChart(chartViewer);
        });
        
        blogCheck.setOnAction(e -> {
            if (blogCheck.isSelected()) {
                context.add("Blog");
            } else {
                context.remove("Blog");
            }
            updateChart(chartViewer);
        });
        
        hobbyCheck.setOnAction(e -> {
            if (hobbyCheck.isSelected()) {
                context.add("Hobby");
            } else {
                context.remove("Hobby");
            }
            updateChart(chartViewer);
        });
        
        travelCheck.setOnAction(e -> {
            if (travelCheck.isSelected()) {
                context.add("Travel");
            } else {
                context.remove("Travel");
            }
            updateChart(chartViewer);
        });
        
        HBox clearButtonContainer = new HBox();
        clearButtonContainer.setAlignment(Pos.CENTER_RIGHT);
        clearButtonContainer.getChildren().add(clearContextButton);
        clearButtonContainer.setPadding(new Insets(5, 0, 0, 0));
        
        contextBox.getChildren().addAll(
            contextLabel,
            newsCheck,
            shoppingCheck,
            socialMediaCheck,
            blogCheck,
            hobbyCheck,
            travelCheck,
            clearButtonContainer
        );
        
        return contextBox;
    }
    
    // Create age options
    private VBox createAgeOptions(ChartViewer chartViewer) {
        VBox ageBox = new VBox(10);
        
        Label ageLabel = new Label("Age");
        ageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        ageLabel.setTextFill(Color.web(HEADER_COLOR));
        
        CheckBox under25Check = createStyledCheckBox("Under 25");
        CheckBox age25to34Check = createStyledCheckBox("25-34");
        CheckBox age35to44Check = createStyledCheckBox("35-44");
        CheckBox age45to54Check = createStyledCheckBox("45-54");
        CheckBox over54Check = createStyledCheckBox("Over 54");
        
        // Button to clear all age filters
        Button clearAgeButton = new Button("Clear All");
        clearAgeButton.setFont(Font.font("Arial", 12));
        clearAgeButton.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #333333;");
        clearAgeButton.setOnAction(e -> {
            under25Check.setSelected(false);
            age25to34Check.setSelected(false);
            age35to44Check.setSelected(false);
            age45to54Check.setSelected(false);
            over54Check.setSelected(false);
            age.clear();
            updateChart(chartViewer);
        });
        
        // Set action handlers for age checkboxes
        under25Check.setOnAction(e -> {
            if (under25Check.isSelected()) {
                age.add("<25");
            } else {
                age.remove("<25");
            }
            updateChart(chartViewer);
        });
        
        age25to34Check.setOnAction(e -> {
            if (age25to34Check.isSelected()) {
                age.add("25-34");
            } else {
                age.remove("25-34");
            }
            updateChart(chartViewer);
        });
        
        age35to44Check.setOnAction(e -> {
            if (age35to44Check.isSelected()) {
                age.add("35-44");
            } else {
                age.remove("35-44");
            }
            updateChart(chartViewer);
        });
        
        age45to54Check.setOnAction(e -> {
            if (age45to54Check.isSelected()) {
                age.add("45-54");
            } else {
                age.remove("45-54");
            }
            updateChart(chartViewer);
        });
        
        over54Check.setOnAction(e -> {
            if (over54Check.isSelected()) {
                age.add(">54");
            } else {
                age.remove(">54");
            }
            updateChart(chartViewer);
        });
        
        HBox clearButtonContainer = new HBox();
        clearButtonContainer.setAlignment(Pos.CENTER_RIGHT);
        clearButtonContainer.getChildren().add(clearAgeButton);
        clearButtonContainer.setPadding(new Insets(5, 0, 0, 0));
        
        ageBox.getChildren().addAll(
            ageLabel,
            under25Check,
            age25to34Check,
            age35to44Check,
            age45to54Check,
            over54Check,
            clearButtonContainer
        );
        
        return ageBox;
    }
    
    // Create a section with title and content
    private VBox createSection(String title, VBox content) {
        VBox section = new VBox(10);
        
        Label sectionTitle = new Label(title);
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        sectionTitle.setTextFill(Color.web(HEADER_COLOR));
        
        Separator separator = new Separator();
        separator.setStyle("-fx-opacity: 0.3;");
        
        section.getChildren().addAll(sectionTitle, separator, content);
        
        return section;
    }
    
    // Create styled button
    public Button createStyledButton(String text, String bgColor, String hoverColor) {
        Button button = new Button(text);
        button.setPrefSize(140, 35);
        button.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        
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
    
    // Create styled radio button
    private RadioButton createStyledRadioButton(String text, ToggleGroup group) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(group);
        radioButton.setFont(Font.font("Arial", 13));
        radioButton.setTextFill(Color.web(TEXT_COLOR));
        return radioButton;
    }
    
    // Create styled checkbox
    private CheckBox createStyledCheckBox(String text) {
        CheckBox checkBox = new CheckBox(text);
        checkBox.setFont(Font.font("Arial", 13));
        checkBox.setTextFill(Color.web(TEXT_COLOR));
        return checkBox;
    }
    
    // Update chart with error handling and no-data check
    private void updateChart(ChartViewer chartViewer) {
        try {
            JFreeChart chart = chartCreator.updateChart(currentChart, timeFlag, gender, income, context, age);
            // Check if the chart's dataset is empty
            if (chart != null && chart.getCategoryPlot() != null) {
                if (chart.getCategoryPlot().getDataset() == null ||
                  chart.getCategoryPlot().getDataset().getRowCount() == 0 ||
                  chart.getCategoryPlot().getDataset().getColumnCount() == 0) {
                    chart.getCategoryPlot().setNoDataMessage("No data available");
                }
            }
            chartViewer.setChart(chart);
        } catch (NullPointerException ex) {
            // Handle cases where a metric value is missing
            showAlert("No data available");
            logger.error("Null pointer exception in updateChart", ex);
        } catch (Exception ex) {
            showAlert("Error updating chart.");
            logger.error("Error updating chart", ex);
        }
    }

}

