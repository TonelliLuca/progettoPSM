package com.example.splitit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.splitit.OnlineDatabase.OnlineDatabase;
import com.google.android.material.navigation.NavigationView;

public class StoreActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        if(savedInstanceState == null)
            Utilities.insertFragment(this,new StoreFragment(),StoreFragment.class.getSimpleName());
    }

}
