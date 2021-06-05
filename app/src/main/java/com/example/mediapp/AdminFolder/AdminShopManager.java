package com.example.mediapp.AdminFolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.mediapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminShopManager extends AppCompatActivity {

    private TextView totalSalesAmount, totalNumberOfOrders, totalNumberOfProducts, totalNumberOfUsers, totalNumberOfAdmins;
    private AnyChartView anyChartView;
    private DatabaseReference getValuesfromDB, getValuesOfProducts, getValuesOfAdmins, getValuesOfUsers;

    int order = 0;
    int products = 0;
    int users = 0;
    int admins = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_shop_manager);

        totalSalesAmount = findViewById(R.id.total_orders_revenue);
        totalNumberOfOrders = findViewById(R.id.total_orders_handled);
        totalNumberOfProducts = findViewById(R.id.total_number_products);
        totalNumberOfUsers = findViewById(R.id.total_users_count);
        totalNumberOfAdmins = findViewById(R.id.total_admin_count);
        anyChartView = findViewById(R.id.pie_chart);

        Intent intent = getIntent();

        String antiAcids = intent.getStringExtra("antiAcids");
        String ayurveda = intent.getStringExtra("ayurveda");
        String babyCares = intent.getStringExtra("babyCares");
        String babyDiaper = intent.getStringExtra("babyDiaper");
        String beautyCares = intent.getStringExtra("beautyCares");
        String bodyCares = intent.getStringExtra("bodyCares");
        String foodAndBeverage = intent.getStringExtra("foodAndBeverage");
        String glucoseAndSpill = intent.getStringExtra("glucoseAndSpill");
        String hairCares = intent.getStringExtra("hairCares");
        String houseHoldCleaner = intent.getStringExtra("houseHoldCleaner");
        String masks = intent.getStringExtra("masks");
        String medicalDevice = intent.getStringExtra("medicalDevice");
        String mensGroomings = intent.getStringExtra("mensGroomings");
        String mosquitoRepellent = intent.getStringExtra("mosquitoRepellent");
        String nutrientAndSupplement = intent.getStringExtra("nutrientAndSupplement");
        String oralCares = intent.getStringExtra("oralCares");
        String orthopedicItem = intent.getStringExtra("orthopedicItem");
        String painKillers = intent.getStringExtra("painKillers");
        String skinCares = intent.getStringExtra("skinCares");
        String thermometers = intent.getStringExtra("thermometers");
        String vaccines = intent.getStringExtra("vaccines");
        String wetWipe = intent.getStringExtra("wetWipe");
        String woundCares = intent.getStringExtra("woundCares");

        int antiAcidsValue = Integer.parseInt(antiAcids);
        int ayurvedaValue = Integer.parseInt(ayurveda);
        int babyCaresValue = Integer.parseInt(babyCares);
        int babyDiaperValue = Integer.parseInt(babyDiaper);
        int beautyCaresValue = Integer.parseInt(beautyCares);
        int bodyCaresValue = Integer.parseInt(bodyCares);
        int foodAndBeverageValue = Integer.parseInt(foodAndBeverage);
        int glucoseAndSpillValue = Integer.parseInt(glucoseAndSpill);
        int hairCaresValue = Integer.parseInt(hairCares);
        int houseHoldCleanerValue = Integer.parseInt(houseHoldCleaner);
        int masksValue = Integer.parseInt(masks);
        int medicalDeviceValue = Integer.parseInt(medicalDevice);
        int mensGroomingsValue = Integer.parseInt(mensGroomings);
        int mosquitoRepellentValue = Integer.parseInt(mosquitoRepellent);
        int nutrientAndSupplementValue = Integer.parseInt(nutrientAndSupplement);
        int oralCaresValue = Integer.parseInt(oralCares);
        int orthopedicItemValue = Integer.parseInt(orthopedicItem);
        int painKillersValue = Integer.parseInt(painKillers);
        int skinCaresValue = Integer.parseInt(skinCares);
        int thermometersValue = Integer.parseInt(thermometers);
        int vaccinesValue = Integer.parseInt(vaccines);
        int wetWipeValue = Integer.parseInt(wetWipe);
        int woundCaresValue = Integer.parseInt(woundCares);

        getValuesfromDB = FirebaseDatabase.getInstance().getReference().child("Sales Data");
        getValuesOfProducts = FirebaseDatabase.getInstance().getReference().child("Products");
        getValuesOfUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        getValuesOfAdmins = FirebaseDatabase.getInstance().getReference().child("Admins");

        getValuesfromDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()){
                    totalSalesAmount.setText("0 LKR");
                    totalNumberOfOrders.setText("Number of orders handled: 0");
                }
                else if (dataSnapshot.exists()){

                    order = (int) dataSnapshot.getChildrenCount();
                    totalNumberOfOrders.setText("Number of orders handled: " + order);


                    int sum = 0;

                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>)ds.getValue();
                        Object price = map.get("totalAmount");
                        int totalPrice = Integer.parseInt(String.valueOf(price));
                        sum += totalPrice;

                        String formattedTotal = NumberFormat.getInstance().format(sum);
                        totalSalesAmount.setText("Total number of orders revenue: " + formattedTotal + " LKR");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getValuesOfProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    totalNumberOfProducts.setText("Products in show: 0");
                }
                else if (dataSnapshot.exists()){
                    products = (int) dataSnapshot.getChildrenCount();
                    String formattedProductsCount = NumberFormat.getInstance().format(products);
                    totalNumberOfProducts.setText("Products in show: " + formattedProductsCount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getValuesOfAdmins.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    totalNumberOfAdmins.setText("Number of admins: 0");
                }
                else if (dataSnapshot.exists()){
                    admins = (int) dataSnapshot.getChildrenCount();
                    String formattedAdminsCount = NumberFormat.getInstance().format(admins);
                    totalNumberOfAdmins.setText("Number of admins: " + formattedAdminsCount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getValuesOfUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    totalNumberOfUsers.setText("Number of users: 0");
                }
                else if (dataSnapshot.exists()){
                    users = (int) dataSnapshot.getChildrenCount();
                    String formattedUsersCount = NumberFormat.getInstance().format(users);
                    totalNumberOfUsers.setText("Number of users: " + formattedUsersCount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();
        String[] Names = {"Anti-acids for gastritis", "Ayurveda products", "Baby care", "Baby diapers", "Beauty care", "Body care", "Food and beverages", "Glucose monitors and splits",
                "Hair care", "Household cleaners", "Mask", "Medical devices", "Mens grooming", "Mosquito repellents", "Nutrition and supplements", "Oral care", "Orthopedic items", "Pain killer",
                "Skin care", "Thermometer", "Vaccine", "Wet wipes", "Wound care"};

        int[] noOfItems = {antiAcidsValue, ayurvedaValue, babyCaresValue, babyDiaperValue, beautyCaresValue, bodyCaresValue, foodAndBeverageValue, glucoseAndSpillValue, hairCaresValue, houseHoldCleanerValue,
        masksValue, medicalDeviceValue, mensGroomingsValue, mosquitoRepellentValue, nutrientAndSupplementValue, oralCaresValue, orthopedicItemValue, painKillersValue, skinCaresValue, thermometersValue, vaccinesValue,
        wetWipeValue, woundCaresValue};

        for (int i = 0; i < Names.length; i++){
            dataEntries.add(new ValueDataEntry(Names[i], noOfItems[i]));
        }
        pie.data(dataEntries);
        pie.animation(true, 100);
        anyChartView.setChart(pie);

    }

}