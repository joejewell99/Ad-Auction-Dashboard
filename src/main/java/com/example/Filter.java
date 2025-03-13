package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Filter {

    /**
     * Filters impression data by gender
     * @param impressions
     * @param gender
     * @return
     */
    public ArrayList<String[]> genderFilter(ArrayList<String[]> impressions, String gender) {
        //No gender filters
        if (gender == null) {
            return impressions;
        } else {
            //Filter impressions
            ArrayList<String[]> filteredImpressions = new ArrayList<>();
            for (String[] row : impressions) {
                if (row[2].equals(gender)) {
                    filteredImpressions.add(row);
                }
            } return filteredImpressions;
        }
    }

    /**
     * Filter impressions by income
     * @param impressions
     * @param income
     * @return
     */
    public ArrayList<String[]> incomeFilter(ArrayList<String[]>impressions, String income) {
        if (income == null) {
            return impressions;
        } else {
            //Filter impressions
            ArrayList<String[]> filteredImpressions = new ArrayList<>();
            for (String[] row : impressions) {
                if (row[4].equals(income)) {
                    filteredImpressions.add(row);
                }
            } return filteredImpressions;
        }
    }

    /**
     * Filter impressions by context
     * @param impressions
     * @param context
     * @return
     */
    public ArrayList<String[]> contextFilter(ArrayList<String[]> impressions, ArrayList<String> context) {
        if (context.size() == 0) {
            return impressions;
        } else {
            ArrayList<String[]> filteredImpressions = new ArrayList<>();
            for (String[] row : impressions) {
                if (context.contains(row[5])) {
                    filteredImpressions.add(row);
                }
            } return filteredImpressions;
        }
    }

    /**
     * Filter impressions by age
     * @param impressions
     * @param age
     * @return
     */
    public ArrayList<String[]> ageFilter(ArrayList<String[]>  impressions, ArrayList<String> age) {
        if (age.size() == 0) {
            return impressions;
        } else {
            ArrayList<String[]> filteredImpressions = new ArrayList<>();
            for (String[] row : impressions) {
                if (age.contains(row[3])) {
                    filteredImpressions.add(row);
                }
            } return filteredImpressions;
        }
    }


    /**
     * Applies all filters to data
     * @param clicks
     * @param impressions
     * @param interactions
     * @param gender
     * @param income
     * @return
     */
    public ArrayList<String[]> [] filterPipeline(ArrayList<String[]> clicks, ArrayList<String[]> impressions, ArrayList<String[]> interactions, String gender, String income, ArrayList<String> context,ArrayList<String> age){
        ArrayList<String[]> [] filteredLogs = new ArrayList[3];
        HashSet<String> users = new HashSet<>();

        // Get filtered impressions and unique IDs
        ArrayList<String[]> filteredImpressions = ageFilter(contextFilter(incomeFilter(genderFilter(impressions,gender),income),context),age);
        for (String [] entry : filteredImpressions) {
            users.add(entry[1]);
        }

        //Get associated clicks and interactions
        ArrayList<String[]> filteredClicks = idMatch(clicks,users);
        ArrayList<String[]> filteredInteractions = idMatch(interactions,users);

        //Add filtered logs to output list
        filteredLogs[0] = filteredClicks;
        filteredLogs[1] = filteredImpressions;
        filteredLogs[2] = filteredInteractions;

        return filteredLogs;
    }

    /**
     * Matches click log or server log to impression log
     * @param log
     * @param users
     * @return
     */
    public ArrayList<String[]> idMatch(ArrayList<String[]> log, HashSet<String> users) {
        ArrayList<String[]> filteredLog = new ArrayList<>();
        for (String[] entry : log) {
            if (users.contains(entry[1])) {
                filteredLog.add(entry);
            }
        }
        return filteredLog;
    }


}
