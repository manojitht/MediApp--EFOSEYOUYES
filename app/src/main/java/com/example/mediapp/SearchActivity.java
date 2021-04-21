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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.mediapp.GetData.GetData;
import com.example.mediapp.Model.Products;
import com.example.mediapp.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private Button searchButton;
    private EditText searchText;
    private RecyclerView searchList;
    private String SearchInput;
    Dialog dialog;
    ArrayList<String> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchButton = (Button) findViewById(R.id.search_button);
        searchText = (EditText) findViewById(R.id.search_product_name);
        searchList = (RecyclerView) findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        categoryList = new ArrayList<>();
        categoryList.add("Mask");
        categoryList.add("Cardiology");
        categoryList.add("Baby");
        categoryList.add("Tablets");
        categoryList.add("Facial");
        categoryList.add("Handwash");
        categoryList.add("Optical");
        categoryList.add("Devices");
        categoryList.add("Nutrients");
        categoryList.add("Syrup");
        categoryList.add("Balm");
        categoryList.add("Drone");

        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(SearchActivity.this);
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                dialog.getWindow().setLayout(1050,1600);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);

                final ArrayAdapter<String> adapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_list_item_1, categoryList);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        searchText.setText(adapter.getItem(i));
                        dialog.dismiss();
                    }
                });
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchInput = searchText.getText().toString().toLowerCase();
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(reference.orderByChild("category").startAt(SearchInput).endAt(SearchInput), Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
//                holder.Description.setText(model.getDescription());
                holder.txtProductName.setText(model.getProductName());
                holder.productPrice.setText("Price "+model.getPrice()+" LKR");
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
        adapter.startListening();
    }
}