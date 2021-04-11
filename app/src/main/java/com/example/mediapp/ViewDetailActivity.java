package com.example.mediapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.mediapp.GetData.GetData;
import com.example.mediapp.Model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ViewDetailActivity extends AppCompatActivity {

    private ImageView ProductImage;
    private Button addToCartButton;
    private ElegantNumberButton integerButton;
    private TextView productOfName, productOfDescription, productOfPrice;
    private String productId = "", status = "Normal";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail);

        productId = getIntent().getStringExtra("pid");

        ProductImage = (ImageView) findViewById(R.id.details_image_view);
        addToCartButton = (Button) findViewById(R.id.product_add_to_cart);
        integerButton = (ElegantNumberButton) findViewById(R.id.increase_button);
        productOfName = (TextView) findViewById(R.id.name_product_text);
        productOfDescription = (TextView) findViewById(R.id.details_product_text);
        productOfPrice = (TextView) findViewById(R.id.price_product_text);


        takeProductDetails(productId);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addingToCart();
                if (status.equals("Order placed") || status.equals("Order Shipped")){
                    Toast.makeText(ViewDetailActivity.this, "You can purchase once your order approved! Thanks..", Toast.LENGTH_SHORT).show();
                }else {
                    addingToCart();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        checkOrder();
    }

    private void addingToCart() {
        String saveCurrentDate, saveCurrentTime;
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(callForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final DatabaseReference soldProducts = FirebaseDatabase.getInstance().getReference().child("Sold products");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productId);
        cartMap.put("pname", productOfName.getText().toString());
        cartMap.put("price", productOfPrice.getText().toString());
        cartMap.put("date", productId);
        cartMap.put("time", productId);
        cartMap.put("quantity", integerButton.getNumber());
        cartMap.put("discount", "");

        cartListRef.child("User View").child(GetData.superOnlineUsers.getName()).child("Products").child(productId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    cartListRef.child("Admin View").child(GetData.superOnlineUsers.getName()).child("Products").child(productId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                soldProducts.child("customers").child(GetData.superOnlineUsers.getName()).child("products").child(productId).updateChildren(cartMap); // making the sold products records to admin
                                Toast.makeText(ViewDetailActivity.this, "Added to cart successfully!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ViewDetailActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
//                    soldProducts.child("customers").child(GetData.superOnlineUsers.getName()).child("products").child(productId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()){
//                                Toast.makeText(ViewDetailActivity.this, "Added to cart successfully!", Toast.LENGTH_SHORT).show();
//
//                                Intent intent = new Intent(ViewDetailActivity.this, HomeActivity.class);
//                                startActivity(intent);
//                            }
//                        }
//                    });
                }
            }
        });
    }

    private void takeProductDetails(final String productId) {

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Products products = dataSnapshot.getValue(Products.class);

                    productOfName.setText(products.getProductName());
                    productOfPrice.setText(products.getPrice());
                    productOfDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(ProductImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkOrder(){
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(GetData.superOnlineUsers.getName());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String shippingState = dataSnapshot.child("status").getValue().toString();


                    if (shippingState.equals("Shipped")){

                        status = "Order Shipped";

                    }else if (shippingState.equals("Not Shipped")){

                        status = "Order placed";

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}