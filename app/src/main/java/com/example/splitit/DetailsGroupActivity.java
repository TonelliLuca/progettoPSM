package com.example.splitit;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class DetailsGroupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent myIntent = getIntent();
        String groupName = myIntent.getStringExtra("group_NAME");
        String groupImage = myIntent.getStringExtra("group_IMAGE");
        String userId = myIntent.getStringExtra("user_ID");
        long id = myIntent.getLongExtra("group_ID",-1);
        long adminId = myIntent.getLongExtra( "admin_ID", -1);

        if (savedInstanceState == null)
            Utilities.insertFragment(this, new DetailsFragment(id, groupName, userId, adminId), DetailsFragment.class.getSimpleName());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
