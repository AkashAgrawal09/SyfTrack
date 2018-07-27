package com.gpstrack.syftrack;

import android.content.Intent;
import android.preference.PreferenceActivity;

import android.os.Bundle;

public class SettingActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.user_setting);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SettingActivity.this,DashBoard.class));
    }
}
