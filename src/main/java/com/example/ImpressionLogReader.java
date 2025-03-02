package com.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ImpressionLogReader {
  public List<Impression> readCSV(String fileName) {
    List<Impression> impressions = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/" + fileName)))) {
      String line;
      boolean firstLine = true;
      while ((line = br.readLine()) != null) {
        if (firstLine) {
          firstLine = false; // Skip header
          continue;
        }

        String[] values = line.split(",");
        if (values.length == 7) {
          impressions.add(new Impression(
            values[0],                 // Date
            values[1],                 // ID
            values[2],                 // Gender
            values[3],                 // Age
            values[4],                 // Income
            values[5],                 // Context
            Double.parseDouble(values[6]) // Impression Cost
          ));
        }
      }
    } catch (IOException | NumberFormatException e) {
      e.printStackTrace();
    }
    return impressions;
  }
}
