package com.example.mediapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mediapp.AdminFolder.AdminDecisionActivity;
import com.example.mediapp.AdminFolder.GenerateReport;
import com.example.mediapp.GetData.GetData;
import com.example.mediapp.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private Button LoginButton;
    private TextView ForgotPassword, return_register;
    private EditText InputLoginName, InputLoginPassword;
    private ProgressDialog loadingBar;
    private LottieAnimationView lottieAnimationViewLogin;
    private Button imAdmin, notAdmin;

    private String DatabaseName = "Users";
    private CheckBox checkboxRemember;

    public LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        LoginButton = (Button) findViewById(R.id.loginActivity_button);
        InputLoginName = (EditText) findViewById(R.id.Login_name);
        InputLoginPassword = (EditText) findViewById(R.id.Login_password);
        ForgotPassword = (TextView) findViewById(R.id.forgot_password);
        loadingBar = new ProgressDialog(this);
        lottieAnimationViewLogin = findViewById(R.id.lottie_layer_name);
        imAdmin = (Button) findViewById(R.id.admin_panel);
        notAdmin = (Button) findViewById(R.id.customer_user);
        checkboxRemember = (CheckBox) findViewById(R.id.remember_me);
        return_register = (TextView) findViewById(R.id.return_register);
        Paper.init(this);
        notAdmin.setVisibility(View.INVISIBLE);

        return_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });

        imAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login Admin");
                imAdmin.setVisibility(View.INVISIBLE);
                notAdmin.setVisibility(View.VISIBLE);
                DatabaseName = "Admins";
            }
        });

        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check", "login");
                startActivity(intent);
            }
        });

        notAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login");
                imAdmin.setVisibility(View.VISIBLE);
                notAdmin.setVisibility(View.INVISIBLE);
                DatabaseName = "Users";
            }
        });

        String UserNameKey = Paper.book().read(GetData.UserNamekey);
        String UserPasswordKey = Paper.book().read(GetData.UserPasswordKey);

        if (!notConnectedToInternet(this)){
            showCustomDialog();
        }
        else
        {
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
    }

    private void LoginUser() {

        if (!notConnectedToInternet(this)){
            showCustomDialog();
        }
        else {
            String Name = InputLoginName.getText().toString();
            String Password = InputLoginPassword.getText().toString();

            if (TextUtils.isEmpty((CharSequence) Name)) {
                Toast.makeText(this, "username cannot be empty", Toast.LENGTH_SHORT).show();
            }
            else if (TextUtils.isEmpty((CharSequence) Password)){
                Toast.makeText(this, "password cannot be empty", Toast.LENGTH_SHORT).show();
            }else {
                loadingDialog.startLoadingDialog();
                AllowUserToLoginAccount(Name, Password);
            }
        }

    }



    private void AllowUserToLoginAccount(final String Name, final String Password) {

        if (checkboxRemember.isChecked()){
            Paper.book().write(GetData.UserNamekey, Name);
            Paper.book().write(GetData.UserPasswordKey, Password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(DatabaseName).child(Name).exists()){
                    final Users usersData = snapshot.child(DatabaseName).child(Name).getValue(Users.class);

                    if (usersData.getName().equals(Name)){
                        if (usersData.getPassword().equals(Password)){
                            if (DatabaseName.equals("Admins")){

                                DatabaseReference GetAddress = FirebaseDatabase.getInstance().getReference().child("Admins").child(Name);
                                GetAddress.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        loadingDialog.dismissDialog();
                                        InputLoginName.setText("");
                                        InputLoginPassword.setText("");
                                        Intent intent = new Intent(getApplicationContext(), AdminDecisionActivity.class);
                                        intent.putExtra("AdminName", Name);
                                        startActivity(intent);
                                        GetData.superOnlineUsers = usersData;
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                            else if (DatabaseName.equals("Users")){
                                DatabaseReference GetAddress = FirebaseDatabase.getInstance().getReference().child("Users").child(Name);
                                GetAddress.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (Objects.equals(dataSnapshot.child("customerStatus").getValue(), "existing")){
                                            if (dataSnapshot.child("message").exists()){
                                                loadingDialog.dismissDialog();
                                                InputLoginName.setText("");
                                                InputLoginPassword.setText("");
                                                Intent intent = new Intent(LoginActivity.this, OrderShippedMessage.class);
                                                GetData.superOnlineUsers = usersData;
                                                startActivity(intent);
                                                finish();
                                            }
                                            else {
                                                loadingDialog.dismissDialog();
                                                InputLoginName.setText("");
                                                InputLoginPassword.setText("");
                                                Intent intent = new Intent(LoginActivity.this, MainHomeActivity.class);
                                                GetData.superOnlineUsers = usersData;
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                        else if (Objects.equals(dataSnapshot.child("customerStatus").getValue(), "new")){
                                            Toast.makeText(LoginActivity.this, "Logged in Successfully!", Toast.LENGTH_SHORT).show();
                                            loadingDialog.dismissDialog();
                                            InputLoginName.setText("");
                                            InputLoginPassword.setText("");
                                            Intent intent = new Intent(LoginActivity.this, WelcomeMessage.class);
                                            GetData.superOnlineUsers = usersData;
                                            startActivity(intent);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }else {
                            Toast.makeText(LoginActivity.this, "Oops! "+ Name + ", Entered credentials are invalid!", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                            InputLoginName.setText("");
                            InputLoginPassword.setText("");
                        }
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Oops! "+ Name + ", Your name doesn't exists.", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismissDialog();
                    InputLoginName.setText("");
                    InputLoginPassword.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

    private void GiveAccess(final String Name, final String Password) {

        final DatabaseReference RootRef, checkMessage;
        RootRef = FirebaseDatabase.getInstance().getReference();
        checkMessage = FirebaseDatabase.getInstance().getReference().child("Users");

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Users").child(Name).exists()){
                    final Users usersData = snapshot.child("Users").child(Name).getValue(Users.class);

                    if (usersData.getName().equals(Name)){
                        if (usersData.getPassword().equals(Password)){
                            checkMessage.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child(Name).child("message").exists()){
                                        loadingBar.dismiss();
                                        Intent intent = new Intent(LoginActivity.this, OrderShippedMessage.class);
                                        GetData.superOnlineUsers = usersData;
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        loadingBar.dismiss();
                                        Intent intent = new Intent(LoginActivity.this, MainHomeActivity.class);
                                        GetData.superOnlineUsers = usersData;
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }else {
                            Toast.makeText(LoginActivity.this, "Oops! "+ Name + ", Entered credentials are invalid!", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Oops! "+ Name + ", Your name doesn't exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean notConnectedToInternet(LoginActivity loginActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) loginActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi != null && wifi.isConnected() || (mobileNetwork != null && mobileNetwork.isConnected())){
            return true;
        }
        else {
            return false;
        }
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Network error occurred! please connect to the internet!").setCancelable(false).setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}