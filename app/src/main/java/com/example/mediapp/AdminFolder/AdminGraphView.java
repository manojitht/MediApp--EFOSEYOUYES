package com.example.mediapp.AdminFolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.mediapp.GetData.GetData;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.PublicKey;
import java.util.ArrayList;

public class AdminGraphView extends AppCompatActivity {

    BarChart barChart;
    ArrayList<BarEntry> barEntryArrayList;
    ArrayList<String> labelsName;
    DatabaseReference GetRecord;
    String setMonth;


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

        BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Weeks");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        Description description = new Description();
        description.setText("Month: " );
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




    public void takeMonthSales(){

        GetRecord = FirebaseDatabase.getInstance().getReference().child("Graph").child("Record");
        GetRecord.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String month = dataSnapshot.child("Month").getValue().toString();
                String firstWeek = dataSnapshot.child("1st week").getValue().toString();
                String secondWeek = dataSnapshot.child("2nd week").getValue().toString();
                String thirdWeek = dataSnapshot.child("3rd week").getValue().toString();
                String fourthWeek = dataSnapshot.child("4th week").getValue().toString();
                int first = Integer.parseInt(firstWeek);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        monthSalesDataArrayList.clear();
        monthSalesDataArrayList.add(new MonthSalesData("1st Week", 8589));
        monthSalesDataArrayList.add(new MonthSalesData("2nd Week", 85925));
        monthSalesDataArrayList.add(new MonthSalesData("3rd Week", 65925));
        monthSalesDataArrayList.add(new MonthSalesData("4th Week", 36925));
    }


}