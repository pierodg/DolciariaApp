package com.example.piero.dolciariaapp1.Fragments;



import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.piero.dolciariaapp1.Activities.ViewProductsListActivity;
import com.example.piero.dolciariaapp1.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondTab extends Fragment {
private CardView cardBiscotti, cardPasqua, cardCarnevale, cardPupidizucchero, cardCialde, cardPasticceria;
public static int varB = 0;

    public SecondTab() {
        // Required empty public constructor
    }




    //IMPOSTA SOLTANTO IL LAYOUT
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second_tab, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {     //IMPOSTARE TUTTE LE VIEW E I METODI QUI
        super.onViewCreated(view, savedInstanceState);


        cardBiscotti = getView().findViewById(R.id.cardBiscottitipici);
        cardPasqua = getView().findViewById(R.id.cardPasqua);
        cardCarnevale = getView().findViewById(R.id.cardCarnevale);
        cardPupidizucchero = getView().findViewById(R.id.cardPupidizucchero);
        cardCialde = getView().findViewById(R.id.cardCialde);
        cardPasticceria = getView().findViewById(R.id.cardPasticceria);




        cardBiscotti();
        cardPasqua();
        cardCarnevale();
        cardPasticceria();
        cardPupidizucchero();
        cardCialde();


    }


    public void cardBiscotti() {
        cardBiscotti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), ViewProductsListActivity.class);
                intent.putExtra("Category", "Biscotti");
                startActivity(intent);
            }
        });



    }

    public void cardPasqua() {
        cardPasqua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), ViewProductsListActivity.class);
                intent.putExtra("Category","Pasqua");
                startActivity(intent);


            }
        });
    }

    public void cardCarnevale() {
        cardCarnevale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), ViewProductsListActivity.class);
                intent.putExtra("Category","Carnevale");
                startActivity(intent);


            }
        });
    }

    public void cardPupidizucchero() {
        varB = 4;

        cardPupidizucchero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), ViewProductsListActivity.class);
                intent.putExtra("Category","Pupi Di Zucchero");
                startActivity(intent);

            }
        });
    }

    public void cardCialde() {
        cardCialde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), ViewProductsListActivity.class);
                intent.putExtra("Category","Cialde Per Gelato");
                startActivity(intent);


            }
        });
    }

    public void cardPasticceria() {
        cardPasticceria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), ViewProductsListActivity.class);
                intent.putExtra("Category","Pasticceria");
                startActivity(intent);

            }
        });
    }


}
