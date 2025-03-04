package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ClickLogReader {
    public List<Click> readCSV(String fileName) {
        List<Click> clickReport = new ArrayList<>();
        try (BufferedReader clickLogReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/" + fileName)))) {
            String line;
            boolean firstLine = true;
            while ((line = clickLogReader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false; // Skip header
                    continue;
                }

                String[] values = line.split(",");
                if (values.length == 3) {
                    clickReport.add(new Click(values[0], values[1], Double.parseDouble(values[2])));
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();

        }

        return clickReport;
    }
}
