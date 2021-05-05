package com.example.mediapp.AdminFolder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mediapp.ConfirmOrderActivity;
import com.example.mediapp.GetData.GetData;
import com.example.mediapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AdminReportSales extends AppCompatActivity {

    private EditText FirstWeek, SecondWeek, ThirdWeek, FourthWeek, ReportMonth;
    private Button Submit, MakeNewReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report_sales);

        FirstWeek = findViewById(R.id.first_week_sales);
        SecondWeek = findViewById(R.id.second_week_sales);
        ThirdWeek = findViewById(R.id.third_week_sales);
        FourthWeek = findViewById(R.id.fourth_week_sales);
        ReportMonth = findViewById(R.id.report_month);
        Submit = findViewById(R.id.submit_week_sales_revenue);
        MakeNewReport = findViewById(R.id.start_new_month_report);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnterReport();
            }
        });

        MakeNewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DatabaseReference removeGraph = FirebaseDatabase.getInstance().getReference().child("Graph");
//                removeGraph.removeValue();
                ReportMonth.setText("");
                FirstWeek.setText("");
                SecondWeek.setText("");
                ThirdWeek.setText("");
                FourthWeek.setText("");
                Toast.makeText(AdminReportSales.this, "Sales record cleared!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void EnterReport(){
        final DatabaseReference GraphView = FirebaseDatabase.getInstance().getReference().child("Month Reports").child("Record");

        if (ReportMonth.getText().toString().equals("")){
            Toast.makeText(AdminReportSales.this, "Enter the month!", Toast.LENGTH_SHORT).show();
        }
        else if (FirstWeek.getText().toString().equals("")){
            FirstWeek.setText("0");
        }else if(SecondWeek.getText().toString().equals("")){
            SecondWeek.setText("0");
        }else if(ThirdWeek.getText().toString().equals("")){
            ThirdWeek.setText("0");
        }else if(FourthWeek.getText().toString().equals("")){
            FourthWeek.setText("0");
        }else {
            HashMap<String, Object> WeekSales = new HashMap<>();
            WeekSales.put("Month", ReportMonth.getText().toString());
            WeekSales.put("1st week", FirstWeek.getText().toString());
            WeekSales.put("2nd week", SecondWeek.getText().toString());
            WeekSales.put("3rd week", ThirdWeek.getText().toString());
            WeekSales.put("4th week", FourthWeek.getText().toString());
            GraphView.updateChildren(WeekSales);
            Toast.makeText(AdminReportSales.this, "Sales record entered successfully!", Toast.LENGTH_SHORT).show();
        }
    }
}