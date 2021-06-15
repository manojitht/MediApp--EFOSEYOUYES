package com.example.mediapp.AdminFolder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mediapp.R;

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