package com.example.mediapp.AdminFolder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.mediapp.R;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView cardio, mask, babyItems;
    private ImageView tablets, facial, handwash;
    private ImageView optical, devices, nutrients;
    private ImageView syrup, balm, drone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        cardio = (ImageView) findViewById(R.id.medi_cardio);
        mask = (ImageView) findViewById(R.id.medi_mask);
        babyItems = (ImageView) findViewById(R.id.medi_newborn);
        tablets = (ImageView) findViewById(R.id.medi_pills);
        facial = (ImageView) findViewById(R.id.medi_shampoo);
        handwash = (ImageView) findViewById(R.id.medi_soap);
        optical = (ImageView) findViewById(R.id.medi_ophtalmology);
        devices = (ImageView) findViewById(R.id.medi_thermometer);
        nutrients = (ImageView) findViewById(R.id.medi_drinks);
        syrup = (ImageView) findViewById(R.id.medi_vitamins);
        balm = (ImageView) findViewById(R.id.medi_balm);
        drone = (ImageView) findViewById(R.id.medi_drone);




        cardio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "cardio");
                startActivity(intent);
            }
        });

        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "mask");
                startActivity(intent);
            }
        });

        babyItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "baby");
                startActivity(intent);
            }
        });

        tablets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "tablets");
                startActivity(intent);
            }
        });

        facial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "facial");
                startActivity(intent);
            }
        });

        handwash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "handwash");
                startActivity(intent);
            }
        });

        optical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "optical");
                startActivity(intent);
            }
        });


        devices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "devices");
                startActivity(intent);
            }
        });

        nutrients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "nutrients");
                startActivity(intent);
            }
        });

        syrup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "syrup");
                startActivity(intent);
            }
        });

        balm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "balm");
                startActivity(intent);
            }
        });

        drone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "drone");
                startActivity(intent);
            }
        });

    }
}