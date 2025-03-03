package com.example;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {


    public static ArrayList<String[]> readCSV(String filename) {
        ArrayList<String[]> result = new ArrayList<>();
        try {
            InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream(filename);
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + filename);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.strip();
                if (!line.isEmpty()) {
                    result.add(line.split(","));
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}