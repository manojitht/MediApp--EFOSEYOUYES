package com.example.mediapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button registernow;
    private EditText InputName, InputEmail, InputPassword, InputConfirmPassword;
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
        InputConfirmPassword = (EditText) findViewById(R.id.confirm_password);
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
        String ConfirmPassword = InputConfirmPassword.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (TextUtils.isEmpty((CharSequence) Name)) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
        } else if (!Password.equals(ConfirmPassword)) {
            Toast.makeText(this, "Confirm password doesn't matches", Toast.LENGTH_SHORT).show();
        } else if (!Email.matches(emailPattern) || TextUtils.isEmpty((CharSequence) Email)) {
            Toast.makeText(this, "Invalid email address given!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty((CharSequence) Password)) {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
        } else if (Name.length() > 30) {
            Toast.makeText(this, "Name can't be more than 30 characters", Toast.LENGTH_SHORT).show();
        } else if (Password.length() > 30) {
            Toast.makeText(this, "Password can't be more than 30 characters", Toast.LENGTH_SHORT).show();
        } else if (Name.length() <= 4) {
            Toast.makeText(this, "Username must be at least 5 characters!", Toast.LENGTH_SHORT).show();
        } else if (Password.length() <= 7) {
            Toast.makeText(this, "Password should be at least 8 characters long!", Toast.LENGTH_SHORT).show();
        } else {
            loadingDialog.startLoadingDialog();
            ValidateEmail(Email, Name, Password);
        }
    }

    private void ValidateEmail(final String Email, final String Name, final String Password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(String.valueOf(Name)).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("name", Name);
                    userdataMap.put("email", Email);
                    userdataMap.put("customerStatus", "new");
                    userdataMap.put("password", Password);

                    RootRef.child("Users").child(String.valueOf(Name)).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, Name + " your account created successfully!", Toast.LENGTH_SHORT).show();
//                                loadingBar.dismiss();
                                loadingDialog.dismissDialog();
                                InputName.setText("");
                                InputEmail.setText("");
                                InputConfirmPassword.setText("");
                                InputPassword.setText("");
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
//                                loadingBar.dismiss();
                                loadingDialog.dismissDialog();
                                Toast.makeText(RegisterActivity.this, "Check your Network connection!", Toast.LENGTH_SHORT).show();
                                InputName.setText("");
                                InputEmail.setText("");
                                InputConfirmPassword.setText("");
                                InputPassword.setText("");
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Username '" + Name + "' already exists!", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismissDialog();
//                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Try with other username (eg. Steve123)", Toast.LENGTH_SHORT).show();
                    InputName.setText("");
                    InputEmail.setText("");
                    InputConfirmPassword.setText("");
                    InputPassword.setText("");
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterActivity.this, "Sorry! account not created successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Trying to add the mail services

//    private void SendMail(){
//        final String username = "manojithtjmkm@gmail.com";
//        final String password = "***********************";
//        String sendMessage =  "Hi " + InputName.getText().toString() + ", welcome to the MediApp!";
//        Properties properties = new Properties();
//        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.starttls.enable", "true");
//        properties.put("mail.smtp.host", "smtp.gmail.com");
//        properties.put("mail.smtp.port", "587");
//        Session session = Session.getInstance(properties, new javax.mail.Authenticator(){
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication(){
//                return new PasswordAuthentication(username, password);
//            }
//        });
//        try {
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(username));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(InputEmail.getText().toString()));
//            message.setSubject("Welcome to MediApp!");
//            message.setText(sendMessage);
//            Transport.send(message);
//            Toast.makeText(getApplicationContext(), "Email sent successfully", Toast.LENGTH_LONG).show();
//        }
//        catch (MessagingException e ){
//            throw new RuntimeException(e);
//        }
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//    }
}