package com.example.piero.dolciariaapp1.Activities;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.piero.dolciariaapp1.Prevalent.Prevalent;
import com.example.piero.dolciariaapp1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    private EditText editTextName, editTextPhone, editTextAddress, editTextPassword, editTextEmail, editTextCity;
    private ImageView closeBtn;
    private TextView userEmail, userName, userAddress, userPhone, userCity, saveBtn;
    private ImageButton modifyNameButton, modifyEmailButton, modifyPhoneButton, modifyAddressButton, modifyCityButton;
    private String checker = "";
    private int clickName, clickEmail, clickPhone, clickAddress, clickCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        userEmail = findViewById(R.id.current_user_email);
        userName = findViewById(R.id.current_user_name);
        userPhone = findViewById(R.id.current_user_phone);
        userAddress = findViewById(R.id.current_user_address);
        userCity = findViewById(R.id.current_user_city);

        editTextName = findViewById(R.id.settings_change_name);
        editTextPhone = findViewById(R.id.settings_change_phone);
        editTextEmail = findViewById(R.id.settings_change_email);
       // editTextPassword = findViewById(R.id.settings_change_password); da aggiungere in segutio
        editTextAddress = findViewById(R.id.settings_change_address);
        editTextCity = findViewById(R.id.settings_change_city);


        modifyNameButton = findViewById(R.id.button_modify_name);
        modifyEmailButton = findViewById(R.id.button_modify_email);
        modifyPhoneButton = findViewById(R.id.button_modify_phone);
        modifyAddressButton = findViewById(R.id.button_modify_address);
        modifyCityButton = findViewById(R.id.button_modify_city);

        closeBtn = findViewById(R.id.back_input_arrow);
        saveBtn = findViewById(R.id.settings_update);


        modifyName();
        modifyEmail();
        modifyPhone();
        modifyAddress();
        modifyCity();


        userInfoDisplay();

        setCloseBtn();
        setSaveBtn();

    }


    private void userInfoDisplay() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getUsername());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    userName.setText(dataSnapshot.child("name").getValue().toString());
                    userEmail.setText(dataSnapshot.child("email").getValue().toString());
                    userPhone.setText(dataSnapshot.child("phone").getValue().toString());
                    userAddress.setText(dataSnapshot.child("address").getValue().toString());
                    userCity.setText(dataSnapshot.child("city").getValue().toString());
                } catch (NullPointerException e) {
                    userAddress.setText("");
                    userCity.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }







    public void setCloseBtn() {
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void setSaveBtn() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checker.equals("clicked")) {
                    userInfoSaved();
                    finish();
                    startActivity(getIntent());
                } else {
                    updateOnlyUserInfo();

                }


            }
        });

    }

    private void updateOnlyUserInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        
        HashMap<String, Object> userMap = new HashMap<>();



        userMap.put("name", editTextName.getText().toString());
        userMap.put("address", editTextAddress.getText().toString());
        userMap.put("city", editTextCity.getText().toString());
        userMap.put("phone", editTextPhone.getText().toString());
        userMap.put("email", editTextEmail.getText().toString());
        userInfoSaved();


        ref.child(Prevalent.currentOnlineUser.getUsername()).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(SettingsActivity.this, "Informazioni account salvate", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                    finish();
                }
            }
        });



        }


    private void userInfoSaved() {


        if(TextUtils.isEmpty(editTextName.getText().toString())) {
            Toast.makeText(this, "Inserisci un nome", Toast.LENGTH_SHORT).show();

        }   if(TextUtils.isEmpty(editTextPhone.getText().toString())) {
            Toast.makeText(this, "Inserisci il numero di telefono", Toast.LENGTH_SHORT).show();



        }   if(TextUtils.isEmpty(editTextAddress.getText().toString())) {
            Toast.makeText(this, "Inserisci indirizzo", Toast.LENGTH_SHORT).show();



        }   if(TextUtils.isEmpty(editTextEmail.getText().toString())) {
            Toast.makeText(this, "Inserisci una email", Toast.LENGTH_SHORT).show();


        }   if(TextUtils.isEmpty(editTextCity.getText().toString())) {

        }

    }



    private void modifyName() {
        modifyNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickName == 0) {
                    editTextName.setVisibility(View.VISIBLE);
                    editTextName.setText(Prevalent.currentOnlineUser.getName());
                    userName.setVisibility(View.GONE);
                    editTextName.setText("");
                    clickName++;
            }
                else if (clickName == 1) {
                    editTextName.setVisibility(View.GONE);
                    userName.setVisibility(View.VISIBLE);
                    editTextName.setText("");
                    clickName = 0;

                }
            }
        });
    }

    private void modifyEmail() {
        modifyEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (clickEmail == 0) {
                    editTextEmail.setVisibility(View.VISIBLE);
                    editTextEmail.setText(Prevalent.currentOnlineUser.getEmail());
                    userEmail.setVisibility(View.GONE);
                    editTextEmail.setText("");
                    clickEmail++;
                }
                else if (clickEmail == 1) {
                    editTextEmail.setVisibility(View.GONE);
                    userEmail.setVisibility(View.VISIBLE);
                    editTextEmail.setText("");
                    clickEmail = 0;

                }
            }
        });
    }


    private void modifyPhone() {
        modifyPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickPhone == 0) {
                    editTextPhone.setVisibility(View.VISIBLE);
                    editTextPhone.setText(Prevalent.currentOnlineUser.getPhone());
                    userPhone.setVisibility(View.GONE);
                    editTextPhone.setText("");
                    clickPhone++;
                }
                else if (clickPhone == 1) {
                    editTextPhone.setVisibility(View.GONE);
                    userPhone.setVisibility(View.VISIBLE);
                    editTextPhone.setText("");
                    clickPhone = 0;

                }
            }
        });
    }

    private void modifyAddress() {
        modifyAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickAddress == 0) {
                    editTextAddress.setVisibility(View.VISIBLE);
                    editTextAddress.setText(Prevalent.currentOnlineUser.getAddress());
                    userAddress.setVisibility(View.GONE);
                    editTextAddress.setText("");
                    clickAddress++;
                }
                else if (clickAddress == 1) {
                    editTextAddress.setVisibility(View.GONE);
                    userAddress.setVisibility(View.VISIBLE);
                    editTextAddress.setText("");
                    clickAddress = 0;

                }
            }
        });
    }

    private void modifyCity() {
        modifyCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickCity == 0) {
                    editTextCity.setVisibility(View.VISIBLE);
                    editTextCity.setText(Prevalent.currentOnlineUser.getCity());
                    userCity.setVisibility(View.GONE);
                    editTextCity.setText("");
                    clickCity++;
                }
                else if (clickCity == 1) {
                    editTextCity.setVisibility(View.GONE);
                    userCity.setVisibility(View.VISIBLE);
                    editTextCity.setText("");
                    clickCity = 0;

                }
            }
        });
    }

}
