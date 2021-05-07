package com.example.mediapp.AdminFolder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mediapp.R;

public class AdminCategoryActivity extends AppCompatActivity {

    ListView categoryListView;

    int[] images = {R.drawable.first_aid_kit, R.drawable.vaccines, R.drawable.balm, R.drawable.vitamin, R.drawable.drink, R.drawable.thermometer,
                    R.drawable.ophtalmology, R.drawable.soap, R.drawable.shampoo, R.drawable.pills, R.drawable.newborn, R.drawable.mask,
                    R.drawable.cardiogram, R.drawable.surgical_components, R.drawable.stethoscope};

    String[] Names = {"First Aid Box", "Vaccines & Injections", "Balm products", "Vitamin tablets", "Energy drinks", "Thermometer", "Optical", "Soaps & sanitizer", "Shampoo", "Tablets", "Baby products",
                                "Face Mask", "Cardiac products", "Surgical equipments", "Stethoscopes"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        categoryListView = findViewById(R.id.ListView);
        CustomAdapter customAdapter = new CustomAdapter();
        categoryListView.setAdapter(customAdapter);

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent;
                if (position == 0) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "First Aid Box");
                    startActivity(intent);
                }else if (position == 1) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Vaccines & Injections");
                    startActivity(intent);
                }
                else if (position == 2) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Balm products");
                    startActivity(intent);
                }
                else if (position == 3) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Vitamin tablets");
                    startActivity(intent);
                }
                else if (position == 4) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Energy drinks");
                    startActivity(intent);
                }
                else if (position == 5) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Thermometer");
                    startActivity(intent);
                }
                else if (position == 6) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Optical");
                    startActivity(intent);
                }
                else if (position == 7) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Soaps & sanitizer");
                    startActivity(intent);
                }
                else if (position == 9) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Shampoo");
                    startActivity(intent);
                }
                else if (position == 10) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Tablets");
                    startActivity(intent);
                }
                else if (position == 11) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Baby products");
                    startActivity(intent);
                }
                else if (position == 12) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Face Mask");
                    startActivity(intent);
                }
                else if (position == 13) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Cardiac products");
                    startActivity(intent);
                }
                else if (position == 14) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Surgical equipments");
                    startActivity(intent);
                }
                else if (position == 15) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Stethoscopes");
                    startActivity(intent);
                }
            }
        });

    }

    class CustomAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.custom_layout_list_view_category, null);
            ImageView categoryImageView = view.findViewById(R.id.imageView);
            TextView categoryNames = view.findViewById(R.id.category_names);

            categoryImageView.setImageResource(images[position]);
            categoryNames.setText(Names[position]);

            return view;
        }
    }
}