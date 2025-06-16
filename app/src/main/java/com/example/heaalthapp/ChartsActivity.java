package com.example.heaalthapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.heaalthapp.databinding.ActivityMainBinding;
import com.example.heaalthapp.viewmodel.HealthViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.example.heaalthapp.database.HealthMetrics;
import android.widget.TextView;
import android.view.View;
import android.util.Log;
import android.widget.Toast;

public class ChartsActivity extends AppCompatActivity {
    private HealthViewModel healthViewModel;
    private TextView noDataText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        Log.d("ChartsActivity", "ChartsActivity opened");
        Toast.makeText(this, "ChartsActivity opened", Toast.LENGTH_SHORT).show();

        // Add a 'No data to display' message programmatically
        TextView noDataText = new TextView(this);
        noDataText.setId(View.generateViewId());
        noDataText.setText("No data to display");
        noDataText.setTextSize(18);
        noDataText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        noDataText.setVisibility(View.GONE);
        // Add to the chart layout
        LinearLayout chartLayout = findViewById(R.id.chartLayout);
        if (chartLayout != null) {
            chartLayout.addView(noDataText);
        }
        this.noDataText = noDataText;

        healthViewModel = new ViewModelProvider(this).get(HealthViewModel.class);
        healthViewModel.getAllMetrics().observe(this, this::updateChartsWithData);
    }

    private void updateChartsWithData(List<HealthMetrics> metrics) {
        // Hide or show charts based on data
        int[] chartIds = {R.id.heartRateChart, R.id.systolicChart, R.id.diastolicChart};
        if (metrics == null || metrics.isEmpty()) {
            for (int id : chartIds) {
                View chart = findViewById(id);
                if (chart != null) chart.setVisibility(View.GONE);
            }
            if (noDataText != null) noDataText.setVisibility(View.VISIBLE);
            return;
        } else {
            for (int id : chartIds) {
                View chart = findViewById(id);
                if (chart != null) chart.setVisibility(View.VISIBLE);
            }
            if (noDataText != null) noDataText.setVisibility(View.GONE);
        }

        // Heart Rate Line Chart
        LineChart heartRateChart = findViewById(R.id.heartRateChart);
        ArrayList<Entry> heartRateEntries = new ArrayList<>();
        for (int i = 0; i < metrics.size(); i++) {
            heartRateEntries.add(new Entry(i + 1, metrics.get(i).getHeartRate()));
        }
        LineDataSet heartRateDataSet = new LineDataSet(heartRateEntries, "Heart Rate");
        heartRateChart.setData(new LineData(heartRateDataSet));
        heartRateChart.invalidate();

        // Systolic Pressure Line Chart
        LineChart systolicChart = findViewById(R.id.systolicChart);
        ArrayList<Entry> systolicEntries = new ArrayList<>();
        for (int i = 0; i < metrics.size(); i++) {
            systolicEntries.add(new Entry(i + 1, metrics.get(i).getSystolic()));
        }
        LineDataSet systolicDataSet = new LineDataSet(systolicEntries, "Systolic Pressure");
        systolicChart.setData(new LineData(systolicDataSet));
        systolicChart.invalidate();

        // Diastolic Pressure Line Chart
        LineChart diastolicChart = findViewById(R.id.diastolicChart);
        ArrayList<Entry> diastolicEntries = new ArrayList<>();
        for (int i = 0; i < metrics.size(); i++) {
            diastolicEntries.add(new Entry(i + 1, metrics.get(i).getDiastolic()));
        }
        LineDataSet diastolicDataSet = new LineDataSet(diastolicEntries, "Diastolic Pressure");
        diastolicChart.setData(new LineData(diastolicDataSet));
        diastolicChart.invalidate();
    }
} 