package com.example.heaalthapp.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.heaalthapp.data.dao.HealthGoalDao;
import com.example.heaalthapp.data.dao.HealthMetricsDao;
import com.example.heaalthapp.data.entity.HealthGoal;
import com.example.heaalthapp.database.HealthMetrics;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {HealthMetrics.class, HealthGoal.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract HealthMetricsDao healthMetricsDao();
    public abstract HealthGoalDao healthGoalDao();

    private static volatile AppDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "health_app_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
} 