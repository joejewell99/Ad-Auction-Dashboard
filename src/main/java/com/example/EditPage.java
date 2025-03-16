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
    private Stage stage;


    public EditPage(){
    }

    /**
     * Display method
     */
    public abstract void show();
}
