package com.example.mediapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mediapp.GetData.GetData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderShippedMessage extends AppCompatActivity {
    private Button goForShopping;
    private TextView showTheMessage;
    private DatabaseReference getTheMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_shipped_message);
        goForShopping = findViewById(R.id.go_for_shopping);
        showTheMessage = findViewById(R.id.message_order_shipped);

        getTheMessage = FirebaseDatabase.getInstance().getReference().child("Users").child(GetData.superOnlineUsers.getName()).child("message");
        getTheMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Message = dataSnapshot.getValue(String.class);
                showTheMessage.setText(Message);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        goForShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderShippedMessage.this, MainHomeActivity.class);
                startActivity(intent);
                getTheMessage.removeValue();
            }
        });
    }
}