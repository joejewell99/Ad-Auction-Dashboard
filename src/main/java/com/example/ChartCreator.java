package com.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ChartCreator {

    private ArrayList<String[]> clicks;
    private ArrayList<String[]> impressions;
    private ArrayList<String[]> interactions;

    public ChartCreator(LogManager logManager) {
        this.clicks = logManager.getClickData();
        this.impressions = logManager.getImpressionData();
        this.interactions = logManager.getServerData();
    }

    /**
     * Generates chart for total clicks
     */
    public JFreeChart genClickChart(String time) {
        Map<String,Integer> clickMap = new TreeMap<>();
        if (time.equals("Daily")) {
            clickMap = getDailyClicks(this.clicks);
        } else if (time.equals("Weekly")) {
            clickMap = getWeeklyClicks(this.clicks);
        } else if (time.equals("Monthly")) {
            clickMap = getMonthlyClicks(this.clicks);
        }

        var dataset = createIntegerDataset(clickMap);
        return(createChart("Total Clicks","Clicks",dataset));
    }

    /**
     * Generates chart for total impressions
     * @return
     */
    public JFreeChart genImpressionChart(String timeFlag) {
        Map<String,Integer> impressionMap = new TreeMap<>();
        if (timeFlag.equals("Daily")) {
            impressionMap = getDailyImpressions(this.impressions);
        } else if (timeFlag.equals("Weekly")) {
            impressionMap = getWeeklyImpressions(this.impressions);
        } else if (timeFlag.equals("Monthly")) {
            impressionMap = getMonthlyImpressions(this.impressions);
        }

        var dataset = createIntegerDataset(impressionMap);
        return(createChart("Total impressions","Impressions",dataset));
    }

    /**
     * Create chart for total uniques
     * @return
     */
    public JFreeChart genUniquesChart(String timeFlag) {
        Map<String,Integer> clickMap = new TreeMap<>();

        if (timeFlag.equals("Daily")) {
            clickMap = getDailyUniques(this.clicks);
        } else if (timeFlag.equals("Weekly")) {
            clickMap = getWeeklyUniques(this.clicks);
        } else if (timeFlag.equals("Monthly")) {
            clickMap = getMonthlyUniques(this.clicks);
        }

        var dataset = createIntegerDataset(clickMap);
        return(createChart("Total Uniques","Uniques",dataset));
    }

    /**
     * Create chart for total bounces
     * @return
     */
    public JFreeChart genBounceChart(String timeFlag) {
        Map<String,Integer> interactionMap = new TreeMap<>();

        if (timeFlag.equals("Daily")) {
            interactionMap = getDailyBounces(this.interactions);
        } else if (timeFlag.equals("Weekly")) {
            interactionMap = getWeeklyBounces(this.interactions);
        } else if (timeFlag.equals("Monthly")) {
            interactionMap = getMonthlyBounces(this.interactions);
        }

        var dataset = createIntegerDataset(interactionMap);
        return(createChart("Total Bounces","Bounces",dataset));
    }
    /**
     * Create chart for total conversions
     * @return
     */
    public JFreeChart genConversionChart(String timeFlag) {
        Map<String,Integer> conversionMap = new TreeMap<>();
        if (timeFlag.equals("Daily")) {
            conversionMap = getDailyConversions(this.interactions);
        } else if (timeFlag.equals("Weekly")) {
            conversionMap = getWeeklyConversions(this.interactions);
        }else if (timeFlag.equals("Monthly")) {
            conversionMap = getMonthlyConversions(this.interactions);
        }

        var dataset = createIntegerDataset(conversionMap);
        return(createChart("Total conversions","Conversions",dataset));
    }


    /**
     * Create chart for total cost
     * @return
     */
    public JFreeChart genCostChart(String timeFlag) {
        Map<String,Float> costMap = new TreeMap<>();
        if (timeFlag.equals("Daily")) {
            costMap = getDailyCost(this.clicks,this.impressions);
        } else if (timeFlag.equals("Weekly")) {
            costMap = getWeeklyCost(this.clicks,this.impressions);
        } else if (timeFlag.equals("Monthly")) {
            costMap = getMonthlyCost(this.clicks,this.impressions);
        }

        var dataset = createFloatDataset(costMap);
        return(createChart("Total cost","Cost",dataset));

    }

    /**
     * Create chart for CTR
     * @return
     */
    public JFreeChart genCTRChart(String timeFlag) {
        Map<String,Integer> clickMap = new TreeMap<>();
        Map<String,Integer> impressionMap = new TreeMap<>();
        Map<String,Float> ctrMap = new TreeMap<>();

        if(timeFlag.equals("Daily")) {
            clickMap = getDailyClicks(this.clicks);
            impressionMap = getDailyImpressions(this.impressions);
        } else if (timeFlag.equals("Weekly")){
            clickMap = getWeeklyClicks(this.clicks);
            impressionMap = getWeeklyImpressions(this.impressions);
        } else if (timeFlag.equals("Monthly")){
            clickMap = getMonthlyClicks(this.clicks);
            impressionMap = getMonthlyImpressions(this.impressions);
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
    public JFreeChart genCPAChart(String timeFlag) {
        Map<String,Integer> conversionMap = new TreeMap<>();
        Map<String,Float> costMap = new TreeMap<>();
        Map<String,Float> cpaMap = new TreeMap<>();

        if (timeFlag.equals("Daily")) {
            conversionMap = getDailyConversions(this.interactions);
            costMap = getDailyCost(this.clicks, this.impressions);
        } else if (timeFlag.equals("Weekly")) {
            conversionMap = getWeeklyConversions(this.interactions);
            costMap = getWeeklyCost(this.clicks,this.impressions);
        } else if (timeFlag.equals("Monthly")) {
            conversionMap = getMonthlyConversions(this.interactions);
            costMap = getMonthlyCost(this.clicks,this.impressions);
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
    public JFreeChart genCPCChart(String timeFlag) {
        Map<String,Integer> clickMap = new TreeMap<>();
        Map<String,Float> costMap = new TreeMap<>();
        Map<String,Float> cpcMap = new TreeMap<>();

        if (timeFlag.equals("Daily")) {
            clickMap = getDailyClicks(this.clicks);
            costMap = getDailyCost(this.clicks, this.impressions);
        } else if (timeFlag.equals("Weekly")) {
            clickMap = getWeeklyClicks(this.clicks);
            costMap = getWeeklyCost(this.clicks,this.impressions);
        } else if (timeFlag.equals("Monthly")) {
            clickMap = getMonthlyClicks(this.clicks);
            costMap = getMonthlyCost(this.clicks,this.impressions);
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
    public JFreeChart genCPMChart(String timeFlag) {
        Map<String,Integer> impressionMap = new TreeMap<>();
        Map<String,Float> costMap = new TreeMap<>();
        Map<String,Float> cpmMap = new TreeMap<>();

        if (timeFlag.equals("Daily")) {
            impressionMap = getDailyImpressions(this.impressions);
            costMap = getDailyCost(this.clicks, this.impressions);
        } else if (timeFlag.equals("Weekly")) {
            impressionMap = getWeeklyImpressions(this.impressions);
            costMap = getWeeklyCost(this.clicks,this.impressions);
        } else if (timeFlag.equals("Monthly")) {
            impressionMap = getMonthlyImpressions(this.impressions);
            costMap = getMonthlyCost(this.clicks,this.impressions);
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
    public JFreeChart genBounceRateChart(String timeFlag) {
        Map<String,Integer> clickMap = new TreeMap<>();
        Map<String,Integer> bounceMap = new TreeMap<>();
        Map<String,Float> bounceRateMap = new TreeMap<>();

        if (timeFlag.equals("Daily")) {
            clickMap = getDailyClicks(this.clicks);
            bounceMap = getDailyBounces(this.interactions);
        } else if (timeFlag.equals("Weekly")) {
            clickMap = getWeeklyClicks(this.clicks);
            bounceMap = getWeeklyBounces(this.interactions);
        } else if (timeFlag.equals("Monthly")) {
            clickMap = getMonthlyClicks(this.clicks);
            bounceMap = getMonthlyBounces(this.interactions);
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
            clickMap.put(month,clickMap.getOrDefault(month,1) + 1);
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
            impressionMap.put(month,impressionMap.getOrDefault(month,1) + 1);
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
                clickMap.put(month,clickMap.getOrDefault(month,1)+1);
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
                conversionMap.put(month,conversionMap.getOrDefault(month,1) + 1);
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
            costMap.put(month,costMap.getOrDefault(month,cost) + cost);
        }

        for (String[] impression : impressions) {
            LocalDate date = LocalDate.parse(impression[0].split(" ")[0]);
            int monthNumber = (int) ChronoUnit.MONTHS.between(earliestDate,date) + 1;
            String month = "Month " + Integer.toString(monthNumber);
            float cost = Float.parseFloat(impression[6]);
            costMap.put(month,costMap.getOrDefault(month,cost) + cost);
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
     * Accumulate monthly bounces
     * @param interactions
     * @return
     */
    public Map<String, Integer> getMonthlyBounces(ArrayList<String[]> interactions) {
        Map<String,Integer> bounceMap = new TreeMap<>();

        LocalDate earliestDate = getEarliestDate(interactions);
        for (String[] interaction : interactions) {
            if (interaction[3].equals("1")) {
                LocalDate date = LocalDate.parse(interaction[0].split(" ")[0]);
                int monthNumber = (int) ChronoUnit.MONTHS.between(earliestDate,date) + 1;
                String month = "Month " + Integer.toString(monthNumber);
                bounceMap.put(month,bounceMap.getOrDefault(month,1) + 1);
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
