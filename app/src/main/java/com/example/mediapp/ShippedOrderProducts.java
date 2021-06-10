package com.example.mediapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mediapp.GetData.GetData;
import com.example.mediapp.Model.Cart;
import com.example.mediapp.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ShippedOrderProducts extends AppCompatActivity {
    private RecyclerView shippedProductRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView titleShowProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipped_order_products);

        shippedProductRecyclerView = findViewById(R.id.shipped_order_products_list);
        titleShowProductList = findViewById(R.id.shipped_products_title);
        shippedProductRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        shippedProductRecyclerView.setLayoutManager(layoutManager);

    }


    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        String getKeyId = intent.getStringExtra("getKey");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef.child("Users").child(GetData.superOnlineUsers.getName()).child("orders").child(getKeyId).child("sold items"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                holder.txtProductQuantity.setText("Quantity = "+model.getQuantity() + " Pcs");
                holder.txtProductCategory.setText("Category: " + model.getCategory());
                holder.txtProductPrice.setText("Price " + model.getPrice() + " lkr");
                holder.editIcon.setVisibility(View.GONE);
                holder.deleteIcon.setVisibility(View.GONE);
                holder.txtProductname.setText(model.getPname());
                Picasso.get().load(model.getImage()).into(holder.productImageView);
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        shippedProductRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}