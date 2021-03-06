package com.example.mediapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.MediaRouteButton;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediapp.AdminFolder.AdminOrdersActivity;
import com.example.mediapp.GetData.GetData;
import com.example.mediapp.Model.AdminOrders;
import com.example.mediapp.ViewHolder.MyOrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class Show_orders extends AppCompatActivity {

    private DatabaseReference ordersCancel, ordersCancelAdmin, salesDataRemove, salesProductsRemove;
    private TextView Username, Contact, Address, Price, Date, DescriptionDate, OrderId, titleText;
    private Button viewOrder, cancelOrder, showOrderHistory;
    private RelativeLayout order_card;
    private ImageView popup_image, orderProcessingImage;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_orders);

        viewOrder = (Button) findViewById(R.id.view_order);
        cancelOrder = (Button) findViewById(R.id.cancel_order);
        Username = (TextView) findViewById(R.id.order_username);
        Contact = (TextView) findViewById(R.id.phone_number_order);
        Address = (TextView) findViewById(R.id.order_address_city);
        Price = (TextView) findViewById(R.id.order_total_price);
        Date = (TextView) findViewById(R.id.order_date_time);
        order_card = (RelativeLayout) findViewById(R.id.order_card);
        popup_image = (ImageView) findViewById(R.id.popup_order_image);
        DescriptionDate = (TextView) findViewById(R.id.description_date);
        OrderId = (TextView) findViewById(R.id.order_id);
        showOrderHistory = findViewById(R.id.take_to_order_history);
        titleText = findViewById(R.id.order_in_process_txt);
        progressBar = findViewById(R.id.progressBar);
        orderProcessingImage = findViewById(R.id.order_process_image);

        ordersCancel = FirebaseDatabase.getInstance().getReference().child("Orders").child(GetData.superOnlineUsers.getName());
        salesDataRemove = FirebaseDatabase.getInstance().getReference().child("Sales Data").child(GetData.superOnlineUsers.getName());
        ordersCancelAdmin = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(GetData.superOnlineUsers.getName());
        salesProductsRemove = FirebaseDatabase.getInstance().getReference().child("Sold products").child("customers").child(GetData.superOnlineUsers.getName());
        orderProcessingImage.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        titleText.setVisibility(View.GONE);
        order_card.setVisibility(View.GONE);
        popup_image.setVisibility(View.VISIBLE);// sets default in invisible mode.
        OrderId.setVisibility(View.GONE);

        orderInfoDisplay(Username, Contact, Address, Price, DescriptionDate, Date, OrderId);

        showOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Show_orders.this, MyOrderHistory.class);
                startActivity(intent);
            }
        });

        viewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Show_orders.this, ViewOrderCx.class);
                startActivity(intent);
            }
        });

        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ordersCancel.removeValue();
                ordersCancelAdmin.removeValue();
                salesDataRemove.removeValue();
                salesProductsRemove.removeValue();
                Toast.makeText(Show_orders.this, "Your order has been cancelled!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Show_orders.this, MainHomeActivity.class);
                startActivity(intent);
            }
        });


    }

    private void orderInfoDisplay(final TextView Username, final TextView Contact, final TextView Address, final TextView Price, final TextView DescriptionDate, final TextView Date, final TextView OrderId) {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(GetData.superOnlineUsers.getName());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("Cname").exists()){
                        orderProcessingImage.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        OrderId.setVisibility(View.VISIBLE);
                        order_card.setVisibility(View.VISIBLE);
                        titleText.setVisibility(View.VISIBLE);
                        popup_image.setVisibility(View.GONE);
                        //String image = dataSnapshot.child("image").getValue().toString(); // This works on the real physical device
                        String name = dataSnapshot.child("Cname").getValue().toString();
                        String contact = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();
                        String price = dataSnapshot.child("totalAmount").getValue().toString();
                        int formattedPrice = Integer.parseInt(price);
                        String date = dataSnapshot.child("date").getValue().toString();
                        String time = dataSnapshot.child("time").getValue().toString();
                        String orderId = dataSnapshot.child("orderId").getValue().toString();
                        OrderId.setText("#"+orderId);
                        Username.setText("Name: " + name);
                        Contact.setText("Contact: " + contact);
                        Address.setText("Address: " + address);
                        Price.setText("Price: " + NumberFormat.getInstance().format(formattedPrice) + " LKR");
                        DescriptionDate.setText("Dear customer you have placed the order in the following date and time that was provided below.");
                        Date.setText("Date: " + date + ", Time: " + time);
                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}