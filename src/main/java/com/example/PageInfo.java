package com.example;


import java.util.ArrayList;
import java.util.function.Consumer;

public class PageInfo {

    public Consumer<User> onSuccess;
    public User loggedInUser;
    public LogManager logManager;
    public ChartCreator chartCreator;
    public ArrayList<String> currentCharts;
    public ArrayList<String> timeFlags;
    public ArrayList<String> genders;
    public ArrayList<String> incomes;
    public ArrayList<ArrayList<String>> contexts;
    public ArrayList<ArrayList<String>> ages;
    public int chartNumber;
    public ChartPage chartPage;
    public boolean darkMode;


    public void setOnSuccess(Consumer<User> onSuccess) {
        this.onSuccess = onSuccess;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

    public void setChartCreator(ChartCreator chartCreator) {
        this.chartCreator = chartCreator;
    }

    public void setCurrentCharts(ArrayList<String> currentCharts) {
        this.currentCharts = currentCharts;
    }

    public void setTimeFlags(ArrayList<String> timeFlags) {
        this.timeFlags = timeFlags;
    }

    public void setGenders(ArrayList<String> genders) {
        this.genders = genders;
    }

    public void setIncomes(ArrayList<String> incomes) {
        this.incomes = incomes;
    }

    public void setContexts(ArrayList<ArrayList<String>> contexts) {
        this.contexts = contexts;
    }

    public void setAges(ArrayList<ArrayList<String>> ages) {
        this.ages = ages;
    }

    public void setChartNumber(int chartNumber) {
        this.chartNumber = chartNumber;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public void setChartPage(ChartPage chartPage) {
        this.chartPage = chartPage;
    }
}
