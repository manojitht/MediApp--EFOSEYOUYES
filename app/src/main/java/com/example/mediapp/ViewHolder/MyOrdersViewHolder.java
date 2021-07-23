package com.example.mediapp.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediapp.Interface.ItemClickListener;
import com.example.mediapp.R;

public class MyOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtOrderId, txtStatus, txtAddress, txtPrice, txtDate, txtRemoveOrder;
    public Button showItems, setDeliver;
    private ItemClickListener itemClickListener;

    public MyOrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderId = itemView.findViewById(R.id.order_number);
        txtDate = itemView.findViewById(R.id.date_time);
        txtStatus = itemView.findViewById(R.id.show_status);
        txtAddress = itemView.findViewById(R.id.billing_address);
        txtPrice = itemView.findViewById(R.id.cart_items_product_price);
        showItems = itemView.findViewById(R.id.show_products_items);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
