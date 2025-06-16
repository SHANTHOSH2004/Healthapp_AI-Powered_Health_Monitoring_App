package com.example.heaalthapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.heaalthapp.services.ReminderService;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.example.heaalthapp.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "HealthAppPrefs";
    private static final String DARK_MODE_KEY = "dark_mode";
    private static final String WATER_REMINDER_KEY = "water_reminder";
    private static final String EXERCISE_REMINDER_KEY = "exercise_reminder";
    private static final String SLEEP_REMINDER_KEY = "sleep_reminder";

    private SwitchMaterial darkModeSwitch;
    private SwitchMaterial waterReminderSwitch;
    private SwitchMaterial exerciseReminderSwitch;
    private SwitchMaterial sleepReminderSwitch;
    private TimePicker waterReminderTime;
    private TimePicker exerciseReminderTime;
    private TimePicker sleepReminderTime;

    private SharedPreferences preferences;
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        initializeViews();
        loadPreferences();
        setupListeners();
    }

    private void initializeViews() {
        darkModeSwitch = binding.darkModeSwitch;
        waterReminderSwitch = binding.waterReminderSwitch;
        exerciseReminderSwitch = binding.exerciseReminderSwitch;
        sleepReminderSwitch = binding.sleepReminderSwitch;
        waterReminderTime = binding.waterReminderTime;
        exerciseReminderTime = binding.exerciseReminderTime;
        sleepReminderTime = binding.sleepReminderTime;
    }

    private void loadPreferences() {
        boolean darkMode = preferences.getBoolean(DARK_MODE_KEY, false);
        boolean waterReminder = preferences.getBoolean(WATER_REMINDER_KEY, false);
        boolean exerciseReminder = preferences.getBoolean(EXERCISE_REMINDER_KEY, false);
        boolean sleepReminder = preferences.getBoolean(SLEEP_REMINDER_KEY, false);

        darkModeSwitch.setChecked(darkMode);
        waterReminderSwitch.setChecked(waterReminder);
        exerciseReminderSwitch.setChecked(exerciseReminder);
        sleepReminderSwitch.setChecked(sleepReminder);

        updateReminderTimeVisibility();
    }

    private void setupListeners() {
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean(DARK_MODE_KEY, isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        waterReminderSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean(WATER_REMINDER_KEY, isChecked).apply();
            if (isChecked) {
                scheduleWaterReminder();
            }
            updateReminderTimeVisibility();
        });

        exerciseReminderSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean(EXERCISE_REMINDER_KEY, isChecked).apply();
            if (isChecked) {
                scheduleExerciseReminder();
            }
            updateReminderTimeVisibility();
        });

        sleepReminderSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean(SLEEP_REMINDER_KEY, isChecked).apply();
            if (isChecked) {
                scheduleSleepReminder();
            }
            updateReminderTimeVisibility();
        });
    }

    private void updateReminderTimeVisibility() {
        waterReminderTime.setVisibility(waterReminderSwitch.isChecked() ? View.VISIBLE : View.GONE);
        exerciseReminderTime.setVisibility(exerciseReminderSwitch.isChecked() ? View.VISIBLE : View.GONE);
        sleepReminderTime.setVisibility(sleepReminderSwitch.isChecked() ? View.VISIBLE : View.GONE);
    }

    private void scheduleWaterReminder() {
        int hour = waterReminderTime.getHour();
        int minute = waterReminderTime.getMinute();
        long delayMillis = calculateDelayMillis(hour, minute);
        ReminderService.scheduleReminder(this, "water", delayMillis);
    }

    private void scheduleExerciseReminder() {
        int hour = exerciseReminderTime.getHour();
        int minute = exerciseReminderTime.getMinute();
        long delayMillis = calculateDelayMillis(hour, minute);
        ReminderService.scheduleReminder(this, "exercise", delayMillis);
    }

    private void scheduleSleepReminder() {
        int hour = sleepReminderTime.getHour();
        int minute = sleepReminderTime.getMinute();
        long delayMillis = calculateDelayMillis(hour, minute);
        ReminderService.scheduleReminder(this, "sleep", delayMillis);
    }

    private long calculateDelayMillis(int hour, int minute) {
        long currentTime = System.currentTimeMillis();
        long reminderTime = java.time.LocalDateTime.now()
                .withHour(hour)
                .withMinute(minute)
                .withSecond(0)
                .atZone(java.time.ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();

        if (reminderTime < currentTime) {
            reminderTime += 24 * 60 * 60 * 1000; // Add 24 hours if the time has passed
        }

        return reminderTime - currentTime;
    }
} 