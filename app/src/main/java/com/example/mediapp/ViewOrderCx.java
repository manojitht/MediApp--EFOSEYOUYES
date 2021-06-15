package com.example.mediapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediapp.GetData.GetData;
import com.example.mediapp.Model.Cart;
import com.example.mediapp.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewOrderCx extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView cartItemsCountListener;
    int cartItemsCounts = 0;
    private DatabaseReference takeCountItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_cx);

        recyclerView = findViewById(R.id.view_cart_list);
        cartItemsCountListener = findViewById(R.id.view_cart_activity_item_count);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        onStart();

        takeCountItems = FirebaseDatabase.getInstance().getReference().child("Orders").child(GetData.superOnlineUsers.getName()).child("cart");
        takeCountItems.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    cartItemsCounts = (int) snapshot.getChildrenCount();
                    cartItemsCountListener.setText("Cart items: " + cartItemsCounts);
                } else {
                    cartItemsCountListener.setText("Cart items: 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }

        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference();
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef.child("Orders")
                .child(GetData.superOnlineUsers.getName()).child("cart"), Cart.class).build();

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

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}