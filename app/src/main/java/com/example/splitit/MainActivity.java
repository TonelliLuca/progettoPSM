package com.example.splitit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private static final String FRAGMENT_TAG_HOME = "HomeFragment";
    private Toolbar topToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utilities.insertFragment(this, new HomeFragment(), FRAGMENT_TAG_HOME);


    }



}