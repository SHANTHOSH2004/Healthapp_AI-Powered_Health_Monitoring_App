package com.example.heaalthapp.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "health_goals")
public class HealthGoal {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String type; // e.g., "steps", "water", "exercise"
    private int target;
    private int current;
    private String period; // "daily" or "weekly"
    private long startDate;
    private boolean completed;

    public HealthGoal(String type, int target, String period) {
        this.type = type;
        this.target = target;
        this.period = period;
        this.current = 0;
        this.startDate = System.currentTimeMillis();
        this.completed = false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getTarget() { return target; }
    public void setTarget(int target) { this.target = target; }
    public int getCurrent() { return current; }
    public void setCurrent(int current) { this.current = current; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public long getStartDate() { return startDate; }
    public void setStartDate(long startDate) { this.startDate = startDate; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public int getProgressPercentage() {
        return (int) ((float) current / target * 100);
    }
} 