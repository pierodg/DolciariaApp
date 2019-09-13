package com.example.piero.dolciariaapp1.Activities;


import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.piero.dolciariaapp1.Interface.ItemClickListener;
import com.example.piero.dolciariaapp1.R;

public class ProductsListActivity extends RecyclerView.ViewHolder implements View.OnClickListener{


    public TextView txtProductName, txtProductDescription, txtProductPrice;
    public ImageView imgProduct;
    public ItemClickListener itemClickListener;


    public ProductsListActivity(View itemView) {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.product_name);
        txtProductDescription = itemView.findViewById(R.id.product_description);
        txtProductPrice = itemView.findViewById(R.id.product_price);
        imgProduct = itemView.findViewById(R.id.product_imageView);



    }



    public void setItemClickListener(ItemClickListener listener) {

        this.itemClickListener = listener;

    }
    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);

    }


}
