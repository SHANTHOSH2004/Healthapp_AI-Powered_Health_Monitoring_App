package com.example.heaalthapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.heaalthapp.data.entity.HealthGoal;
import java.util.List;

@Dao
public interface HealthGoalDao {
    @Insert
    void insert(HealthGoal goal);

    @Update
    void update(HealthGoal goal);

    @Delete
    void delete(HealthGoal goal);

    @Query("SELECT * FROM health_goals ORDER BY startDate DESC")
    LiveData<List<HealthGoal>> getAllGoals();

    @Query("SELECT * FROM health_goals WHERE type = :type AND period = :period ORDER BY startDate DESC LIMIT 1")
    LiveData<HealthGoal> getLatestGoal(String type, String period);

    @Query("SELECT * FROM health_goals WHERE completed = 0 ORDER BY startDate DESC")
    LiveData<List<HealthGoal>> getActiveGoals();
} 