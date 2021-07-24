package com.example.mediapp.AdminFolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mediapp.GetData.GetData;
import com.example.mediapp.LoginActivity;
import com.example.mediapp.MainHomeActivity;
import com.example.mediapp.R;
import com.example.mediapp.ShowCategoryList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.paperdb.Paper;

public class AdminDecisionActivity extends AppCompatActivity {

    private Button CheckOrders, AdminLogoff, MaintainItems, AddProducts, ShowGraph;
    private TextView WelcomeMessage;
    private DatabaseReference LastLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_desicion);

        CheckOrders = (Button) findViewById(R.id.check_new_orders);
        AdminLogoff = (Button) findViewById(R.id.admin_logout);
        MaintainItems = (Button) findViewById(R.id.maintain_admin_products);
        AddProducts = (Button) findViewById(R.id.add_admin_products);
        ShowGraph = (Button) findViewById(R.id.show_graph);
        WelcomeMessage = (TextView) findViewById(R.id.welcome_message);


        Intent intent = getIntent();

        final String admin = intent.getStringExtra("AdminName");


        DatabaseReference LastLoginMessage = FirebaseDatabase.getInstance().getReference().child("Admins").child(admin);
        LastLoginMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("lastLogin").exists()) {
                    String LoginAtLast = dataSnapshot.child("lastLogin").getValue().toString();
                    WelcomeMessage.setText("Hi " + admin + ", Last logout at " + LoginAtLast);
                } else {
                    WelcomeMessage.setText("Hi " + admin + ", Welcome to Mediapp as Admin");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        MaintainItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDecisionActivity.this, ShowCategoryList.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
            }
        });

        AdminLogoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDecisionActivity.this, LoginActivity.class);
                startActivity(intent);
//                LogoutSession();
                Paper.book().destroy();
                finish();
            }
        });

        CheckOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDecisionActivity.this, AdminOrdersActivity.class);
                startActivity(intent);
            }
        });

        AddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDecisionActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
            }
        });

        ShowGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDecisionActivity.this, ReportDecision.class);
                startActivity(intent);
            }
        });
    }

//    private void LogoutSession(){
//        final String LastLoginDate, LastLoginTime;
//        Calendar callForDate = Calendar.getInstance();
//        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd");
//        LastLoginDate = currentDate.format(callForDate.getTime());
//        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm a");
//        LastLoginTime = currentTime.format(callForDate.getTime());
//        LastLogin = FirebaseDatabase.getInstance().getReference("Admins").child(GetData.superOnlineUsers.getName());
//        LastLogin.child("lastLogin").setValue(LastLoginDate + ", " + LastLoginTime);
//    }

}