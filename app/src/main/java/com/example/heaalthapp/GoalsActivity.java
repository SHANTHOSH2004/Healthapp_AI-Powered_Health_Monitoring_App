package com.example.heaalthapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.heaalthapp.adapter.HealthGoalAdapter;
import com.example.heaalthapp.database.HealthGoal;
import com.example.heaalthapp.databinding.ActivityGoalsBinding;
import com.example.heaalthapp.viewmodel.HealthViewModel;

public class GoalsActivity extends AppCompatActivity {
    private ActivityGoalsBinding binding;
    private HealthViewModel healthViewModel;
    private HealthGoalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGoalsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Health Goals");
        }

        healthViewModel = new ViewModelProvider(this).get(HealthViewModel.class);
        setupRecyclerView();
        setupButtons();
        observeGoals();
    }

    private void setupRecyclerView() {
        adapter = new HealthGoalAdapter(new HealthGoalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(HealthGoal goal) {
                Toast.makeText(GoalsActivity.this, 
                    "Goal: " + goal.getType() + " - Progress: " + goal.getProgressPercentage() + "%", 
                    Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(HealthGoal goal) {
                healthViewModel.deleteGoal(goal);
                Toast.makeText(GoalsActivity.this, "Goal deleted", Toast.LENGTH_SHORT).show();
            }
        });
        binding.recyclerViewGoals.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewGoals.setAdapter(adapter);
    }

    private void setupButtons() {
        binding.buttonAddGoal.setOnClickListener(v -> {
            String type = binding.editTextGoalType.getText().toString();
            String targetStr = binding.editTextTarget.getText().toString();

            if (type.isEmpty() || targetStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int target = Integer.parseInt(targetStr);
                HealthGoal goal = new HealthGoal(type, target, "daily");
                healthViewModel.insertGoal(goal);
                
                // Clear input fields
                binding.editTextGoalType.setText("");
                binding.editTextTarget.setText("");
                
                Toast.makeText(this, "Goal added", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observeGoals() {
        healthViewModel.getAllGoals().observe(this, goals -> {
            adapter.submitList(goals);
        });
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