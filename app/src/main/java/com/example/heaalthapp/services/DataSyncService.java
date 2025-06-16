package com.example.heaalthapp.services;

import android.content.Context;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.heaalthapp.models.HealthData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class DataSyncService extends Worker {
    private static final String TAG = "DataSyncService";
    private final FirebaseFirestore db;
    private final FirebaseAuth auth;

    public DataSyncService(Context context, WorkerParameters params) {
        super(context, params);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public Result doWork() {
        if (auth.getCurrentUser() == null) {
            return Result.failure();
        }

        String userId = auth.getCurrentUser().getUid();
        Map<String, Object> data = getInputData().getKeyValueMap();

        if (data.isEmpty()) {
            return Result.failure();
        }

        try {
            // Sync health data
            db.collection("users")
                    .document(userId)
                    .collection("healthData")
                    .document(String.valueOf(System.currentTimeMillis()))
                    .set(data, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Data synced successfully"))
                    .addOnFailureListener(e -> Log.e(TAG, "Error syncing data", e));

            return Result.success();
        } catch (Exception e) {
            Log.e(TAG, "Error in data sync", e);
            return Result.failure();
        }
    }

    public static void syncHealthData(HealthData healthData) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            return;
        }

        String userId = auth.getCurrentUser().getUid();
        Map<String, Object> data = new HashMap<>();
        data.put("weight", healthData.getWeight());
        data.put("bloodPressure", healthData.getBloodPressure());
        data.put("heartRate", healthData.getHeartRate());
        data.put("sleepHours", healthData.getSleepHours());
        data.put("waterIntake", healthData.getWaterIntake());
        data.put("steps", healthData.getSteps());
        data.put("timestamp", System.currentTimeMillis());

        db.collection("users")
                .document(userId)
                .collection("healthData")
                .document(String.valueOf(System.currentTimeMillis()))
                .set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Health data synced successfully"))
                .addOnFailureListener(e -> Log.e(TAG, "Error syncing health data", e));
    }
} 