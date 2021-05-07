package com.example.mediapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediapp.AdminFolder.AdminDecisionActivity;
import com.example.mediapp.AdminFolder.AdminOrdersActivity;
import com.example.mediapp.GetData.GetData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterAddress extends AppCompatActivity {

    private EditText addressOne, addressTwo, addressThree, addressFour, addressFive;
//    private ProgressDialog loadingBar;

    public CustomAllSetDialog customAllSetDialog = new CustomAllSetDialog(RegisterAddress.this);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_address);

        addressOne = findViewById(R.id.address_one);
        addressTwo = findViewById(R.id.address_two);
        addressThree = findViewById(R.id.address_three);
        addressFour = findViewById(R.id.address_four);
        addressFive = findViewById(R.id.address_five);
        TextView message = findViewById(R.id.username_message);
        Button registerAddress = findViewById(R.id.submit_address);


        message.setText("Hello " + GetData.superOnlineUsers.getName() +  ", please register your address for the future order deliveries!");


        registerAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        if (TextUtils.isEmpty((CharSequence) addressOne.getText().toString())){
            Toast.makeText(RegisterAddress.this, "1st field cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty((CharSequence) addressTwo.getText().toString())){
            Toast.makeText(RegisterAddress.this, "2nd field cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty((CharSequence) addressThree.getText().toString())){
            Toast.makeText(RegisterAddress.this, "3rd field cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty((CharSequence) addressFour.getText().toString())){
            Toast.makeText(RegisterAddress.this, "District cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty((CharSequence) addressFive.getText().toString())){
            Toast.makeText(RegisterAddress.this, "Postal cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else {
            //EnterAddress();
            DatabaseReference UserAddress = FirebaseDatabase.getInstance().getReference("Users").child(GetData.superOnlineUsers.getName());
            UserAddress.child("address").setValue(addressOne.getText().toString() + ", " + addressTwo.getText().toString() + ", " + addressThree.getText().toString() + ", " + addressFour.getText().toString() + ", " + addressFive.getText().toString() + ".");
            UserAddress.child("customerStatus").setValue("existing");
            Toast.makeText(RegisterAddress.this, "Address entered successfully!",  Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterAddress.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
            }
        });

    }


}