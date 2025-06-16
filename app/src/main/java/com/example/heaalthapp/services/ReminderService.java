package com.example.heaalthapp.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.heaalthapp.MainActivity;
import com.example.heaalthapp.R;

public class ReminderService extends Worker {
    private static final String CHANNEL_ID = "health_reminders";
    private static final int NOTIFICATION_ID = 1;

    public ReminderService(Context context, WorkerParameters params) {
        super(context, params);
        createNotificationChannel(context);
    }

    @Override
    public Result doWork() {
        String reminderType = getInputData().getString("reminder_type");
        String title = getReminderTitle(reminderType);
        String message = getReminderMessage(reminderType);

        showNotification(title, message);
        return Result.success();
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Health Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Reminders for health tracking activities");

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(String title, String message) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = 
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private String getReminderTitle(String reminderType) {
        switch (reminderType) {
            case "water":
                return "Time to Hydrate!";
            case "exercise":
                return "Exercise Reminder";
            case "medication":
                return "Medication Reminder";
            case "sleep":
                return "Bedtime Reminder";
            default:
                return "Health Reminder";
        }
    }

    private String getReminderMessage(String reminderType) {
        switch (reminderType) {
            case "water":
                return "Don't forget to drink water!";
            case "exercise":
                return "Time for your daily exercise!";
            case "medication":
                return "Time to take your medication";
            case "sleep":
                return "Time to prepare for bed";
            default:
                return "Time to check your health metrics";
        }
    }

    public static void scheduleReminder(Context context, String reminderType, long delayMillis) {
        androidx.work.Data inputData = new androidx.work.Data.Builder()
                .putString("reminder_type", reminderType)
                .build();

        androidx.work.OneTimeWorkRequest reminderWork =
                new androidx.work.OneTimeWorkRequest.Builder(ReminderService.class)
                        .setInputData(inputData)
                        .setInitialDelay(delayMillis, java.util.concurrent.TimeUnit.MILLISECONDS)
                        .build();

        androidx.work.WorkManager.getInstance(context).enqueue(reminderWork);
    }
} 