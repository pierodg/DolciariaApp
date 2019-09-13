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

import com.example.piero.dolciariaapp1.Activities.AdminCategoryActivity;
import com.example.piero.dolciariaapp1.Activities.AdminNewOrdersActivity;
import com.example.piero.dolciariaapp1.R;

import io.paperdb.Paper;

import static com.example.piero.dolciariaapp1.Fragments.FirstTab.varA;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoggedAdmin extends Fragment {
    private Button logout, addProductActivity, viewNewOrdersActivity;


    public LoggedAdmin() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logout = view.findViewById(R.id.logoutButton);
        viewNewOrdersActivity = view.findViewById(R.id.viewOrderActivityButton);
        addProductActivity = view.findViewById(R.id.addProductActivityButton);

        startNewProductActivity();
        startViewOrdersProductActivity();
        setLogout();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logged_admin, container, false);
    }


    public void startNewProductActivity() {
        addProductActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AdminCategoryActivity.class);
                startActivity(intent);

            }
        });

}
    public void startViewOrdersProductActivity() {
        viewNewOrdersActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AdminNewOrdersActivity.class);
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
