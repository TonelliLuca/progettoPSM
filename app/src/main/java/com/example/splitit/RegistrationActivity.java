package com.example.splitit;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.registration);
        }

        public void checkRegistration(View view){
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
}
