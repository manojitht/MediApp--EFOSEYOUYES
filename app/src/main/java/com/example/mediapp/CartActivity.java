package com.example.mediapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediapp.AdminFolder.AdminOrdersActivity;
import com.example.mediapp.GetData.GetData;
import com.example.mediapp.Model.Cart;
import com.example.mediapp.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.PriorityQueue;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button CalculateTotal;
    private TextView cartItemsCount, textMessage;
    private ImageView front_image1;
    private int overTotalPrice;
    String formattedPrice;
    int cartItems = 0;
    private DatabaseReference countOfCartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        CalculateTotal = (Button) findViewById(R.id.calculate_process_button);
        textMessage = (TextView) findViewById(R.id.text_message);
        front_image1 = (ImageView) findViewById(R.id.front_image1);
        cartItemsCount = findViewById(R.id.cart_activity_item_count);

        countOfCartItems = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(GetData.superOnlineUsers.getName()).child("Products");
        countOfCartItems.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    cartItems = (int) snapshot.getChildrenCount();
                    cartItemsCount.setText("Cart items: " + cartItems);
                } else {
                    cartItemsCount.setText("Cart items: 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }

        });

        CalculateTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formattedPrice = NumberFormat.getInstance().format(overTotalPrice);
//                CalculateTotal.setVisibility(View.INVISIBLE);

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        CartActivity.this, R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.layout_checkout_bottom_sheet, (ConstraintLayout) findViewById(R.id.checkout_sheet_bottom_layout)
                );
                TextView totalPriceCost = (TextView) bottomSheetView.findViewById(R.id.total_checkout_price);
                TextView shippingCost = (TextView) bottomSheetView.findViewById(R.id.total_checkout_shipping);
                TextView totalAmountCost = (TextView) bottomSheetView.findViewById(R.id.total_amount_price);
                totalPriceCost.setText(formattedPrice + " LKR");
                shippingCost.setText("0" + " LKR");
                totalAmountCost.setText(formattedPrice + " LKR");
                bottomSheetView.findViewById(R.id.checkout_proceed).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (overTotalPrice == 0) {
                            textMessage.setText("Cart is empty can't go further..");
                            front_image1.setVisibility(View.VISIBLE);
                            textMessage.setTextColor(Color.DKGRAY);
                            textMessage.setVisibility(View.VISIBLE);
                            Toast.makeText(CartActivity.this, "Your cart is empty, please add some products!", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(CartActivity.this, ConfirmOrderActivity.class);
                            intent.putExtra("Total Price", String.valueOf(formattedPrice));
                            startActivity(intent);
                            finish();
                        }
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        checkOrder();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef.child("User View")
                .child(GetData.superOnlineUsers.getName()).child("Products"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                holder.txtProductQuantity.setText("Quantity " + model.getQuantity() + " Pcs");
                holder.txtProductCategory.setText("Category: " + model.getCategory());
                holder.txtProductPrice.setText("Rs. " + model.getPrice() + " lkr");
                holder.txtProductname.setText(model.getPname());
                Picasso.get().load(model.getImage()).into(holder.productImageView);

                int oneTypeProductPrice = ((Integer.valueOf(model.getPrice().replace(",", "")))) * Integer.valueOf(model.getQuantity());
                overTotalPrice = overTotalPrice + oneTypeProductPrice;

                holder.editIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CartActivity.this, ViewDetailActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
                    }
                });

                holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cartListRef.child("User View").child(GetData.superOnlineUsers.getName()).child("Products").child(model.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    cartListRef.child("Admin View").child(GetData.superOnlineUsers.getName()).child("Products").child(model.getPid()).removeValue();
                                    final DatabaseReference soldProducts = FirebaseDatabase.getInstance().getReference().child("Sold products"); // declaring the child of Sold products
                                    soldProducts.child("customers").child(GetData.superOnlineUsers.getName()).child("products").child(model.getPid()).removeValue();// removes product from sold products
                                    Toast.makeText(CartActivity.this, model.getPname() + " removed successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CartActivity.this, CartActivity.class);
                                    finish();
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                });
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

    private void checkOrder() {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(GetData.superOnlineUsers.getName());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String shippingState = dataSnapshot.child("status").getValue().toString();
                    DatabaseReference userStatus = FirebaseDatabase.getInstance().getReference().child("User View");
                    String userName = dataSnapshot.child("Cname").getValue().toString();

                    if (shippingState.equals("Shipped")) {
                        recyclerView.setVisibility(View.GONE);
                        textMessage.setVisibility(View.VISIBLE);
                        textMessage.setText("We received your order successfully. Please wait until we process the order to your home address Thank you!");
                        front_image1.setVisibility(View.VISIBLE);
                    } else if (shippingState.equals("Not Shipped")) {
                        recyclerView.setVisibility(View.GONE);
                        textMessage.setVisibility(View.VISIBLE);
                        front_image1.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        textMessage.setVisibility(View.VISIBLE);
                        front_image1.setVisibility(View.VISIBLE);
                        textMessage.setText("Sorry you didn't purchase any items yet!");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}