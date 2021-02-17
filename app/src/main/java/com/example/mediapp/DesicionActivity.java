package com.example.mediapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mediapp.GetData.GetData;
import com.example.mediapp.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class DesicionActivity extends AppCompatActivity {

    private Button loginbutton, registerbutton;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desicion);final LoadingDialog loadingDialog = new LoadingDialog(DesicionActivity.this);
        loginbutton = (Button) findViewById(R.id.login_button);
        registerbutton = (Button) findViewById(R.id.register_button);
        loadingBar = new ProgressDialog(this);

        Paper.init(this);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DesicionActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DesicionActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        String UserNameKey = Paper.book().read(GetData.UserNamekey);
        String UserPasswordKey = Paper.book().read(GetData.UserPasswordKey);

        if (UserNameKey != "" && UserPasswordKey != ""){
            if (!TextUtils.isEmpty(UserNameKey) && !TextUtils.isEmpty(UserPasswordKey)){
                GiveAccess(UserNameKey, UserPasswordKey);

                loadingBar.setTitle("Opening!");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }

    private void GiveAccess(final String Name, final String Password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Users").child(Name).exists()){
                    Users usersData = snapshot.child("Users").child(Name).getValue(Users.class);

                    if (usersData.getName().equals(Name)){
                        if (usersData.getPassword().equals(Password)){
                            Toast.makeText(DesicionActivity.this, "Logged in Successfully!", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(DesicionActivity.this, HomeActivity.class);
                            GetData.superOnlineUsers = usersData;
                            startActivity(intent);
                        }else {
                            Toast.makeText(DesicionActivity.this, "Oops! "+ Name + ", Entered credentials are invalid!", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                }
                else {
                    Toast.makeText(DesicionActivity.this, "Oops! "+ Name + ", Your name doesn't exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}