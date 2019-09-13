package com.example.piero.dolciariaapp1.Activities;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.piero.dolciariaapp1.Prevalent.Prevalent;
import com.example.piero.dolciariaapp1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private ImageView backBtn;
    private EditText nameEditText, phoneEditText, addressEditText, cityEditText, emailEditText;
    private Button confirmOrderBtn;

    private String totalAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        backBtn = findViewById(R.id.back_input_arrow);

        confirmOrderBtn = findViewById(R.id.confirm_order_button);
        nameEditText = findViewById(R.id.shipment_name);
        emailEditText = findViewById(R.id.shipment_email);
        phoneEditText = findViewById(R.id.shipment_numberphone);
        cityEditText = findViewById(R.id.shipment_city);
        addressEditText = findViewById(R.id.shipment_address);

        totalAmount = getIntent().getStringExtra("Total Price");

        backBtn();



        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });
    }

    private void backBtn() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfirmFinalOrderActivity.this, CartActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void check() {
        if (TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(this,"Inserisci il tuo nome completo", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            Toast.makeText(this, "Inserisci il tuo numero di telefono", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(emailEditText.getText().toString()))
        {
            Toast.makeText(this, "Inserisci la tua email", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Inserisci il tuo indirizzo", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(this,"Inserisci città di destinazione", Toast.LENGTH_SHORT).show();
        }
        else
            {
                confirmOrder();
            }
    }

    private void confirmOrder() {
        final String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getUsername());
        HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("totalAmount", totalAmount);
        orderMap.put("name", nameEditText.getText().toString());
        orderMap.put("phone", phoneEditText.getText().toString());
        orderMap.put("email", emailEditText.getText().toString());
        orderMap.put("address", addressEditText.getText().toString());
        orderMap.put("city", cityEditText.getText().toString());
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);
        orderMap.put("state", "non spedito");

        ordersRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View")
                            .child(Prevalent.currentOnlineUser.getUsername())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if( task.isSuccessful()) {
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "Il tuo ordine è stato completato", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });


    }


}
