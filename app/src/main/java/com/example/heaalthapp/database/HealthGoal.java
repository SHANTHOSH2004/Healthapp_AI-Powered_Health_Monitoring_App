package com.example.heaalthapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "health_goals")
public class HealthGoal {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String type;
    private int target;
    private String frequency;
    private int currentProgress;

    public HealthGoal(String type, int target, String frequency) {
        this.type = type;
        this.target = target;
        this.frequency = frequency;
        this.currentProgress = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }

    public int getProgressPercentage() {
        if (target == 0) return 0;
        return (currentProgress * 100) / target;
    }
} 