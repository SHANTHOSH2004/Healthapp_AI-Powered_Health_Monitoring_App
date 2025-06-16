package com.example.heaalthapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.heaalthapp.database.HealthMetrics;
import java.util.Date;
import java.util.List;

@Dao
public interface HealthMetricsDao {
    @Insert
    void insert(HealthMetrics healthMetrics);

    @Update
    void update(HealthMetrics healthMetrics);

    @Delete
    void delete(HealthMetrics healthMetrics);

    @Query("SELECT * FROM health_metrics ORDER BY date DESC")
    LiveData<List<HealthMetrics>> getAllMetrics();

    @Query("SELECT * FROM health_metrics WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    LiveData<List<HealthMetrics>> getMetricsBetweenDates(Date startDate, Date endDate);

    @Query("SELECT AVG(heartRate) FROM health_metrics WHERE date BETWEEN :startDate AND :endDate")
    Double getAverageHeartRate(Date startDate, Date endDate);

    @Query("SELECT * FROM health_metrics WHERE heartRate > :maxThreshold OR heartRate < :minThreshold ORDER BY date DESC")
    LiveData<List<HealthMetrics>> getAbnormalHeartRates(int maxThreshold, int minThreshold);

    @Query("SELECT * FROM health_metrics WHERE systolic > :threshold ORDER BY date DESC")
    LiveData<List<HealthMetrics>> getAbnormalBloodPressure(int threshold);
} 