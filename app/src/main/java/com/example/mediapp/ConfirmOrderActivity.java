package com.example.mediapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mediapp.GetData.GetData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmOrderActivity extends AppCompatActivity {
    private EditText ShipName, ShipAddress, ShipCity, ShipPhone;
    private Button ShipConfirm;
    private String totalAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        final LoadingDialog loadingDialog = new LoadingDialog(ConfirmOrderActivity.this);
        totalAmount = getIntent().getStringExtra("Total Price");

        ShipAddress = (EditText) findViewById(R.id.shipment_address);
        ShipName = (EditText) findViewById(R.id.shipment_name);
        ShipCity = (EditText) findViewById(R.id.shipment_city);
        ShipPhone = (EditText) findViewById(R.id.shipment_PhNumber);
        ShipConfirm = (Button) findViewById(R.id.shipment_confirm);

        ShipConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check();
//                loadingDialog.startLoadingDialog();
            }
        });

    }

    private void Check() {
        if (TextUtils.isEmpty(ShipName.getText().toString())){
            Toast.makeText(ConfirmOrderActivity.this, "Please provide your name!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(ShipCity.getText().toString())){
            Toast.makeText(ConfirmOrderActivity.this, "Please provide your city!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(ShipPhone.getText().toString())){
            Toast.makeText(ConfirmOrderActivity.this, "Please provide your phone number!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(ShipAddress.getText().toString())){
            Toast.makeText(ConfirmOrderActivity.this, "Please provide your address!", Toast.LENGTH_SHORT).show();
        }
        else {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {
        final String saveCurrentDate, saveCurrentTime;
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(GetData.superOnlineUsers.getName()); //creation of the "Orders" child

        HashMap<String, Object> ordersMap = new HashMap<>(); ///creating the object as hash map
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("Cname", ShipName.getText().toString());
        ordersMap.put("phone", ShipPhone.getText().toString());
        ordersMap.put("address", ShipAddress.getText().toString());
        ordersMap.put("city", ShipCity.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("status", "Not Shipped");

        final DatabaseReference salesData = FirebaseDatabase.getInstance().getReference().child("Sales Data").child(GetData.superOnlineUsers.getName()); //creation of the "Orders" child

        HashMap<String, Object> orderSales = new HashMap<>(); ///creating the object as hash map
        orderSales.put("totalAmount", totalAmount);
        orderSales.put("Cname", ShipName.getText().toString());
        orderSales.put("phone", ShipPhone.getText().toString());
        orderSales.put("address", ShipAddress.getText().toString());
        orderSales.put("city", ShipCity.getText().toString());
        orderSales.put("date", saveCurrentDate);
        orderSales.put("time", saveCurrentTime);
        salesData.updateChildren(orderSales);

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
             if (task.isSuccessful()){
                 FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(GetData.superOnlineUsers.getName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         if (task.isSuccessful()){
                             Toast.makeText(ConfirmOrderActivity.this, "You've confirmed the order!", Toast.LENGTH_SHORT).show();

                             Intent intent = new Intent(ConfirmOrderActivity.this, HomeActivity.class);
                             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                             startActivity(intent);
                             finish();
                         }
                     }
                 });
             }
            }
        });

    }
}