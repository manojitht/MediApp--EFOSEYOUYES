package com.example.mediapp.AdminFolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediapp.GetData.GetData;
import com.example.mediapp.LoadingDialog;
import com.example.mediapp.Model.AdminOrders;
import com.example.mediapp.OrderConfirmMessage;
import com.example.mediapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private Button applyChanges, deleteItems;
    private EditText mtnName, mtnPrice, mtnDescription;
    private ImageView mtnImageView;
    private String productId = "";
    private RadioGroup radioGroup;
    private RadioButton pointedButton;
    private TextView lastUpdatedBy;
    private DatabaseReference productsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productId = getIntent().getStringExtra("pid");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);

        applyChanges = (Button) findViewById(R.id.product_button_maintain);
        deleteItems = (Button) findViewById(R.id.product_delete_maintain);
        mtnName = (EditText) findViewById(R.id.product_name_maintain);
        mtnPrice = (EditText) findViewById(R.id.product_price_maintain);
        mtnDescription = (EditText) findViewById(R.id.product_description_maintain);
        mtnImageView = (ImageView) findViewById(R.id.product_image_maintain);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        lastUpdatedBy = (TextView) findViewById(R.id.updated_lastly_by);

        displayTheProductWithTheInformation();

        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doTheChanges();
            }
        });

        deleteItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTheItems();
            }
        });

    }

    private void deleteTheItems() {
        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(getApplicationContext(), ProductMessage.class);
                intent.putExtra("ProductName", "Item");
                startActivity(intent);
                finish();
            }
        });
    }

    private void doTheChanges() {
        final String mName = mtnName.getText().toString();
        String mPrice = mtnPrice.getText().toString();
        String mDescription = mtnDescription.getText().toString();
        pointedButton  = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
        String getStockStatus = pointedButton.getText().toString();

        if (mName.equals("")){
            Toast.makeText(AdminMaintainProductsActivity.this, "Enter the product name..", Toast.LENGTH_SHORT).show();
        }else if (mPrice.equals("")){
            Toast.makeText(AdminMaintainProductsActivity.this, "Enter the price ..", Toast.LENGTH_SHORT).show();
        }else if (mDescription.equals("")){
            Toast.makeText(AdminMaintainProductsActivity.this, "Enter the description ..", Toast.LENGTH_SHORT).show();
        }else {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productId);
            productMap.put("description", mDescription);
            productMap.put("price", mPrice);
            productMap.put("productName", mName);
            productMap.put("stock", getStockStatus);
            productMap.put("lastUpdatedBy", GetData.superOnlineUsers.getName());

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Intent intent = new Intent(getApplicationContext(), ProductMessage.class);
                        intent.putExtra("ProductName", mName);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }

    }

    private void displayTheProductWithTheInformation() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String mName = dataSnapshot.child("productName").getValue().toString();
                    String mPrice = dataSnapshot.child("price").getValue().toString();
                    String mDescription = dataSnapshot.child("description").getValue().toString();
                    String mImage = dataSnapshot.child("image").getValue().toString();
                    String lastlyUpdatedBy = dataSnapshot.child("lastUpdatedBy").getValue().toString();

                    mtnName.setText(mName);
                    mtnPrice.setText(mPrice);
                    mtnDescription.setText(mDescription);
                    Picasso.get().load(mImage).into(mtnImageView);
                    lastUpdatedBy.setText("Last updated by: " +  lastlyUpdatedBy);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}