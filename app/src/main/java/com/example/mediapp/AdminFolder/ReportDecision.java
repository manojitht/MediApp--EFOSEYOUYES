package com.example.mediapp.AdminFolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class ReportDecision extends AppCompatActivity {
    private Button ShowSalesReport, PointOutGraph, SystemInfo;
    private TextView getOne, getTwo, getThree, getFour, getFive, monthOne, antiAcidsForGastritis, ayurvedaProducts, babyCare, babyDiapers, beautyCare, bodyCare, foodAndBeverages, glucoseAndSpills, hairCare, houseHoldCleaners, mask, medicalDevices, mensGrooming, mosquitoRepellents, nutrientAndSupplements, oralCare, orthopedicItems, painKiller, skinCare, thermometer, vaccine, wetWipes, woundCare;
    DatabaseReference productManagerInfo, getSalesOfWeek;
    int totalNumberOfProducts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_decision);

        ShowSalesReport = findViewById(R.id.show_sales_report);
        PointOutGraph = findViewById(R.id.graph_generator);
        SystemInfo = findViewById(R.id.system_info_button);
        getOne = findViewById(R.id.retreiveOne);
        getTwo = findViewById(R.id.retreiveTwo);
        getThree = findViewById(R.id.retreiveThree);
        getFour = findViewById(R.id.retreiveFour);
        getFive = findViewById(R.id.retreiveFive);
        monthOne = findViewById(R.id.monthOne);
        antiAcidsForGastritis = findViewById(R.id.anti_acids_gastritis_tv);
        ayurvedaProducts = findViewById(R.id.ayurveda_products_tv);
        babyCare = findViewById(R.id.baby_care_tv);
        babyDiapers = findViewById(R.id.baby_diapers_tv);
        beautyCare = findViewById(R.id.beauty_care_tv);
        bodyCare = findViewById(R.id.body_care_tv);
        foodAndBeverages = findViewById(R.id.food_and_beverages_tv);
        glucoseAndSpills = findViewById(R.id.glucose_monitor_and_spills_tv);
        hairCare = findViewById(R.id.hair_care_tv);
        houseHoldCleaners = findViewById(R.id.household_cleaners_tv);
        mask = findViewById(R.id.mask_tv);
        medicalDevices = findViewById(R.id.medical_devices_tv);
        mensGrooming = findViewById(R.id.mens_grooming_tv);
        mosquitoRepellents = findViewById(R.id.mosquito_repellents_tv);
        nutrientAndSupplements = findViewById(R.id.nutrition_supplements_tv);
        oralCare = findViewById(R.id.oral_care_tv);
        orthopedicItems = findViewById(R.id.orthopedic_items_tv);
        painKiller = findViewById(R.id.pain_killer_tv);
        skinCare = findViewById(R.id.skin_care_tv);
        thermometer = findViewById(R.id.thermometer_tv);
        vaccine = findViewById(R.id.vaccine_tv);
        wetWipes = findViewById(R.id.wet_wipes_tv);
        woundCare = findViewById(R.id.wound_care_tv);


        getSalesOfWeek = FirebaseDatabase.getInstance().getReference().child("Sales Data");
        productManagerInfo = FirebaseDatabase.getInstance().getReference().child("Products");


        getSalesOfWeek.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    getOne.setText(String.valueOf(0));
                    getTwo.setText(String.valueOf(0));
                    getThree.setText(String.valueOf(0));
                    getFour.setText(String.valueOf(0));
                    getFive.setText(String.valueOf(0));
                } else {
                    final String saveCurrentMonth, saveCurrentYear;
                    Calendar callForDate = Calendar.getInstance();
                    SimpleDateFormat currentMonth = new SimpleDateFormat("MMM");
                    SimpleDateFormat currentYear = new SimpleDateFormat("yyyy");
                    saveCurrentMonth = currentMonth.format(callForDate.getTime());
                    saveCurrentYear = currentYear.format(callForDate.getTime());

                    String firstFrom = saveCurrentMonth + " 01, " + saveCurrentYear;
                    String firstTo = saveCurrentMonth + " 07, " + saveCurrentYear;
                    String secondFrom = saveCurrentMonth + " 08, " + saveCurrentYear;
                    String secondTo = saveCurrentMonth + " 14, " + saveCurrentYear;
                    String thirdFrom = saveCurrentMonth + " 15, " + saveCurrentYear;
                    String thirdTo = saveCurrentMonth + " 21, " + saveCurrentYear;
                    String fourthFrom = saveCurrentMonth + " 22, " + saveCurrentYear;
                    String fourthTo = saveCurrentMonth + " 28, " + saveCurrentYear;
                    String fifthFrom = saveCurrentMonth + " 29, " + saveCurrentYear;
                    String fifthTo = saveCurrentMonth + " 31, " + saveCurrentYear;
                    monthOne.setText(saveCurrentMonth);
                    getSalesOfWeek.orderByChild("date").startAt(firstFrom).endAt(firstTo).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                getOne.setText(String.valueOf(0));
                            } else {
                                int sum = 0;

                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                                    Object price = map.get("totalAmount");
                                    int totalPrice = Integer.parseInt(String.valueOf(price));
                                    sum += totalPrice;
                                    getOne.setText(String.valueOf(sum));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    getSalesOfWeek.orderByChild("date").startAt(secondFrom).endAt(secondTo).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                getTwo.setText(String.valueOf(0));
                            } else {
                                int sum = 0;

                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                                    Object price = map.get("totalAmount");
                                    int totalPrice = Integer.parseInt(String.valueOf(price));
                                    sum += totalPrice;
                                    getTwo.setText(String.valueOf(sum));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    getSalesOfWeek.orderByChild("date").startAt(thirdFrom).endAt(thirdTo).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                getThree.setText(String.valueOf(0));
                            } else {
                                int sum = 0;

                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                                    Object price = map.get("totalAmount");
                                    int totalPrice = Integer.parseInt(String.valueOf(price));
                                    sum += totalPrice;
                                    getThree.setText(String.valueOf(sum));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    getSalesOfWeek.orderByChild("date").startAt(fourthFrom).endAt(fourthTo).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                getFour.setText(String.valueOf(0));
                            } else {
                                int sum = 0;

                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                                    Object price = map.get("totalAmount");
                                    int totalPrice = Integer.parseInt(String.valueOf(price));
                                    sum += totalPrice;
                                    getFour.setText(String.valueOf(sum));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    getSalesOfWeek.orderByChild("date").startAt(fifthFrom).endAt(fifthTo).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                getFive.setText(String.valueOf(0));
                            } else {
                                int sum = 0;

                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                                    Object price = map.get("totalAmount");
                                    int totalPrice = Integer.parseInt(String.valueOf(price));
                                    sum += totalPrice;
                                    getFive.setText(String.valueOf(sum));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Anti-acids for gastritis").endAt("Anti-acids for gastritis").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                antiAcidsForGastritis.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Ayurveda products").endAt("Ayurveda products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                ayurvedaProducts.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Baby care").endAt("Baby care").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                babyCare.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Baby diapers").endAt("Baby diapers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                babyDiapers.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Beauty care").endAt("Beauty care").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                beautyCare.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Body care").endAt("Body care").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                bodyCare.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Food and beverages").endAt("Food and beverages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                foodAndBeverages.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Glucose monitors and splits").endAt("Glucose monitors and splits").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                glucoseAndSpills.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Hair care").endAt("Hair care").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                hairCare.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Household cleaners").endAt("Household cleaners").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                houseHoldCleaners.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Mask").endAt("Mask").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                mask.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Medical devices").endAt("Medical devices").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                medicalDevices.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Mens grooming").endAt("Mens grooming").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                mensGrooming.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Mosquito repellents").endAt("Mosquito repellents").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                mosquitoRepellents.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Nutrition and supplements").endAt("Nutrition and supplements").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                nutrientAndSupplements.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Oral care").endAt("Oral care").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                oralCare.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Orthopedic items").endAt("Orthopedic items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                orthopedicItems.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Pain killer").endAt("Pain killer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                painKiller.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Skin care").endAt("Skin care").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                skinCare.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Thermometer").endAt("Thermometer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                thermometer.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Vaccine").endAt("Vaccine").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                vaccine.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Wet wipes").endAt("Wet wipes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                wetWipes.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productManagerInfo.orderByChild("category").startAt("Wound care").endAt("Wound care").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumberOfProducts = (int) dataSnapshot.getChildrenCount();
                woundCare.setText(String.valueOf(totalNumberOfProducts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ShowSalesReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportDecision.this, AdminOrderHistory.class);
                startActivity(intent);
            }
        });

        PointOutGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String one = getOne.getText().toString();
                String two = getTwo.getText().toString();
                String three = getThree.getText().toString();
                String four = getFour.getText().toString();
                String five = getFive.getText().toString();
                String month = monthOne.getText().toString();

                Intent intent = new Intent(getApplicationContext(), GenerateReport.class);
                intent.putExtra("BarOne", one);
                intent.putExtra("BarTwo", two);
                intent.putExtra("BarThree", three);
                intent.putExtra("BarFour", four);
                intent.putExtra("BarFive", five);
                intent.putExtra("GraphMonth", month);

                startActivity(intent);
            }
        });

        SystemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String antiAcids = antiAcidsForGastritis.getText().toString();
                String ayurveda = ayurvedaProducts.getText().toString();
                String babyCares = babyCare.getText().toString();
                String babyDiaper = babyDiapers.getText().toString();
                String beautyCares = beautyCare.getText().toString();
                String bodyCares = bodyCare.getText().toString();
                String foodAndBeverage = foodAndBeverages.getText().toString();
                String glucoseAndSpill = glucoseAndSpills.getText().toString();
                String hairCares = hairCare.getText().toString();
                String houseHoldCleaner = houseHoldCleaners.getText().toString();
                String masks = mask.getText().toString();
                String medicalDevice = medicalDevices.getText().toString();
                String mensGroomings = mensGrooming.getText().toString();
                String mosquitoRepellent = mosquitoRepellents.getText().toString();
                String nutrientAndSupplement = nutrientAndSupplements.getText().toString();
                String oralCares = oralCare.getText().toString();
                String orthopedicItem = orthopedicItems.getText().toString();
                String painKillers = painKiller.getText().toString();
                String skinCares = skinCare.getText().toString();
                String thermometers = thermometer.getText().toString();
                String vaccines = vaccine.getText().toString();
                String wetWipe = wetWipes.getText().toString();
                String woundCares = woundCare.getText().toString();

                Intent intent = new Intent(getApplicationContext(), AdminShopManager.class);
                intent.putExtra("antiAcids", antiAcids);
                intent.putExtra("ayurveda", ayurveda);
                intent.putExtra("babyCares", babyCares);
                intent.putExtra("babyDiaper", babyDiaper);
                intent.putExtra("beautyCares", beautyCares);
                intent.putExtra("bodyCares", bodyCares);
                intent.putExtra("foodAndBeverage", foodAndBeverage);
                intent.putExtra("glucoseAndSpill", glucoseAndSpill);
                intent.putExtra("hairCares", hairCares);
                intent.putExtra("houseHoldCleaner", houseHoldCleaner);
                intent.putExtra("masks", masks);
                intent.putExtra("medicalDevice", medicalDevice);
                intent.putExtra("mensGroomings", mensGroomings);
                intent.putExtra("mosquitoRepellent", mosquitoRepellent);
                intent.putExtra("nutrientAndSupplement", nutrientAndSupplement);
                intent.putExtra("oralCares", oralCares);
                intent.putExtra("orthopedicItem", orthopedicItem);
                intent.putExtra("painKillers", painKillers);
                intent.putExtra("skinCares", skinCares);
                intent.putExtra("thermometers", thermometers);
                intent.putExtra("vaccines", vaccines);
                intent.putExtra("wetWipe", wetWipe);
                intent.putExtra("woundCares", woundCares);

                startActivity(intent);
            }
        });
    }
}