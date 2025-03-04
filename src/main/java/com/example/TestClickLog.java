package com.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TestClickLog {
    public static void main(String[] args) {
        ClickLogReader reader = new ClickLogReader();
        List<Click> clicks = reader.readCSV("clicks_log.csv");

        if (!clicks.isEmpty()) {
            System.out.println("CSV Loaded! Total Rows: " + clicks.size());

            // Save all impressions to a file
            try (FileWriter writer = new FileWriter("clicks_output.txt")) {
                for (Click click : clicks) {
                    writer.write(click.toString() + "\n");
                }
                System.out.println("All clicks saved to `clicks_output.txt`.");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to save data to file.");
            }

        } else {
            System.out.println("No data found!");
        }
    }
}
