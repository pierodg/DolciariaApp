package com.example.piero.dolciariaapp1.Activities;



import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.example.piero.dolciariaapp1.Fragments.FirstTab;

import com.example.piero.dolciariaapp1.Fragments.SecondTab;
import com.example.piero.dolciariaapp1.Fragments.ThirdTab;
import com.example.piero.dolciariaapp1.R;
import com.example.piero.dolciariaapp1.Utilies.ViewPagerAdapter;



public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        logo = findViewById(R.id.image_info);

        tabLayout.setupWithViewPager(viewPager);
        setViewPager(viewPager); // aggiunge i layout dei fragment al viewpager
        customToolbar(); // imposta Toolbar personalizzata ed elimina il titolo

        clickOnLogo();
    }



    private void setViewPager(ViewPager viewPager) { //aggiunge i layout dei fragment al viewpager
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment("HOME", new FirstTab());
        adapter.addFragment("PRODOTTI", new SecondTab());
        adapter.addFragment("NOVITÀ", new ThirdTab());

        viewPager.setAdapter(adapter);



    }

    private void customToolbar() {
        setSupportActionBar(toolbar); //setta la toolbar personalizzata
        getSupportActionBar().setDisplayShowTitleEnabled(false); // elimina Titolo dalla toolbar

    }

    private void clickOnLogo() {
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


}
