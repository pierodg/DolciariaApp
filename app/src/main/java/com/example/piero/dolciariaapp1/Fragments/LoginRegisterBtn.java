package com.example.piero.dolciariaapp1.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.piero.dolciariaapp1.Activities.LoginActivity;
import com.example.piero.dolciariaapp1.Activities.MainActivity;
import com.example.piero.dolciariaapp1.Activities.RegisterActivity;
import com.example.piero.dolciariaapp1.Model.Users;
import com.example.piero.dolciariaapp1.Prevalent.Prevalent;
import com.example.piero.dolciariaapp1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

import static com.example.piero.dolciariaapp1.Fragments.FirstTab.varA;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginRegisterBtn extends Fragment {
    private Button loginButton, registerButton;
    private ProgressDialog loadingBar;


    public LoginRegisterBtn() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        loginButton = (Button) view.findViewById(R.id.loginButton);
        registerButton = (Button) view.findViewById(R.id.registerButton);
        loadingBar = new ProgressDialog(getContext());

        Paper.init(getContext());


        loginButton();
        registerButton();

        String UserNameKey = Paper.book().read(Prevalent.UserName);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPassword);
        
        if(UserNameKey != "" && UserPasswordKey != "") {
            if(!TextUtils.isEmpty(UserNameKey) && !TextUtils.isEmpty(UserPasswordKey)) {
                allowAccess(UserNameKey,UserPasswordKey);

                loadingBar.setTitle("Sei connesso");
                loadingBar.setMessage("Aspetta qualche secondo");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_register_btn, container, false);



    }


    private void allowAccess(final String username,final String password) {

        final DatabaseReference RootRef; // root del database
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           //     String my_email = email.replace(".", ","); //FIREBASE NON ACCETTA PUNTI

                if (dataSnapshot.child("Users").child(username).exists()) {

                    Users usersData = dataSnapshot.child("Users").child(username).getValue(Users.class);

                    if (usersData.getUsername().equals(username)) {

                        if (usersData.getPassword().equals(password)) {
                            Toast.makeText(getContext(), "Login effettuato", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            varA = 2;
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(intent);



                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(getContext(), "Password non corretta", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Account con questo " + username + " non esiste", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(getContext(), "Crea un account", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void loginButton() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);

            }
        });
    }
    private void registerButton() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
