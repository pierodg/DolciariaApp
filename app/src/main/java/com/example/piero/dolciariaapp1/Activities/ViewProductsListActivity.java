package com.example.piero.dolciariaapp1.Activities;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.piero.dolciariaapp1.Model.Products;
import com.example.piero.dolciariaapp1.Prevalent.Prevalent;
import com.example.piero.dolciariaapp1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

import static com.example.piero.dolciariaapp1.Fragments.SecondTab.varB;

public class ViewProductsListActivity extends AppCompatActivity {
    private String categoryName;
    private DatabaseReference productsRef;
    private RecyclerView recyclerView;
    private ImageView imageBackButton;
    private TextView txtCategoryName;
    RecyclerView.LayoutManager layoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products_list);

        categoryName = getIntent().getExtras().get("Category").toString();

        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child("Category").child(categoryName);
        


        imageBackButton = findViewById(R.id.back_input_arrow);

        txtCategoryName = findViewById(R.id.txtCategory);

        recyclerView = findViewById(R.id.recycle_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        txtCategoryName.setText(categoryName);

        Paper.init(this);


        backBtn();



        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(productsRef, Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductsListActivity> adapter =
                new FirebaseRecyclerAdapter<Products, ProductsListActivity>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductsListActivity holder, int position, @NonNull final Products model) {

                            holder.txtProductName.setText(model.getPname());
                            holder.txtProductDescription.setText(model.getDescription());
                            holder.txtProductPrice.setText("Prezzo: " + model.getPrice() + " €");

                            Picasso.get().load(model.getImage()).resize(500, 500).into(holder.imgProduct);

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(Prevalent.currentOnlineUser == null) {
                                        Dialog myDialog;
                                        myDialog = new Dialog(ViewProductsListActivity.this);
                                        myDialog.setContentView(R.layout.dialog_image);
                                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        ImageView dialog_image = myDialog.findViewById(R.id.dialog_product_image);

                                        // TextView dialog_name = myDialog.findViewById(R.id.dialog_product_name);
                                        // TextView dialog_description = myDialog.findViewById(R.id.dialog_product_description);
                                        // dialog_name.setText(model.getPname());
                                        // dialog_description.setText(model.getDescription());

                                        Picasso.get().load(model.getImage()).resize(1000, 950).into(dialog_image);
                                        myDialog.show();




                                    } else {

                                    Intent intent = new Intent(ViewProductsListActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    intent.putExtra("Category", categoryName);
                                    startActivity(intent);
                                    finish();
                                    }
                                }
                            });

                    }

                    @NonNull
                    @Override
                    public ProductsListActivity onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_products_list, parent, false);
                        ProductsListActivity holder = new ProductsListActivity(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    public void backBtn() {

        imageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewProductsListActivity.super.onBackPressed();

                varB = 0;

            }
        });

    }

}
