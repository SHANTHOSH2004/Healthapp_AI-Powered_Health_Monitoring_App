package com.example.heaalthapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.heaalthapp.database.HealthMetrics;
import com.example.heaalthapp.databinding.ActivityChartBinding;
import com.example.heaalthapp.viewmodel.HealthViewModel;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {
    private ActivityChartBinding binding;
    private HealthViewModel healthViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Health Charts");
        }

        healthViewModel = new ViewModelProvider(this).get(HealthViewModel.class);
        setupCharts();
        observeData();
    }

    private void setupCharts() {
        // Setup Blood Pressure Chart
        binding.chartBloodPressure.getDescription().setEnabled(false);
        binding.chartBloodPressure.setDrawGridBackground(false);
        binding.chartBloodPressure.setDrawBarShadow(false);
        binding.chartBloodPressure.setScaleEnabled(true);
        binding.chartBloodPressure.setPinchZoom(true);

        XAxis xAxis = binding.chartBloodPressure.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        binding.chartBloodPressure.getAxisLeft().setDrawGridLines(true);
        binding.chartBloodPressure.getAxisRight().setEnabled(false);
        binding.chartBloodPressure.getLegend().setEnabled(true);

        // Setup Heart Rate Chart
        binding.chartHeartRate.getDescription().setEnabled(false);
        binding.chartHeartRate.setDrawGridBackground(false);
        binding.chartHeartRate.setScaleEnabled(true);
        binding.chartHeartRate.setPinchZoom(true);

        xAxis = binding.chartHeartRate.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        binding.chartHeartRate.getAxisLeft().setDrawGridLines(true);
        binding.chartHeartRate.getAxisRight().setEnabled(false);
        binding.chartHeartRate.getLegend().setEnabled(true);
    }

    private void observeData() {
        healthViewModel.getAllMetrics().observe(this, metrics -> {
            if (metrics != null && !metrics.isEmpty()) {
                updateBloodPressureChart(metrics);
                updateHeartRateChart(metrics);
            }
        });
    }

    private void updateBloodPressureChart(List<HealthMetrics> metrics) {
        List<BarEntry> systolicEntries = new ArrayList<>();
        List<BarEntry> diastolicEntries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < metrics.size(); i++) {
            HealthMetrics metric = metrics.get(i);
            systolicEntries.add(new BarEntry(i, metric.getSystolic()));
            diastolicEntries.add(new BarEntry(i, metric.getDiastolic()));
            labels.add(metric.getDate().toString());
        }

        BarDataSet systolicDataSet = new BarDataSet(systolicEntries, "Systolic");
        systolicDataSet.setColor(Color.RED);

        BarDataSet diastolicDataSet = new BarDataSet(diastolicEntries, "Diastolic");
        diastolicDataSet.setColor(Color.BLUE);

        BarData data = new BarData(systolicDataSet, diastolicDataSet);
        binding.chartBloodPressure.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        binding.chartBloodPressure.setData(data);
        binding.chartBloodPressure.invalidate();
    }

    private void updateHeartRateChart(List<HealthMetrics> metrics) {
        List<Entry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < metrics.size(); i++) {
            HealthMetrics metric = metrics.get(i);
            entries.add(new Entry(i, metric.getHeartRate()));
            labels.add(metric.getDate().toString());
        }

        LineDataSet dataSet = new LineDataSet(entries, "Heart Rate");
        dataSet.setColor(Color.GREEN);
        dataSet.setCircleColor(Color.GREEN);
        dataSet.setDrawValues(false);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);

        LineData data = new LineData(dataSet);
        binding.chartHeartRate.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        binding.chartHeartRate.setData(data);
        binding.chartHeartRate.invalidate();
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