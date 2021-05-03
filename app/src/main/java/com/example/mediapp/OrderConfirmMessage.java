package com.example.mediapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrderConfirmMessage extends AppCompatActivity {

    Button ViewOrdersPage;
    TextView DisplayOrderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm_message);

        ViewOrdersPage = findViewById(R.id.go_to_orders);
        DisplayOrderId = findViewById(R.id.display_order_id);

        Intent intent = getIntent();

        String order = intent.getStringExtra("OrderIdMigrate");
        DisplayOrderId.setText(order);

        ViewOrdersPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderConfirmMessage.this, Show_orders.class);
                startActivity(intent);
            }
        });

    }
}