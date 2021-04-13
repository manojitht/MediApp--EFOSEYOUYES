package com.example.mediapp.AdminFolder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.mediapp.R;

public class ReportDecision extends AppCompatActivity {
    private Button ShowSalesReport, PointOutGraph, TodaySalesRevenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_decision);

        ShowSalesReport = findViewById(R.id.show_sales_report);
        PointOutGraph = findViewById(R.id.graph_generator);
        TodaySalesRevenue = findViewById(R.id.enter_weekly_sales);

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
                Intent intent = new Intent(ReportDecision.this, AdminGraphView.class);
                startActivity(intent);
            }
        });
    }
}