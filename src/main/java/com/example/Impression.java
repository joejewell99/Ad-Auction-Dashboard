package com.example;

/**
 * Represents an individual impression (advertisement view) from the CSV file.
 * Each instance of this class corresponds to one row in the dataset.
 */
public class Impression {
  private String date;          // Date when the impression occurred
  private String id;            // Unique identifier for the impression
  private String gender;        // Gender of the user who viewed the ad
  private String age;           // Age group of the user
  private String income;        // Income level of the user
  private String context;       // Context in which the ad was displayed (e.g., News, Blog)
  private double impressionCost;// Cost of this impression in monetary units

  /**
   * Constructor for creating an Impression object.
   *
   * @param date           Date of the impression
   * @param id             Unique ID of the impression
   * @param gender         Gender of the viewer
   * @param age            Age group of the viewer
   * @param income         Income level of the viewer
   * @param context        Context where the ad was displayed
   * @param impressionCost Cost of the impression
   */
  public Impression(String date, String id, String gender, String age, String income, String context, double impressionCost) {
    this.date = date;
    this.id = id;
    this.gender = gender;
    this.age = age;
    this.income = income;
    this.context = context;
    this.impressionCost = impressionCost;
  }

  /**
   * Converts the Impression object into a readable string format.
   *
   * @return A string representation of the impression details.
   */
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
