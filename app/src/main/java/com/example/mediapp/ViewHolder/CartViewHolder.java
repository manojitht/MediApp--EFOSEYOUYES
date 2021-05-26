package com.example.mediapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediapp.Interface.ItemClickListener;
import com.example.mediapp.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductname, txtProductPrice, txtProductQuantity, txtProductCategory;
    public ImageView productImageView;
    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductname = itemView.findViewById(R.id.cart_items_productname);
        txtProductCategory = itemView.findViewById(R.id.cart_product_category);
        txtProductPrice = itemView.findViewById(R.id.cart_items_productprice);
        txtProductQuantity = itemView.findViewById(R.id.cart_items_productquantity);
        productImageView = itemView.findViewById(R.id.cart_items_productimage);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
