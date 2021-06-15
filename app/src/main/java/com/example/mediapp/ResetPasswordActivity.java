package com.example.mediapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediapp.GetData.GetData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check = "";
    private TextView showText;
    private EditText secureUsername, questionOne, questionTwo, questionThree;
    private Button verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check = getIntent().getStringExtra("check");

        showText = findViewById(R.id.textView2);
        secureUsername = findViewById(R.id.security_username);
        questionOne = findViewById(R.id.security_questionone);
        questionTwo = findViewById(R.id.security_questiontwo);
        questionThree = findViewById(R.id.security_questionthree);
        verify = findViewById(R.id.security_submit_button);

    }

    @Override
    protected void onStart() {
        super.onStart();

        secureUsername.setVisibility(View.GONE);

        if (check.equals("settings")){
            showText.setText("Please set the answers for the required questions.");
            verify.setText("Set Answers");
            displayTheAnswers();

            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   setQuestions();
                }
            });


        }else if (check.equals("login")){
            secureUsername.setVisibility(View.VISIBLE);

            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verifyUser();
                }
            });
        }
    }

    private void verifyUser() {
        final String Name = secureUsername.getText().toString();
        final String answer1 = questionOne.getText().toString().toLowerCase();
        final String answer2 = questionTwo.getText().toString().toLowerCase();
        final String answer3 = questionThree.getText().toString().toLowerCase();

        if (!Name.equals("") && !answer1.equals("") && !answer2.equals("") && !answer3.equals("")){


            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Name);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String uName = dataSnapshot.child("name").getValue().toString();

                        if (dataSnapshot.hasChild("Security Questions")){
                            String ans1 = dataSnapshot.child("Security Questions").child("answer1").getValue().toString();
                            String ans2 = dataSnapshot.child("Security Questions").child("answer2").getValue().toString();
                            String ans3 = dataSnapshot.child("Security Questions").child("answer3").getValue().toString();

                            if (!ans1.equals(answer1)){
                                Toast.makeText(ResetPasswordActivity.this, "The first answer is mismatch!", Toast.LENGTH_SHORT).show();
                            }else if (!ans2.equals(answer2)){
                                Toast.makeText(ResetPasswordActivity.this, "The second answer is mismatch!", Toast.LENGTH_SHORT).show();
                            }else if (!ans3.equals(answer3)){
                                Toast.makeText(ResetPasswordActivity.this, "The third answer is mismatch!", Toast.LENGTH_SHORT).show();
                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");

                                final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Create new password!");
                                builder.setView(newPassword);
                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (!newPassword.getText().toString().equals("")){
                                            ref.child("password").setValue(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(ResetPasswordActivity.this, "Password reset successfully!", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(ResetPasswordActivity.this, MainHomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                       dialogInterface.cancel();
                                    }
                                });
                                builder.show();
                            }
                        } else{
                            Toast.makeText(ResetPasswordActivity.this, "Sorry, you've not completed the security questions!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(ResetPasswordActivity.this, "This username doesn't match!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else {
            Toast.makeText(ResetPasswordActivity.this, "Please complete the form!", Toast.LENGTH_SHORT).show();
        }


    }

    private void setQuestions() {

        String answer1 = questionOne.getText().toString().toLowerCase();
        String answer2 = questionTwo.getText().toString().toLowerCase();
        String answer3 = questionThree.getText().toString().toLowerCase();

        if (questionOne.equals("") || questionTwo.equals("") || questionThree.equals("")){
            Toast.makeText(ResetPasswordActivity.this, "Answer the remaining questions!", Toast.LENGTH_SHORT).show();
        }else {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(GetData.superOnlineUsers.getName());

            HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("answer1", answer1);
            userdataMap.put("answer2", answer2);
            userdataMap.put("answer3", answer3);

            ref.child("Security Questions").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ResetPasswordActivity.this, "Answers saved successfully!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ResetPasswordActivity.this, UserSettingsActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void displayTheAnswers(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(GetData.superOnlineUsers.getName());

        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    String ans1 = dataSnapshot.child("answer1").getValue().toString();
                    String ans2 = dataSnapshot.child("answer2").getValue().toString();
                    String ans3 = dataSnapshot.child("answer3").getValue().toString();

                    questionOne.setText(ans1);
                    questionTwo.setText(ans2);
                    questionThree.setText(ans3);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}