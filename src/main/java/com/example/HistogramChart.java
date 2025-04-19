package com.example;

import com.itextpdf.text.DocumentException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.chart.NumberAxis;


import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class HistogramChart {
    private Stage Stage;
    private int weekOffset = 0; // 0 = current week
    private final String CSV_FILE = "resources/clicks_log.csv";
    private LocalDate dynamicBaseDate = null;
    private BarChart<String, Number> barChart;
    private boolean darkMode;
    private LogManager logManager;
    private ChartCreator chartCreator;



    // Define style constants
    private final String BACKGROUND_COLOR = "#f5f5f7";
    private final String PRIMARY_COLOR = "#4285F4";
    private final String PRIMARY_DARK_COLOR = "#3367d6";
    private final String SECTION_BACKGROUND = "white";
    private final String HEADER_COLOR = "#333333";
    private final String TEXT_COLOR = "#555555";
    private final String LOGOUT_COLOUR = "#ff0000";
    private final String LOGOUT_HOVER_COLOUR = "#8b0000";
    private final String SETTINGS = "#888888";
    private final String SETTINGS_HOVER = "#555555";
    private final String DARKMODE_SECTION = "#2b2b2b";
    private final String DARKMODE_BACKGROUND = "#1f1f1f";
    private final String DARKMODE_TEXT = "#fafafa";

    public HistogramChart(Stage stage,LogManager logManager, ChartCreator chartCreator, boolean darkMode) {
        this.Stage = stage;
        this.logManager = logManager;
        this.chartCreator = chartCreator;
        this.darkMode = darkMode;
    }

    public void show() {
        BorderPane root = new BorderPane();
        if (!darkMode) {
            root.setStyle("-fx-background-color: #f5f5f7;");
        } else {
            root.setStyle("-fx-background-color: " + DARKMODE_BACKGROUND);
        }

        // Create top navigation bar
        HBox navBar = createNavigationBar();

        // Create chart area
        VBox chartSection = createChartSection();

        //Logout Button
        Button logoutButton = createStyledButton("Logout", LOGOUT_COLOUR, LOGOUT_HOVER_COLOUR);
        logoutButton.setOnAction(e -> {
                    Login login = new Login(Stage,darkMode);
                    login.show();
                });
        HBox logoutBox = new HBox(logoutButton);
        logoutBox.setAlignment(Pos.BOTTOM_RIGHT);
        logoutBox.setPadding(new Insets(10, 20, 20, 20));



        // Set layout
        root.setTop(navBar);
        root.setCenter(chartSection);
        root.setBottom(logoutBox);

        // Set margins
        BorderPane.setMargin(chartSection, new Insets(20, 20, 20, 20));
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        Stage.setScene(scene);
        Stage.setFullScreen(true);
        Stage.setTitle("Ad Auction Dashboard - Click-Histogram View");
        Stage.show();





    }

    private HBox createNavigationBar() {
        HBox navBar = new HBox(15);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(15, 20, 15, 20));
        if (!darkMode) {
            navBar.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        } else {
            navBar.setStyle("-fx-background-color: " + DARKMODE_SECTION + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        }

        Label title = new Label("Click-Histogram Chart");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web(darkMode ? DARKMODE_TEXT : HEADER_COLOR));

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        Button backButton = createStyledButton("Back to Charts", PRIMARY_COLOR, PRIMARY_DARK_COLOR);
        Button saveToPdfButton = createStyledButton("Save To Pdf", PRIMARY_COLOR, PRIMARY_DARK_COLOR);
        //Button logOutButton = createStyledButton("Logout", "#757575", "#616161");

        Button settingsButton = createStyledButton("Settings", SETTINGS,SETTINGS_HOVER);
        settingsButton.setOnAction(e -> {
            PageInfo pageInfo = new PageInfo();
            pageInfo.setDarkMode(darkMode);
            SettingsPage settingsPage = new SettingsPage(Stage,"Histogram",pageInfo,darkMode);
            settingsPage.show();
        });

        // Set button events
        backButton.setOnAction(e -> {
            ChartPage chartPage = new ChartPage(Stage,logManager,chartCreator,darkMode);
            chartPage.show();
        });

        /**
        logOutButton.setOnAction(e -> {
            Login login = new Login(Stage);
            login.show();
        }); */

        saveToPdfButton.setOnAction(e -> {
            saveChartToPDF(barChart);
        });



        // Add to navigation bar
        navBar.getChildren().addAll(title,spacer,backButton,saveToPdfButton,settingsButton);

        return navBar;
    }

    private VBox createChartSection() {
        VBox chartSection = new VBox(20);
        chartSection.setAlignment(Pos.CENTER);

        Label weekRangeLabel = new Label();
        weekRangeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        weekRangeLabel.setTextFill(Color.web(darkMode ? DARKMODE_TEXT: TEXT_COLOR));

        Label descriptionLabel = new Label("Click Histogram by Day of Week");
        descriptionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        descriptionLabel.setTextFill(Color.web(darkMode ? DARKMODE_TEXT :TEXT_COLOR));

        barChart = createHistogramChart();

        // Initial update
        updateChart(barChart, weekRangeLabel);

        // Navigation Buttons
        HBox weekNav = new HBox(10);
        weekNav.setAlignment(Pos.CENTER);

        Button prevWeek = createStyledButton("← Previous Week", "#757575", "#616161");
        Button nextWeek = createStyledButton("Next Week →", "#757575", "#616161");

        prevWeek.setOnAction(e -> {
            weekOffset--;
            updateChart(barChart, weekRangeLabel);
        });

        nextWeek.setOnAction(e -> {
            weekOffset++;
            updateChart(barChart, weekRangeLabel);
        });



        weekNav.getChildren().addAll(prevWeek, nextWeek);

        chartSection.getChildren().addAll(weekRangeLabel, descriptionLabel, barChart, weekNav);
        return chartSection;
    }

    private BarChart<String, Number> createHistogramChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Day of the Week");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of Clicks");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Weekly Click Distribution");
        barChart.setLegendVisible(false);
        barChart.setCategoryGap(10);
        barChart.setBarGap(5);
        barChart.setStyle("-fx-background-color: transparent;");

        // Don't update chart here — it's handled in createChartSection()

        return barChart;
    }


    private void updateChart(BarChart<String, Number> chart, Label weekRangeLabel) {
        chart.getData().clear();

        Map<String, Integer> clickCounts = getClicksByDayOfWeek(weekOffset);
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            String name = capitalize(day.toString().toLowerCase());
            int count = clickCounts.getOrDefault(name, 0);
            series.getData().add(new XYChart.Data<>(name, count));
        }

        chart.getData().add(series);

        // ✅ Update the label too!
        updateWeekRangeLabel(weekRangeLabel);
    }


    private Map<String, Integer> getClicksByDayOfWeek(int weekOffset) {
        Map<String, Integer> dayCounts = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDate baseDate = LocalDate.of(2015, 1, 2); // Adjust if needed
        LocalDate startOfWeek = baseDate.plusWeeks(weekOffset).with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        try (
                InputStream input = getClass().getClassLoader().getResourceAsStream("clicks_log.csv");
                BufferedReader br = new BufferedReader(new InputStreamReader(input))
        ) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    LocalDateTime dateTime = LocalDateTime.parse(parts[0].trim(), formatter);
                    LocalDate date = dateTime.toLocalDate();

                    if (!date.isBefore(startOfWeek) && !date.isAfter(endOfWeek)) {
                        String day = capitalize(date.getDayOfWeek().toString().toLowerCase());
                        dayCounts.put(day, dayCounts.getOrDefault(day, 0) + 1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dayCounts;
    }


    private String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    // Helper method to update the label with the current week range
    private void updateWeekRangeLabel(Label weekRangeLabel) {
        if (dynamicBaseDate == null) {
            dynamicBaseDate = getEarliestDateFromCSV();
        }

        if (dynamicBaseDate != null) {
            LocalDate startOfWeek = dynamicBaseDate.plusWeeks(weekOffset).with(DayOfWeek.MONDAY);
            LocalDate endOfWeek = startOfWeek.plusDays(6);

            String weekRange = startOfWeek.toString() + " to " + endOfWeek.toString();
            weekRangeLabel.setText("Week: " + weekRange);
        } else {
            weekRangeLabel.setText("Week: Data unavailable");
        }
    }

    private LocalDate getEarliestDateFromCSV() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (
                InputStream input = getClass().getClassLoader().getResourceAsStream("clicks_log.csv");
                BufferedReader br = new BufferedReader(new InputStreamReader(input))
        ) {
            String line = br.readLine(); // skip header
            if ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    LocalDateTime dateTime = LocalDateTime.parse(parts[0].trim(), formatter);
                    return dateTime.toLocalDate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // fallback if something goes wrong
    }

    private void saveChartToPDF(BarChart<String, Number> chart) {
        WritableImage image = chart.snapshot(null, null);
        File outputFile = new File("histogram_chart.pdf");

        try {
            // Save snapshot as PNG image first
            File tempImageFile = new File("temp_chart.png");
            javax.imageio.ImageIO.write(javafx.embed.swing.SwingFXUtils.fromFXImage(image, null), "png", tempImageFile);

            // Now create a PDF with the image
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(outputFile));
            document.open();

            com.itextpdf.text.Image chartImage = com.itextpdf.text.Image.getInstance(tempImageFile.getAbsolutePath());
            chartImage.scaleToFit(500, 500); // Resize to fit the page
            document.add(chartImage);

            document.close();

            // Optional: Delete the temp image
            tempImageFile.delete();

            System.out.println("PDF saved successfully: " + outputFile.getAbsolutePath());

        } catch (IOException | com.itextpdf.text.DocumentException e) {
            e.printStackTrace();
        }
    }







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

}
