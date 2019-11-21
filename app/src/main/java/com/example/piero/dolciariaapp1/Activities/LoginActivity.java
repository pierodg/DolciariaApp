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
import android.widget.TextView;
import android.widget.Toast;


import com.example.piero.dolciariaapp1.Model.Users;
import com.example.piero.dolciariaapp1.Prevalent.Prevalent;
import com.example.piero.dolciariaapp1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.paperdb.Paper;

import static com.example.piero.dolciariaapp1.Fragments.FirstTab.varA;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout inputPassword, inputUsername;
    private Button loginButton;
    private ProgressDialog loadingBar;
    private TextView adminLink, notAdminLink;
    private ImageView imageBackButton;


    private String parentDbName = "Users";
    private CheckBox checkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imageBackButton = findViewById(R.id.back_input_arrow);

        loginButton = findViewById(R.id.login_btn_login);
        inputUsername = findViewById(R.id.login_username_input);
        inputPassword = findViewById(R.id.login_password_input);
        adminLink = findViewById(R.id.admin_panel_link);
        notAdminLink = findViewById(R.id.not_admin_panel_link);


        loadingBar = new ProgressDialog(this);

        checkBoxRememberMe = findViewById(R.id.rememberMeChk);
        Paper.init(this);

        backBtn();

        adminLink();
        notAdminLink();
        loginButton();


    }

    private void loginButton() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();

            }
        });
    }


    private void adminLink() {
        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.setText("Login admin");
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";

            }
        });

    }

    private void notAdminLink() {
        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.setText("Login");
                adminLink.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });
        }



    private void loginUser() {
        String username = inputUsername.getEditText().getText().toString().toLowerCase();
        String password = inputPassword.getEditText().getText().toString();



        if(TextUtils.isEmpty(username)) //controllo se i campi di testo sono vuoti o pieni
        {
            Toast.makeText(this, "Inserisci il tuo username nel campo indicato", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)) //controllo se i campi di testo sono vuoti o pieni
        {
            Toast.makeText(this, "Inserisci la tua password nel campo indicato", Toast.LENGTH_SHORT).show();
        }
        else {
            allowAccessToAccount(username, password);

            loadingBar.setTitle("Login in corso");
            loadingBar.setMessage("Controllo delle credenziali");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


        }
    }

    private void allowAccessToAccount(final String username, final String password) {
      //  final String my_email  = email.replace(".", ","); //FIREBASE NON ACCETTA PUNTI => (?)
        final String passwordToMD5 = hashCodeGenMD5(password);



        if(checkBoxRememberMe.isChecked()) { //se ritorna true salva le credenziali di accesso
            Paper.book().write(Prevalent.UserName, username);
            Paper.book().write(Prevalent.UserPassword, passwordToMD5);
        }

        final DatabaseReference RootRef; // root del database
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(parentDbName).child(username).exists()) {

                    Users usersData = (dataSnapshot.child(parentDbName).child(username).getValue(Users.class));

                    if (usersData.getUsername().equals(username)) {

                        if (usersData.getPassword().equals(passwordToMD5)) {

                           if(parentDbName.equals("Admins")) {
                               Toast.makeText(LoginActivity.this, "Login effettuato come admin", Toast.LENGTH_SHORT).show();
                               loadingBar.dismiss();

                               varA = 3; //cambia fragment come logged-admin
                               Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //permette all'app tornando indietro di non sloggare
                               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                               startActivity(intent);
                               finish();

                           }

                           else if(parentDbName.equals("Users")) {
                               Toast.makeText(LoginActivity.this, "Login effettuato", Toast.LENGTH_SHORT).show();
                               loadingBar.dismiss();
                               Prevalent.currentOnlineUser = usersData;

                               varA = 2; //cambia fragment come logged-user
                               Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //permette all'app tornando indietro di non sloggare
                               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                               startActivity(intent);
                               finish();
                           }
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password non corretta", Toast.LENGTH_SHORT).show();
                        }
                     }
                    } else {
                    Toast.makeText(LoginActivity.this, "Account con questo username non esiste", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Crea un account", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

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


    public void backBtn() {
        imageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            LoginActivity.super.onBackPressed();
            }
        });
    }



}
