package com.example.mediapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
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

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class ViewDetailActivity extends AppCompatActivity {

    private ImageView ProductImage;
    private Button addToCartButton;
    private ElegantNumberButton integerButton;
    private TextView productOfName, productOfDescription, productOfPrice, ImageUrl, StockStatus, productCategory;
    private String productId = "", status = "Normal";
    private String category = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail);

        productId = getIntent().getStringExtra("pid");
        category = getIntent().getStringExtra("category");

        ProductImage = (ImageView) findViewById(R.id.details_image_view);
        addToCartButton = (Button) findViewById(R.id.product_add_to_cart);
        integerButton = (ElegantNumberButton) findViewById(R.id.increase_button);
        productOfName = (TextView) findViewById(R.id.name_product_text);
        productOfDescription = (TextView) findViewById(R.id.details_product_text);
        productOfPrice = (TextView) findViewById(R.id.price_product_text);
        ImageUrl = (TextView) findViewById(R.id.image_url);
        StockStatus = (TextView) findViewById(R.id.stock_status);
        productCategory = (TextView) findViewById(R.id.product_category);


        takeProductDetails(productId);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                addingToCart();
                if (status.equals("Order placed") || status.equals("Order Shipped")){
                    Toast.makeText(ViewDetailActivity.this, "You can purchase once your order approved! Thanks..", Toast.LENGTH_SHORT).show();
                }
                else if (StockStatus.getText().toString().equals("Out of stock")){
                    Toast.makeText(ViewDetailActivity.this, "Sorry, this product is unavailable.", Toast.LENGTH_SHORT).show();
                }
                else {
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
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final DatabaseReference soldProducts = FirebaseDatabase.getInstance().getReference().child("Sold products");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productId);
        cartMap.put("pname", productOfName.getText().toString());
        String productPrice = productOfPrice.getText().toString().replace(" LKR", "");
        cartMap.put("price", productPrice);
        cartMap.put("image", ImageUrl.getText());
        cartMap.put("date", productId);
        cartMap.put("time", productId);
        cartMap.put("quantity", integerButton.getNumber());
        cartMap.put("discount", "");
        cartMap.put("category", productCategory.getText().toString());

        cartListRef.child("User View").child(GetData.superOnlineUsers.getName()).child("Products").child(productId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
//                    cartListRef.child("Admin View").child(GetData.superOnlineUsers.getName()).child("Products").child(productId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()){
//                                //soldProducts.child("customers").child(GetData.superOnlineUsers.getName()).child("products").child(productId).updateChildren(cartMap); // making the sold products records to admin
//                                Toast.makeText(ViewDetailActivity.this, "Added to cart successfully!", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
                    Intent intent = new Intent(ViewDetailActivity.this, MainHomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(ViewDetailActivity.this, "Added to cart successfully!", Toast.LENGTH_SHORT).show();
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
                    productOfPrice.setText(products.getPrice() + " LKR");
                    productOfDescription.setText(products.getDescription());
                    productCategory.setText(products.getCategory());
                    Picasso.get().load(products.getImage()).into(ProductImage);
                    ImageUrl.setText(products.getImage());
                    StockStatus.setText(products.getStock());
                    if (StockStatus.getText().toString().equals("Out of stock")){
                        StockStatus.setTextColor(Color.RED);
                    }
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