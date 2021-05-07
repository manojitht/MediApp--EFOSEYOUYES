package com.example.mediapp;

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

import com.example.mediapp.AdminFolder.AdminCategoryActivity;
import com.example.mediapp.AdminFolder.AdminProductActivity;

public class ShowCategoryList extends AppCompatActivity {

    ListView categoryListView;

    int[] images = {R.drawable.first_aid_kit, R.drawable.vaccines, R.drawable.balm, R.drawable.vitamin, R.drawable.drink, R.drawable.thermometer,
            R.drawable.ophtalmology, R.drawable.soap, R.drawable.shampoo, R.drawable.pills, R.drawable.newborn, R.drawable.mask,
            R.drawable.cardiogram, R.drawable.surgical_components, R.drawable.stethoscope};

    String[] Names = {"First Aid Box", "Vaccines & Injections", "Balm products", "Vitamin tablets", "Energy drinks", "Thermometer", "Optical", "Soaps & sanitizer", "Shampoo", "Tablets", "Baby products",
            "Face Mask", "Cardiac products", "Surgical equipments", "Stethoscopes"};

    String[] Descriptions = {
            "Helps to make the heal quickly..",
            "Vaccination is a simple, safe, and effective way of protecting people against harmful diseases",
            "A fragrant cream or liquid used to heal or soothe the skin",
            "Multivitamins are not a ticket to optimal health",
            "An energy drink is a type of drink containing stimulant compounds",
            "thermometer is a device that measures temperature or a temperature gradient",
            "relating to vision visual optical wavelength",
            "Highly effective perfumed hand wash with anti-bacterial ingredients",
            "Shampoo commonly recognized as a dandruff scalp treatment",
            "No worries after taking it..",
            "Small needs for the kids like soaps, oil, shampoo for babies",
            "Facial masks are here with different types",
            "Prevents disease from the cardiac attack",
            "All kinds of surgical equipments available",
            "Measuring the heart beat..."
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_category_list);

        categoryListView = findViewById(R.id.listView_categories);
        ShowCategoryList.CustomAdapter customAdapter = new ShowCategoryList.CustomAdapter();
        categoryListView.setAdapter(customAdapter);

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent;
                if (position == 0) {
                    intent = new Intent(ShowCategoryList.this, ShowByCategory.class);
                    intent.putExtra("category", "First Aid Box");
                    startActivity(intent);
                }else if (position == 1) {
                    intent = new Intent(ShowCategoryList.this, ShowByCategory.class);
                    intent.putExtra("category", "Vaccines & Injections");
                    startActivity(intent);
                }
                else if (position == 2) {
                    intent = new Intent(ShowCategoryList.this, ShowByCategory.class);
                    intent.putExtra("category", "Balm products");
                    startActivity(intent);
                }
                else if (position == 3) {
                    intent = new Intent(ShowCategoryList.this, ShowByCategory.class);
                    intent.putExtra("category", "Vitamin tablets");
                    startActivity(intent);
                }
                else if (position == 4) {
                    intent = new Intent(ShowCategoryList.this, ShowByCategory.class);
                    intent.putExtra("category", "Energy drinks");
                    startActivity(intent);
                }
                else if (position == 5) {
                    intent = new Intent(ShowCategoryList.this, ShowByCategory.class);
                    intent.putExtra("category", "Thermometer");
                    startActivity(intent);
                }
                else if (position == 6) {
                    intent = new Intent(ShowCategoryList.this, ShowByCategory.class);
                    intent.putExtra("category", "Optical");
                    startActivity(intent);
                }
                else if (position == 7) {
                    intent = new Intent(ShowCategoryList.this, ShowByCategory.class);
                    intent.putExtra("category", "Soaps & sanitizer");
                    startActivity(intent);
                }
                else if (position == 8) {
                    intent = new Intent(ShowCategoryList.this, ShowByCategory.class);
                    intent.putExtra("category", "Shampoo");
                    startActivity(intent);
                }
                else if (position == 9) {
                    intent = new Intent(ShowCategoryList.this, ShowByCategory.class);
                    intent.putExtra("category", "Tablets");
                    startActivity(intent);
                }
                else if (position == 10) {
                    intent = new Intent(ShowCategoryList.this, ShowByCategory.class);
                    intent.putExtra("category", "Baby products");
                    startActivity(intent);
                }
                else if (position == 11) {
                    intent = new Intent(ShowCategoryList.this, ShowByCategory.class);
                    intent.putExtra("category", "Face Mask");
                    startActivity(intent);
                }
                else if (position == 12) {
                    intent = new Intent(ShowCategoryList.this, ShowByCategory.class);
                    intent.putExtra("category", "Cardiac products");
                    startActivity(intent);
                }
                else if (position == 13) {
                    intent = new Intent(ShowCategoryList.this, ShowByCategory.class);
                    intent.putExtra("category", "Surgical equipments");
                    startActivity(intent);
                }
                else if (position == 14) {
                    intent = new Intent(ShowCategoryList.this, ShowByCategory.class);
                    intent.putExtra("category", "Stethoscopes");
                    startActivity(intent);
                }
                else if (position == 15) {
                    intent = new Intent(ShowCategoryList.this, ShowByCategory.class);
                    intent.putExtra("category", "Stethoscopes");
                    startActivity(intent);
                }
            }
        });
    }

    class CustomAdapter extends BaseAdapter {
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
            View view = getLayoutInflater().inflate(R.layout.custom_layout_category_customer, null);
            ImageView categoryImageView = view.findViewById(R.id.imageView_category);
            TextView categoryNames = view.findViewById(R.id.category_names_customer);
            TextView categoryDescription = view.findViewById(R.id.category_description);

            categoryImageView.setImageResource(images[position]);
            categoryNames.setText(Names[position]);
            categoryDescription.setText(Descriptions[position]);

            return view;
        }
    }
}