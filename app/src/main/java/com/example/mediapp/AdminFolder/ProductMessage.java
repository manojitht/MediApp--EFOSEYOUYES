package com.example.mediapp.AdminFolder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mediapp.HomeActivity;
import com.example.mediapp.OrderConfirmMessage;
import com.example.mediapp.R;
import com.example.mediapp.Show_orders;

public class ProductMessage extends AppCompatActivity {

    TextView ProductNameMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_message);


        ProductNameMessage = findViewById(R.id.product_name_message);

        Intent intent = getIntent();

        String order = intent.getStringExtra("ProductName");
        ProductNameMessage.setText(order);


    }
}