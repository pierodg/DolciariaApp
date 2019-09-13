package com.example.piero.dolciariaapp1.Activities;


import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.piero.dolciariaapp1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button createAccountButton;
    private TextInputLayout inputName, inputEmail, inputPassword, inputPhoneNumber, inputUsername, inputConfirmPassword;
    private ProgressDialog loadingBar;
    private ImageView imageBackButton;
    private String saveCurrentDate, saveCurrentTime, userTimeRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        imageBackButton = findViewById(R.id.back_input_arrow);

        createAccountButton =  findViewById(R.id.register_btn);
        inputUsername = (TextInputLayout) findViewById(R.id.register_username_input);
        inputName =  findViewById(R.id.register_name_input);
        inputEmail = findViewById(R.id.register_email_input);
        inputPhoneNumber =  findViewById(R.id.register_phone_number_input);
        inputPassword = findViewById(R.id.register_password_input);
        inputConfirmPassword = findViewById(R.id.repeat_register_password_input);

        loadingBar = new ProgressDialog(this);


        backBtn();
        createAccountButton();


    }

    private void createAccountButton() {
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();


            }
        });
    }

    private void createAccount() {

            if(validate()) {
            loadingBar.setTitle("Creazione account");
            loadingBar.setMessage("Controllo delle credenziali in corso");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

         }
        }



    private void validateUser(final String name,final String email,final String password, final String phone, final String username) {

        final DatabaseReference RootRef; // root del database
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String passwordToMD5 = hashCodeGenMD5(password);

                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                saveCurrentDate = currentDate.format(calendar.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime = currentTime.format(calendar.getTime());


                if (!dataSnapshot.child("Users").child(username).exists()) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("username", username);
                    userdataMap.put("email", email);
                    userdataMap.put("name", name);
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", passwordToMD5);
                    userdataMap.put("date", saveCurrentDate);
                    userdataMap.put("time", saveCurrentTime);

                    RootRef.child("Users").child(username).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Account creato con successo", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //permette all'app tornando indietro di non sloggare
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Errore di collegamento", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });

                } if(dataSnapshot.child("Users").child(username).exists()) {
                    Toast.makeText(RegisterActivity.this, "Username " + username + " gia utilizzato", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Usare un'altro username", Toast.LENGTH_SHORT).show();

                } if(dataSnapshot.child("Users").child(username).child(phone).equals(phone)) { //non funziona
                    Toast.makeText(RegisterActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RegisterActivity.this, "E' stato riscontrato un errore", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void backBtn() {
        imageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.super.onBackPressed();
            }
        });
    }

    public boolean validate() {

        boolean valid = true;

        String name = inputName.getEditText().getText().toString();
        String email = inputEmail.getEditText().getText().toString().toLowerCase();
        String phone = inputPhoneNumber.getEditText().getText().toString();
        String password = inputPassword.getEditText().getText().toString();
        String confirmPassword = inputConfirmPassword.getEditText().getText().toString();
        String username = inputUsername.getEditText().getText().toString().toLowerCase();


        if (TextUtils.isEmpty(username)) //controllo se i campi di testo sono vuoti o pieni
        {
            inputUsername.setError("Inserisci username");
            valid = false;
        } else  {
            inputUsername.setError(null);
        }

        if (TextUtils.isEmpty(email)) //controllo se i campi di testo sono vuoti o pieni
        {
            inputEmail.setError("Inserisci email");
            valid = false;

        } else {
            inputEmail.setError(null);
        }

        if (TextUtils.isEmpty(name)) //controllo se i campi di testo sono vuoti o pieni
        {
            inputName.setError("Inserisci nome");
            valid = false;

        } else {
            inputName.setError(null);
        }

        if  (TextUtils.isEmpty(phone)) //controllo se i campi di testo sono vuoti o pieni
        {
            inputPhoneNumber.setError("Inserisci numero");
            valid = false;

        }   else {
            inputPhoneNumber.setError(null);
        }

        if  (TextUtils.isEmpty(password)) //controllo se i campi di testo sono vuoti o pieni
        {
            inputPassword.setError("Inserisci password");
            valid = false;
        } else {
            inputPassword.setError(null);
        }
        if(!confirmPassword.equals(password)) {
            inputConfirmPassword.setError("Password non corrisponde");
            valid = false;
        } else  {
            inputConfirmPassword.setError(null);
        }

        if(valid==true) {
            validateUser(name, email, password, phone, username);
        }

        return valid;
    }


    //Genera hash code MD5
    public static String hashCodeGenMD5(String password) {

        String md5 = null;
        if(null == password ) return null;

        try {
            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5"); //"MD5" per hash piu corto o "SHA-256" per hash più lungo

            //Update input string in message digest
            digest.update(password.getBytes(), 0, password.length());

            //Converts message digest value in base 16 (hex)
            md5 = new BigInteger(1, digest.digest()).toString(16);

            } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();

        }

        return md5;
    }
}
