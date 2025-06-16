package com.example.heaalthapp.models;

public class User {
    private String uid;
    private String email;
    private String displayName;
    private boolean darkModeEnabled;
    private String preferredLanguage;
    private String[] trackedMetrics;

    public User() {
        // Required empty constructor for Firebase
    }

    public User(String uid, String email, String displayName) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.darkModeEnabled = false;
        this.preferredLanguage = "en";
        this.trackedMetrics = new String[]{"weight", "bloodPressure", "heartRate", "sleepHours", "waterIntake", "steps"};
    }

    // Getters and Setters
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    
    public boolean isDarkModeEnabled() { return darkModeEnabled; }
    public void setDarkModeEnabled(boolean darkModeEnabled) { this.darkModeEnabled = darkModeEnabled; }
    
    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }
    
    public String[] getTrackedMetrics() { return trackedMetrics; }
    public void setTrackedMetrics(String[] trackedMetrics) { this.trackedMetrics = trackedMetrics; }
} 