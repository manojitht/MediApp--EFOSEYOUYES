package com.example.mediapp.AdminFolder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mediapp.Model.MonthSalesData;
import com.example.mediapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class AdminGraphView extends AppCompatActivity {

    BarChart barChart;
    ArrayList<BarEntry> barEntryArrayList;
    ArrayList<String> labelsName;

    ArrayList<MonthSalesData> monthSalesDataArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_graph_view);
        barChart = findViewById(R.id.barChart);
        // create the new object for the array list of sales and month
        barEntryArrayList = new ArrayList<>();
        labelsName = new ArrayList<>();
        takeMonthSales();

        for (int i = 0; i < monthSalesDataArrayList.size(); i++){
            String month = monthSalesDataArrayList.get(i).getMonth();
            int sales = monthSalesDataArrayList.get(i).getSales();
            barEntryArrayList.add(new BarEntry(i, sales));
            labelsName.add(month);
        }

        BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Months");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        Description description = new Description();
        description.setText("Year: 2021");
        barChart.setDescription(description);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        // Needs to get the x-axis value form
        XAxis XAxis = barChart.getXAxis();
        XAxis.setValueFormatter(new IndexAxisValueFormatter(labelsName));
        //set position for the label names (Months).
        XAxis.setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.TOP);
        XAxis.setDrawGridLines(false);
        XAxis.setDrawAxisLine(false);
        XAxis.setGranularity(1f);
        XAxis.setLabelCount(labelsName.size());
        XAxis.setLabelRotationAngle(270);
        barChart.animateY(2000);
        barChart.invalidate();
    }

    private void takeMonthSales(){
        monthSalesDataArrayList.clear();
        monthSalesDataArrayList.add(new MonthSalesData("January", 26525));
        monthSalesDataArrayList.add(new MonthSalesData("February", 65925));
        monthSalesDataArrayList.add(new MonthSalesData("March", 65925));
        monthSalesDataArrayList.add(new MonthSalesData("April", 26925));
        monthSalesDataArrayList.add(new MonthSalesData("May", 2595));
        monthSalesDataArrayList.add(new MonthSalesData("June", 25925));
        monthSalesDataArrayList.add(new MonthSalesData("July", 36525));
        monthSalesDataArrayList.add(new MonthSalesData("August", 265925));
        monthSalesDataArrayList.add(new MonthSalesData("September", 9655));
        monthSalesDataArrayList.add(new MonthSalesData("October", 5625));
        monthSalesDataArrayList.add(new MonthSalesData("November", 81625));
        monthSalesDataArrayList.add(new MonthSalesData("December", 96625));
    }
}