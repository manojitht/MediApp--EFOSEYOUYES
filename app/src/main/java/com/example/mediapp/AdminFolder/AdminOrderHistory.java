package com.example.mediapp.AdminFolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediapp.Model.AdminOrders;
import com.example.mediapp.Model.Products;
import com.example.mediapp.ProductDetailAdminActivity;
import com.example.mediapp.R;
import com.example.mediapp.SearchActivity;
import com.example.mediapp.ViewDetailActivity;
import com.example.mediapp.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class AdminOrderHistory extends AppCompatActivity {

    private Button searchButton;
    private EditText DateFrom, DateTo;
    private RecyclerView searchList;
    private String dateFromInput, dateToInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_history);
        searchButton = (Button) findViewById(R.id.search_button);
        DateFrom = (EditText) findViewById(R.id.search_date_from);
        DateTo = (EditText) findViewById(R.id.search_date_to);
        searchList = (RecyclerView) findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(AdminOrderHistory.this));

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateFromInput = DateFrom.getText().toString();
                dateToInput = DateTo.getText().toString();
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Sales Data");

        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(ordersRef.orderByChild("date").startAt(dateFromInput).endAt(dateToInput), AdminOrders.class).build();
        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersActivity.AdminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersActivity.AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersActivity.AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {
                holder.userName.setText("Name: " + model.getCname());
                holder.userPhoneNumber.setText("Contact: " + model.getPhone());
                holder.userTotalAmount.setText("Cost of price: " + model.getTotalAmount() + " LKR");
                holder.userDateTime.setText("Date: " + model.getDate() + ", Time: " + model.getTime());
                holder.userShippingAddress.setText("Shipping Address: " + model.getAddress() + " " + model.getCity());

                holder.showOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String uID = getRef(position).getKey();

                        Intent intent = new Intent(AdminOrderHistory.this, AdminSoldProducts.class);
                        intent.putExtra("uid", uID);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public AdminOrdersActivity.AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                return new AdminOrdersActivity.AdminOrdersViewHolder(view);
            }
        };
        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}