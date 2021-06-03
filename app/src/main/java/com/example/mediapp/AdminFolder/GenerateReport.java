package com.example.mediapp.AdminFolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediapp.LoginActivity;
import com.example.mediapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;

public class GenerateReport extends AppCompatActivity {
    BarChart barchart;
    TextView monthStatus, totalSalesOfMonth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_report);

        barchart = findViewById(R.id.graph);
        monthStatus = findViewById(R.id.current_sales_month);
        totalSalesOfMonth = findViewById(R.id.total_amount_for_month);

        Intent intent = getIntent();

        String barOne = intent.getStringExtra("BarOne").replace(",", "");
        String barTwo = intent.getStringExtra("BarTwo").replace(",", "");
        String barThree = intent.getStringExtra("BarThree").replace(",", "");
        String barFour = intent.getStringExtra("BarFour").replace(",", "");
        String graphMonth = intent.getStringExtra("GraphMonth");

        int salesFirstWeek = Integer.parseInt(barOne);
        int salesSecondWeek = Integer.parseInt(barTwo);
        int salesThirdWeek = Integer.parseInt(barThree);
        int salesFourthWeek = Integer.parseInt(barFour);
        int addAllWeeks = salesFirstWeek + salesSecondWeek + salesThirdWeek + salesFourthWeek;
        String formattedTotal = NumberFormat.getInstance().format(addAllWeeks);

        totalSalesOfMonth.setText(formattedTotal + " LKR");

        monthStatus.setText("Month of sales: " + graphMonth);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(Integer.parseInt(barOne), 0));
        barEntries.add(new BarEntry(Integer.parseInt(barTwo), 1));
        barEntries.add(new BarEntry(Integer.parseInt(barThree), 2));
        barEntries.add(new BarEntry(Integer.parseInt(barFour), 3));
        BarDataSet barDataSet = new BarDataSet(barEntries, "Weeks");

        ArrayList<String> theWeeks;
        theWeeks = new ArrayList<>();
        theWeeks.add("1st week");
        theWeeks.add("2nd week");
        theWeeks.add("3rd week");
        theWeeks.add("4th week");

        BarData theData = new BarData(theWeeks, barDataSet);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barchart.setData(theData);
        barchart.animateY(3000);

        barchart.setTouchEnabled(true);
        barchart.setDragEnabled(true);
        barchart.setScaleEnabled(true);

        //////////////////////////////////////////


    }



}