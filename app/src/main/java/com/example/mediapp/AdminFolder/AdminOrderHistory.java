package com.example.mediapp.AdminFolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediapp.Model.AdminOrders;
import com.example.mediapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class AdminOrderHistory extends AppCompatActivity {

    private Button searchButton;
    private EditText DateFrom, DateTo;
    private RecyclerView searchList;
    private DatePickerDialog datePickerDialogDateFrom, datePickerDialogDateTo;
    private String dateFromInput, dateToInput;
    private ImageView noSearchFound;
    private TextView TextMessage, TotalAmountOfPeriod, TotalOrderCount;
    int orders = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_history);
        searchButton = (Button) findViewById(R.id.search_button);
        initDatePickerDateFrom();
        initDatePickerDateTo();
        DateFrom = (EditText) findViewById(R.id.search_date_from);
        DateFrom.setText(GetDate());
        DateTo = (EditText) findViewById(R.id.search_date_to);
        DateTo.setText(GetDate());
        TextMessage = findViewById(R.id.text_message);
        noSearchFound = findViewById(R.id.no_search_image);
        TotalAmountOfPeriod = findViewById(R.id.text_total_amount_period);
        TotalOrderCount = findViewById(R.id.text_total_order_count);
        searchList = (RecyclerView) findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(AdminOrderHistory.this));
        noSearchFound.setVisibility(View.GONE);
        TotalAmountOfPeriod.setVisibility(View.GONE);
        TextMessage.setVisibility(View.VISIBLE);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextMessage.setVisibility(View.GONE);
                noSearchFound.setVisibility(View.GONE);
                String monthFromValidator = DateFrom.getText().toString().substring(0, 3);
                String monthToValidator = DateTo.getText().toString().substring(0, 3);
                String yearFromValidator = DateFrom.getText().toString().substring(8, 12);
                String yearToValidator = DateTo.getText().toString().substring(8, 12);
                if (monthFromValidator.equals(monthToValidator) && yearFromValidator.equals(yearToValidator)) {
                    searchList.setVisibility(View.VISIBLE);
                    dateFromInput = DateFrom.getText().toString();
                    dateToInput = DateTo.getText().toString();
                    onStart();
                } else {
                    searchList.setVisibility(View.GONE);
                    TextMessage.setVisibility(View.VISIBLE);
                    TextMessage.setText("Invalid months that couldn't match!");
                }
            }
        });
    }

    private String GetDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        return makeDateString(month, date, year);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Sales Data");

        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(ordersRef.orderByChild("date").startAt(dateFromInput).endAt(dateToInput), AdminOrders.class).build();
        ordersRef.orderByChild("date").startAt(dateFromInput).endAt(dateToInput).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    TotalOrderCount.setText("");
                    TotalAmountOfPeriod.setText("");
                    noSearchFound.setVisibility(View.VISIBLE);
                    TextMessage.setVisibility(View.VISIBLE);
                    TextMessage.setText("No orders found for the period!");
                } else {
                    int sum = 0;
                    orders = (int) dataSnapshot.getChildrenCount();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object price = map.get("totalAmount");
                        int totalPrice = Integer.parseInt(String.valueOf(price));
                        sum += totalPrice;

                        String formattedTotal = NumberFormat.getInstance().format(sum);
                        TotalAmountOfPeriod.setVisibility(View.VISIBLE);
                        TotalAmountOfPeriod.setText("Period of sales: " + formattedTotal + " LKR");
                        TotalOrderCount.setText("Orders: " + orders);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersActivity.AdminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersActivity.AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersActivity.AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {
                holder.customerName.setText("Name: " + model.getCname());
                holder.orderId.setText(model.getOrderId());
                holder.userPhoneNumber.setText("Contact: " + model.getPhone());
                int formattedPrice = Integer.parseInt(model.getTotalAmount());
                String orderHistoryPrice = NumberFormat.getInstance().format(formattedPrice);
                holder.userTotalAmount.setText("Cost of price: " + orderHistoryPrice + " LKR");
                holder.userDate.setText("Date: " + model.getDate());
                holder.userTime.setText("Time: " + model.getTime());
                holder.userShippingAddress.setText("Shipping Address: " + model.getAddress());
                holder.approvedBy.setText("Approved by: " + model.getApprovedBy());
                if (model.getStatus().equals("Shipped")) {
                    holder.status.setText("Order packed & completed!");
                } else {
                    holder.status.setText("Status: " + model.getStatus());
                }
                holder.showOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String uID = getRef(position).getKey();
                        Toast.makeText(AdminOrderHistory.this, uID + " selected!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminOrderHistory.this, AdminSoldProducts.class);
                        intent.putExtra("uid", uID);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public AdminOrdersActivity.AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout_admin, parent, false);
                return new AdminOrdersActivity.AdminOrdersViewHolder(view);
            }
        };
        searchList.setAdapter(adapter);
        adapter.startListening();
    }

    private void initDatePickerDateFrom() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                month = month + 1;
                String day = makeDateString(month, date, year);
                DateFrom.setText(day);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialogDateFrom = new DatePickerDialog(this, style, dateSetListener, year, month, date);
        datePickerDialogDateFrom.getDatePicker().setMaxDate(System.currentTimeMillis());


    }

    private void initDatePickerDateTo() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                month = month + 1;
                String day = makeDateString(month, date, year);
                DateTo.setText(day);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialogDateTo = new DatePickerDialog(this, style, dateSetListener, year, month, date);
        datePickerDialogDateTo.getDatePicker().setMaxDate(System.currentTimeMillis());


    }

    private String makeDateString(int month, int date, int year) {

        String dates = "";
        if (date < 10) {
            dates = "0" + String.valueOf(date);
        } else {
            dates = String.valueOf(date);
        }

        return getMonthFormat(month) + " " + dates + ", " + year;
    }


    private String getMonthFormat(int month) {

        if (month == 1) {
            return "Jan";
        }
        if (month == 2) {
            return "Feb";
        }
        if (month == 3) {
            return "Mar";
        }
        if (month == 4) {
            return "Apr";
        }
        if (month == 5) {
            return "May";
        }
        if (month == 6) {
            return "Jun";
        }
        if (month == 7) {
            return "Jul";
        }
        if (month == 8) {
            return "Aug";
        }
        if (month == 9) {
            return "Sep";
        }
        if (month == 10) {
            return "Oct";
        }
        if (month == 11) {
            return "Nov";
        }
        if (month == 12) {
            return "Dec";
        }
        return "Jan";
    }

    public void showDatePickerDateFrom(View view) {
        datePickerDialogDateFrom.show();
    }

    public void showDatePickerDateTo(View view) {
        datePickerDialogDateTo.show();
    }

}