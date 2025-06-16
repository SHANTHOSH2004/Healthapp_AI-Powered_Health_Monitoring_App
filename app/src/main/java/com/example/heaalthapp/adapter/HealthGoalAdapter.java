package com.example.heaalthapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.heaalthapp.R;
import com.example.heaalthapp.database.HealthGoal;
import android.widget.ImageButton;

public class HealthGoalAdapter extends ListAdapter<HealthGoal, HealthGoalAdapter.HealthGoalViewHolder> {
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(HealthGoal goal);
        void onDeleteClick(HealthGoal goal);
    }

    public HealthGoalAdapter(OnItemClickListener listener) {
        super(new DiffUtil.ItemCallback<HealthGoal>() {
            @Override
            public boolean areItemsTheSame(@NonNull HealthGoal oldItem, @NonNull HealthGoal newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull HealthGoal oldItem, @NonNull HealthGoal newItem) {
                return oldItem.getType().equals(newItem.getType()) &&
                       oldItem.getTarget() == newItem.getTarget() &&
                       oldItem.getCurrentProgress() == newItem.getCurrentProgress() &&
                       oldItem.getFrequency().equals(newItem.getFrequency());
            }
        });
        this.listener = listener;
    }

    @NonNull
    @Override
    public HealthGoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_health_goal, parent, false);
        return new HealthGoalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HealthGoalViewHolder holder, int position) {
        HealthGoal current = getItem(position);
        holder.bind(current, listener);
    }

    static class HealthGoalViewHolder extends RecyclerView.ViewHolder {
        private final TextView typeText;
        private final TextView targetText;
        private final TextView progressText;
        private final ImageButton deleteButton;

        public HealthGoalViewHolder(@NonNull View itemView) {
            super(itemView);
            typeText = itemView.findViewById(R.id.goal_type_text);
            targetText = itemView.findViewById(R.id.goal_target_text);
            progressText = itemView.findViewById(R.id.goal_progress_text);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }

        public void bind(HealthGoal goal, OnItemClickListener listener) {
            typeText.setText(goal.getType());
            targetText.setText(String.format("Target: %d", goal.getTarget()));
            progressText.setText(String.format("Progress: %d%%", goal.getProgressPercentage()));

            itemView.setOnClickListener(v -> listener.onItemClick(goal));
            deleteButton.setOnClickListener(v -> listener.onDeleteClick(goal));
        }
    }
} 