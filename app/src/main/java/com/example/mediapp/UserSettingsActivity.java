package com.example.mediapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediapp.GetData.GetData;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSettingsActivity extends AppCompatActivity {

    private CircleImageView settingsProfileImage;
    private EditText userName, eMail, homeAddress;
    private Button close, update, setSecurity;
    private TextView changeAvatar;

    private Uri imageUri;
    private String pictureUrl = "";
    private StorageReference storageProfileImageRef;
    private StorageTask uploadTask;
    private String checker = "";
//    private static int TakeGallery = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        storageProfileImageRef = FirebaseStorage.getInstance().getReference().child("ProfilePictures");

        settingsProfileImage = (CircleImageView) findViewById(R.id.settings_profile_image);
        userName = (EditText) findViewById(R.id.edit_username);
        eMail = (EditText) findViewById(R.id.edit_email);
        homeAddress = (EditText) findViewById(R.id.edit_address);
        close = (Button) findViewById(R.id.settings_close);
        update = (Button) findViewById(R.id.settings_update);
        setSecurity = (Button) findViewById(R.id.set_security_questions);
        changeAvatar = (TextView) findViewById(R.id.avatar_changer);
        
        userInfoDiasplay(settingsProfileImage, userName, eMail, homeAddress);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(userName.getText().toString())){
                    Toast.makeText(UserSettingsActivity.this, "Username is empty..", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(eMail.getText().toString())){
                    Toast.makeText(UserSettingsActivity.this, "Email is empty..", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(homeAddress.getText().toString())){
                    Toast.makeText(UserSettingsActivity.this, "Address is empty..", Toast.LENGTH_SHORT).show();
                }else if(checker.equals("clicked")){
                    uploadImage();
                }else {

                    updateDataOfUser();

                }
            }
        });

        setSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSettingsActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check", "settings");
                startActivity(intent);
            }
        });

        changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";

                Intent imageIntent = new Intent();
                imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                imageIntent.setType("image/*");
//                startActivityForResult(imageIntent, TakeGallery);

                CropImage.activity(imageUri).setAspectRatio(1,1)
                        .start(UserSettingsActivity.this);
            }
        });

    }

    private void updateDataOfUser() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", userName.getText().toString());
        userMap.put("email", eMail.getText().toString());
        userMap.put("address", homeAddress.getText().toString());
        ref.child(GetData.superOnlineUsers.getName()).updateChildren(userMap);

        startActivity(new Intent(UserSettingsActivity.this, UserSettingsActivity.class));
        Toast.makeText(UserSettingsActivity.this, "Updated successfully !",  Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            settingsProfileImage.setImageURI(imageUri);
        }else {
            Toast.makeText(this,"Try Again...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserSettingsActivity.this, UserSettingsActivity.class));
            finish();
        }
    }

//    private void saveInfoOfUser() {
//        if (TextUtils.isEmpty(userName.getText().toString())){
//            Toast.makeText(UserSettingsActivity.this, "Username is empty..", Toast.LENGTH_SHORT).show();
//        }else if (TextUtils.isEmpty(eMail.getText().toString())){
//            Toast.makeText(UserSettingsActivity.this, "Email is empty..", Toast.LENGTH_SHORT).show();
//        }else if (TextUtils.isEmpty(homeAddress.getText().toString())){
//            Toast.makeText(UserSettingsActivity.this, "Address is empty..", Toast.LENGTH_SHORT).show();
//        }else if(checker.equals("clicked")){
//            uploadImage();
//        }
//    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Profile Update");
        progressDialog.setMessage("Please wait to upload your image");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null){
            final StorageReference fileRef = storageProfileImageRef.child(GetData.superOnlineUsers.getName() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUrl = task.getResult();
                        pictureUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name", userName.getText().toString());
                        userMap.put("email", eMail.getText().toString());
                        userMap.put("address", homeAddress.getText().toString());
                        userMap.put("image", pictureUrl);
                        ref.child(GetData.superOnlineUsers.getName()).updateChildren(userMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(UserSettingsActivity.this, HomeActivity.class));
                        Toast.makeText(UserSettingsActivity.this, "Updated successfully !",  Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(UserSettingsActivity.this, "SomeThing Wrong Check Again !",  Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Sorry! Image isn't selected..",  Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDiasplay(final CircleImageView settingsProfileImage, final EditText userName, final EditText eMail, final EditText homeAddress) {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(GetData.superOnlineUsers.getName());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("name").exists()){
//                        String image = dataSnapshot.child("image").getValue().toString(); // This works on the real physical device
                        String name = dataSnapshot.child("name").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

//                        Picasso.get().load(image).into(settingsProfileImage);     // This works on the real physical device
                        userName.setText(name);
                        eMail.setText(email);
                        homeAddress.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}