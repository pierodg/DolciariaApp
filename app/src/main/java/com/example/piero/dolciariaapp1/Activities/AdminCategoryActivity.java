package com.example.piero.dolciariaapp1.Activities;


import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.piero.dolciariaapp1.R;

public class AdminCategoryActivity extends AppCompatActivity {
    private CardView cardBiscotti, cardPasqua, cardCarnevale, cardPupidizucchero, cardCialde, cardPasticceria;
    private ImageView imageBackButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        cardBiscotti = findViewById(R.id.cardBiscottitipici);
        cardPasqua = findViewById(R.id.cardPasqua);
        cardCarnevale = findViewById(R.id.cardCarnevale);
        cardPupidizucchero = findViewById(R.id.cardPupidizucchero);
        cardCialde = findViewById(R.id.cardCialde);
        cardPasticceria = findViewById(R.id.cardPasticceria);
        imageBackButton = findViewById(R.id.back_input_arrow);


        cardBiscotti();
        cardPasqua();
        cardCarnevale();
        cardPasticceria();
        cardPupidizucchero();
        cardCialde();
        backBtn();


    }



    public void cardBiscotti() {
        cardBiscotti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","Biscotti");
                startActivity(intent);
                finish();
            }
        });
    }

    public void cardPasqua() {
        cardPasqua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","Pasqua");
                startActivity(intent);
                finish();

            }
        });
    }

    public void cardCarnevale() {
        cardCarnevale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","Carnevale");
                startActivity(intent);
                finish();

            }
        });
    }

    public void cardPupidizucchero() {
        cardPupidizucchero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","Pupi Di Zucchero");
                startActivity(intent);
                finish();

            }
        });
    }

    public void cardCialde() {
        cardCialde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","Cialde Per Gelato");
                startActivity(intent);
                finish();

            }
        });
    }

    public void cardPasticceria() {
        cardPasticceria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","Pasticceria");
                startActivity(intent);
                finish();

            }
        });
    }
    public void backBtn() {

        imageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              AdminCategoryActivity.super.onBackPressed();
            }
        });
    }
}