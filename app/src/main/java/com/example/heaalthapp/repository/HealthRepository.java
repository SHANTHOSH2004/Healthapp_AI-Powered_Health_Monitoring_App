package com.example.heaalthapp.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.heaalthapp.database.HealthDatabase;
import com.example.heaalthapp.database.dao.HealthMetricsDao;
import com.example.heaalthapp.database.dao.HealthGoalDao;
import com.example.heaalthapp.database.HealthMetrics;
import com.example.heaalthapp.database.HealthGoal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HealthRepository {
    private final HealthMetricsDao healthMetricsDao;
    private final HealthGoalDao healthGoalDao;
    private final ExecutorService executorService;

    public HealthRepository(Application application) {
        HealthDatabase database = HealthDatabase.getInstance(application);
        healthMetricsDao = database.healthMetricsDao();
        healthGoalDao = database.healthGoalDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(HealthMetrics healthMetrics) {
        executorService.execute(() -> healthMetricsDao.insert(healthMetrics));
    }

    public void update(HealthMetrics healthMetrics) {
        executorService.execute(() -> healthMetricsDao.update(healthMetrics));
    }

    public void delete(HealthMetrics healthMetrics) {
        executorService.execute(() -> healthMetricsDao.delete(healthMetrics));
    }

    public LiveData<List<HealthMetrics>> getAllMetrics() {
        return healthMetricsDao.getAllMetrics();
    }

    public LiveData<List<HealthMetrics>> getMetricsBetweenDates(Date startDate, Date endDate) {
        return healthMetricsDao.getMetricsBetweenDates(startDate, endDate);
    }

    public LiveData<Double> getAverageHeartRate(Date startDate, Date endDate) {
        MutableLiveData<Double> result = new MutableLiveData<>();
        executorService.execute(() -> {
            Double average = healthMetricsDao.getAverageHeartRate(startDate, endDate);
            result.postValue(average);
        });
        return result;
    }

    public LiveData<List<HealthMetrics>> getAbnormalHeartRates(int threshold, int minThreshold) {
        return healthMetricsDao.getAbnormalHeartRates(threshold, minThreshold);
    }

    public LiveData<List<HealthMetrics>> getAbnormalBloodPressure(int threshold) {
        return healthMetricsDao.getAbnormalBloodPressure(threshold);
    }

    public LiveData<List<HealthGoal>> getAllGoals() {
        return healthGoalDao.getAllGoals();
    }

    public void insertGoal(HealthGoal goal) {
        executorService.execute(() -> healthGoalDao.insert(goal));
    }

    public void updateGoal(HealthGoal goal) {
        executorService.execute(() -> healthGoalDao.update(goal));
    }

    public void deleteGoal(HealthGoal goal) {
        executorService.execute(() -> healthGoalDao.delete(goal));
    }
} 