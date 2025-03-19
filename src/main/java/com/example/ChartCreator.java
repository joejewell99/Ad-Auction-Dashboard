package com.example;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
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

    public ChartCreator(LogManager logManager) {
        this.clicks = logManager.getClickData();
        this.impressions = logManager.getImpressionData();
        this.interactions = logManager.getServerData();
        this.filter = new Filter();
    }

    public ArrayList<String[]> getClicks() {
        return clicks;
    }

    /**
     * Generates chart for total clicks

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
     */

    public JFreeChart genClickChart(String time,String gender,String income,ArrayList<String> context,ArrayList<String> age) {
        // Sample data for the histogram (values just to create a meaningful single bar)
        // Create a dataset for the histogram with one bin
        HistogramDataset dataset = new HistogramDataset();
        // Create the chart
        JFreeChart chart = ChartFactory.createHistogram(
                "Total Clicks",  // Chart title
                "Day",              // X-axis label
                "Number Of Clicks",           // Y-axis label
                dataset,               // Dataset
                PlotOrientation.VERTICAL, // Vertical orientation
                true,                  // Include legend
                true,                  // Tooltips
                false                  // URLs
        );

        // Customize the chart appearance
        chart.setBackgroundPaint(Color.white);

        // Get the plot and set the range axis
        XYPlot plot = chart.getXYPlot();

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();

        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();

        if (time.equals("Daily")) {
            int numberOfClicks = getIntegerDailyClicks(getClicks(), getFormattedDateTime());
            double[] data = generateClickArray(numberOfClicks,0.0,24.0);
            dataset.addSeries(getFormattedDateTime().split(" ")[0], data, 1);  // Only one bin for the histogram
            rangeAxis.setRange(0, 50);  // Set the range from 0 to 50

            // Set the tick unit for the Y-axis (interval of 5)
            rangeAxis.setTickUnit(new org.jfree.chart.axis.NumberTickUnit(5));

            domainAxis.setRange(0, 24);  // Set X-axis range from 0 to 8
            domainAxis.setTickUnit(new NumberTickUnit(24));  // Set tick unit for X-axis


        } else if (time.equals("Weekly")) {
            LocalDate now = LocalDate.now();

            // Loop through the week (0 to 6)
            for (int i = 0; i < 7; i++) {
                // Get the Monday of the current week
                LocalDate monday = now.with(DayOfWeek.MONDAY);

                // Calculate the date for the specific day of the week
                LocalDate targetDate = monday.plusDays(i);
                int clicksOnDay = getIntegerDailyClicks(getClicks(), targetDate.toString());
                if (clicksOnDay > 0) {

                    // Switch statement to handle each day of the week
                    switch (i) {
                        case 0: // Monday
                            dataset.addSeries("Monday", generateClickArray(clicksOnDay, 0.0, 1.0), 1);  // Only one bin for the histogram
                            break;
                        case 1: // Tuesday
                            dataset.addSeries("Tuesday", generateClickArray(clicksOnDay, 1.0, 2.0), 1);  // Only one bin for the histogram
                            break;
                        case 2: // Wednesday
                            dataset.addSeries("Wednesday", generateClickArray(clicksOnDay, 2.0, 3.0), 1);  // Only one bin for the histogram
                            break;
                        case 3: // Thursday
                            dataset.addSeries("Thursday", generateClickArray(clicksOnDay, 3.0, 4.0), 1);  // Only one bin for the histogram
                            break;
                        case 4: // Friday
                            dataset.addSeries("Friday", generateClickArray(clicksOnDay, 4.0, 5.0), 1);  // Only one bin for the histogram
                            break;
                        case 5: // Saturday
                            dataset.addSeries("Saturday", generateClickArray(clicksOnDay, 5.0, 6.0), 1);  // Only one bin for the histogram
                            break;
                        case 6: // Sunday
                            dataset.addSeries("Sunday", generateClickArray(clicksOnDay, 6.0, 7.0), 1);  // Only one bin for the histogram
                            break;
                        default:
                            System.out.println("Invalid day");
                    }
                }
            }

            // Set the tick unit for the Y-axis (interval of 5)
            rangeAxis.setRange(0, 100);  // Set the range from 0 to 50
            rangeAxis.setTickUnit(new org.jfree.chart.axis.NumberTickUnit(5));

            domainAxis.setRange(0, 7);  // Set X-axis range from 0 to 8
            domainAxis.setTickUnit(new NumberTickUnit(1));  // Set tick unit for X-axis


        }else if (time.equals("Monthly")) {
            LocalDate now = LocalDate.now();
            // Get the current date
            LocalDate today = LocalDate.now();

            // Get the first day of the current month
            LocalDate firstDayOfMonth = today.withDayOfMonth(1);

            // Get the number of days in the current month
            int daysInMonth = today.lengthOfMonth();

            // Loop through the days of the month (1 to max day in month)
            for (int i = 1; i <= daysInMonth; i++) {
                // Generate the date for this specific day of the month
                LocalDate targetDate = firstDayOfMonth.plusDays(i - 1); // i-1 to adjust for the first day

                // Get the day of the week for the target date
                DayOfWeek dayOfWeek = targetDate.getDayOfWeek();

                // Format the date as "YYYY-MM-dd"
                String formattedDate = targetDate.toString();

                // Get the number of clicks for this specific day (assuming getClicks() is already defined)
                int clicksOnDay = getIntegerDailyClicks(getClicks(), formattedDate);
                double minXToDouble = i - 1.0;
                double maxXToDouble = i + 0.0;
                if (clicksOnDay > 0) {
                    // Switch statement to handle each day of the week
                    switch (dayOfWeek) {
                        case MONDAY:
                            dataset.addSeries("Monday" + formattedDate, generateClickArray(clicksOnDay, minXToDouble, maxXToDouble), 1);  // Only one bin for the histogram
                            break;
                        case TUESDAY:
                            dataset.addSeries("Tuesday", generateClickArray(clicksOnDay, minXToDouble, maxXToDouble), 1);  // Only one bin for the histogram
                            break;
                        case WEDNESDAY:
                            dataset.addSeries("Wednesday", generateClickArray(clicksOnDay, minXToDouble, maxXToDouble), 1);  // Only one bin for the histogram
                            break;
                        case THURSDAY:
                            dataset.addSeries("Thursday", generateClickArray(clicksOnDay, minXToDouble, maxXToDouble), 1);  // Only one bin for the histogram
                            break;
                        case FRIDAY:
                            dataset.addSeries("Friday", generateClickArray(clicksOnDay, minXToDouble, maxXToDouble), 1);  // Only one bin for the histogram
                            break;
                        case SATURDAY:
                            dataset.addSeries("Saturday", generateClickArray(clicksOnDay, minXToDouble, maxXToDouble), 1);  // Only one bin for the histogram
                            break;
                        case SUNDAY:
                            dataset.addSeries("Sunday", generateClickArray(clicksOnDay, minXToDouble, maxXToDouble), 1);  // Only one bin for the histogram
                            break;
                        default:
                            System.out.println("Invalid day");
                    }
                }
            }

            // Set the tick unit for the Y-axis (interval of 5)
            rangeAxis.setRange(0, 100);  // Set the range from 0 to 50
            rangeAxis.setTickUnit(new org.jfree.chart.axis.NumberTickUnit(5));

            domainAxis.setRange(0, daysInMonth);  // Set X-axis range from 0 to 8
            domainAxis.setTickUnit(new NumberTickUnit(1));  // Set tick unit for X-axis
        }

        return chart;
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
            interactionMap = getDailyBounces(filteredInteractions);
        } else if (timeFlag.equals("Weekly")) {
            interactionMap = getWeeklyBounces(filteredInteractions);
        } else if (timeFlag.equals("Monthly")) {
            interactionMap = getMonthlyBounces(filteredInteractions);
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
            bounceMap = getDailyBounces(filteredInteractions);
        } else if (timeFlag.equals("Weekly")) {
            clickMap = getWeeklyClicks(filteredClicks);
            bounceMap = getWeeklyBounces(filteredInteractions);
        } else if (timeFlag.equals("Monthly")) {
            clickMap = getMonthlyClicks(filteredClicks);
            bounceMap = getMonthlyBounces(filteredInteractions);
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

    public int getIntegerDailyClicks (ArrayList<String[]> clicks, String date) {
        int dailyClicks = 0;
        for(String[] click : clicks) {
            String dateAndTime = click[0].split(" ")[0];
            if(dateAndTime.equals(date.split(" ")[0])) {
                dailyClicks+=1;
            }
        }
       return dailyClicks;
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

    // Method to get the current date and time in the specified format
    public static String getFormattedDateTime() {
        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Define a date and time format pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the current date and time and return the result
        return currentDateTime.format(formatter);
    }

    public static double[] generateClickArray(int numberOfClicks, Double XStart, Double XFinish) {
        // Check for valid number of clicks
        if (numberOfClicks == 1){
            return new double[]{(XStart+XFinish)/2};
        }else if(numberOfClicks == 0){
            return new double[]{1000.0};
        }
        // Create an array of the desired length (numberOfClicks)
        double[] data = new double[numberOfClicks];

        // Set the first element to 0.0
        data[0] = XStart;

        // Set the last element to 24.0
        data[numberOfClicks - 1] = XFinish;

        // Set the middle elements to 1.0 (numberOfClicks - 2 elements)
        for (int i = 1; i < numberOfClicks - 1; i++) {
            data[i] = (XStart + XFinish) / 2;
        }

        return data;
    }

    public void saveChartAsPdf(BufferedImage chartImage) throws IOException, DocumentException {

        Document document = new Document();

        PdfWriter.getInstance(document, new FileOutputStream("chart.pdf"));

        document.open();

        File chartPdfFile = File.createTempFile("chart", ".png");
        ImageIO.write(chartImage, "PNG", chartPdfFile);

        com.itextpdf.text.Image pdfImage = com.itextpdf.text.Image.getInstance(chartPdfFile.getAbsolutePath());

        pdfImage.scaleToFit(500, 500);  // Adjust the size as needed
        document.add(pdfImage);

        document.close();
        ArrayList<String[]> chartList = getClicks();
        for (String[] clickImpression: chartList){
            String date = clickImpression[0];
            String id = clickImpression[1];
            String cc = clickImpression[2];
            System.out.println(date + " " + id + " " + cc + " ");
        }
        System.out.println("Chart saved as PDF!");
    }

}
