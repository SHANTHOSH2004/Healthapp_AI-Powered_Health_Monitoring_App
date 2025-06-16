package com.example.heaalthapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import com.example.heaalthapp.database.HealthMetrics;
import com.example.heaalthapp.databinding.ActivityDataEntryBinding;
import com.example.heaalthapp.utils.VoiceInputHelper;
import com.example.heaalthapp.viewmodel.HealthViewModel;
import java.util.Date;

public class DataEntryActivity extends AppCompatActivity {
    private ActivityDataEntryBinding binding;
    private HealthViewModel healthViewModel;
    private static final int PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDataEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add Health Metrics");
        }

        healthViewModel = new ViewModelProvider(this).get(HealthViewModel.class);
        setupButtons();
        requestPermissions();
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    PERMISSION_REQUEST_CODE);
        }
    }

    private void setupButtons() {
        binding.voiceInputButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED) {
                VoiceInputHelper.startVoiceInput(this);
            } else {
                Toast.makeText(this, "Microphone permission required", Toast.LENGTH_SHORT).show();
                requestPermissions();
            }
        });

        binding.buttonSave.setOnClickListener(v -> saveHealthMetrics());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String voiceInput = VoiceInputHelper.processVoiceInput(requestCode, resultCode, data);
        if (voiceInput != null) {
            processVoiceInput(voiceInput);
        }
    }

    private void processVoiceInput(String text) {
        String[] words = text.toLowerCase().split("\\s+");
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals("heart") && i + 1 < words.length) {
                try {
                    int heartRate = Integer.parseInt(words[i + 1]);
                    binding.editTextHeartRate.setText(String.valueOf(heartRate));
                } catch (NumberFormatException e) {
                    // Ignore invalid numbers
                }
            } else if (words[i].equals("blood") && i + 2 < words.length) {
                try {
                    int systolic = Integer.parseInt(words[i + 1]);
                    int diastolic = Integer.parseInt(words[i + 2]);
                    binding.editTextBloodPressure.setText(systolic + "/" + diastolic);
                } catch (NumberFormatException e) {
                    // Ignore invalid numbers
                }
            } else if (words[i].equals("steps") && i + 1 < words.length) {
                try {
                    int steps = Integer.parseInt(words[i + 1]);
                    binding.editTextSteps.setText(String.valueOf(steps));
                } catch (NumberFormatException e) {
                    // Ignore invalid numbers
                }
            } else if (words[i].equals("exercise") && i + 1 < words.length) {
                binding.editTextExercise.setText(words[i + 1]);
            }
        }
    }

    private void saveHealthMetrics() {
        try {
            int heartRate = Integer.parseInt(binding.editTextHeartRate.getText().toString());
            String[] bp = binding.editTextBloodPressure.getText().toString().split("/");
            int systolic = Integer.parseInt(bp[0]);
            int diastolic = Integer.parseInt(bp[1]);

            HealthMetrics metrics = new HealthMetrics(systolic, diastolic, heartRate, new Date());
            healthViewModel.insert(metrics);
            Toast.makeText(this, "Health metrics saved", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 