package com.example.piero.dolciariaapp1.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.example.piero.dolciariaapp1.Activities.CartActivity;
import com.example.piero.dolciariaapp1.Activities.SettingsActivity;
import com.example.piero.dolciariaapp1.Prevalent.Prevalent;
import com.example.piero.dolciariaapp1.R;

import io.paperdb.Paper;

import static com.example.piero.dolciariaapp1.Fragments.FirstTab.varA;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoggedUser extends Fragment {
    private Button logout, settings, cart;
    private TextView username;


    public LoggedUser() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        logout = view.findViewById(R.id.logoutButton);
        username = view.findViewById(R.id.usernameLogged);
        settings = view.findViewById(R.id.settingsButton);
        cart = view.findViewById(R.id.cartButton);


        setUsernameLogged();

        setCartBtn();
        setSettingsBtn();
        setLogout();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_logged_user, container, false);
    }


    public void setUsernameLogged() {
        username.setText(Prevalent.currentOnlineUser.getName());
    }


    public void setCartBtn() {
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CartActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setSettingsBtn() {
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setLogout() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().destroy();

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color='#3E2723'>LOGOUT</font>"));
                builder.setMessage( Html.fromHtml("<font color='#3E2723'>Sei sicuro di voler effettuare il logout?</font>"));


                builder.setPositiveButton(Html.fromHtml("<font color='#e08e00'>SI</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        varA = 1;
                        Prevalent.currentOnlineUser = null;

                        getFragmentManager().beginTransaction().replace(R.id.adapter, new LoginRegisterBtn()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

                    }
                }).setNegativeButton(Html.fromHtml("<font color='#e08e00'>NO</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();

            }
        });
    }
}
