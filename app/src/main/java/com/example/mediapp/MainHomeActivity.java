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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aldoapps.autoformatedittext.AutoFormatEditText;
import com.example.mediapp.AdminFolder.AdminMaintainProductsActivity;
import com.example.mediapp.AdminFolder.AdminOrdersActivity;
import com.example.mediapp.GetData.GetData;
import com.example.mediapp.Model.Products;
import com.example.mediapp.ViewHolder.ProductViewHolder;
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

import java.util.HashMap;

public class MainHomeActivity extends AppCompatActivity {

    private RecyclerView productContainer;
    private Button  myAccount, myOrders, myCategories;
    private ImageView cartImage, logout;
    private TextView searchLink, cartItemsIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        productContainer = findViewById(R.id.show_home_products);
        myAccount = findViewById(R.id.my_account);
        myOrders = findViewById(R.id.my_orders);
        myCategories = findViewById(R.id.my_categories);
        logout = findViewById(R.id.my_logout);
        cartImage = findViewById(R.id.cart_button_image_home);
        searchLink = findViewById(R.id.search_link_line);
        cartItemsIndicator = findViewById(R.id.total_cart_items_count);

        myAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainHomeActivity.this, UserSettingsActivity.class);
                startActivity(intent);
            }
        });

        myOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainHomeActivity.this, Show_orders.class);
                startActivity(intent);
            }
        });

        myCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainHomeActivity.this, ShowCategoryList.class);
                startActivity(intent);
            }
        });

        cartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainHomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        searchLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainHomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CharSequence options[] = new CharSequence[]{
                        "Yes, logout",
                        "No, stay on!"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(MainHomeActivity.this);
                builder.setTitle("Do you want to logout?");

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0){
                            Intent intent = new Intent(MainHomeActivity.this, LoginActivity.class);
                            finish();
                            startActivity(intent);
                        }
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(reference, Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                holder.txtProductName.setText(model.getProductName());
                holder.productCategory.setText("Category: " + model.getCategory());
                holder.productCardName.setText(model.getProductName());
                holder.productPrice.setText("Price "+model.getPrice()+" lkr");
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final DatabaseReference RootRef;
                        RootRef = FirebaseDatabase.getInstance().getReference();

                        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Intent intent = new Intent(MainHomeActivity.this, ViewDetailActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    intent.putExtra("category", model.getCategory());
                                    startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        productContainer.setAdapter(adapter);
        productContainer.setLayoutManager(new LinearLayoutManager(this)); // setting the layout manager (This is the issue and it solved..)
        adapter.startListening();
    }
}