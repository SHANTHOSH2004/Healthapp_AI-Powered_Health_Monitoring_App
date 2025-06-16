package com.example.heaalthapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.heaalthapp.R;
import com.example.heaalthapp.adapter.HealthGoalAdapter;
import com.example.heaalthapp.database.HealthGoal;
import com.example.heaalthapp.viewmodel.HealthViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

public class GoalsFragment extends Fragment {
    private HealthViewModel healthViewModel;
    private HealthGoalAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        healthViewModel = new ViewModelProvider(requireActivity()).get(HealthViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);
        
        RecyclerView recyclerView = view.findViewById(R.id.goals_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        adapter = new HealthGoalAdapter(new HealthGoalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(HealthGoal goal) {
                showUpdateGoalDialog(goal);
            }

            @Override
            public void onDeleteClick(HealthGoal goal) {
                showDeleteConfirmationDialog(goal);
            }
        });
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fab_add_goal);
        fab.setOnClickListener(v -> showAddGoalDialog());

        healthViewModel.getAllGoals().observe(getViewLifecycleOwner(), goals -> {
            adapter.submitList(goals);
        });

        return view;
    }

    private void showAddGoalDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_goal, null);
        TextInputEditText typeInput = dialogView.findViewById(R.id.goal_type_input);
        TextInputEditText targetInput = dialogView.findViewById(R.id.goal_target_input);
        TextInputEditText frequencyInput = dialogView.findViewById(R.id.goal_frequency_input);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Add New Goal")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String type = typeInput.getText().toString();
                    String targetStr = targetInput.getText().toString();
                    String frequency = frequencyInput.getText().toString();

                    if (!type.isEmpty() && !targetStr.isEmpty() && !frequency.isEmpty()) {
                        try {
                            int target = Integer.parseInt(targetStr);
                            HealthGoal goal = new HealthGoal(type, target, frequency);
                            healthViewModel.insertGoal(goal);
                        } catch (NumberFormatException e) {
                            Toast.makeText(getContext(), "Invalid target value", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showUpdateGoalDialog(HealthGoal goal) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_goal, null);
        TextInputEditText typeInput = dialogView.findViewById(R.id.goal_type_input);
        TextInputEditText targetInput = dialogView.findViewById(R.id.goal_target_input);
        TextInputEditText frequencyInput = dialogView.findViewById(R.id.goal_frequency_input);

        typeInput.setText(goal.getType());
        targetInput.setText(String.valueOf(goal.getTarget()));
        frequencyInput.setText(goal.getFrequency());

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Update Goal")
                .setView(dialogView)
                .setPositiveButton("Update", (dialog, which) -> {
                    String type = typeInput.getText().toString();
                    String targetStr = targetInput.getText().toString();
                    String frequency = frequencyInput.getText().toString();

                    if (!type.isEmpty() && !targetStr.isEmpty() && !frequency.isEmpty()) {
                        try {
                            int target = Integer.parseInt(targetStr);
                            goal.setType(type);
                            goal.setTarget(target);
                            goal.setFrequency(frequency);
                            healthViewModel.updateGoal(goal);
                        } catch (NumberFormatException e) {
                            Toast.makeText(getContext(), "Invalid target value", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDeleteConfirmationDialog(HealthGoal goal) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete Goal")
                .setMessage("Are you sure you want to delete this goal?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    healthViewModel.deleteGoal(goal);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
} 