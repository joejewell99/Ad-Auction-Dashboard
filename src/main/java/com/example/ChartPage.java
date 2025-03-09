package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ChartPage {
    private Stage stage;
    private LogManager logManager;
    private String timeFlag;
    private String currentChart;

    public ChartPage(Stage stage, LogManager logManager) {
        this.stage = stage;
        this.logManager = logManager;
        this.timeFlag = "Daily";
        this.currentChart = "Clicks";
    }

    public void show() {

        //Holder for chart
        ChartViewer chartViewer = new ChartViewer(genClickChart(this.timeFlag));
        chartViewer.setMaxSize(600,450);


        //Metric options
        ToggleGroup metricOptions = new ToggleGroup();

        RadioButton clickMetric = new RadioButton("Total Clicks");
        clickMetric.setToggleGroup(metricOptions);
        clickMetric.setSelected(true);
        clickMetric.setOnAction(e -> {
            this.currentChart = "Clicks";
            chartViewer.setChart(updateChart(currentChart,timeFlag));
        });

        RadioButton impressionMetric = new RadioButton("Total Impressions");
        impressionMetric.setToggleGroup(metricOptions);
        impressionMetric.setOnAction(e -> {
            this.currentChart = "Impressions";
            chartViewer.setChart(updateChart(currentChart,timeFlag));
        });

        RadioButton uniqueMetric = new RadioButton("Total uniques");
        uniqueMetric.setToggleGroup(metricOptions);
        uniqueMetric.setOnAction(e -> {
            this.currentChart = "Uniques";
            chartViewer.setChart(updateChart(currentChart,timeFlag));
        });

        RadioButton bouncesMetric = new RadioButton("Total bounces");
        bouncesMetric.setToggleGroup(metricOptions);
        bouncesMetric.setOnAction(e -> {
            this.currentChart = "Bounces";
            chartViewer.setChart(updateChart(currentChart,timeFlag));
        });

        RadioButton conversionMetric = new RadioButton("Total conversions");
        conversionMetric.setToggleGroup(metricOptions);
        conversionMetric.setOnAction(e -> {
            this.currentChart = "Conversions";
            chartViewer.setChart(updateChart(currentChart,timeFlag));
        });

        RadioButton costMetric = new RadioButton("Total cost");
        costMetric.setToggleGroup(metricOptions);
        costMetric.setOnAction(e -> {
            this.currentChart = "Cost";
            chartViewer.setChart(updateChart(currentChart,timeFlag));
        });

        RadioButton ctrMetric = new RadioButton("CTR");
        ctrMetric.setToggleGroup(metricOptions);
        ctrMetric.setOnAction(e -> {
            this.currentChart = "CTR";
            chartViewer.setChart(updateChart(currentChart,timeFlag));
        });

        RadioButton cpaMetric = new RadioButton("CPA");
        cpaMetric.setToggleGroup(metricOptions);
        cpaMetric.setOnAction(e -> {
            this.currentChart = "CPA";
            chartViewer.setChart(updateChart(currentChart,timeFlag));
        });

        RadioButton cpcMetric = new RadioButton("CPC");
        cpcMetric.setToggleGroup(metricOptions);
        cpcMetric.setOnAction(e -> {
            this.currentChart = "CPC";
            chartViewer.setChart(updateChart(currentChart,timeFlag));
        });

        RadioButton cpmMetric = new RadioButton("CPM");
        cpmMetric.setToggleGroup(metricOptions);
        cpmMetric.setOnAction(e -> {
            this.currentChart = "CPM";
            chartViewer.setChart(updateChart(currentChart,timeFlag));
        });

        RadioButton bounceRateMetric = new RadioButton("Bounce Rate");
        bounceRateMetric.setToggleGroup(metricOptions);
        bounceRateMetric.setOnAction(e -> {
            this.currentChart = "BounceRate";
            chartViewer.setChart(updateChart(currentChart,timeFlag));
        });

        ToggleGroup timeOptions = new ToggleGroup();

        RadioButton dailyButton = new RadioButton("Daily");
        dailyButton.setSelected(true);
        dailyButton.setToggleGroup(timeOptions);
        dailyButton.setOnAction(e -> {
            this.timeFlag = "Daily";
            chartViewer.setChart(updateChart(currentChart,timeFlag));
        });

        RadioButton weeklyButton = new RadioButton("Weekly");
        weeklyButton.setToggleGroup(timeOptions);
        weeklyButton.setOnAction(e -> {
            this.timeFlag = "Weekly";
            chartViewer.setChart(updateChart(currentChart,timeFlag));
        });



        var metricsHeader = new Label("Metrics");
        var timeLabel = new Label("Time Granularity");
        var metricOptionHolder = new VBox();
        metricOptionHolder.getChildren().addAll(metricsHeader,clickMetric,impressionMetric,uniqueMetric,bouncesMetric,conversionMetric,costMetric,ctrMetric,cpaMetric,cpcMetric,cpmMetric,bounceRateMetric);
        metricOptionHolder.getChildren().addAll(timeLabel,dailyButton,weeklyButton);

        //Navigation Buttons
        var buttonHolder = new HBox();
        var backButton = new Button("Back");
        var logOutButton = new Button("Logout");
        var overallMetricsButton = new Button("Overall Metrics");
        buttonHolder.getChildren().addAll(backButton,overallMetricsButton,logOutButton);

        backButton.setOnAction(e -> {
            App.getInstance().showInputFilesPage();
        });

        overallMetricsButton.setOnAction(e -> {
            OverallMetricsPage metricsPage = new OverallMetricsPage(stage,logManager);
            metricsPage.show();
        });

        logOutButton.setOnAction(e -> {
            Login login = new Login(stage);
            login.show();
        });

        //Layout
        BorderPane root = new BorderPane();
        root.setTop(buttonHolder);
        root.setCenter(chartViewer);
        root.setRight(metricOptionHolder);
        BorderPane.setMargin(metricOptionHolder, new Insets(0,10,0,0));



        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Graphs");
        stage.show();
    }

    /**
     * Generates chart for total clicks
     */
    public JFreeChart genClickChart(String time) {
        ArrayList<String[]> clicks = logManager.getClickData();
        Map<String,Integer> clickMap = new TreeMap<>();
        if (time.equals("Daily")) {
            clickMap = getDailyClicks(clicks);
        } else if (time.equals("Weekly")) {
            clickMap = getWeeklyClicks(clicks);
        }

        var dataset = createIntegerDataset(clickMap);
        return(createChart("Total Clicks","Clicks",dataset));
    }

    /**
     * Generates chart for total impressions
     * @return
     */
    public JFreeChart genImpressionChart(String timeFlag) {
        ArrayList<String[]> impressions = logManager.getImpressionData();
        Map<String,Integer> impressionMap = new TreeMap<>();
        if (timeFlag.equals("Daily")) {
            impressionMap = getDailyImpressions(impressions);
        } else if (timeFlag.equals("Weekly")) {
            impressionMap = getWeeklyImpressions(impressions);
        }

        var dataset = createIntegerDataset(impressionMap);
        return(createChart("Total impressions","Impressions",dataset));

    }

    /**
     * Create chart for total uniques
     * @return
     */
    public JFreeChart genUniquesChart(String timeFlag) {
        ArrayList<String[]> clicks = logManager.getClickData();
        Map<String,Integer> clickMap = new TreeMap<>();

        if (timeFlag.equals("Daily")) {
            clickMap = getDailyUniques(clicks);
        } else if (timeFlag.equals("Weekly")) {
            clickMap = getWeeklyUniques(clicks);
        }

        var dataset = createIntegerDataset(clickMap);
        return(createChart("Total Uniques","Uniques",dataset));
    }

    /**
     * Create chart for total bounces
     * @return
     */
    public JFreeChart genBounceChart(String timeFlag) {
        ArrayList<String[]> interactions = logManager.getServerData();
        Map<String,Integer> interactionMap = new TreeMap<>();

        if (timeFlag.equals("Daily")) {
            interactionMap = getDailyBounces(interactions);
        } else if (timeFlag.equals("Weekly")) {
            interactionMap = getWeeklyBounces(interactions);
        }

        var dataset = createIntegerDataset(interactionMap);
        return(createChart("Total Bounces","Bounces",dataset));
    }
    /**
     * Create chart for total conversions
     * @return
     */
    public JFreeChart genConversionChart(String timeFlag) {
        ArrayList<String[]> interactions = logManager.getServerData();
        Map<String,Integer> conversionMap = new TreeMap<>();
        if (timeFlag.equals("Daily")) {
            conversionMap = getDailyConversions(interactions);
        } else if (timeFlag.equals("Weekly")) {
            conversionMap = getWeeklyConversions(interactions);
        }
        var dataset = createIntegerDataset(conversionMap);
        return(createChart("Total conversions","Conversions",dataset));
    }


    /**
     * Create chart for total cost
     * @return
     */
    public JFreeChart genCostChart(String timeFlag) {
        ArrayList<String[]> clicks = logManager.getClickData();
        ArrayList<String[]> impressions = logManager.getImpressionData();
        Map<String,Float> costMap = new TreeMap<>();
        if (timeFlag.equals("Daily")) {
            costMap = getDailyCost(clicks,impressions);
        } else if (timeFlag.equals("Weekly")) {
            costMap = getWeeklyCost(clicks,impressions);
        }

        var dataset = createFloatDataset(costMap);
        return(createChart("Total cost","Cost",dataset));

    }

    /**
     * Create chart for CTR
     * @return
     */
    public JFreeChart genCTRChart(String timeFlag) {
        ArrayList<String[]> clicks = logManager.getClickData();
        ArrayList<String[]> impressions = logManager.getImpressionData();
        Map<String,Integer> clickMap = new TreeMap<>();
        Map<String,Integer> impressionMap = new TreeMap<>();
        Map<String,Float> ctrMap = new TreeMap<>();

        if(timeFlag.equals("Daily")) {
            clickMap = getDailyClicks(clicks);
            impressionMap = getDailyImpressions(impressions);
        } else if (timeFlag.equals("Weekly")){
            clickMap = getWeeklyClicks(clicks);
            impressionMap = getWeeklyImpressions(impressions);
        }

        for(String date : impressionMap.keySet()) {
            int clicksDaily = clickMap.getOrDefault(date,0);
            int impressionsDaily = impressionMap.get(date);
            float ctr = (float) clicksDaily/impressionsDaily;
            ctrMap.put(date,ctr);
        }

        var dataset = createFloatDataset(ctrMap);
        return(createChart("CTR", "Clicks per impression", dataset));
    }

    /**
     * Create chart for CPA
     * @return
     */
    public JFreeChart genCPAChart(String timeFlag) {
        ArrayList<String[]> clicks = logManager.getClickData();
        ArrayList<String[]> interactions = logManager.getServerData();
        ArrayList<String[]> impressions = logManager.getImpressionData();
        Map<String,Integer> conversionMap = new TreeMap<>();
        Map<String,Float> costMap = new TreeMap<>();
        Map<String,Float> cpaMap = new TreeMap<>();

        if (timeFlag.equals("Daily")) {
            conversionMap = getDailyConversions(interactions);
            costMap = getDailyCost(clicks, impressions);
        } else if (timeFlag.equals("Weekly")) {
            conversionMap = getWeeklyConversions(interactions);
            costMap = getWeeklyCost(clicks,impressions);
        }

        for(String date : conversionMap.keySet()) {
            float costDaily = costMap.getOrDefault(date,(float) 0);
            int conversionsDaily = conversionMap.get(date);
            float cpa = costDaily/conversionsDaily;
            cpaMap.put(date,cpa);
        }

        var dataset = createFloatDataset(cpaMap);
        return(createChart("CPA","Cost per conversion",dataset));
    }

    /**
     * Create chart for CPC
     * @return
     */
    public JFreeChart genCPCChart(String timeFlag) {
        ArrayList<String[]> clicks = logManager.getClickData();
        ArrayList<String[]> impressions = logManager.getImpressionData();
        Map<String,Integer> clickMap = new TreeMap<>();
        Map<String,Float> costMap = new TreeMap<>();
        Map<String,Float> cpcMap = new TreeMap<>();

        if (timeFlag.equals("Daily")) {
            clickMap = getDailyClicks(clicks);
            costMap = getDailyCost(clicks, impressions);
        } else if (timeFlag.equals("Weekly")) {
            clickMap = getWeeklyClicks(clicks);
            costMap = getWeeklyCost(clicks,impressions);
        }

        for(String date : clickMap.keySet()) {
            float costDaily = costMap.getOrDefault(date,(float) 0);
            int clicksDaily = clickMap.get(date);
            float cpc = costDaily/clicksDaily;
            cpcMap.put(date,cpc);
        }

        var dataset = createFloatDataset(cpcMap);
        return(createChart("CPC","Cost per click",dataset));
    }

    /**
     * Generate chart for CPM
     * @return
     */
    public JFreeChart genCPMChart(String timeFlag) {
        ArrayList<String[]> clicks = logManager.getClickData();
        ArrayList<String[]> impressions = logManager.getImpressionData();
        Map<String,Integer> impressionMap = new TreeMap<>();
        Map<String,Float> costMap = new TreeMap<>();
        Map<String,Float> cpmMap = new TreeMap<>();

        if (timeFlag.equals("Daily")) {
            impressionMap = getDailyImpressions(impressions);
            costMap = getDailyCost(clicks, impressions);
        } else if (timeFlag.equals("Weekly")) {
            impressionMap = getWeeklyImpressions(impressions);
            costMap = getWeeklyCost(clicks,impressions);
        }

        //Convert impressions into thousands
        Map<String,Float> impressionThousandMap = new TreeMap<>();
        for (Map.Entry<String,Integer> entry : impressionMap.entrySet()) {
            impressionThousandMap.put(entry.getKey(),(float) entry.getValue()/1000);
        }

        for(String date : impressionThousandMap.keySet()) {
            float costDaily = costMap.get(date);
            float thousandImpressionsDaily = impressionThousandMap.get(date);
            float cpm = costDaily/thousandImpressionsDaily;
            cpmMap.put(date,cpm);
        }

        var dataset = createFloatDataset(cpmMap);
        return(createChart("CPM","Cost per thousand impressions",dataset));
    }

    /**
     * Create chart for bounce rate
     * @return
     */
    public JFreeChart genBounceRateChart(String timeFlag) {
        ArrayList<String[]> clicks = logManager.getClickData();
        ArrayList<String[]> interactions = logManager.getServerData();
        Map<String,Integer> clickMap = new TreeMap<>();
        Map<String,Integer> bounceMap = new TreeMap<>();
        Map<String,Float> bounceRateMap = new TreeMap<>();

        if (timeFlag.equals("Daily")) {
            clickMap = getDailyClicks(clicks);
            bounceMap = getDailyBounces(interactions);
        } else if (timeFlag.equals("Weekly")) {
            clickMap = getWeeklyClicks(clicks);
            bounceMap = getWeeklyBounces(interactions);
        }

        for (String date : clickMap.keySet()) {
            int clicksDaily = clickMap.get(date);
            int bouncesDaily = bounceMap.get(date);
            float bounceRate = (float) bouncesDaily/clicksDaily;
            bounceRateMap.put(date,bounceRate);
        }

        var dataset = createFloatDataset(bounceRateMap);
        return(createChart("Bounce Rate","Bounces per click",dataset));

    }


    /**
     * Reusable method for creating chart
     * @param name
     * @param metric
     * @param dataset
     * @return
     */
    public JFreeChart createChart(String name, String metric, DefaultCategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createLineChart(
                name,
                "Date",
                metric,
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );
        // Adjusting font
        CategoryPlot plot = chart.getCategoryPlot();
        CategoryAxis xAxis = plot.getDomainAxis();
        xAxis.setTickLabelFont(new Font("Arial", Font.PLAIN, 9));  // Change font size here
        xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

        return chart;
    }

    /**
     * Creates dataset with float values
     * @param map
     * @return
     */
    public DefaultCategoryDataset createFloatDataset(Map<String,Float> map) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Float> entry: map.entrySet()) {
            dataset.addValue(entry.getValue(), "Entry", entry.getKey());
        }
        return  dataset;
    }

    /**
     * Creates dataset with integer values
     * @param map
     * @return
     */
    public DefaultCategoryDataset createIntegerDataset(Map<String,Integer> map) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry: map.entrySet()) {
            dataset.addValue(entry.getValue(), "Entry", entry.getKey());
        }
        return  dataset;
    }

    /**
     * Accumulate daily clicks
     * @param clicks
     * @return
     */
    public Map<String,Integer> getDailyClicks (ArrayList<String[]> clicks) {
        Map<String,Integer> clickMap = new TreeMap<>();
        for (String[] click : clicks){
            String date = click[0].split(" ")[0];
            clickMap.put(date, clickMap.getOrDefault(date, 1) + 1);
        }
        return clickMap;
    }

    /**
     * Accumulate weekly clicks
     * @param clicks
     * @return
     */
    public Map<String,Integer> getWeeklyClicks (ArrayList<String[]> clicks) {
        Map<String,Integer> clickMap = new TreeMap<>();

        LocalDate earliestDate = getEarliestDate(clicks);
        for (String[] click : clicks) {
            LocalDate date = LocalDate.parse(click[0].split(" ")[0]);
            int weekNumber = (int) ChronoUnit.WEEKS.between(earliestDate,date) + 1;
            String week = "Week " + Integer.toString(weekNumber);
            clickMap.put(week,clickMap.getOrDefault(week,1) + 1);
        }

        return clickMap;
    }


    /**
     * Accumulate daily impressions
     * @param impressions
     * @return
     */
    public Map<String,Integer> getDailyImpressions (ArrayList<String[]> impressions) {
        Map<String,Integer> impressionMap = new TreeMap<>();
        for (String[] impression : impressions){
            String date = impression[0].split(" ")[0];
            impressionMap.put(date, impressionMap.getOrDefault(date, 1) + 1);
        }
        return impressionMap;
    }

    /**
     * Accumulate weekly impressions
     * @param impressions
     * @return
     */
    public Map<String,Integer> getWeeklyImpressions (ArrayList<String[]> impressions) {
        Map<String,Integer> impressionMap = new TreeMap<>();

        LocalDate earliestDate = getEarliestDate(impressions);
        for (String[] impression : impressions) {
            LocalDate date = LocalDate.parse(impression[0].split(" ")[0]);
            int weekNumber = (int) ChronoUnit.WEEKS.between(earliestDate,date) + 1;
            String week = "Week " + Integer.toString(weekNumber);
            impressionMap.put(week,impressionMap.getOrDefault(week,1) + 1);
        }
        return impressionMap;
    }

    /**
     * Accumulate daily uniques
     * @param clicks
     * @return
     */
    public Map<String,Integer> getDailyUniques (ArrayList<String[]> clicks) {
        Map<String,Integer> clickMap = new TreeMap<>();
        Set<String> uniqueIDs = new HashSet<>();

        for(String[] click : clicks) {
            String dateTime = click[0];
            String id = click[1];

            if (!uniqueIDs.contains(id)) {
                uniqueIDs.add(id);
                String date = dateTime.split(" ")[0];

                clickMap.put(date,clickMap.getOrDefault(date,1)+1);
            }
        }
        return clickMap;
    }

    /**
     * Accumulate weekly uniques
     * @param clicks
     * @return
     */
    public Map<String,Integer> getWeeklyUniques (ArrayList<String[]> clicks) {
        Map<String,Integer> clickMap = new TreeMap<>();
        Set<String> uniqueIDs = new HashSet<>();

        LocalDate earliestDate = getEarliestDate(clicks);

        for(String[] click : clicks) {
            LocalDate date = LocalDate.parse(click[0].split(" ")[0]);
            int weekNumber = (int) ChronoUnit.WEEKS.between(earliestDate,date) + 1;
            String week = "Week " + Integer.toString(weekNumber);
            String id = click[1];

            if (!uniqueIDs.contains(id)) {
                uniqueIDs.add(id);
                clickMap.put(week,clickMap.getOrDefault(week,1)+1);
            }
        }
        return clickMap;

    }

    /**
     * Accumulate daily conversions
     * @param interactions
     * @return
     */
    public Map<String,Integer> getDailyConversions (ArrayList<String[]> interactions) {
        Map<String,Integer> conversionMap = new TreeMap<>();
        for (String[] interaction : interactions){
            if (interaction[4].equals("Yes")) {
                String date = interaction[0].split(" ")[0];
                conversionMap.put(date, conversionMap.getOrDefault(date, 1) + 1);
            }
        }
        return conversionMap;
    }

    /**
     * Accumulate weekly conversions
     * @param interactions
     * @return
     */
    public Map<String,Integer> getWeeklyConversions(ArrayList<String[]> interactions) {
        Map<String,Integer> conversionMap = new TreeMap<>();

        LocalDate earliestDate = getEarliestDate(interactions);
        for (String[] interaction : interactions) {
            if (interaction[4].equals("Yes")) {
                LocalDate date = LocalDate.parse(interaction[0].split(" ")[0]);
                int weekNumber = (int) ChronoUnit.WEEKS.between(earliestDate,date) + 1;
                String week = "Week " + Integer.toString(weekNumber);
                conversionMap.put(week,conversionMap.getOrDefault(week,1) + 1);
            }
        }
        return conversionMap;
    }


    /**
     * Accumulate daily cost
     * @param clicks
     * @param impressions
     * @return
     */
    public Map<String,Float> getDailyCost (ArrayList<String[]> clicks, ArrayList<String[]> impressions) {
        Map<String,Float> costMap = new TreeMap<>();

        for (String[] click : clicks) {
            String dateTime = click[0];
            String date = dateTime.split(" ")[0];
            float cost = Float.parseFloat(click[2]);
            costMap.put(date,costMap.getOrDefault(date,cost) + cost);
        }

        for (String[] impression: impressions) {
            String dateTime = impression[0];
            String date = dateTime.split(" ")[0];
            float cost = Float.parseFloat(impression[6]);
            costMap.put(date,costMap.getOrDefault(date,cost) + cost);
        }

        return costMap;
    }

    /**
     * Accumulate weekly cost
     * @param clicks
     * @param impressions
     * @return
     */
    public Map<String,Float> getWeeklyCost (ArrayList<String[]> clicks, ArrayList<String[]> impressions) {
        Map<String,Float> costMap = new TreeMap<>();

        LocalDate earliestDate = getEarliestDate(impressions);
        for (String[] click : clicks) {
            LocalDate date = LocalDate.parse(click[0].split(" ")[0]);
            int weekNumber = (int) ChronoUnit.WEEKS.between(earliestDate,date) + 1;
            String week = "Week " + Integer.toString(weekNumber);
            float cost = Float.parseFloat(click[2]);
            costMap.put(week,costMap.getOrDefault(week,cost) + cost);
        }

        for (String[] impression : impressions) {
            LocalDate date = LocalDate.parse(impression[0].split(" ")[0]);
            int weekNumber = (int) ChronoUnit.WEEKS.between(earliestDate,date) + 1;
            String week = "Week " + Integer.toString(weekNumber);
            float cost = Float.parseFloat(impression[6]);
            costMap.put(week,costMap.getOrDefault(week,cost) + cost);
        }
        return costMap;
    }

    /**
     * Accumulate daily bounces
     * @param interactions
     * @return
     */
    public Map<String,Integer> getDailyBounces(ArrayList<String[]> interactions) {
        Map<String,Integer> interactionMap = new TreeMap<>();

        for (String[] interaction : interactions){
            if (interaction[3].equals("1")) {
                String dateTime = interaction[0];
                String date = dateTime.split(" ")[0];

                interactionMap.put(date, interactionMap.getOrDefault(date, 1) + 1);
            }
        }
        return interactionMap;
    }

    /**
     * Accumulate weekly bounces
     * @param interactions
     * @return
     */
    public Map<String, Integer> getWeeklyBounces(ArrayList<String[]> interactions) {
        Map<String,Integer> bounceMap = new TreeMap<>();

        LocalDate earliestDate = getEarliestDate(interactions);
        for (String[] interaction : interactions) {
            if (interaction[3].equals("1")) {
                LocalDate date = LocalDate.parse(interaction[0].split(" ")[0]);
                int weekNumber = (int) ChronoUnit.WEEKS.between(earliestDate,date) + 1;
                String week = "Week " + Integer.toString(weekNumber);
                bounceMap.put(week,bounceMap.getOrDefault(week,1) + 1);
            }
        }
        return bounceMap;
    }

    /**
     * Get earliest date from set of data entries
     * @param data
     * @return
     */
    public LocalDate getEarliestDate(ArrayList<String[]> data) {
        LocalDate firstDate = null;
        for(String[] entry : data) {
            LocalDate date = LocalDate.parse(entry[0].split(" ")[0]);
            if(firstDate == null || date.isBefore(firstDate)) {
                firstDate = date;
            }
        }
        return firstDate;
    }


    /**
     * Updates chart with time filter applied
     * @param currentChart
     * @param timeFlag
     * @return
     */
    public JFreeChart updateChart(String currentChart, String timeFlag) {
        if (currentChart.equals("Clicks")) {
            return (genClickChart(timeFlag));
        } else if (currentChart.equals("Impressions")) {
            return (genImpressionChart(timeFlag));
        } else if (currentChart.equals("Conversions")) {
            return (genConversionChart(timeFlag));
        } else if (currentChart.equals("Bounces")) {
            return (genBounceChart(timeFlag));
        } else if (currentChart.equals("Cost")) {
            return (genCostChart(timeFlag));
        } else if (currentChart.equals("Uniques")) {
            return (genUniquesChart(timeFlag));
        } else if (currentChart.equals("BounceRate")){
            return (genBounceRateChart(timeFlag));
        } else if (currentChart.equals("CTR")) {
            return (genCTRChart(timeFlag));
        } else if (currentChart.equals("CPA")) {
            return (genCPAChart(timeFlag));
        } else if (currentChart.equals("CPC")) {
            return (genCPCChart(timeFlag));
        } else {
            return (genCPMChart(timeFlag));
        }

    }





}

