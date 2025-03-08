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
import java.util.*;

public class ChartPage {
    private Stage stage;
    private LogManager logManager;


    public ChartPage(Stage stage, LogManager logManager) {
        this.stage = stage;
        this.logManager = logManager;
    }

    public void show() {

        //Holder for chart
        ChartViewer chartViewer = new ChartViewer(genClickChart());
        chartViewer.setMaxSize(600,450);


        //Metric options
        ToggleGroup metricOptions = new ToggleGroup();

        RadioButton clickMetric = new RadioButton("Total Clicks");
        clickMetric.setToggleGroup(metricOptions);
        clickMetric.setSelected(true);
        clickMetric.setOnAction(e -> {
            chartViewer.setChart(genClickChart());
        });

        RadioButton impressionMetric = new RadioButton("Total Impressions");
        impressionMetric.setToggleGroup(metricOptions);
        impressionMetric.setOnAction(e -> {
            chartViewer.setChart(genImpressionChart());
        });

        RadioButton uniqueMetric = new RadioButton("Total uniques");
        uniqueMetric.setToggleGroup(metricOptions);
        uniqueMetric.setOnAction(e -> {
            chartViewer.setChart(genUniquesChart());
        });

        RadioButton bouncesMetric = new RadioButton("Total bounces");
        bouncesMetric.setToggleGroup(metricOptions);
        bouncesMetric.setOnAction(e -> {
            chartViewer.setChart(genBounceChart());
        });

        RadioButton conversionMetric = new RadioButton("Total conversions");
        conversionMetric.setToggleGroup(metricOptions);
        conversionMetric.setOnAction(e -> {
            chartViewer.setChart(genConversionChart());
        });

        RadioButton costMetric = new RadioButton("Total cost");
        costMetric.setToggleGroup(metricOptions);
        costMetric.setOnAction(e -> {
            chartViewer.setChart(genCostChart());
        });

        RadioButton ctrMetric = new RadioButton("CTR");
        ctrMetric.setToggleGroup(metricOptions);
        ctrMetric.setOnAction(e -> {
            chartViewer.setChart(genCTRChart());
        });

        RadioButton cpaMetric = new RadioButton("CPA");
        cpaMetric.setToggleGroup(metricOptions);
        cpaMetric.setOnAction(e -> {
            chartViewer.setChart(genCPAChart());
        });

        RadioButton cpcMetric = new RadioButton("CPC");
        cpcMetric.setToggleGroup(metricOptions);
        cpcMetric.setOnAction(e -> {
            chartViewer.setChart(genCPCChart());
        });

        RadioButton cpmMetric = new RadioButton("CPM");
        cpmMetric.setToggleGroup(metricOptions);
        cpmMetric.setOnAction(e -> {
            chartViewer.setChart(genCPMChart());
        });

        RadioButton bounceRateMetric = new RadioButton("Bounce Rate");
        bounceRateMetric.setToggleGroup(metricOptions);
        bounceRateMetric.setOnAction(e -> {
            chartViewer.setChart(genBounceRateChart());
        });

        var metricsHeader = new Label("Metrics");
        var metricOptionHolder = new VBox();
        metricOptionHolder.getChildren().addAll(metricsHeader,clickMetric,impressionMetric,uniqueMetric,bouncesMetric,conversionMetric,costMetric,ctrMetric,cpaMetric,cpcMetric,cpmMetric,bounceRateMetric);


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
    public JFreeChart genClickChart() {
        ArrayList<String[]> clicks = logManager.getClickData();
        Map<String,Integer> clickMap = new TreeMap<>();

        clickMap = getDailyClicks(clicks);
        var dataset = createIntegerDataset(clickMap);
        return(createChart("Total Clicks","Clicks",dataset));
    }

    /**
     * Generates chart for total impressions
     * @return
     */
    public JFreeChart genImpressionChart() {
        ArrayList<String[]> impressions = logManager.getImpressionData();
        Map<String,Integer> impressionMap = new TreeMap<>();

        impressionMap = getDailyImpressions(impressions);
        var dataset = createIntegerDataset(impressionMap);
        return(createChart("Total impressions","Impressions",dataset));

    }

    /**
     * Create chart for total uniques
     * @return
     */
    public JFreeChart genUniquesChart() {
        ArrayList<String[]> clicks = logManager.getClickData();
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

        var dataset = createIntegerDataset(clickMap);
        return(createChart("Total Uniques","Uniques",dataset));
    }

    /**
     * Create chart for total bounces
     * @return
     */
    public JFreeChart genBounceChart() {
        ArrayList<String[]> interactions = logManager.getServerData();
        Map<String,Integer> interactionMap = new TreeMap<>();

        interactionMap = getDailyBounces(interactions);
        var dataset = createIntegerDataset(interactionMap);
        return(createChart("Total Bounces","Bounces",dataset));
    }
    /**
     * Create chart for total conversions
     * @return
     */
    public JFreeChart genConversionChart() {
        ArrayList<String[]> interactions = logManager.getServerData();
        Map<String,Integer> conversionMap = new TreeMap<>();

        conversionMap = getDailyConversions(interactions);
        var dataset = createIntegerDataset(conversionMap);
        return(createChart("Total conversions","Conversions",dataset));
    }


    /**
     * Create chart for total cost
     * @return
     */
    public JFreeChart genCostChart() {
        ArrayList<String[]> clicks = logManager.getClickData();
        ArrayList<String[]> impressions = logManager.getImpressionData();
        Map<String,Float> costMap = new TreeMap<>();

        costMap = getDailyCost(clicks,impressions);
        var dataset = createFloatDataset(costMap);
        return(createChart("Total cost","Cost",dataset));

    }

    /**
     * Create chart for CTR
     * @return
     */
    public JFreeChart genCTRChart() {
        ArrayList<String[]> clicks = logManager.getClickData();
        ArrayList<String[]> impressions = logManager.getImpressionData();
        Map<String,Integer> clickMap = new TreeMap<>();
        Map<String,Integer> impressionMap = new TreeMap<>();
        Map<String,Float> ctrMap = new TreeMap<>();

        clickMap = getDailyClicks(clicks);
        impressionMap = getDailyImpressions(impressions);

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
    public JFreeChart genCPAChart() {
        ArrayList<String[]> clicks = logManager.getClickData();
        ArrayList<String[]> interactions = logManager.getServerData();
        ArrayList<String[]> impressions = logManager.getImpressionData();
        Map<String,Integer> conversionMap = new TreeMap<>();
        Map<String,Float> costMap = new TreeMap<>();
        Map<String,Float> cpaMap = new TreeMap<>();

        conversionMap = getDailyConversions(interactions);
        costMap = getDailyCost(clicks,impressions);

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
    public JFreeChart genCPCChart() {
        ArrayList<String[]> clicks = logManager.getClickData();
        ArrayList<String[]> impressions = logManager.getImpressionData();
        Map<String,Integer> clickMap = new TreeMap<>();
        Map<String,Float> costMap = new TreeMap<>();
        Map<String,Float> cpcMap = new TreeMap<>();

        clickMap = getDailyClicks(clicks);
        costMap = getDailyCost(clicks,impressions);

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
    public JFreeChart genCPMChart() {
        ArrayList<String[]> clicks = logManager.getClickData();
        ArrayList<String[]> impressions = logManager.getImpressionData();
        Map<String,Integer> impressionMap = new TreeMap<>();
        Map<String,Float> costMap = new TreeMap<>();
        Map<String,Float> cpmMap = new TreeMap<>();

        impressionMap = getDailyImpressions(impressions);
        costMap = getDailyCost(clicks,impressions);

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
    public JFreeChart genBounceRateChart() {
        ArrayList<String[]> clicks = logManager.getClickData();
        ArrayList<String[]> interactions = logManager.getServerData();
        Map<String,Integer> clickMap = new TreeMap<>();
        Map<String,Integer> bounceMap = new TreeMap<>();
        Map<String,Float> bounceRateMap = new TreeMap<>();

        clickMap = getDailyClicks(clicks);
        bounceMap = getDailyBounces(interactions);

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




}

