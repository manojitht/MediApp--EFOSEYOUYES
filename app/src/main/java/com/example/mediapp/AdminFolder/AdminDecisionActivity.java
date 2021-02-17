package com.example.mediapp.AdminFolder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mediapp.DesicionActivity;
import com.example.mediapp.HomeActivity;
import com.example.mediapp.MainActivity;
import com.example.mediapp.R;

public class AdminDecisionActivity extends AppCompatActivity {

    private Button CheckOrders, AdminLogoff,MaintainItems, AddProducts, ShowGraph;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_desicion);

        CheckOrders = (Button) findViewById(R.id.check_new_orders);
        AdminLogoff = (Button) findViewById(R.id.admin_logout);
        MaintainItems = (Button) findViewById(R.id.maintain_admin_products);
        AddProducts = (Button) findViewById(R.id.add_admin_products);
        ShowGraph = (Button) findViewById(R.id.show_graph);

        MaintainItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDecisionActivity.this, HomeActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
                finish();
            }
        });

        AdminLogoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDecisionActivity.this, DesicionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        CheckOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDecisionActivity.this, AdminOrdersActivity.class);
                startActivity(intent);
            }
        });

        AddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDecisionActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
            }
        });

        ShowGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDecisionActivity.this, AdminGraphView.class);
                startActivity(intent);
            }
        });
    }
}