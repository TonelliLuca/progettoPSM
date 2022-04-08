package com.example.splitit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


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
            Utilities.insertFragment(this, new DetailsFragment(id, groupName, groupImage,userId, adminId), DetailsFragment.class.getSimpleName());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast toast = Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT);

                toast.show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                EditText userCode = findViewById(R.id.et_add_code);
                userCode.setText(intentResult.getContents());
                Log.e("ActivityDetails",intentResult.getContents());
                Log.e("ActivityDetails",intentResult.getFormatName());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
