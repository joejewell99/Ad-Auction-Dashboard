package com.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TestImpressionLog {
  public static void main(String[] args) {
    ImpressionLogReader reader = new ImpressionLogReader();
    List<Impression> impressions = reader.readCSV("impression_log.csv");

    if (!impressions.isEmpty()) {
      System.out.println("CSV Loaded! Total Rows: " + impressions.size());

      // Save all impressions to a file
      try (FileWriter writer = new FileWriter("impressions_output.txt")) {
        for (Impression impression : impressions) {
          writer.write(impression.toString() + "\n");
        }
        System.out.println("All impressions saved to `impressions_output.txt`.");
      } catch (IOException e) {
        e.printStackTrace();
        System.out.println("Failed to save data to file.");
      }

    } else {
      System.out.println("No data found!");
    }
  }
}
