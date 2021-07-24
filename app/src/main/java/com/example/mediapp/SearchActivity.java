package com.example.mediapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediapp.GetData.GetData;
import com.example.mediapp.Model.Products;
import com.example.mediapp.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private AutoCompleteTextView searchText;
    private RecyclerView searchList;
    private String SearchInput;
    private ImageView noProductFoundImage;
    private TextView noProductFoundText;
    DatabaseReference getAllProducts;
    List<String> productNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchText = (AutoCompleteTextView) findViewById(R.id.search_product_name);
        searchList = (RecyclerView) findViewById(R.id.search_list);
        noProductFoundImage = findViewById(R.id.no_search_image_product);
        noProductFoundText = findViewById(R.id.text_message_search);
        noProductFoundImage.setVisibility(View.GONE);
        noProductFoundText.setVisibility(View.GONE);
        productNames = new ArrayList<>();

        getAllProducts = FirebaseDatabase.getInstance().getReference().child("Products");
        getAllProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
                    String autoTextProducts = childSnapShot.child("productName").getValue(String.class);
                    productNames.add(autoTextProducts);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_dropdown_item_1line, productNames);
                searchText.setThreshold(1);
                searchText.setAdapter(arrayAdapter);
                searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        SearchInput = searchText.getText().toString();
                        onStart();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(getAllProducts.orderByChild("productName").startAt(SearchInput).endAt(SearchInput), Products.class).build();

        getAllProducts.orderByChild("productName").startAt(SearchInput).endAt(SearchInput).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    noProductFoundImage.setVisibility(View.GONE);
                    noProductFoundText.setVisibility(View.GONE);
                } else if (!dataSnapshot.exists()) {
                    noProductFoundImage.setVisibility(View.VISIBLE);
                    noProductFoundText.setVisibility(View.VISIBLE);
                    noProductFoundText.setText("Search for products!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                        Intent intent = new Intent(SearchActivity.this, ViewDetailActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
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

        searchList.setAdapter(adapter);
        searchList.setLayoutManager(new LinearLayoutManager(this));
        adapter.startListening();
    }
}