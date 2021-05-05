package com.example.mediapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mediapp.GetData.GetData;

public class WelcomeMessage extends AppCompatActivity {
    Button nextButton;
    TextView welcomeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_message);
        nextButton = findViewById(R.id.go_to_next);
        welcomeName = findViewById(R.id.username_message);

        welcomeName.setText("Hi " + GetData.superOnlineUsers.getName() + ",");

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeMessage.this, RegisterAddress.class);
                startActivity(intent);
            }
        });
    }
}