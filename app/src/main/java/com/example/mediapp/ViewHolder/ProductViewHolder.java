package com.example.mediapp.ViewHolder;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediapp.Interface.ItemClickListener;
import com.example.mediapp.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, Description, productPrice, productCategory, productCardName;
    public ImageView imageView;
    public ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        productCategory = (TextView) itemView.findViewById(R.id.product_category);
        productCardName = (TextView) itemView.findViewById(R.id.product_name_card);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        Description = (TextView) itemView.findViewById(R.id.product_description);
        productPrice = (TextView) itemView.findViewById(R.id.product_price);
    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
