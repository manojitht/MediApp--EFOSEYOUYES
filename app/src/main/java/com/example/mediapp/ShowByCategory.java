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
import android.widget.Toast;

import com.example.mediapp.AdminFolder.AdminMaintainProductsActivity;
import com.example.mediapp.GetData.GetData;
import com.example.mediapp.Model.Products;
import com.example.mediapp.Model.Users;
import com.example.mediapp.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ShowByCategory extends AppCompatActivity {

    private RecyclerView CategoryContainer;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_by_category);

        TextView showCategoryName = findViewById(R.id.category_name_title);
        CategoryContainer = findViewById(R.id.render_category_list);

        categoryName = getIntent().getExtras().get("category").toString();
        showCategoryName.setText(categoryName);

    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(reference.orderByChild("category").startAt(categoryName).endAt(categoryName), Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                holder.txtProductName.setText(model.getProductName());
                holder.productCategory.setText("Category: " + model.getCategory());
                holder.productCardName.setText(model.getProductName());
                holder.productPrice.setText("Price " + model.getPrice() + " lkr");
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final DatabaseReference RootRef;
                        RootRef = FirebaseDatabase.getInstance().getReference();

                        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.child("Admins").child(GetData.superOnlineUsers.getName()).exists()) {
                                    Intent intent = new Intent(ShowByCategory.this, AdminMaintainProductsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    intent.putExtra("category", model.getCategory());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(ShowByCategory.this, ViewDetailActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    intent.putExtra("category", model.getCategory());
                                    startActivity(intent);
                                }
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

        CategoryContainer.setAdapter(adapter);
        CategoryContainer.setLayoutManager(new LinearLayoutManager(this)); // setting the layout manager (This is the issue and it solved..)
        adapter.startListening();
    }
}