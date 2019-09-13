package com.example.piero.dolciariaapp1.Utilies;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<String> listFragmentTitles = new ArrayList<>(); //nomi delle tabs
    private final List<Fragment> listFragment = new ArrayList<>();



    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return listFragment.get(i); //prende fragment per riempire il ViewPageAdapter
    }

    @Override
    public int getCount() {
        return listFragment.size(); //quanti fragment ci sono
    }


    public void addFragment(String FragmentTitles, Fragment fragment) {
        this.listFragmentTitles.add(FragmentTitles); //aggiunge il titolo
        this.listFragment.add(fragment); //aggiunge fragment
    }

    public void removeFragment(String FragmentTitles, Fragment fragment) {
        this.listFragmentTitles.remove(FragmentTitles);
        this.listFragment.remove(fragment);

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) { // se return null non viene mostrato il nome Title
        return listFragmentTitles.get(position);
    }
}
