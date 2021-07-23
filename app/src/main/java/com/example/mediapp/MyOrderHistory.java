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

import com.example.mediapp.AdminFolder.AdminSoldProducts;
import com.example.mediapp.AdminFolder.GenerateReport;
import com.example.mediapp.GetData.GetData;
import com.example.mediapp.Model.AdminOrders;
import com.example.mediapp.Model.Cart;
import com.example.mediapp.Model.CustomerOrder;
import com.example.mediapp.ViewHolder.CartViewHolder;
import com.example.mediapp.ViewHolder.MyOrdersViewHolder;
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

import java.text.NumberFormat;
import java.util.Objects;

public class MyOrderHistory extends AppCompatActivity {
    private RecyclerView myOrdersRecyclerView;
    private ImageView validatorImage;
    private TextView validatorText;
    private RecyclerView.LayoutManager layoutManager;
    DatabaseReference updateDeliver, updateDeliverOnSalesData;
    private String username = GetData.superOnlineUsers.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_history);

        myOrdersRecyclerView = findViewById(R.id.my_orders_list);
        validatorImage = findViewById(R.id.image_order_history);
        validatorText = findViewById(R.id.text_order_history_message);
        myOrdersRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        myOrdersRecyclerView.setLayoutManager(layoutManager);

        validatorImage.setVisibility(View.GONE);
        validatorText.setVisibility(View.GONE);

//        DatabaseReference accessForOrdersHistory = FirebaseDatabase.getInstance().getReference().child("Users").child(GetData.superOnlineUsers.getName()).child("orders");
//        accessForOrdersHistory.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (!dataSnapshot.exists()){
//
//                }
//                else {
//                    onStart();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Sales Data");

        FirebaseRecyclerOptions<CustomerOrder> options = new FirebaseRecyclerOptions.Builder<CustomerOrder>().setQuery(ordersRef.orderByChild("username").startAt(username).endAt(username), CustomerOrder.class).build();
        ordersRef.orderByChild("username").startAt(username).endAt(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    validatorImage.setVisibility(View.VISIBLE);
                    validatorText.setVisibility(View.VISIBLE);
                    validatorText.setText("No orders history found!");
                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<CustomerOrder, MyOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<CustomerOrder, MyOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MyOrdersViewHolder holder, int position, @NonNull final CustomerOrder model) {
                holder.txtOrderId.setText("#" + model.getOrderId());
                holder.txtStatus.setText("Status: " + model.getStatus());
                holder.txtDate.setText("Placed on "+ model.getDate() + ", " + model.getTime());
                holder.txtAddress.setText("Address: " + model.getAddress());
                int formattedPrice = Integer.parseInt(model.getTotalAmount());
                String orderHistoryPrice = NumberFormat.getInstance().format(formattedPrice);
                holder.txtPrice.setText("Price: " + orderHistoryPrice + " LKR");
                holder.showItems.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String getKeyId = model.getOrderId();
                        Intent intent = new Intent(getApplicationContext(), AdminSoldProducts.class);
                        intent.putExtra("uid", getKeyId);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public MyOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shipped_orders_layout, parent, false);
                MyOrdersViewHolder holder = new MyOrdersViewHolder(view);
                return holder;
            }
        };

        myOrdersRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }


}