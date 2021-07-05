package com.example.splitit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

public class SplashActivity extends AppCompatActivity {

    ImageView logo, appName, splashImg;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.splash_logo);
        appName = findViewById(R.id.splash_app_name);
        splashImg = findViewById(R.id.splash_bg);
        logo = findViewById(R.id.splash_logo);
        lottieAnimationView = findViewById(R.id.raw_splash);
        lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);

        Intent loginIntent = new Intent(this, LoginActivity.class);

        splashImg.animate().translationY(-2300).setDuration(1000).setStartDelay(2000);
        logo.animate().translationYBy(1400).setDuration(1000).setStartDelay(2000);
        appName.animate().translationY(1400).setDuration(1000).setStartDelay(2000);
        lottieAnimationView.animate().translationY(1400).setDuration(1500).setStartDelay(2000);

        new Handler().postDelayed(() -> {
            startActivity(loginIntent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 3000);
    }

}