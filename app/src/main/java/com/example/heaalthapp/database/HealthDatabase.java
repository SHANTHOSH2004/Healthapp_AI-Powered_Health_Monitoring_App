package com.example.heaalthapp.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.heaalthapp.database.dao.HealthMetricsDao;
import com.example.heaalthapp.database.dao.HealthGoalDao;
import com.example.heaalthapp.database.converter.DateConverter;

@Database(entities = {HealthMetrics.class, HealthGoal.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class HealthDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "health_database";
    private static HealthDatabase instance;

    public abstract HealthMetricsDao healthMetricsDao();
    public abstract HealthGoalDao healthGoalDao();

    public static synchronized HealthDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    HealthDatabase.class,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
} 