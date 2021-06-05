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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class GenerateReport extends AppCompatActivity {
    BarChart barchart;
    TextView monthStatus, totalSalesOfMonth, totalSalesAmount;
    DatabaseReference getValuesfromDB;
    int totalNumberOfOrders = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_report);

        barchart = findViewById(R.id.graph);
        monthStatus = findViewById(R.id.current_sales_month);
        totalSalesOfMonth = findViewById(R.id.total_amount_for_month);
        totalSalesAmount = findViewById(R.id.total_amount_from_start);

        getValuesfromDB = FirebaseDatabase.getInstance().getReference().child("Sales Data");

//        getValuesfromDB.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                if (!dataSnapshot.exists()){
//                    totalSalesAmount.setText("0 LKR");
//                   // totalNumberOfOrders.setText("Number of orders: 0");
//                }
//                else if (dataSnapshot.exists()){
//
////                    order = (int) dataSnapshot.getChildrenCount();
////                    totalNumberOfOrders.setText("Number of orders: " + order);
//
//
//                    int sum = 0;
//
//                    for (DataSnapshot ds : dataSnapshot.getChildren()){
//                        Map<String, Object> map = (Map<String, Object>)ds.getValue();
//                        Object price = map.get("totalAmount");
//                        int totalPrice = Integer.parseInt(String.valueOf(price));
//                        sum += totalPrice;
//
//                        String formattedTotal = NumberFormat.getInstance().format(sum);
//                        totalSalesAmount.setText("Total sales from start: " + formattedTotal + " LKR");
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        getTotalOrdersForMonth();

        Intent intent = getIntent();

        String barOne = intent.getStringExtra("BarOne");
        String barTwo = intent.getStringExtra("BarTwo");
        String barThree = intent.getStringExtra("BarThree");
        String barFour = intent.getStringExtra("BarFour");
        String barFive = intent.getStringExtra("BarFive");
        String graphMonth = intent.getStringExtra("GraphMonth");

        int salesFirstWeek = Integer.parseInt(barOne);
        int salesSecondWeek = Integer.parseInt(barTwo);
        int salesThirdWeek = Integer.parseInt(barThree);
        int salesFourthWeek = Integer.parseInt(barFour);
        int salesFifthWeek = Integer.parseInt(barFive);
        int addAllWeeks = salesFirstWeek + salesSecondWeek + salesThirdWeek + salesFourthWeek + salesFifthWeek;
        String formattedTotal = NumberFormat.getInstance().format(addAllWeeks);

        totalSalesOfMonth.setText(formattedTotal + " LKR");

        monthStatus.setText("Month of sales: " + graphMonth);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(Integer.parseInt(barOne), 0));
        barEntries.add(new BarEntry(Integer.parseInt(barTwo), 1));
        barEntries.add(new BarEntry(Integer.parseInt(barThree), 2));
        barEntries.add(new BarEntry(Integer.parseInt(barFour), 3));
        barEntries.add(new BarEntry(Integer.parseInt(barFive), 4));
        BarDataSet barDataSet = new BarDataSet(barEntries, "Weeks");

        ArrayList<String> theWeeks;
        theWeeks = new ArrayList<>();
        theWeeks.add("1st week");
        theWeeks.add("2nd week");
        theWeeks.add("3rd week");
        theWeeks.add("4th week");
        theWeeks.add("5th week");

        BarData theData = new BarData(theWeeks, barDataSet);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barchart.setData(theData);
        barchart.animateY(3000);

        barchart.setTouchEnabled(true);
        barchart.setDragEnabled(true);
        barchart.setScaleEnabled(true);

        //////////////////////////////////////////


    }

    private void getTotalOrdersForMonth(){
        final String saveCurrentMonth, saveCurrentYear;
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentMonth = new SimpleDateFormat("MMM");
        SimpleDateFormat currentYear = new SimpleDateFormat("yyyy");
        saveCurrentMonth = currentMonth.format(callForDate.getTime());
        saveCurrentYear = currentYear.format(callForDate.getTime());

        String dateFrom = saveCurrentMonth  + " 01, " + saveCurrentYear;
        String dateTo = saveCurrentMonth  + " 31, " + saveCurrentYear;

        System.out.println(dateFrom + " to " + dateTo);

        getValuesfromDB.orderByChild("date").startAt(dateFrom).endAt(dateTo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfOrders = (int) dataSnapshot.getChildrenCount();
                totalSalesAmount.setText("Total number of orders this month: " + String.valueOf(totalNumberOfOrders));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}