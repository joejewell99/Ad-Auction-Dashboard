package com.example;

import javafx.stage.Stage;

import java.util.ArrayList;

public abstract class EditPage {
    private String timeFlag;
    private String currentChart;
    private String gender;
    private ArrayList<String> age;
    private String income;
    private ArrayList<String> context;

    private LogManager logManager;


    public EditPage(Stage stage, LogManager logManager, String currentChart, String timeFlag, String gender, String income, ArrayList<String> context, ArrayList<String> age)  {
        this.currentChart = currentChart;
        this.timeFlag = timeFlag;
        this.gender = gender;
        this.income = income;
        this.context = context;
        this.age = age;
        this.logManager = logManager;
    }

    /**
     * Display method
     */
    public abstract void show();
}
