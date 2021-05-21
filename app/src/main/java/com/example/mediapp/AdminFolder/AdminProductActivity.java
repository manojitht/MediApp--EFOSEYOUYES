package com.example.mediapp.AdminFolder;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mediapp.GetData.GetData;
import com.example.mediapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminProductActivity extends AppCompatActivity {

    private String categoryName, Description, Price, ProductName, savePresentDate, savePresentTime;

    private ImageView AddNewProduct;
    private EditText NewProduct, ProductPrice, ProductDescription;
    private Button EnterProduct;
    private static int TakeGallery = 1;
    private Uri imageUri;
    private String  productRandomKey, downloadUrl;
    private StorageReference ProductImageRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);

        categoryName = getIntent().getExtras().get("category").toString();
        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        Toast.makeText(this, categoryName, Toast.LENGTH_SHORT).show();

        AddNewProduct = (ImageView) findViewById(R.id.image_finder);
        NewProduct = (EditText) findViewById(R.id.product_name);
        ProductPrice = (EditText) findViewById(R.id.product_price);
        ProductDescription = (EditText) findViewById(R.id.product_description);
        EnterProduct = (Button) findViewById(R.id.click_add);
        loadingBar = new ProgressDialog(this);

        AddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TakeImage();
            }
        });


        EnterProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadData();
            }
        });

    }

    private void TakeImage() {
        Intent imageIntent = new Intent();
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
        imageIntent.setType("image/*");
        startActivityForResult(imageIntent, TakeGallery);
    }

    private void UploadData() {
        ProductName = NewProduct.getText().toString();
        Price = ProductPrice.getText().toString();
        Description = ProductDescription.getText().toString();

        if (imageUri == null){
            Toast.makeText(this, "Image is not selected!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description)){
            Toast.makeText(this, "Please describe about the product...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price)){
            Toast.makeText(this, "You've missed to enter the price!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(ProductName)){
            Toast.makeText(this, "Write the Product Name..", Toast.LENGTH_SHORT).show();
        }
        else {
            SaveProductDetails();
        }

    }

    private void SaveProductDetails() {

        loadingBar.setTitle("Adding Product");
        loadingBar.setMessage("Please wait for the upload...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat presentDate = new SimpleDateFormat("MM dd, yyyy");
        savePresentDate = presentDate.format(calendar.getTime());

        SimpleDateFormat presentTime = new SimpleDateFormat("HH:mm:ss a");
        savePresentTime = presentTime.format(calendar.getTime());

        productRandomKey = savePresentDate + savePresentTime;

        final StorageReference filePath = ProductImageRef.child(imageUri.getLastPathSegment() + productRandomKey);

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminProductActivity.this, "Error found! " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminProductActivity.this, "Product Image uploaded successfully!", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }

                        downloadUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){

                            downloadUrl = task.getResult().toString();

                            Toast.makeText(AdminProductActivity.this, "Successfully got the image url.", Toast.LENGTH_SHORT).show();

                            StoreInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void StoreInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", savePresentDate);
        productMap.put("time", savePresentTime);
        productMap.put("description", Description);
        productMap.put("image", downloadUrl);
        productMap.put("category", categoryName);
        productMap.put("price", Price);
        productMap.put("stock", "Stock available");
        productMap.put("productName", ProductName);
        productMap.put("lastUpdatedBy", GetData.superOnlineUsers.getName());

        ProductsRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Intent intent = new Intent(AdminProductActivity.this, AdminCategoryActivity.class);
                    startActivity(intent);

                    loadingBar.dismiss();
                    Toast.makeText(AdminProductActivity.this, "Product added successfully!", Toast.LENGTH_SHORT).show();
                }
                else {
                    loadingBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(AdminProductActivity.this, "Error found! " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==TakeGallery && resultCode==RESULT_OK && data!= null){
            imageUri = data.getData();
            AddNewProduct.setImageURI(imageUri);
        }
    }
}