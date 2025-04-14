package com.example;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ChartCreator {

    private ArrayList<String[]> clicks;
    private ArrayList<String[]> impressions;
    private ArrayList<String[]> interactions;
    private Filter filter;
    private int timeSpent;

    public ChartCreator(LogManager logManager, int timeSpent) {
        this.clicks = logManager.getClickData();
        this.impressions = logManager.getImpressionData();
        this.interactions = logManager.getServerData();
        this.filter = new Filter();
        this.timeSpent = timeSpent;
    }

    /**
     * Set time requirement for bounce
     * @param time
     */
    public void setTimeSpent(int time) {
        this.timeSpent = time;
    }


    public void getClicks() {
        System.out.println(this.clicks);
    }

    /**
     * Generates chart for total clicks
     */
    public JFreeChart genClickChart(String time,String gender,String income,ArrayList<String> context,ArrayList<String> age) {
        Map<String,Integer> clickMap = new TreeMap<>();
        ArrayList<String[]> filteredClicks= filter.filterPipeline(clicks,impressions,interactions,gender,income,context,age)[0];
        if (time.equals("Daily")) {
            clickMap = getDailyClicks(filteredClicks);
        } else if (time.equals("Weekly")) {
            clickMap = getWeeklyClicks(filteredClicks);
        } else if (time.equals("Monthly")) {
            clickMap = getMonthlyClicks(filteredClicks);
        }

        DefaultCategoryDataset dataset = createIntegerDataset(clickMap);
        System.out.println("Row count: " + dataset.getRowCount());
        System.out.println("Column count: " + dataset.getColumnCount());
        return(createChart("Total Clicks","Clicks",dataset));
    }

    /**
     * Generates chart for total impressions
     * @return
     */
    public JFreeChart genImpressionChart(String timeFlag,String gender,String income,ArrayList<String> context,ArrayList<String> age) {
        Map<String,Integer> impressionMap = new TreeMap<>();
        ArrayList<String[]> filteredImpressions= filter.filterPipeline(clicks,impressions,interactions,gender,income,context,age)[1];

        if (timeFlag.equals("Daily")) {
            impressionMap = getDailyImpressions(filteredImpressions);
        } else if (timeFlag.equals("Weekly")) {
            impressionMap = getWeeklyImpressions(filteredImpressions);
        } else if (timeFlag.equals("Monthly")) {
            impressionMap = getMonthlyImpressions(filteredImpressions);
        }

        var dataset = createIntegerDataset(impressionMap);
        return(createChart("Total impressions","Impressions",dataset));
    }

    /**
     * Create chart for total uniques
     * @return
     */
    public JFreeChart genUniquesChart(String timeFlag,String gender,String income,ArrayList<String> context,ArrayList<String> age) {
        Map<String,Integer> clickMap = new TreeMap<>();
        ArrayList<String[]> filteredClicks= filter.filterPipeline(clicks,impressions,interactions,gender,income,context,age)[0];

        if (timeFlag.equals("Daily")) {
            clickMap = getDailyUniques(filteredClicks);
        } else if (timeFlag.equals("Weekly")) {
            clickMap = getWeeklyUniques(filteredClicks);
        } else if (timeFlag.equals("Monthly")) {
            clickMap = getMonthlyUniques(filteredClicks);
        }

        var dataset = createIntegerDataset(clickMap);
        return(createChart("Total Uniques","Uniques",dataset));
    }

    /**
     * Create chart for total bounces
     * @return
     */
    public JFreeChart genBounceChart(String timeFlag,String gender,String income,ArrayList<String> context,ArrayList<String> age) {
        Map<String,Integer> interactionMap = new TreeMap<>();
        ArrayList<String[]> filteredInteractions= filter.filterPipeline(clicks,impressions,interactions,gender,income,context,age)[2];

        if (timeFlag.equals("Daily")) {
            interactionMap = getDailyBounces(filteredInteractions,timeSpent);
        } else if (timeFlag.equals("Weekly")) {
            interactionMap = getWeeklyBounces(filteredInteractions,timeSpent);
        } else if (timeFlag.equals("Monthly")) {
            interactionMap = getMonthlyBounces(filteredInteractions,timeSpent);
        }

        var dataset = createIntegerDataset(interactionMap);
        return(createChart("Total Bounces","Bounces",dataset));
    }
    /**
     * Create chart for total conversions
     * @return
     */
    public JFreeChart genConversionChart(String timeFlag,String gender,String income,ArrayList<String> context,ArrayList<String> age) {
        Map<String,Integer> conversionMap = new TreeMap<>();
        ArrayList<String[]> filteredInteractions= filter.filterPipeline(clicks,impressions,interactions,gender,income,context,age)[2];

        if (timeFlag.equals("Daily")) {
            conversionMap = getDailyConversions(filteredInteractions);
        } else if (timeFlag.equals("Weekly")) {
            conversionMap = getWeeklyConversions(filteredInteractions);
        }else if (timeFlag.equals("Monthly")) {
            conversionMap = getMonthlyConversions(filteredInteractions);
        }

        var dataset = createIntegerDataset(conversionMap);
        return(createChart("Total conversions","Conversions",dataset));
    }


    /**
     * Create chart for total cost
     * @return
     */
    public JFreeChart genCostChart(String timeFlag,String gender,String income,ArrayList<String> context,ArrayList<String> age) {
        Map<String,Float> costMap = new TreeMap<>();
        ArrayList<String[]> [] filteredLogs = filter.filterPipeline(clicks,impressions,interactions,gender,income,context,age);
        ArrayList<String[]> filteredClicks = filteredLogs[0];
        ArrayList<String[]> filteredImpressions = filteredLogs[1];

        if (timeFlag.equals("Daily")) {
            costMap = getDailyCost(filteredClicks,filteredImpressions);
        } else if (timeFlag.equals("Weekly")) {
            costMap = getWeeklyCost(filteredClicks,filteredImpressions);
        } else if (timeFlag.equals("Monthly")) {
            costMap = getMonthlyCost(filteredClicks,filteredImpressions);
        }

        var dataset = createFloatDataset(costMap);
        return(createChart("Total cost","Cost",dataset));

    }

    /**
     * Create chart for CTR
     * @return
     */
    public JFreeChart genCTRChart(String timeFlag,String gender, String income,ArrayList<String> context,ArrayList<String> age) {
        Map<String,Integer> clickMap = new TreeMap<>();
        Map<String,Integer> impressionMap = new TreeMap<>();
        Map<String,Float> ctrMap = new TreeMap<>();
        ArrayList<String[]> [] filteredLogs = filter.filterPipeline(clicks,impressions,interactions,gender,income,context,age);
        ArrayList<String[]> filteredClicks = filteredLogs[0];
        ArrayList<String[]> filteredImpressions = filteredLogs[1];

        if(timeFlag.equals("Daily")) {
            clickMap = getDailyClicks(filteredClicks);
            impressionMap = getDailyImpressions(filteredImpressions);
        } else if (timeFlag.equals("Weekly")){
            clickMap = getWeeklyClicks(filteredClicks);
            impressionMap = getWeeklyImpressions(filteredImpressions);
        } else if (timeFlag.equals("Monthly")){
            clickMap = getMonthlyClicks(filteredClicks);
            impressionMap = getMonthlyImpressions(filteredImpressions);
        }

        for(String date : impressionMap.keySet()) {
            int clicks = clickMap.getOrDefault(date,0);
            int impressions = impressionMap.get(date);
            float ctr = (float) clicks/impressions;
            ctrMap.put(date,ctr);
        }

        var dataset = createFloatDataset(ctrMap);
        return(createChart("CTR", "Clicks per impression", dataset));
    }

    /**
     * Create chart for CPA
     * @return
     */
    public JFreeChart genCPAChart(String timeFlag,String gender,String income,ArrayList<String> context,ArrayList<String> age) {
        Map<String,Integer> conversionMap = new TreeMap<>();
        Map<String,Float> costMap = new TreeMap<>();
        Map<String,Float> cpaMap = new TreeMap<>();
        ArrayList<String[]> [] filteredLogs = filter.filterPipeline(clicks,impressions,interactions,gender,income,context,age);
        ArrayList<String[]> filteredClicks = filteredLogs[0];
        ArrayList<String[]> filteredImpressions = filteredLogs[1];
        ArrayList<String[]> filteredInteractions = filteredLogs[2];


        if (timeFlag.equals("Daily")) {
            conversionMap = getDailyConversions(filteredInteractions);
            costMap = getDailyCost(filteredClicks, filteredImpressions);
        } else if (timeFlag.equals("Weekly")) {
            conversionMap = getWeeklyConversions(filteredInteractions);
            costMap = getWeeklyCost(filteredClicks,filteredImpressions);
        } else if (timeFlag.equals("Monthly")) {
            conversionMap = getMonthlyConversions(filteredInteractions);
            costMap = getMonthlyCost(filteredClicks,filteredImpressions);
        }

        for(String date : conversionMap.keySet()) {
            float cost = costMap.getOrDefault(date,(float) 0);
            int conversions = conversionMap.get(date);
            float cpa = cost/conversions;
            cpaMap.put(date,cpa);
        }

        var dataset = createFloatDataset(cpaMap);
        return(createChart("CPA","Cost per conversion",dataset));
    }

    /**
     * Create chart for CPC
     * @return
     */
    public JFreeChart genCPCChart(String timeFlag, String gender,String income,ArrayList<String> context,ArrayList<String> age) {
        Map<String,Integer> clickMap = new TreeMap<>();
        Map<String,Float> costMap = new TreeMap<>();
        Map<String,Float> cpcMap = new TreeMap<>();
        ArrayList<String[]> [] filteredLogs = filter.filterPipeline(clicks,impressions,interactions,gender,income,context,age);
        ArrayList<String[]> filteredClicks = filteredLogs[0];
        ArrayList<String[]> filteredImpressions = filteredLogs[1];


        if (timeFlag.equals("Daily")) {
            clickMap = getDailyClicks(filteredClicks);
            costMap = getDailyCost(filteredClicks, filteredImpressions);
        } else if (timeFlag.equals("Weekly")) {
            clickMap = getWeeklyClicks(filteredClicks);
            costMap = getWeeklyCost(filteredClicks,filteredImpressions);
        } else if (timeFlag.equals("Monthly")) {
            clickMap = getMonthlyClicks(filteredClicks);
            costMap = getMonthlyCost(filteredClicks,filteredImpressions);
        }

        for(String date : clickMap.keySet()) {
            float cost = costMap.getOrDefault(date,(float) 0);
            int clicks = clickMap.get(date);
            float cpc = cost/clicks;
            cpcMap.put(date,cpc);
        }

        var dataset = createFloatDataset(cpcMap);
        return(createChart("CPC","Cost per click",dataset));
    }

    /**
     * Generate chart for CPM
     * @return
     */
    public JFreeChart genCPMChart(String timeFlag,String gender,String income,ArrayList<String> context,ArrayList<String> age) {
        Map<String,Integer> impressionMap = new TreeMap<>();
        Map<String,Float> costMap = new TreeMap<>();
        Map<String,Float> cpmMap = new TreeMap<>();
        ArrayList<String[]> [] filteredLogs = filter.filterPipeline(clicks,impressions,interactions,gender,income,context,age);
        ArrayList<String[]> filteredClicks = filteredLogs[0];
        ArrayList<String[]> filteredImpressions = filteredLogs[1];


        if (timeFlag.equals("Daily")) {
            impressionMap = getDailyImpressions(filteredImpressions);
            costMap = getDailyCost(filteredClicks, filteredImpressions);
        } else if (timeFlag.equals("Weekly")) {
            impressionMap = getWeeklyImpressions(filteredImpressions);
            costMap = getWeeklyCost(filteredClicks,filteredImpressions);
        } else if (timeFlag.equals("Monthly")) {
            impressionMap = getMonthlyImpressions(filteredImpressions);
            costMap = getMonthlyCost(filteredClicks,filteredImpressions);
        }

        //Convert impressions into thousands
        Map<String,Float> impressionThousandMap = new TreeMap<>();
        for (Map.Entry<String,Integer> entry : impressionMap.entrySet()) {
            impressionThousandMap.put(entry.getKey(),(float) entry.getValue()/1000);
        }

        for(String date : impressionThousandMap.keySet()) {
            float cost = costMap.get(date);
            float thousandImpressions = impressionThousandMap.get(date);
            float cpm = cost/thousandImpressions;
            cpmMap.put(date,cpm);
        }

        var dataset = createFloatDataset(cpmMap);
        return(createChart("CPM","Cost per thousand impressions",dataset));
    }

    /**
     * Create chart for bounce rate
     * @return
     */
    public JFreeChart genBounceRateChart(String timeFlag, String gender,String income,ArrayList<String> context,ArrayList<String> age) {
        Map<String,Integer> clickMap = new TreeMap<>();
        Map<String,Integer> bounceMap = new TreeMap<>();
        Map<String,Float> bounceRateMap = new TreeMap<>();
        ArrayList<String[]> [] filteredLogs = filter.filterPipeline(clicks,impressions,interactions,gender,income,context,age);
        ArrayList<String[]> filteredClicks = filteredLogs[0];
        ArrayList<String[]> filteredInteractions = filteredLogs[2];

        if (timeFlag.equals("Daily")) {
            clickMap = getDailyClicks(filteredClicks);
            bounceMap = getDailyBounces(filteredInteractions,timeSpent);
        } else if (timeFlag.equals("Weekly")) {
            clickMap = getWeeklyClicks(filteredClicks);
            bounceMap = getWeeklyBounces(filteredInteractions,timeSpent);
        } else if (timeFlag.equals("Monthly")) {
            clickMap = getMonthlyClicks(filteredClicks);
            bounceMap = getMonthlyBounces(filteredInteractions,timeSpent);
        }

        for (String date : clickMap.keySet()) {
            int clicks = clickMap.get(date);
            int bounces = bounceMap.get(date);
            float bounceRate = (float) bounces/clicks;
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

        //Creating marker dot at plot points
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesLinesVisible(0, true);
        Shape dot = new Ellipse2D.Double(-5, -5, 10, 10);
        renderer.setSeriesShape(0, dot);
        plot.setRenderer(renderer);

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
            clickMap.put(date, clickMap.getOrDefault(date, 0) + 1);
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
            clickMap.put(week,clickMap.getOrDefault(week,0) + 1);
        }

        return clickMap;
    }

    /**
     * Accumulate monthly clicks
     * @param clicks
     * @return
     */
    public Map<String,Integer> getMonthlyClicks (ArrayList<String[]> clicks) {
        Map<String,Integer> clickMap = new TreeMap<>();

        LocalDate earliestDate = getEarliestDate(clicks);
        for (String[] click : clicks) {
            LocalDate date = LocalDate.parse(click[0].split(" ")[0]);
            int monthNumber = (int) ChronoUnit.MONTHS.between(earliestDate,date) + 1;
            String month = "Month " + Integer.toString(monthNumber);
            clickMap.put(month,clickMap.getOrDefault(month,0) + 1);
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
            impressionMap.put(date, impressionMap.getOrDefault(date, 0) + 1);
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
            impressionMap.put(week,impressionMap.getOrDefault(week,0) + 1);
        }
        return impressionMap;
    }

    /**
     * Accumulate monthly impressions
     * @param impressions
     * @return
     */
    public Map<String,Integer> getMonthlyImpressions (ArrayList<String[]> impressions) {
        Map<String,Integer> impressionMap = new TreeMap<>();

        LocalDate earliestDate = getEarliestDate(impressions);
        for (String[] impression : impressions) {
            LocalDate date = LocalDate.parse(impression[0].split(" ")[0]);
            int monthNumber = (int) ChronoUnit.MONTHS.between(earliestDate,date) + 1;
            String month = "Month " + Integer.toString(monthNumber);
            impressionMap.put(month,impressionMap.getOrDefault(month,0) + 1);
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

                clickMap.put(date,clickMap.getOrDefault(date,0)+1);
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
                clickMap.put(week,clickMap.getOrDefault(week,0)+1);
            }
        }
        return clickMap;

    }

    /**
     * Accumulate monthly uniques
     * @param clicks
     * @return
     */
    public Map<String,Integer> getMonthlyUniques (ArrayList<String[]> clicks) {
        Map<String,Integer> clickMap = new TreeMap<>();
        Set<String> uniqueIDs = new HashSet<>();

        LocalDate earliestDate = getEarliestDate(clicks);

        for(String[] click : clicks) {
            LocalDate date = LocalDate.parse(click[0].split(" ")[0]);
            int monthNumber = (int) ChronoUnit.MONTHS.between(earliestDate,date) + 1;
            String month = "Month " + Integer.toString(monthNumber);
            String id = click[1];

            if (!uniqueIDs.contains(id)) {
                uniqueIDs.add(id);
                clickMap.put(month,clickMap.getOrDefault(month,0)+1);
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
                conversionMap.put(date, conversionMap.getOrDefault(date, 0) + 1);
            } else {
                String date = interaction[0].split(" ")[0];
                conversionMap.put(date, 0);
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
                conversionMap.put(week,conversionMap.getOrDefault(week,0) + 1);
            } else {
                LocalDate date = LocalDate.parse(interaction[0].split(" ")[0]);
                int weekNumber = (int) ChronoUnit.WEEKS.between(earliestDate,date) + 1;
                String week = "Week " + Integer.toString(weekNumber);
                conversionMap.put(week,conversionMap.getOrDefault(week,0));
            }
        }
        return conversionMap;
    }

    /**
     * Accumulate monthly conversions
     * @param interactions
     * @return
     */
    public Map<String,Integer> getMonthlyConversions(ArrayList<String[]> interactions) {
        Map<String,Integer> conversionMap = new TreeMap<>();

        LocalDate earliestDate = getEarliestDate(interactions);
        for (String[] interaction : interactions) {
            if (interaction[4].equals("Yes")) {
                LocalDate date = LocalDate.parse(interaction[0].split(" ")[0]);
                int monthNumber = (int) ChronoUnit.MONTHS.between(earliestDate,date) + 1;
                String month = "Month " + Integer.toString(monthNumber);
                conversionMap.put(month,conversionMap.getOrDefault(month,0) + 1);
            } else {
                LocalDate date = LocalDate.parse(interaction[0].split(" ")[0]);
                int monthNumber = (int) ChronoUnit.MONTHS.between(earliestDate,date) + 1;
                String month = "Month " + Integer.toString(monthNumber);
                conversionMap.put(month,conversionMap.getOrDefault(month,0));
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
            costMap.put(date,costMap.getOrDefault(date,(float) 0) + cost);
        }

        for (String[] impression: impressions) {
            String dateTime = impression[0];
            String date = dateTime.split(" ")[0];
            float cost = Float.parseFloat(impression[6]);
            costMap.put(date,costMap.getOrDefault(date,(float) 0) + cost);
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
            costMap.put(week,costMap.getOrDefault(week,(float) 0) + cost);
        }

        for (String[] impression : impressions) {
            LocalDate date = LocalDate.parse(impression[0].split(" ")[0]);
            int weekNumber = (int) ChronoUnit.WEEKS.between(earliestDate,date) + 1;
            String week = "Week " + Integer.toString(weekNumber);
            float cost = Float.parseFloat(impression[6]);
            costMap.put(week,costMap.getOrDefault(week,(float) 0) + cost);
        }
        return costMap;
    }

    /**
     * Accumulate monthly cost
     * @param clicks
     * @param impressions
     * @return
     */
    public Map<String,Float> getMonthlyCost (ArrayList<String[]> clicks, ArrayList<String[]> impressions) {
        Map<String,Float> costMap = new TreeMap<>();

        LocalDate earliestDate = getEarliestDate(impressions);
        for (String[] click : clicks) {
            LocalDate date = LocalDate.parse(click[0].split(" ")[0]);
            int monthNumber = (int) ChronoUnit.MONTHS.between(earliestDate,date) + 1;
            String month = "Month " + Integer.toString(monthNumber);
            float cost = Float.parseFloat(click[2]);
            costMap.put(month,costMap.getOrDefault(month,(float) 0) + cost);
        }

        for (String[] impression : impressions) {
            LocalDate date = LocalDate.parse(impression[0].split(" ")[0]);
            int monthNumber = (int) ChronoUnit.MONTHS.between(earliestDate,date) + 1;
            String month = "Month " + Integer.toString(monthNumber);
            float cost = Float.parseFloat(impression[6]);
            costMap.put(month,costMap.getOrDefault(month, (float) 0) + cost);
        }
        return costMap;
    }

    /**
     * Accumulate daily bounces
     * @param interactions
     * @return
     */
    public Map<String,Integer> getDailyBounces(ArrayList<String[]> interactions, int time) {
        Map<String,Integer> interactionMap = new TreeMap<>();
        if (time == 0) {
            for (String[] interaction : interactions){
                if (interaction[3].equals("1")) {
                    String dateTime = interaction[0];
                    String date = dateTime.split(" ")[0];

                    interactionMap.put(date, interactionMap.getOrDefault(date, 0) + 1);
                }
            }
        } else {
            for (String[] interaction : interactions) {
                if (interaction[2].trim().equalsIgnoreCase("n/a")) {
                    continue;
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime entryTime = LocalDateTime.parse(interaction[0],formatter);
                LocalDateTime exitTime = LocalDateTime.parse(interaction[2],formatter);

                long timeDifference = Duration.between(entryTime,exitTime).getSeconds();

                if (timeDifference <= time) {
                    String dateTime = interaction[0];
                    String date = dateTime.split(" ")[0];

                    interactionMap.put(date,interactionMap.getOrDefault(date,0) + 1);
                }
            }
        }
        return interactionMap;
    }

    /**
     * Accumulate weekly bounces
     * @param interactions
     * @return
     */
    public Map<String, Integer> getWeeklyBounces(ArrayList<String[]> interactions,int time) {
        Map<String,Integer> bounceMap = new TreeMap<>();

        LocalDate earliestDate = getEarliestDate(interactions);
        if (time == 0) {
            for (String[] interaction : interactions) {
                if (interaction[3].equals("1")) {
                    LocalDate date = LocalDate.parse(interaction[0].split(" ")[0]);
                    int weekNumber = (int) ChronoUnit.WEEKS.between(earliestDate,date) + 1;
                    String week = "Week " + Integer.toString(weekNumber);
                    bounceMap.put(week,bounceMap.getOrDefault(week,0) + 1);
                }
            }
        } else {
            for (String[] interaction : interactions) {
                if (interaction[2].trim().equalsIgnoreCase("n/a")) {
                    continue;
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime entryTime = LocalDateTime.parse(interaction[0],formatter);
                LocalDateTime exitTime = LocalDateTime.parse(interaction[2],formatter);

                long timeDifference = Duration.between(entryTime,exitTime).getSeconds();

                if (timeDifference <= time) {
                    LocalDate date = LocalDate.parse(interaction[0].split(" ")[0]);
                    int weekNumber = (int) ChronoUnit.WEEKS.between(earliestDate,date) + 1;
                    String week = "Week " + Integer.toString(weekNumber);
                    bounceMap.put(week,bounceMap.getOrDefault(week,0) + 1);
                }
            }
        }
        return bounceMap;
    }

    /**
     * Accumulate monthly bounces
     * @param interactions
     * @return
     */
    public Map<String, Integer> getMonthlyBounces(ArrayList<String[]> interactions,int time) {
        Map<String,Integer> bounceMap = new TreeMap<>();

        LocalDate earliestDate = getEarliestDate(interactions);
        if (time == 0) {
            for (String[] interaction : interactions) {
                if (interaction[3].equals("1")) {
                    LocalDate date = LocalDate.parse(interaction[0].split(" ")[0]);
                    int monthNumber = (int) ChronoUnit.MONTHS.between(earliestDate,date) + 1;
                    String month = "Month " + Integer.toString(monthNumber);
                    bounceMap.put(month,bounceMap.getOrDefault(month,0) + 1);
                }
            }
        } else {
            for (String[] interaction : interactions) {
                if (interaction[2].trim().equalsIgnoreCase("n/a")) {
                    continue;
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime entryTime = LocalDateTime.parse(interaction[0],formatter);
                LocalDateTime exitTime = LocalDateTime.parse(interaction[2],formatter);

                long timeDifference = Duration.between(entryTime,exitTime).getSeconds();

                if (timeDifference <= time) {
                    LocalDate date = LocalDate.parse(interaction[0].split(" ")[0]);
                    int monthNumber = (int) ChronoUnit.MONTHS.between(earliestDate,date) + 1;
                    String month = "Month " + Integer.toString(monthNumber);
                    bounceMap.put(month,bounceMap.getOrDefault(month,0) + 1);
                }
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

    public void saveChartAsPdf(WritableImage chartImage, Window window) throws IOException, DocumentException, NullPointerException {
        if (chartImage == null) {
            throw new NullPointerException();
        }
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(chartImage, null);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Chart as PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));
        File file = fileChooser.showSaveDialog(window);

        if (file != null) {
            if (!file.getName().toLowerCase().endsWith(".pdf")) {
                file = new File(file.getAbsolutePath() + ".pdf");
            }

            File chartFile = File.createTempFile("chart", ".png");
            ImageIO.write(bufferedImage, "png", chartFile);
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(chartFile.getAbsolutePath());
            image.scaleToFit(document.getPageSize().getWidth() - 50, document.getPageSize().getHeight() - 50);
            document.add(image);

            document.close();
            chartFile.delete();
        }
    }

    public void saveChartAsPdfToFile(BufferedImage chartImage, String outputPath) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(outputPath));
        document.open();

        File chartPdfFile = File.createTempFile("chart", ".png");
        ImageIO.write(chartImage, "PNG", chartPdfFile);
        com.itextpdf.text.Image pdfImage = com.itextpdf.text.Image.getInstance(chartPdfFile.getAbsolutePath());

        pdfImage.scaleToFit(500, 500);
        document.add(pdfImage);
        document.close();
    }



    /**
     * Updates chart with time filter applied
     * @param currentChart
     * @param timeFlag
     * @return
     */
    public JFreeChart updateChart(String currentChart, String timeFlag, String gender,String income, ArrayList<String> context, ArrayList<String> age) {
        if (currentChart.equals("Clicks")) {
            return (genClickChart(timeFlag,gender,income,context,age));
        } else if (currentChart.equals("Impressions")) {
            return (genImpressionChart(timeFlag,gender,income,context,age));
        } else if (currentChart.equals("Conversions")) {
            return (genConversionChart(timeFlag,gender,income,context,age));
        } else if (currentChart.equals("Bounces")) {
            return (genBounceChart(timeFlag,gender,income,context,age));
        } else if (currentChart.equals("Cost")) {
            return (genCostChart(timeFlag,gender,income,context,age));
        } else if (currentChart.equals("Uniques")) {
            return (genUniquesChart(timeFlag,gender,income,context,age));
        } else if (currentChart.equals("BounceRate")){
            return (genBounceRateChart(timeFlag,gender,income,context,age));
        } else if (currentChart.equals("CTR")) {
            return (genCTRChart(timeFlag,gender,income,context,age));
        } else if (currentChart.equals("CPA")) {
            return (genCPAChart(timeFlag,gender,income,context,age));
        } else if (currentChart.equals("CPC")) {
            return (genCPCChart(timeFlag,gender,income,context,age));
        } else {
            return (genCPMChart(timeFlag,gender,income,context,age));
        }

    }

}
