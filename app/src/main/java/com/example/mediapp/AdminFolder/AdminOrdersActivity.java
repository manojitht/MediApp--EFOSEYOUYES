package com.example.mediapp.AdminFolder;

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
import com.example.mediapp.JavaMailAPI;
import com.example.mediapp.Model.AdminOrders;
import com.example.mediapp.ProductDetailAdminActivity;
import com.example.mediapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.HashMap;

public class AdminOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef, ordersRefA, updateSalesOrders, updateSalesProducts, updateUsersAccount;
    private ImageView notFoundImage;
    private TextView notFoundText;
    private Button refreshButton;
    String nameOfAdmin = GetData.superOnlineUsers.getName();
    int order = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);
        notFoundImage = findViewById(R.id.not_found_image);
        notFoundText = findViewById(R.id.show_text);
        refreshButton = findViewById(R.id.refresh_button);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        ordersRefA = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View");

        updateSalesOrders = FirebaseDatabase.getInstance().getReference().child("Sales Data");
        updateSalesProducts = FirebaseDatabase.getInstance().getReference().child("Orders");
        updateUsersAccount = FirebaseDatabase.getInstance().getReference().child("Users");

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminOrdersActivity.this, AdminOrdersActivity.class);
                finish();
                startActivity(intent);
            }
        });

        ordersList = findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));

        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    onStart();
                    order = (int) snapshot.getChildrenCount();
                    notFoundText.setText("Orders " + order);
                } else {
                    notFoundImage.setVisibility(View.VISIBLE);
                    notFoundText.setText("No orders!");
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

        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(ordersRef, AdminOrders.class).build();
        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {
                holder.customerName.setText("Name: " + model.getCname());
                holder.userPhoneNumber.setText("Contact: " + model.getPhone());
                holder.orderId.setText(model.getOrderId());
                int formattedPrice = Integer.parseInt(model.getTotalAmount());
                String orderHistoryPrice = NumberFormat.getInstance().format(formattedPrice);
                holder.userTotalAmount.setText("Cost of price: " + orderHistoryPrice + " LKR");
                holder.userDate.setText("Date: " + model.getDate());
                holder.userTime.setText("Time: " + model.getTime());
                holder.userShippingAddress.setText("Shipping Address: " + model.getAddress());
                holder.username.setText(model.getUsername());
                holder.approvedBy.setText("E-mail: " + model.getEmail());
                holder.status.setVisibility(View.GONE);

                holder.showOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String uID = getRef(position).getKey();

                        Intent intent = new Intent(AdminOrdersActivity.this, ProductDetailAdminActivity.class);
                        intent.putExtra("uid", uID);
                        startActivity(intent);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]{
                                "Yes",
                                "No"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminOrdersActivity.this);
                        builder.setTitle("Have you shipped this order to the customer?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    final String uID = getRef(position).getKey();


                                    final HashMap<String, Object> cartMap = new HashMap<>();
                                    cartMap.put("orderId", holder.orderId.getText().toString());
                                    cartMap.put("Cname", holder.customerName.getText().toString().replace("Name: ", ""));
                                    cartMap.put("phone", holder.userPhoneNumber.getText().toString().replace("Contact: ", ""));
                                    String totalPrice = holder.userTotalAmount.getText().toString().replace("Cost of price: ", "");
                                    String formattedPrice = totalPrice.replace(" LKR", "");
                                    String finalPrice = formattedPrice.replace(",", "");
                                    cartMap.put("totalAmount", finalPrice);
                                    cartMap.put("address", holder.userShippingAddress.getText().toString().replace("Shipping Address: ", ""));
                                    cartMap.put("date", holder.userDate.getText().toString().replace("Date: ", ""));
                                    cartMap.put("time", holder.userTime.getText().toString().replace("Time: ", ""));
                                    cartMap.put("status", "Shipped");
                                    cartMap.put("approvedBy", nameOfAdmin);
                                    cartMap.put("username", holder.username.getText().toString());
                                    cartMap.put("sold items", "");

                                    updateSalesOrders.child(holder.orderId.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                final String email = holder.approvedBy.getText().toString().replace("E-mail: ", "");
                                                final String orderId = holder.orderId.getText().toString();
                                                final String customerName = holder.customerName.getText().toString().replace("Name: ", "");
                                                sendEmail(email, orderId, customerName);
                                                moveRecord(updateSalesProducts.child(uID).child("cart"), updateSalesOrders.child(holder.orderId.getText().toString()).child("sold items"));
                                                Toast.makeText(AdminOrdersActivity.this, uID + "'s order shipped successfully!", Toast.LENGTH_SHORT).show();
                                                RemoveOrder(uID);
                                                Intent intent = new Intent(AdminOrdersActivity.this, AdminOrdersActivity.class);
                                                finish();
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout_admin, parent, false);
                return new AdminOrdersViewHolder(view);
            }
        };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }


    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder {
        public TextView customerName, userPhoneNumber, userTotalAmount, userDate, userShippingAddress, orderId, userTime, approvedBy, status, username;
        public Button showOrder;

        public AdminOrdersViewHolder(View itemView) {
            super(itemView);

            customerName = itemView.findViewById(R.id.order_cart_items_username);
            userPhoneNumber = itemView.findViewById(R.id.phone_number_order);
            userTotalAmount = itemView.findViewById(R.id.order_total_price);
            userTime = itemView.findViewById(R.id.order_time);
            userDate = itemView.findViewById(R.id.order_date);
            userShippingAddress = itemView.findViewById(R.id.order_address_city);
            showOrder = itemView.findViewById(R.id.show_all_products);
            orderId = itemView.findViewById(R.id.order_id);
            approvedBy = itemView.findViewById(R.id.approved_admin);
            status = itemView.findViewById(R.id.shipping_status);
            username = itemView.findViewById(R.id.username_of_customer);

        }
    }

    private void sendEmail(String email, String orderId, String customerName) {
        String mEmail = email;
        String mSubject = "From MediApp, order reference #" + orderId;
        String mMessage = "Hi " + customerName + ",\n Your order #" + orderId + " has been processed and shipped successfully!\n Thank you for " +
                "purchasing our products.\n If you have any queries, please reach out us with contact number: 0777548623\n Welcome you again!\n From, \n MediApp team.";
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail, mSubject, mMessage);
        javaMailAPI.execute();
    }


    private void RemoveOrder(String uID) {
        ordersRef.child(uID).removeValue();
        ordersRefA.child(uID).removeValue();
    }

    private void moveRecord(final DatabaseReference fromPath, final DatabaseReference toPath) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
//                            fromPath.removeValue();
                        } else {
                            Toast.makeText(AdminOrdersActivity.this, "Something went wrong, try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        fromPath.addListenerForSingleValueEvent(valueEventListener);
    }

}