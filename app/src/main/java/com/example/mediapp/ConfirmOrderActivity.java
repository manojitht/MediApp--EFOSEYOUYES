package com.example.mediapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mediapp.AdminFolder.AdminOrdersActivity;
import com.example.mediapp.AdminFolder.GenerateReport;
import com.example.mediapp.GetData.GetData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfirmOrderActivity extends AppCompatActivity {
    private EditText ShipName, ShipAddress, ShipCity, ShipPhone;
    private Button ShipConfirm;
    private String totalAmount = "";
    private String username = GetData.superOnlineUsers.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        final LoadingDialog loadingDialog = new LoadingDialog(ConfirmOrderActivity.this);
        totalAmount = getIntent().getStringExtra("Total Price").replace(",", "");

        ShipAddress = (EditText) findViewById(R.id.shipment_address);
        ShipName = (EditText) findViewById(R.id.shipment_name);
        ShipCity = (EditText) findViewById(R.id.shipment_city);
        ShipPhone = (EditText) findViewById(R.id.shipment_PhNumber);
        ShipConfirm = (Button) findViewById(R.id.shipment_confirm);

        userInfoDisplay(ShipName, ShipAddress, ShipPhone);

        ShipConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check();
            }
        });

    }

    private void userInfoDisplay(final EditText ShipName, final EditText ShipAddress, final EditText shipPhone) {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(GetData.superOnlineUsers.getName());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("name").exists()){
                        String name = dataSnapshot.child("name").getValue().toString();
                        //String email = dataSnapshot.child("email").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();
                        if (dataSnapshot.child("phone").exists()){
                            String phone = dataSnapshot.child("phone").getValue().toString();
                            shipPhone.setText(phone);
                        }else {
                            shipPhone.setText("");
                        }
                        ShipName.setText(name);
                        ShipAddress.setText(address);
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Check() {

        if (TextUtils.isEmpty(ShipName.getText().toString())){
            Toast.makeText(ConfirmOrderActivity.this, "Please provide your name!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(ShipCity.getText().toString())){
            Toast.makeText(ConfirmOrderActivity.this, "Please provide your city!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(ShipPhone.getText().toString())){
            Toast.makeText(ConfirmOrderActivity.this, "Please provide your phone number!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(ShipAddress.getText().toString())){
            Toast.makeText(ConfirmOrderActivity.this, "Please provide your address!", Toast.LENGTH_SHORT).show();
        }
        else {
            ConfirmOrder();

        }
    }

    private void ConfirmOrder() {
        Random random = new Random();  // Generating the random number...
        final int number = random.nextInt(999999999);//
        final String orderId;
        final String saveCurrentDate, saveCurrentTime, getCurrentRefTime;
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());
        SimpleDateFormat RefTime = new SimpleDateFormat("HHmmss");
        getCurrentRefTime = RefTime.format(callForDate.getTime());
        orderId= "REF" + number + getCurrentRefTime;

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(orderId); //creation of the "Orders" child
        final DatabaseReference getProducts = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View");

        final HashMap<String, Object> ordersMap = new HashMap<>(); ///creating the object as hash map
        ordersMap.put("orderId", orderId);
        totalAmount.replace(" LKR", "");
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("username", username);
        ordersMap.put("Cname", ShipName.getText().toString());
        ordersMap.put("phone", ShipPhone.getText().toString());
        ordersMap.put("address", ShipAddress.getText().toString());
        ordersMap.put("city", ShipCity.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("status", "Not Shipped");

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
             if (task.isSuccessful()){
                 ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         if (task.isSuccessful()){
                             moveRecord(getProducts.child(GetData.superOnlineUsers.getName()).child("Products"), ordersRef.child("cart"));
                             FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(GetData.superOnlineUsers.getName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if (task.isSuccessful()){
                                         Intent intent = new Intent(getApplicationContext(), OrderConfirmMessage.class);
                                         intent.putExtra("OrderIdMigrate", orderId);

                                         startActivity(intent);
                                         ShipAddress.setText("");
                                         ShipName.setText("");
                                         ShipPhone.setText("");
                                         ShipCity.setText("");
                                         finish();
                                     }
                                 }
                             });
                         }
                     }
                 });

             }
            }
        });

    }

    private void moveRecord(final DatabaseReference fromPath, final DatabaseReference toPath) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()){
                        }
                        else {
                            Toast.makeText(ConfirmOrderActivity.this, "Something went wrong, try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        fromPath.addListenerForSingleValueEvent(valueEventListener);
    }
}