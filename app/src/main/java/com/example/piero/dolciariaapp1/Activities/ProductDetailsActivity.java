package com.example.piero.dolciariaapp1.Activities;

import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.piero.dolciariaapp1.Model.Products;
import com.example.piero.dolciariaapp1.Prevalent.Prevalent;
import com.example.piero.dolciariaapp1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
private FloatingActionButton addToCartBtn;
private ImageView productImage, imageBackButton;
private ElegantNumberButton numberButton;
private TextView productName, productDescription, productPrice;
private String productID = "", state = "Normal", categoryName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");

        imageBackButton = findViewById(R.id.back_input_arrow);

        addToCartBtn = findViewById(R.id.add_product_to_cart_btn);
        numberButton = findViewById(R.id.number_button);
        productImage = findViewById(R.id.product_details_image);
        productName = findViewById(R.id.product_details_name);
        productDescription = findViewById(R.id.product_details_description);
        productPrice = findViewById(R.id.product_details_price);

        categoryName = getIntent().getExtras().get("Category").toString();

        backBtn();

        setAddToCartBtn();
        getProductDetails(productID);

    }

    private void getProductDetails(final String productID) {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child("Category").child(categoryName);
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productDescription.setText(products.getDescription());
                    productPrice.setText(products.getPrice());
                    Glide.with(ProductDetailsActivity.this).load(products.getImage()).dontAnimate().into(productImage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setAddToCartBtn() {
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state.equals("non spedito") || state.equals("spedito"))
                {
                    Toast.makeText(ProductDetailsActivity.this, "Hai già effettuato un ordine \n Non puoi aggiungere prodotti al carrello", Toast.LENGTH_SHORT).show();
                } else {
                    addToCartList();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrderState();
    }

    public void addToCartList() {

        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");


        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getUsername())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {

                            cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getUsername())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Toast.makeText(ProductDetailsActivity.this, productName.getText().toString() + " aggiunto al carrello.", Toast.LENGTH_SHORT).show();
                                                 ProductDetailsActivity.super.onBackPressed();

                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void backBtn() {
        imageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDetailsActivity.super.onBackPressed();
                finish();
            }
        });

    }

    private void checkOrderState() {
            DatabaseReference ordersRef;
            ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getUsername());

            ordersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String shippingState = dataSnapshot.child("state").getValue().toString();

                        if (shippingState.equals("spedito")) {
                            state = "spedito";

                        } else if (shippingState.equals("non spedito")) {
                            state = "non spedito";
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }
}
