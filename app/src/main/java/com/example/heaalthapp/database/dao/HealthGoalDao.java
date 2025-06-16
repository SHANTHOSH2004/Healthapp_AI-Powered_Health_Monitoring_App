package com.example.heaalthapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.heaalthapp.database.HealthGoal;
import java.util.List;

@Dao
public interface HealthGoalDao {
    @Insert
    void insert(HealthGoal goal);

    @Update
    void update(HealthGoal goal);

    @Delete
    void delete(HealthGoal goal);

    @Query("SELECT * FROM health_goals ORDER BY type ASC")
    LiveData<List<HealthGoal>> getAllGoals();

    @Query("SELECT * FROM health_goals WHERE type = :type")
    LiveData<List<HealthGoal>> getGoalsByType(String type);
} 