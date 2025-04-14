package com.example;

import com.itextpdf.text.DocumentException;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.jfree.chart.fx.ChartViewer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.App.logger;

public class MultiChartPage {
    private Stage stage;
    private ArrayList<String> currentCharts;
    private ArrayList<String> timeFlags;
    private ArrayList<String> genders;
    private ArrayList<String> incomes;
    private ArrayList<ArrayList<String>> contexts;
    private ArrayList<ArrayList<String>> ages;
    private HBox chartContainer = null;
    private ChartCreator chartCreator;
    private LogManager logManager;
    private int timeSpent;

    // Define style constants
    private final String BACKGROUND_COLOR = "#f5f5f7";
    private final String PRIMARY_COLOR = "#4285F4";
    private final String PRIMARY_DARK_COLOR = "#3367d6";
    private final String SECTION_BACKGROUND = "white";
    private final String HEADER_COLOR = "#333333";
    private final String TEXT_COLOR = "#555555";
    private final String LOGOUT_COLOUR = "#ff0000";
    private final String LOGOUT_HOVER_COLOUR = "#8b0000";

    public MultiChartPage(Stage stage, LogManager logManager, ArrayList<String> currentCharts, ArrayList<String> timeFlags,
                          ArrayList<String> genders, ArrayList<String> incomes, ArrayList<ArrayList<String>> contexts,
                          ArrayList<ArrayList<String>> ages,ChartCreator chartCreator) {
        this.stage = stage;
        this.logManager = logManager;
        this.chartCreator = chartCreator;

        this.currentCharts = currentCharts;
        this.timeFlags = timeFlags;
        this.genders = genders;
        this.incomes = incomes;
        this.contexts = contexts;
        this.ages = ages;
    }

    public void show() {
        // Create main layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        
        // Create top navigation bar
        HBox navBar = createNavigationBar();
        
        // Create chart area
        VBox chartSection = createChartSection();
        
        // Create bottom edit button area
        HBox editBar = createEditButtonBar();
        
        // Set layout
        root.setTop(navBar);
        root.setCenter(chartSection);
        root.setBottom(editBar);
        
        // Set margins
        BorderPane.setMargin(chartSection, new Insets(20, 20, 20, 20));
        BorderPane.setMargin(editBar, new Insets(0, 0, 30, 0));
        
        Scene scene = new Scene(root, 1300, 800);
        stage.setScene(scene);
        stage.setTitle("Ad Auction Dashboard - Multi-Chart View");
        stage.show();
    }
    
    // Create top navigation bar
    private HBox createNavigationBar() {
        HBox navBar = new HBox(15);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(15, 20, 15, 20));
        navBar.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        Label title = new Label("Multi-Chart Comparison View");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web(HEADER_COLOR));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        
        Button backButton = createStyledButton("Back to Charts", "#757575", "#616161");
        Button saveToPdfButton = createStyledButton("Save To Pdf", PRIMARY_COLOR, PRIMARY_DARK_COLOR);
        Button overallMetricsButton = createStyledButton("Overall Metrics", PRIMARY_COLOR, PRIMARY_DARK_COLOR);
        Button logOutButton = createStyledButton("Logout", "#757575", "#616161");
        
        // Set button events
        backButton.setOnAction(e -> {
            ChartPage chartPage = new ChartPage(stage, logManager,chartCreator);
            chartPage.show();
        });

        overallMetricsButton.setOnAction(e -> {
            OverallMetricsPage metricsPage = new OverallMetricsPage(stage, logManager,chartCreator);
            metricsPage.show();
        });

        logOutButton.setOnAction(e -> {
            Login login = new Login(stage);
            login.show();
        });

        saveToPdfButton.setOnAction(e -> {
            try {
                // Convert JavaFX Chart to image
                WritableImage writableImage = chartContainer.snapshot(null, null);


                // Save image as PDF
                chartCreator.saveChartAsPdf(writableImage,stage);

            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (DocumentException ex) {
                throw new RuntimeException(ex);
            }
        });
        
        // Add to navigation bar
        navBar.getChildren().addAll(title, spacer, backButton,saveToPdfButton, overallMetricsButton, logOutButton);
        
        return navBar;
    }

    /**
     * Create chart area with two charts and error handling.
     */
    private VBox createChartSection() {
        VBox chartSection = new VBox(30);
        chartSection.setAlignment(Pos.CENTER);

        // Add description text
        Label descriptionLabel = new Label("Dual Chart View - Compare different metrics or filter conditions simultaneously");
        descriptionLabel.setFont(Font.font("Arial", 14));
        descriptionLabel.setTextFill(Color.web("#666666"));

        // Create chart container
        chartContainer = new HBox(30);
        chartContainer.setAlignment(Pos.CENTER);

        // Create ChartViewer instance for chart 1 with error handling and no-data check
        ChartViewer chartViewer1;
        try {
            chartViewer1 = new ChartViewer(chartCreator.updateChart(currentCharts.get(0), timeFlags.get(0),
              genders.get(0), incomes.get(0),
              contexts.get(0), ages.get(0)));
            if (chartViewer1.getChart() != null && chartViewer1.getChart().getCategoryPlot() != null) {
                if (chartViewer1.getChart().getCategoryPlot().getDataset() == null ||
                  chartViewer1.getChart().getCategoryPlot().getDataset().getRowCount() == 0 ||
                  chartViewer1.getChart().getCategoryPlot().getDataset().getColumnCount() == 0) {
                    chartViewer1.getChart().getCategoryPlot().setNoDataMessage("No data available");
                }
            }
        } catch (Exception ex) {
            showAlert("Error creating Chart 1.");
            logger.error("Error creating Chart 1", ex);
            chartViewer1 = new ChartViewer();
        }

        // Create ChartViewer instance for chart 2 with error handling and no-data check
        ChartViewer chartViewer2;
        try {
            chartViewer2 = new ChartViewer(chartCreator.updateChart(currentCharts.get(1), timeFlags.get(1),
              genders.get(1), incomes.get(1),
              contexts.get(1), ages.get(1)));
            if (chartViewer2.getChart() != null && chartViewer2.getChart().getCategoryPlot() != null) {
                if (chartViewer2.getChart().getCategoryPlot().getDataset() == null ||
                  chartViewer2.getChart().getCategoryPlot().getDataset().getRowCount() == 0 ||
                  chartViewer2.getChart().getCategoryPlot().getDataset().getColumnCount() == 0) {
                    chartViewer2.getChart().getCategoryPlot().setNoDataMessage("No data available");
                }
            }
        } catch (Exception ex) {
            showAlert("Error creating Chart 2.");
            logger.error("Error creating Chart 2", ex);
            chartViewer2 = new ChartViewer();
        }

        // Create first chart area
        VBox chart1Box = createChartBox("Chart 1", chartViewer1);

        // Create second chart area
        VBox chart2Box = createChartBox("Chart 2", chartViewer2);

        // Add charts to container
        chartContainer.getChildren().addAll(chart1Box, chart2Box);

        // Add all elements to chart section
        chartSection.getChildren().addAll(descriptionLabel, chartContainer);

        return chartSection;
    }

    /**
     * Helper method to show an alert with a user-friendly error message.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Create single chart box
    private VBox createChartBox(String title, ChartViewer chartViewer) {
        VBox chartBox = new VBox(10);
        chartBox.setAlignment(Pos.CENTER);
        chartBox.setPadding(new Insets(15));
        chartBox.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                          "-fx-background-radius: 10; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        // Chart title
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLabel.setTextFill(Color.web(HEADER_COLOR));
        
        // Set chart size
        chartViewer.setMinSize(550, 400);
        chartViewer.setMaxSize(550, 400);
        
        chartBox.getChildren().addAll(titleLabel, chartViewer);
        
        return chartBox;
    }
    
    // Create bottom edit button area
    private HBox createEditButtonBar() {
        HBox editBar = new HBox(20);
        editBar.setAlignment(Pos.CENTER);
        
        Button editChart1Button = createStyledButton("Edit Chart 1", PRIMARY_COLOR, PRIMARY_DARK_COLOR);
        Button editChart2Button = createStyledButton("Edit Chart 2", PRIMARY_COLOR, PRIMARY_DARK_COLOR);
        Button saveToPdfButton = createStyledButton("Save To Pdf", PRIMARY_COLOR, PRIMARY_DARK_COLOR);
        
        // Set button events
        editChart1Button.setOnAction(e -> {
            EditPage editPage = new EditPage(stage, logManager, currentCharts, timeFlags, genders, incomes, contexts, ages, 0,chartCreator);
            editPage.show();
        });
        
        editChart2Button.setOnAction(e -> {
            EditPage editPage2 = new EditPage(stage,logManager, currentCharts, timeFlags, genders, incomes, contexts, ages ,1,chartCreator);
            editPage2.show();
        });

        saveToPdfButton.setOnAction(e -> {
            try {
                // Convert JavaFX Chart to image
                WritableImage writableImage = chartContainer.snapshot(null, null);

                // Save image as PDF
                chartCreator.saveChartAsPdf(writableImage,stage);

            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (DocumentException ex) {
                throw new RuntimeException(ex);
            }
        });
        
        editBar.getChildren().addAll(editChart1Button, editChart2Button);
        
        return editBar;
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
}
