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

import com.example.mediapp.AdminFolder.AdminOrderHistory;
import com.example.mediapp.AdminFolder.AdminOrdersActivity;
import com.example.mediapp.AdminFolder.AdminSoldProducts;
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
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Show_orders extends AppCompatActivity {


    private Button showOrderHistory;
    private ImageView noOrder;
    private RecyclerView customerOrderList;
    private String username = GetData.superOnlineUsers.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_orders);
        showOrderHistory = findViewById(R.id.take_to_order_history);
        customerOrderList = (RecyclerView) findViewById(R.id.customer_order_list);
        noOrder = findViewById(R.id.no_orders_image);

        onStart();

        showOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Show_orders.this, MyOrderHistory.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(ordersRef.orderByChild("username").startAt(username).endAt(username), AdminOrders.class).build();
        ordersRef.orderByChild("username").startAt(username).endAt(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    noOrder.setVisibility(View.VISIBLE);
                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseRecyclerAdapter<AdminOrders, CustomerOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, CustomerOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CustomerOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {
                holder.referenceId.setText("#"+ model.getOrderId());
                holder.dateAndTime.setText("Date: " + model.getDate() + ", " + "time: " + model.getTime());
                holder.deliveryAddress.setText("Delivery address: " + model.getCname() + ", \n" + model.getAddress() + " \n" + "Contact no: " + model.getPhone() + ".");
                int formattedPrice = Integer.parseInt(model.getTotalAmount());
                String orderPrice = NumberFormat.getInstance().format(formattedPrice);
                holder.price.setText("Price: " + orderPrice + " LKR");
                holder.viewOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uID = getRef(position).getKey();
                        Toast.makeText(Show_orders.this, uID + " selected!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Show_orders.this, ShippedOrderProducts.class);
                        intent.putExtra("getKey", uID);
                        startActivity(intent);
                    }
                });
                holder.cancelOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]{
                                "Yes, cancel",
                                "No"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(Show_orders.this);
                        builder.setTitle("Do you want to cancel the order "+ model.getOrderId() + " ?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0){
                                    String uID = getRef(position).getKey();
                                    ordersRef.child(uID).removeValue();
                                    Toast.makeText(Show_orders.this, uID + " cancelled successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Show_orders.this, Show_orders.class);
                                    finish();
                                    startActivity(intent);
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CustomerOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout_customer, parent, false);
                return new CustomerOrdersViewHolder(view);
            }
        };
        customerOrderList.setAdapter(adapter);
        customerOrderList.setLayoutManager(new LinearLayoutManager(this));
        adapter.startListening();
    }


    public static class CustomerOrdersViewHolder extends RecyclerView.ViewHolder{
        public TextView referenceId, dateAndTime, deliveryAddress, price;
        public Button viewOrder, cancelOrder;
        public CustomerOrdersViewHolder(View itemView){
            super(itemView);

            referenceId = itemView.findViewById(R.id.order_reference_number);
            dateAndTime = itemView.findViewById(R.id.order_date_time);
            deliveryAddress = itemView.findViewById(R.id.order_address_city);
            price = itemView.findViewById(R.id.order_total_price);
            viewOrder = itemView.findViewById(R.id.view_order);
            cancelOrder = itemView.findViewById(R.id.cancel_order);
        }
    }
}