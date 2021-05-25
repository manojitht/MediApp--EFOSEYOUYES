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

    int[] images = {R.drawable.antiacids_for_gastritis, R.drawable.ayurveda_products, R.drawable.baby_care, R.drawable.baby_diapers, R.drawable.beauty_care, R.drawable.body_care,
            R.drawable.food_and_beverages, R.drawable.glucose_monitors_and_splits, R.drawable.hair_care, R.drawable.house_hold_cleaners, R.drawable.mask, R.drawable.medical_devices,
            R.drawable.mens_grooming, R.drawable.mosquito_repellant, R.drawable.nutrition_and_supplements, R.drawable.oral_care, R.drawable.orthopedic_items, R.drawable.pain_management,
            R.drawable.skin_care, R.drawable.thermometer, R.drawable.vaccine, R.drawable.wet_wipes, R.drawable.wound_care};

    String[] Names = {"Anti-acids for gastritis", "Ayurveda products", "Baby care", "Baby diapers", "Beauty care", "Body care", "Food and beverages", "Glucose monitors and splits",
            "Hair care", "Household cleaners", "Mask", "Medical devices", "Mens grooming", "Mosquito repellents", "Nutrition and supplements", "Oral care", "Orthopedic items", "Pain killer",
            "Skin care", "Thermometer", "Vaccine", "Wet wipes", "Wound care"};

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
                    intent.putExtra("category", "Anti-acids for gastritis");
                    startActivity(intent);
                }else if (position == 1) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Ayurveda products");
                    startActivity(intent);
                }
                else if (position == 2) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Baby care");
                    startActivity(intent);
                }
                else if (position == 3) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Baby diapers");
                    startActivity(intent);
                }
                else if (position == 4) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Beauty care");
                    startActivity(intent);
                }
                else if (position == 5) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Body care");
                    startActivity(intent);
                }
                else if (position == 6) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Food and beverages");
                    startActivity(intent);
                }
                else if (position == 7) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Glucose monitors and splits");
                    startActivity(intent);
                }
                else if (position == 8) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Hair care");
                    startActivity(intent);
                }
                else if (position == 9) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Household cleaners");
                    startActivity(intent);
                }
                else if (position == 10) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Mask");
                    startActivity(intent);
                }
                else if (position == 11) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Medical devices");
                    startActivity(intent);
                }
                else if (position == 12) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Mens grooming");
                    startActivity(intent);
                }
                else if (position == 13) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Mosquito repellents");
                    startActivity(intent);
                }
                else if (position == 14) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Nutrition and supplements");
                    startActivity(intent);
                }
                else if (position == 15) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Oral care");
                    startActivity(intent);
                }
                else if (position == 16) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Orthopedic items");
                    startActivity(intent);
                }
                else if (position == 17) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Pain killer");
                    startActivity(intent);
                }
                else if (position == 18) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Skin care");
                    startActivity(intent);
                }
                else if (position == 19) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Thermometer");
                    startActivity(intent);
                }
                else if (position == 20) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Vaccine");
                    startActivity(intent);
                }
                else if (position == 21) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Wet wipes");
                    startActivity(intent);
                }
                else if (position == 22) {
                    intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                    intent.putExtra("category", "Wound care");
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