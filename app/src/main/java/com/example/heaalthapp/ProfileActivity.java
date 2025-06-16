package com.example.heaalthapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.heaalthapp.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        loadProfileData();
        setupSaveButton();
    }

    private void loadProfileData() {
        binding.nameInput.setText(sharedPreferences.getString("name", ""));
        binding.ageInput.setText(sharedPreferences.getString("age", ""));
        binding.weightInput.setText(sharedPreferences.getString("weight", ""));
        binding.heightInput.setText(sharedPreferences.getString("height", ""));
        binding.targetWeightInput.setText(sharedPreferences.getString("targetWeight", ""));
        binding.targetStepsInput.setText(sharedPreferences.getString("targetSteps", ""));
        binding.targetWaterInput.setText(sharedPreferences.getString("targetWater", ""));
    }

    private void setupSaveButton() {
        binding.saveProfileButton.setOnClickListener(v -> {
            if (validateInput()) {
                saveProfileData();
                Toast.makeText(this, "Profile saved successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private boolean validateInput() {
        String name = binding.nameInput.getText().toString();
        String age = binding.ageInput.getText().toString();
        String weight = binding.weightInput.getText().toString();
        String height = binding.heightInput.getText().toString();

        if (name.isEmpty() || age.isEmpty() || weight.isEmpty() || height.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            int ageValue = Integer.parseInt(age);
            double weightValue = Double.parseDouble(weight);
            double heightValue = Double.parseDouble(height);

            if (ageValue <= 0 || weightValue <= 0 || heightValue <= 0) {
                Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveProfileData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", binding.nameInput.getText().toString());
        editor.putString("age", binding.ageInput.getText().toString());
        editor.putString("weight", binding.weightInput.getText().toString());
        editor.putString("height", binding.heightInput.getText().toString());
        editor.putString("targetWeight", binding.targetWeightInput.getText().toString());
        editor.putString("targetSteps", binding.targetStepsInput.getText().toString());
        editor.putString("targetWater", binding.targetWaterInput.getText().toString());
        editor.apply();
    }
} 