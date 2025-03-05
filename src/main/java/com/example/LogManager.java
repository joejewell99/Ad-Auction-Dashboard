package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Responsible for handling csv and data conversions
 */
public class LogManager {
    private File impressionLog;
    private File clickLog;
    private File serverLog;

    private ArrayList<String[]> impressionData = new ArrayList<>();
    private ArrayList<String[]> clickData = new ArrayList<>();
    private ArrayList<String[]> serverData = new ArrayList<>();

    /**
     * Stores impression csv
     * @param impressionLog
     */
    public void assignImpressionLog(File impressionLog) {
        this.impressionLog = impressionLog;
    }

    /**
     * Stores click csv
     * @param clickLog
     */
    public void assignClickLog(File clickLog) {
        this.clickLog = clickLog;
    }

    /**
     * Stores server csv
     * @param serverLog
     */
    public void assignServerLog(File serverLog){
        this.serverLog = serverLog;
    }

    /**
     * Convert click csv into list
     */
    public void convertClickLog() {
        String line;
        String comma = ",";
        boolean headerLine = true;

        try (BufferedReader br = new BufferedReader(new FileReader(this.clickLog))) {
            while ((line = br.readLine()) != null) {
                if (headerLine) {
                    headerLine = false;
                    continue;
                }
                String[] row = line.split(comma);
                this.clickData.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert server csv into list
     */
    public void convertServerLog() {
        String line;
        String comma = ",";
        boolean headerLine = true;

        try (BufferedReader br = new BufferedReader(new FileReader(this.serverLog))) {
            while ((line = br.readLine()) != null) {
                if (headerLine) {
                    headerLine = false;
                    continue;
                }
                String[] row = line.split(comma);
                this.serverData.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert impression csv into list
     */
    public void convertImpressionLog() {
        String line;
        String comma = ",";
        boolean headerLine = true;

        try (BufferedReader br = new BufferedReader(new FileReader(this.impressionLog))) {
            while ((line = br.readLine()) != null) {
                if (headerLine) {
                    headerLine = false;
                    continue;
                }
                String[] row = line.split(comma);
                this.impressionData.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //BELOW HERE ARE ALL GETTER AND PRINTING METHODS
    public ArrayList<String[]> getImpressionData(){
        return this.impressionData;
    }

    public ArrayList<String[]> getClickData(){
        return this.clickData;
    }

    public ArrayList<String[]> getServerData(){
        return this.serverData;
    }

    public void printImpressionData() {
        for (String[] row : this.impressionData) {
            System.out.println(Arrays.toString(row));
        }
    }

    public void printClickData() {
        for (String[] row : this.clickData) {
            System.out.println(Arrays.toString(row));
        }
    }

    public void printServerData() {
        for (String[] row : this.serverData) {
            System.out.println(Arrays.toString(row));
        }
    }


    public File getClickLog() {
        return clickLog;
    }

    public File getServerLog() {
        return serverLog;
    }

    public File getImpressionLog() {
        return impressionLog;
    }
}
