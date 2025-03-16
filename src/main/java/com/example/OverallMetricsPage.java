package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;


public class OverallMetricsPage {
    private Stage stage;
    private Scene scene;

    public LogManager logManager;
    private ArrayList<String[]> clickData;
    private ArrayList<String[]> impressionData;
    private ArrayList<String[]> serverData;
    private OverallMetricsCalculator metricsCalculator = new OverallMetricsCalculator();

    // Define style constants
    private final String BACKGROUND_COLOR = "#f5f5f7";
    private final String PRIMARY_COLOR = "#4285F4";
    private final String PRIMARY_DARK_COLOR = "#3367d6";
    private final String SECTION_BACKGROUND = "white";
    private final String HEADER_COLOR = "#333333";
    private final String TEXT_COLOR = "#555555";
    private final String ERROR_COLOR = "#F44336";
    private final String SUCCESS_COLOR = "#4CAF50";

    OverallMetricsPage(Stage stage, LogManager logManager) {
        this.stage = stage;
        this.logManager = logManager;
        this.clickData = logManager.getClickData();
        this.impressionData = logManager.getImpressionData();
        this.serverData = logManager.getServerData();
        initialize();
    }

    private void initialize() {
        // Create main layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        
        // Create top title bar
        HBox topBar = createTopBar();
        
        // Create central content area
        VBox contentBox = createContentBox();
        
        // Create bottom button area
        HBox bottomBar = createBottomBar();
        
        // Set layout
        root.setTop(topBar);
        root.setCenter(contentBox);
        root.setBottom(bottomBar);
        
        // Set margins
        BorderPane.setMargin(contentBox, new Insets(20, 30, 20, 30));
        BorderPane.setMargin(bottomBar, new Insets(0, 0, 20, 0));
        
        // Create scene
        scene = new Scene(root, 600, 650);
    }
    
    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(20, 0, 10, 0));
        
        Label titleLabel = new Label("Overall Ad Campaign Metrics");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web(HEADER_COLOR));
        
        topBar.getChildren().add(titleLabel);
        
        return topBar;
    }
    
    private VBox createContentBox() {
        VBox contentBox = new VBox(15);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(30, 40, 30, 40));
        contentBox.setMaxWidth(540);
        contentBox.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                "-fx-background-radius: 10; " + 
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        // Add description text
        Label descriptionLabel = new Label("Below are the overall ad campaign metrics calculated from uploaded data files");
        descriptionLabel.setFont(Font.font("Arial", 14));
        descriptionLabel.setTextFill(Color.web("#666666"));
        descriptionLabel.setWrapText(true);
        descriptionLabel.setTextAlignment(TextAlignment.CENTER);
        
        Separator separator = new Separator();
        separator.setOpacity(0.3);
        separator.setPadding(new Insets(10, 0, 10, 0));
        
        // Create metrics grid
        GridPane metricsGrid = new GridPane();
        metricsGrid.setHgap(20);
        metricsGrid.setVgap(15);
        metricsGrid.setAlignment(Pos.CENTER);
        
        // Calculate all metric values
        int impressionsValue = metricsCalculator.calcImpressions(impressionData);
        int clicksValue = metricsCalculator.calcClicks(clickData);
        int uniquesValue = metricsCalculator.calcUniques(clickData);
        int bouncesValue = metricsCalculator.calcBounces(serverData);
        int conversionsValue = metricsCalculator.calcConversions(serverData);
        float costValue = metricsCalculator.calcCost(clickData, impressionData);
        float ctrValue = metricsCalculator.calcCTR(clickData, impressionData);
        float cpaValue = metricsCalculator.calcCPA(clickData, impressionData, serverData);
        float cpcValue = metricsCalculator.calcCPC(clickData, impressionData, serverData);
        float cpmValue = metricsCalculator.calcCPM(clickData, impressionData);
        float bounceRateValue = metricsCalculator.calcBounceRate(clickData, serverData);
        
        // First column: Basic quantitative metrics
        Label basicMetricsTitle = new Label("Basic Quantitative Metrics");
        basicMetricsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        basicMetricsTitle.setTextFill(Color.web(HEADER_COLOR));
        
        VBox basicMetricsBox = new VBox(12);
        basicMetricsBox.getChildren().addAll(
            basicMetricsTitle,
            createMetricItem("Total Impressions", impressionsValue + "", false),
            createMetricItem("Total Clicks", clicksValue + "", false),
            createMetricItem("Unique Visitors", uniquesValue + "", false),
            createMetricItem("Bounces", bouncesValue + "", false),
            createMetricItem("Conversions", conversionsValue + "", false),
            createMetricItem("Total Cost", String.format("%.2f", costValue) + " ¥", false)
        );
        
        // Second column: Ratio metrics
        Label ratioMetricsTitle = new Label("Ratio and Performance Metrics");
        ratioMetricsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        ratioMetricsTitle.setTextFill(Color.web(HEADER_COLOR));
        
        VBox ratioMetricsBox = new VBox(12);
        ratioMetricsBox.getChildren().addAll(
            ratioMetricsTitle,
            createMetricItem("Click-Through Rate (CTR)", formatPercentage(ctrValue), true),
            createMetricItem("Cost Per Acquisition (CPA)", String.format("%.2f", cpaValue) + " ¥", true),
            createMetricItem("Cost Per Click (CPC)", String.format("%.2f", cpcValue) + " ¥", true),
            createMetricItem("Cost Per Mille (CPM)", String.format("%.2f", cpmValue) + " ¥", true),
            createMetricItem("Bounce Rate", formatPercentage(bounceRateValue), true)
        );
        
        // Add to metrics grid
        metricsGrid.add(basicMetricsBox, 0, 0);
        metricsGrid.add(ratioMetricsBox, 1, 0);
        
        // Add all components to content box
        contentBox.getChildren().addAll(
            descriptionLabel,
            separator,
            metricsGrid
        );
        
        return contentBox;
    }
    
    private HBox createBottomBar() {
        HBox bottomBar = new HBox(15);
        bottomBar.setAlignment(Pos.CENTER);
        
        Button backButton = createStyledButton("Back to Charts", PRIMARY_COLOR, PRIMARY_DARK_COLOR);
        Button logoutButton = createStyledButton("Logout", "#757575", "#616161");
        
        // Button events
        backButton.setOnAction(e -> {
            ChartPage chartPage = new ChartPage(stage, logManager);
            chartPage.show();
        });
        
        logoutButton.setOnAction(e -> {
            System.out.println("Logout Button clicked");
            Login login = new Login(stage);
            login.show();
        });
        
        bottomBar.getChildren().addAll(backButton, logoutButton);
        
        return bottomBar;
    }
    
    // Create metric item
    private HBox createMetricItem(String label, String value, boolean isRatio) {
        HBox item = new HBox(10);
        item.setAlignment(Pos.CENTER_LEFT);
        
        Label metricLabel = new Label(label + ":");
        metricLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        metricLabel.setTextFill(Color.web(TEXT_COLOR));
        metricLabel.setMinWidth(150);
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        // Set text color based on whether it's a ratio metric
        if (isRatio) {
            // If value contains NaN, display in red
            if (value.contains("NaN")) {
                valueLabel.setTextFill(Color.web(ERROR_COLOR));
            } else {
                valueLabel.setTextFill(Color.web(PRIMARY_COLOR));
            }
        } else {
            valueLabel.setTextFill(Color.web(HEADER_COLOR));
        }
        
        item.getChildren().addAll(metricLabel, valueLabel);
        
        return item;
    }
    
    // Create styled button
    private Button createStyledButton(String text, String bgColor, String hoverColor) {
        Button button = new Button(text);
        button.setPrefSize(120, 40);
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
    
    // Format percentage display
    private String formatPercentage(float value) {
        if (Float.isNaN(value)) {
            return "NaN";
        }
        return String.format("%.2f%%", value * 100);
    }

    public void show() {
        stage.setScene(scene);
        stage.setTitle("Ad Auction Dashboard - Overall Metrics");
        stage.show();
    }
}
