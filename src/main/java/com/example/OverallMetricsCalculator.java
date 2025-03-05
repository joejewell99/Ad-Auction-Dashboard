package com.example;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Does all calculations for overall metric values
 */
public class OverallMetricsCalculator {

    /**
     * Calculate total impressions for campaign
     * @param impressionData
     * @return
     */
    public int calcImpressions (ArrayList<String[]> impressionData) {
        int impressions = 0;
        for (String[] impression : impressionData){
            impressions += 1;
        }
        return impressions;
    }

    /**
     * Calculate total clicks for campaign
     * @param clickData
     * @return
     */
    public int calcClicks (ArrayList<String[]> clickData) {
        int clicks = 0;
        for (String[] click : clickData){
            clicks += 1;
        }
        return clicks;
    }

    /**
     * Calculate total unique clicks for campaign
     * @param clickData
     * @return
     */
    public int calcUniques (ArrayList<String[]> clickData) {
        int uniques;
        HashSet<String> uniqueID = new HashSet<String>();
        for (String[] click : clickData) {
            uniqueID.add(click[1]);
        }
        uniques = uniqueID.size();
        return uniques;
    }

    /**
     * Calculate total bounces for campaign
     * @param serverData
     * @return
     */
    public int calcBounces (ArrayList<String[]> serverData) {
        int bounces = 0;
        for (String[] interaction : serverData) {
            if (interaction[3].equals("1")) {
                bounces += 1;
            }
        }
        return bounces;
    }

    /**
     * Calculate total conversions for campaign
     * @param serverData
     * @return
     */
    public int calcConversions (ArrayList<String[]> serverData) {
        int conversions = 0;
        for (String[] interaction : serverData) {
            if (interaction[4].equals("Yes")) {
                conversions += 1;
            }
        }
        return conversions;
    }

    /**
     * Calculates total cost for campaign
     * @param clickData
     * @param impressionData
     * @return
     */
    public float calcCost (ArrayList<String[]> clickData, ArrayList<String[]> impressionData) {
        float totalCost = 0;
        for (String[] click : clickData) {
            totalCost += Float.parseFloat(click[2]);
        }
        for (String[] impression : impressionData) {
            totalCost += Float.parseFloat(impression[6]);
        }
        return totalCost;
    }

    /**
     * Returns number of clicks per impression for campaign
     * @param clickData
     * @param impressionData
     * @return
     */
    public float calcCTR (ArrayList<String[]> clickData, ArrayList<String[]> impressionData) {
        int clicks = this.calcClicks(clickData);
        int impressions = this.calcImpressions(impressionData);
        return ( (float) clicks/impressions);
    }

    /**
     * Calculate cost per conversion for campaign
     * @param clickData
     * @param impressionData
     * @param serverData
     * @return
     */
    public float calcCPA(ArrayList<String[]> clickData, ArrayList<String[]> impressionData, ArrayList<String[]> serverData){
        int conversions = calcConversions(serverData);
        float cost = calcCost(clickData,impressionData);
        return(cost/conversions);
    }

    /**
     * Calculate cost per click for campaign
     * @param clickData
     * @param impressionData
     * @return
     */
    public float calcCPC(ArrayList<String[]> clickData, ArrayList<String[]> impressionData, ArrayList<String[]> serverData) {
        int clicks = calcClicks(clickData);
        float cost = calcCost(clickData,impressionData);
        return (cost/clicks);
    }

    /**
     * Calculate cost per thousand impressions for campaign
     * @param clickData
     * @param impressionData
     * @return
     */
    public float calcCPM(ArrayList<String[]> clickData, ArrayList<String[]> impressionData) {
        float cost = calcCost(clickData,impressionData);
        float thousandImpressions = (float) this.calcImpressions(impressionData)/1000;
        return(cost/thousandImpressions);
    }


    /**
     * Calculate bounce rate for campaign
     * @param clickData
     * @param serverData
     * @return
     */
    public float calcBounceRate(ArrayList<String[]> clickData, ArrayList<String[]> serverData){
        int clicks = this.calcClicks(clickData);
        int bounces = this.calcBounces(serverData);
        return ((float) clicks/bounces);
    }

}
