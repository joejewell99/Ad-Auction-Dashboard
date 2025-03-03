package com.example;

import java.util.ArrayList;

public class ServerLog {

    private String filepath;

    private ArrayList<ServerLogRecord> records;

    public ServerLog(String filepath) {
        this.filepath = filepath;
        this.records = new ArrayList<>();
        this.loadLogFile(filepath);
    }

    private void loadLogFile(String filename) {
        ArrayList<String[]> data = Utils.readCSV(filename);
        System.out.println("CSV Loaded, size: " + data.size());
        for (int i = 1; i < data.size(); i++) {
            String[] elements = data.get(i);
            System.out.println("Parsing row: " + String.join("|", elements));
            try {
                this.records.add(new ServerLogRecord(
                        elements[0], elements[1], elements[2],
                        Integer.parseInt(elements[3]),
                        elements[4].equalsIgnoreCase("yes")
                ));
            } catch (Exception e) {
                System.err.println("Error parsing row: " + String.join("|", elements));
                e.printStackTrace();
            }
        }
    }


    public int size() {
        return this.records.size();
    }

    public ServerLogRecord getRecordAt(int index) {
        return this.records.get(index);
    }

    public int getNumberOfBounces() {
        int total = 0;
        for (ServerLogRecord record: this.records) {
            total += record.browseTime() < 120 || record.getPagesViewed() == 1 ? 1 : 0;
        }
        return total;
    }

    public int getNumberOfConversions() {
        int total = 0;
        for (ServerLogRecord record: this.records) {
            total += record.isConversion() ? 1 : 0;
        }
        return total;
    }

    public static void main(String[] args) {
        ServerLog serverLog = new ServerLog("server_log.csv");
        System.out.println("Bounces: " + serverLog.getNumberOfBounces());
        System.out.println("Conversions: " + serverLog.getNumberOfConversions());
        System.out.println("Total Records: " + serverLog.size());
    }


}
