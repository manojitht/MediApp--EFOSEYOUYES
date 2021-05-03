package com.example.mediapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button registernow;
    private EditText InputName, InputEmail, InputPassword, InputAddress;
    private ProgressDialog loadingBar;
    private LottieAnimationView lottieAnimationView;
    private TextView text_click;

    public LoadingDialog loadingDialog = new LoadingDialog(RegisterActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        registernow = (Button) findViewById(R.id.register_now);
        InputName = (EditText) findViewById(R.id.name_register);
        InputEmail = (EditText) findViewById(R.id.email_register);
        InputPassword = (EditText) findViewById(R.id.password_register);
        InputAddress = (EditText) findViewById(R.id.password_address);
        loadingBar = new ProgressDialog(this);
        lottieAnimationView = findViewById(R.id.lottie_layer_name);
        text_click = (TextView) findViewById(R.id.text_click);

        text_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RegisterAccount();
            }
        });
    }

    private void RegisterAccount() {
        String Name = InputName.getText().toString();
        String Email = InputEmail.getText().toString();
        String Password = InputPassword.getText().toString();
        String Address = InputAddress.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (TextUtils.isEmpty((CharSequence) Name)){
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if (!Email.matches(emailPattern) || TextUtils.isEmpty((CharSequence) Email)){
            Toast.makeText(this, "Invalid email address given!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty((CharSequence) Password)){
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty((CharSequence) Address)){
            Toast.makeText(this, "Address cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if (Name.length() > 30){
            Toast.makeText(this, "Name can't be more than 30 characters", Toast.LENGTH_SHORT).show();
        }
        else if (Password.length() > 30){
            Toast.makeText(this, "Password can't be more than 30 characters", Toast.LENGTH_SHORT).show();
        }
        else if (Name.length() <= 8){
            Toast.makeText(this, "Username must be at least 8 characters!", Toast.LENGTH_SHORT).show();
        }
        else if (Password.length() <= 8){
            Toast.makeText(this, "Password should be at least 8 characters long!", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingDialog.startLoadingDialog();

//            loadingBar.setTitle("Create Account");
//            loadingBar.setMessage("Please wait for the Account creation.");
//            loadingBar.setCanceledOnTouchOutside(false);
//            loadingBar.show();
            ValidateEmail(Email, Name, Address, Password);
        }
    }

    private void ValidateEmail(final String Email, final String Name, final String Address, final String Password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(String.valueOf(Name)).exists())){
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("name", Name);
                    userdataMap.put("email", Email);
                    userdataMap.put("address", Address);
                    userdataMap.put("password", Password);

                    RootRef.child("Users").child(String.valueOf(Name)).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, Name +" your account created successfully!", Toast.LENGTH_SHORT).show();
//                                loadingBar.dismiss();
                                loadingDialog.dismissDialog();
                                InputName.setText("");
                                InputEmail.setText("");
                                InputAddress.setText("");
                                InputPassword.setText("");
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }else {
//                                loadingBar.dismiss();
                                loadingDialog.dismissDialog();
                                Toast.makeText(RegisterActivity.this, "Check your Network connection!", Toast.LENGTH_SHORT).show();
                                InputName.setText("");
                                InputEmail.setText("");
                                InputAddress.setText("");
                                InputPassword.setText("");
                            }
                        }
                    });
                }else {
                    Toast.makeText(RegisterActivity.this, "Username '"+Name+"' already exists!", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismissDialog();
//                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Try with other username (eg. Steve123)", Toast.LENGTH_SHORT).show();
                    InputName.setText("");
                    InputEmail.setText("");
                    InputAddress.setText("");
                    InputPassword.setText("");
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterActivity.this, "Sorry! account not created successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, DesicionActivity.class);
                startActivity(intent);
            }
        });
    }
}