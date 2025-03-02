package com.example;

public class Impression {
  private String date;
  private String id;
  private String gender;
  private String age;
  private String income;
  private String context;
  private double impressionCost;

  public Impression(String date, String id, String gender, String age, String income, String context, double impressionCost) {
    this.date = date;
    this.id = id;
    this.gender = gender;
    this.age = age;
    this.income = income;
    this.context = context;
    this.impressionCost = impressionCost;
  }

  @Override
  public String toString() {
    return "Impression{" +
      "date='" + date + '\'' +
      ", id='" + id + '\'' +
      ", gender='" + gender + '\'' +
      ", age='" + age + '\'' +
      ", income='" + income + '\'' +
      ", context='" + context + '\'' +
      ", impressionCost=" + impressionCost +
      '}';
  }
}
