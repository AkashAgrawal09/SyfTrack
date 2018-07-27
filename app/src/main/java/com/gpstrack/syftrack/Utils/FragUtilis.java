package com.gpstrack.syftrack.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class FragUtilis
{

    public static void openFrag(AppCompatActivity activity, int layoutID, Fragment fragment, String addTOStack)
    {
        String back1="back";
        FragmentManager fragmentManager =activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(layoutID,fragment);
        fragmentTransaction.commit();

    }
}
