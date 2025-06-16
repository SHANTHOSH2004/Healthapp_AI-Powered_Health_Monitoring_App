package com.example.heaalthapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.heaalthapp.adapter.HealthMetricsAdapter;
import com.example.heaalthapp.database.HealthMetrics;
import com.example.heaalthapp.databinding.ActivityMainBinding;
import com.example.heaalthapp.utils.NotificationHelper;
import com.example.heaalthapp.viewmodel.HealthViewModel;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private HealthViewModel healthViewModel;
    private HealthMetricsAdapter adapter;
    private static final int PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        // Initialize notification channel
        NotificationHelper.createNotificationChannel(this);
        
        // Request permissions
        requestPermissions();

        healthViewModel = new ViewModelProvider(this).get(HealthViewModel.class);
        setupRecyclerView();
        setupButtons();
        observeHealthMetrics();
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void setupRecyclerView() {
        adapter = new HealthMetricsAdapter(new HealthMetricsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(HealthMetrics healthMetrics) {
                Toast.makeText(MainActivity.this, "Selected: " + healthMetrics.getDate(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(HealthMetrics healthMetrics) {
                healthViewModel.delete(healthMetrics);
                Toast.makeText(MainActivity.this, "Entry deleted", Toast.LENGTH_SHORT).show();
            }
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupButtons() {
        // Add Health Metrics button
        binding.buttonAddMetrics.setOnClickListener(v -> {
            startActivity(new Intent(this, DataEntryActivity.class));
        });

        // Voice Input button
        binding.buttonVoiceInput.setOnClickListener(v -> {
            startActivity(new Intent(this, DataEntryActivity.class));
        });

        // Set Reminder button
        binding.buttonSetReminder.setOnClickListener(v -> {
            showReminder();
        });

        // Goals button
        binding.buttonGoals.setOnClickListener(v -> {
            startActivity(new Intent(this, GoalsActivity.class));
        });

        // Chart button
        binding.buttonChart.setOnClickListener(v -> {
            startActivity(new Intent(this, ChartActivity.class));
        });

        // FAB
        binding.fabAddMetrics.setOnClickListener(v -> {
            startActivity(new Intent(this, DataEntryActivity.class));
        });
    }

    private void observeHealthMetrics() {
        healthViewModel.getAllMetrics().observe(this, healthMetrics -> {
            adapter.submitList(healthMetrics);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_charts) {
            startActivity(new Intent(this, ChartsActivity.class));
            return true;
        } else if (id == R.id.action_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showReminder() {
        NotificationHelper.showHealthReminder(this,
                "Health Check Reminder",
                "Time to log your health metrics!");
    }
}