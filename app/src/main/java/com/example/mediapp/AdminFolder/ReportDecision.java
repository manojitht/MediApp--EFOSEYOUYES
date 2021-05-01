package com.example.mediapp.AdminFolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReportDecision extends AppCompatActivity {
    private Button ShowSalesReport, PointOutGraph, WeekSalesRevenue;
    private TextView getOne, getTwo, getThree, getFour, monthOne;
    DatabaseReference GetRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_decision);

        ShowSalesReport = findViewById(R.id.show_sales_report);
        PointOutGraph = findViewById(R.id.graph_generator);
        WeekSalesRevenue = findViewById(R.id.enter_weekly_sales);
        getOne = findViewById(R.id.retreiveOne);
        getTwo = findViewById(R.id.retreiveTwo);
        getThree = findViewById(R.id.retreiveThree);
        getFour = findViewById(R.id.retreiveFour);
        monthOne = findViewById(R.id.monthOne);


        GetRecord = FirebaseDatabase.getInstance().getReference().child("Month Reports").child("Record");
        GetRecord.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String month = dataSnapshot.child("Month").getValue().toString();
                String firstWeek = dataSnapshot.child("1st week").getValue().toString();
                String secondWeek = dataSnapshot.child("2nd week").getValue().toString();
                String thirdWeek = dataSnapshot.child("3rd week").getValue().toString();
                String fourthWeek = dataSnapshot.child("4th week").getValue().toString();
                getOne.setText(firstWeek);
                getTwo.setText(secondWeek);
                getThree.setText(thirdWeek);
                getFour.setText(fourthWeek);
                monthOne.setText(month);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        ShowSalesReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportDecision.this, AdminOrderHistory.class);
                startActivity(intent);
            }
        });

        PointOutGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String one = getOne.getText().toString();
                String two = getTwo.getText().toString();
                String three = getThree.getText().toString();
                String four = getFour.getText().toString();
                String month = monthOne.getText().toString();

                Intent intent = new Intent(getApplicationContext(), GenerateReport.class);
                intent.putExtra("BarOne", one);
                intent.putExtra("BarTwo", two);
                intent.putExtra("BarThree", three);
                intent.putExtra("BarFour", four);
                intent.putExtra("GraphMonth", month);

                startActivity(intent);
            }
        });



        WeekSalesRevenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportDecision.this, AdminReportSales.class);
                startActivity(intent);
            }
        });
    }
}