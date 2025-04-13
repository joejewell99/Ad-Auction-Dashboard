package com.example;

import com.itextpdf.text.DocumentException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    private ChartPage chartPage;
    private int weekOffset = 0; // 0 = current week
    private final String CSV_FILE = "resources/clicks_log.csv";
    private LocalDate dynamicBaseDate = null;


    // Define style constants
    private final String BACKGROUND_COLOR = "#f5f5f7";
    private final String PRIMARY_COLOR = "#4285F4";
    private final String PRIMARY_DARK_COLOR = "#3367d6";
    private final String SECTION_BACKGROUND = "white";
    private final String HEADER_COLOR = "#333333";
    private final String TEXT_COLOR = "#555555";

    public HistogramChart(Stage stage, ChartPage chartPage) {
        this.Stage = stage;
        this.chartPage= chartPage;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        // Create top navigation bar
        HBox navBar = createNavigationBar();

        // Create chart area

        VBox chartSection = createChartSection();

        // Set layout
        root.setTop(navBar);
        root.setCenter(chartSection);

        // Set margins
        BorderPane.setMargin(chartSection, new Insets(20, 20, 20, 20));

        Scene scene = new Scene(root, 1300, 800);
        Stage.setScene(scene);
        Stage.setTitle("Ad Auction Dashboard - Click-Histogram View");
        Stage.show();





    }

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

        Button backButton = chartPage.createStyledButton("Back to Charts", "#757575", "#616161");
        Button saveToPdfButton = chartPage.createStyledButton("Save To Pdf", PRIMARY_COLOR, PRIMARY_DARK_COLOR);
        Button logOutButton = chartPage.createStyledButton("Logout", "#757575", "#616161");

        // Set button events
        backButton.setOnAction(e -> {
            chartPage.show();
        });

        logOutButton.setOnAction(e -> {
            Login login = new Login(Stage);
            login.show();
        });



        // Add to navigation bar
        navBar.getChildren().addAll(title,spacer, saveToPdfButton, backButton, logOutButton);

        return navBar;
    }

    private VBox createChartSection() {
        VBox chartSection = new VBox(20);
        chartSection.setAlignment(Pos.CENTER);

        Label weekRangeLabel = new Label();
        weekRangeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        weekRangeLabel.setTextFill(Color.web(TEXT_COLOR));

        Label descriptionLabel = new Label("Click Histogram by Day of Week");
        descriptionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        descriptionLabel.setTextFill(Color.web(TEXT_COLOR));

        BarChart<String, Number> barChart = createHistogramChart();

        // Initial update
        updateChart(barChart, weekRangeLabel);

        // Navigation Buttons
        HBox weekNav = new HBox(10);
        weekNav.setAlignment(Pos.CENTER);

        Button prevWeek = chartPage.createStyledButton("← Previous Week", "#757575", "#616161");
        Button nextWeek = chartPage.createStyledButton("Next Week →", "#757575", "#616161");

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






    // Create styled button

}
