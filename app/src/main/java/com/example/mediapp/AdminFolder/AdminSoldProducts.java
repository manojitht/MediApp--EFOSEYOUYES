package com.example.mediapp.AdminFolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mediapp.Model.Cart;
import com.example.mediapp.R;
import com.example.mediapp.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminSoldProducts extends AppCompatActivity {

    private RecyclerView itemsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference salesItemsRef, UpdateChildOrder;

    private String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sold_products);

        userID = getIntent().getStringExtra("uid");

        itemsList = findViewById(R.id.items_list);
        itemsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        itemsList.setLayoutManager(layoutManager);

        salesItemsRef = FirebaseDatabase.getInstance().getReference().child("Sales Data").child(userID).child("sold items");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(salesItemsRef, Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.txtProductQuantity.setText("Quantity = " + model.getQuantity() + " Pcs");
                holder.txtProductCategory.setText("Category: " + model.getCategory());
                holder.txtProductPrice.setText("Price = " + model.getPrice() + " LKR");
                holder.txtProductname.setText(model.getPname());
                holder.editIcon.setVisibility(View.GONE);
                holder.deleteIcon.setVisibility(View.GONE);
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

        itemsList.setAdapter(adapter);
        adapter.startListening();
    }
}