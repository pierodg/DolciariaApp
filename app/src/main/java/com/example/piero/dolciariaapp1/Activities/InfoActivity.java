package com.example.piero.dolciariaapp1.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ColorSpace;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.piero.dolciariaapp1.Model.Users;
import com.example.piero.dolciariaapp1.Prevalent.Prevalent;
import com.example.piero.dolciariaapp1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import static android.Manifest.permission.CALL_PHONE;

public class InfoActivity extends AppCompatActivity {

    private ImageButton imageBackButton, callBtn, emailBtn;
    private Button btnHelp;
    private ImageView mapImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


        btnHelp = findViewById(R.id.btn_help);
        callBtn = findViewById(R.id.btn_call);
        emailBtn = findViewById(R.id.btn_email);
        imageBackButton = findViewById(R.id.back_input_arrow);
        mapImage = findViewById(R.id.mapImage);


        setBtnHelp();
        setEmailBtn();
        setCallBtn();
        onMapClick();
        backBtn();



    }

    private void onMapClick() {
        mapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("geo:0,0?q=Via XX Settembre 72, Castelvetrano"));

                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivity(i);
                }

            }
        });
    }

    private void setBtnHelp() {
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setData(Uri.parse("mailto:"));


                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"dg_piero@hotmail.it"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Problema con app Dolciaria Di Giovanni");
                i.putExtra(Intent.EXTRA_TEXT, "Scrivimi il tuo problema:");

                if(i.resolveActivity(getPackageManager()) != null); {
                    startActivity(i);
                }

            }
        });
}

    public void backBtn() {
        imageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();

            }
        });
    }

    public void setEmailBtn() {
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentEmail = new Intent(Intent.ACTION_SENDTO);
                intentEmail.setData(Uri.parse("mailto:"));
                intentEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{"dolciariadigiovanni@libero.it"});
                if(intentEmail.resolveActivity(getPackageManager()) != null); {
                    startActivity(intentEmail);
                }
            }
        });
    }

    public void setCallBtn() {
        callBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                String mNumber = "092489248";
                Intent intentCall = new Intent(Intent.ACTION_CALL);
                intentCall.setData(Uri.parse("tel:"+mNumber));

                if(intentCall.resolveActivity(getPackageManager()) != null ) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(intentCall);

                    } else {

                        Toast.makeText(InfoActivity.this, "L'app non ha i permessi per chiamare", Toast.LENGTH_SHORT).show();
                        requestPermissions(new String[]{CALL_PHONE}, 1);
                    }
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(InfoActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
