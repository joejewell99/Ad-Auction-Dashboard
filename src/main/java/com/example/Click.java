package com.example;

public class Click {
    private String date;
    private String id;
    private double clickCost;

    public Click(String date, String iD, double clickCost) {
        this.date = date;
        this.id = iD;
        this.clickCost = clickCost;
    }

    @Override
    public String toString() {
        return "Click{" +
                "date='" + date + '\'' +
                ", id='" + id + '\'' +
                ", ClickCost='" + clickCost + '\''+"}";
    }
}
