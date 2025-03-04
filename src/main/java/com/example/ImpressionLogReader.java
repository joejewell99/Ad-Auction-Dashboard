package com.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the impression_log.csv file and converts each row into an Impression object.
 * This class is responsible for loading the dataset into a structured format.
 */
public class ImpressionLogReader {
  /**
   * Reads a CSV file and parses its contents into a list of Impression objects.
   *
   * @param fileName The name of the CSV file to read.
   * @return A list of Impression objects containing the parsed data.
   */
  public List<Impression> readCSV(String fileName) {
    List<Impression> impressions = new ArrayList<>();

    // Try to open the file and read its contents
    try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/" + fileName)))) {
      String line;
      boolean firstLine = true; // Flag to skip the header row

      while ((line = br.readLine()) != null) {
        if (firstLine) {
          firstLine = false; // Skip the first line (column names)
          continue;
        }

        // Split the line by commas to extract individual values
        String[] values = line.split(",");

        // Ensure the row has the expected number of columns before processing
        if (values.length == 7) {
          impressions.add(new Impression(
            values[0],                 // Date
            values[1],                 // ID
            values[2],                 // Gender
            values[3],                 // Age
            values[4],                 // Income
            values[5],                 // Context
            Double.parseDouble(values[6]) // Impression Cost (converted from String to double)
          ));
        }
      }
    } catch (IOException | NumberFormatException e) {
      e.printStackTrace(); // Print error details if file reading fails
    }

    return impressions; // Return the list of impressions
  }
}
