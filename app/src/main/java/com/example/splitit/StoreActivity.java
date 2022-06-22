package com.example.splitit;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


public class StoreActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        if(savedInstanceState == null)
            Utilities.insertFragment(this,new StoreFragment(),StoreFragment.class.getSimpleName());
    }

}
