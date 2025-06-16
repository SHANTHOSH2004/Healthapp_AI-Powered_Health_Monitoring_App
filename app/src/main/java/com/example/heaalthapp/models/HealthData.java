package com.example.heaalthapp.models;

import java.util.Date;

public class HealthData {
    private String id;
    private String userId;
    private Date timestamp;
    private double weight;
    private String bloodPressure;
    private int heartRate;
    private double sleepHours;
    private int waterIntake;
    private int steps;

    public HealthData() {
        // Default constructor required for Firebase
    }

    public HealthData(String userId, Date timestamp, double weight, String bloodPressure,
                     int heartRate, double sleepHours, int waterIntake, int steps) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.weight = weight;
        this.bloodPressure = bloodPressure;
        this.heartRate = heartRate;
        this.sleepHours = sleepHours;
        this.waterIntake = waterIntake;
        this.steps = steps;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public double getSleepHours() {
        return sleepHours;
    }

    public void setSleepHours(double sleepHours) {
        this.sleepHours = sleepHours;
    }

    public int getWaterIntake() {
        return waterIntake;
    }

    public void setWaterIntake(int waterIntake) {
        this.waterIntake = waterIntake;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
} 