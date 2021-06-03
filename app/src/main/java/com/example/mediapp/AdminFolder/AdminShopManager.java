package com.example.mediapp.AdminFolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mediapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Map;

public class AdminShopManager extends AppCompatActivity {

    private TextView totalSalesAmount, totalNumberOfOrders, productInfo, orderInfo, usersInfo, adminsInfo;

    private DatabaseReference getValuesfromDB, getValuesProducts, getValuesRemainingOrders, getValuesUsers, getValuesAdmin;

    int order = 0;
    int products = 0;
    int remainingOrder = 0;
    int users = 0;
    int admins = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_shop_manager);

        totalSalesAmount = findViewById(R.id.total_amount_from_start);
        totalNumberOfOrders = findViewById(R.id.total_order_count);
        productInfo = findViewById(R.id.product_info);
        orderInfo = findViewById(R.id.orders_info);
        usersInfo = findViewById(R.id.users_info);
        adminsInfo = findViewById(R.id.admin_info);

        getValuesfromDB = FirebaseDatabase.getInstance().getReference().child("Sales Data");
        getValuesProducts = FirebaseDatabase.getInstance().getReference().child("Products");
        getValuesRemainingOrders = FirebaseDatabase.getInstance().getReference().child("Orders");
        getValuesUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        getValuesAdmin = FirebaseDatabase.getInstance().getReference().child("Admins");

        getValuesfromDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()){
                    totalSalesAmount.setText("0 LKR");
                    totalNumberOfOrders.setText("Number of orders: 0");
                }
                else if (dataSnapshot.exists()){

                    order = (int) dataSnapshot.getChildrenCount();
                    totalNumberOfOrders.setText("Number of orders: " + order);


                    int sum = 0;

                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>)ds.getValue();
                        Object price = map.get("totalAmount");
                        int totalPrice = Integer.parseInt(String.valueOf(price));
                        sum += totalPrice;

                        String formattedTotal = NumberFormat.getInstance().format(sum);
                        totalSalesAmount.setText(formattedTotal + " LKR");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getValuesProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()){
                    productInfo.setText("Number of products in show: 0");
                }
                else if (dataSnapshot.exists()){

                    products = (int) dataSnapshot.getChildrenCount();
                    productInfo.setText("Number of products in show: " + products);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getValuesRemainingOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()){
                    orderInfo.setText("Number of orders remains: 0");
                }
                else if (dataSnapshot.exists()){

                    remainingOrder = (int) dataSnapshot.getChildrenCount();
                    orderInfo.setText("Number of orders remains: " + remainingOrder);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getValuesUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()){
                    usersInfo.setText("Number of active users: 0");
                }
                else if (dataSnapshot.exists()){

                    users = (int) dataSnapshot.getChildrenCount();
                    usersInfo.setText("Number of active users: " + users);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getValuesAdmin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()){
                    adminsInfo.setText("Number of admins: 0");
                }
                else if (dataSnapshot.exists()){

                    admins = (int) dataSnapshot.getChildrenCount();
                    adminsInfo.setText("Number of admins: " + admins);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}