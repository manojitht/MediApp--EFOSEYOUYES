package com.example.mediapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.mediapp.GetData.GetData;
import com.example.mediapp.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    }

    private void LoginUser() {
        String Name = InputLoginName.getText().toString();
        String Password = InputLoginPassword.getText().toString();

        if (TextUtils.isEmpty((CharSequence) Name)) {
            Toast.makeText(this, "username cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty((CharSequence) Password)){
            Toast.makeText(this, "password cannot be empty", Toast.LENGTH_SHORT).show();
        }else {
//            final LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);
            loadingDialog.startLoadingDialog();


//            loadingBar.setTitle("Logging in");
//            loadingBar.setMessage("Please wait for the Login...");
//            loadingBar.setCanceledOnTouchOutside(false);
//            loadingBar.show();

            AllowUserToLoginAccount(Name, Password);
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
                    Users usersData = snapshot.child(DatabaseName).child(Name).getValue(Users.class);

                    if (usersData.getName().equals(Name)){
                        if (usersData.getPassword().equals(Password)){
                            if (DatabaseName.equals("Admins")){
                                Toast.makeText(LoginActivity.this, "Admin, Logged in Successfully!", Toast.LENGTH_SHORT).show();
//                                loadingBar.dismiss();
                                loadingDialog.dismissDialog();
                                InputLoginName.setText("");
                                InputLoginPassword.setText("");
                                Intent intent = new Intent(LoginActivity.this, AdminDecisionActivity.class);
                                startActivity(intent);
                            }
                            else if (DatabaseName.equals("Users")){
                                Toast.makeText(LoginActivity.this, "Logged in Successfully!", Toast.LENGTH_SHORT).show();
//                                loadingBar.dismiss();
                                loadingDialog.dismissDialog();
                                InputLoginName.setText("");
                                InputLoginPassword.setText("");
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                GetData.superOnlineUsers = usersData;
                                startActivity(intent);
                            }
                        }else {
                            Toast.makeText(LoginActivity.this, "Oops! "+ Name + ", Entered credentials are invalid!", Toast.LENGTH_SHORT).show();
//                            loadingBar.dismiss();
                            loadingDialog.dismissDialog();
                            InputLoginName.setText("");
                            InputLoginPassword.setText("");
//                            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
//                            startActivity(intent);
                        }
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Oops! "+ Name + ", Your name doesn't exists.", Toast.LENGTH_SHORT).show();
//                    loadingBar.dismiss();
                    loadingDialog.dismissDialog();
                    InputLoginName.setText("");
                    InputLoginPassword.setText("");
//                    Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
//                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }
}