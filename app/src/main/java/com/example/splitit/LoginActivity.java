package com.example.splitit;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executor;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;

public class LoginActivity  extends AppCompatActivity {

    private ImageView fingerprint;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //init biometric
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError (int errorCode, @NonNull CharSequence errString){
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(LoginActivity.this, "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed (){
                super.onAuthenticationFailed();
                Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded (@NonNull BiometricPrompt.AuthenticationResult result){
                super.onAuthenticationSucceeded(result);
                Toast.makeText(LoginActivity.this, "Authentication succeeded", Toast.LENGTH_SHORT).show();
                goToHome();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Login using fingerprints authentication")
                .setNegativeButtonText("Use password")
                .build();
    }

    public void biometricAuthentication(View v){
        biometricPrompt.authenticate(promptInfo);
    }

    public void register(View view){
        Intent intentRegistration = new Intent(this, RegistrationActivity.class);
        startActivity(intentRegistration);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }


    public void login(View view){
        if(checkLogin(view)){
            goToHome();
        }
        Snackbar snackbar_error = Snackbar.make(view, R.string.error_login,   Snackbar.LENGTH_SHORT);
        View snackbar_error_view = snackbar_error.getView();
        snackbar_error_view.setBackgroundColor(ContextCompat.getColor(this, R.color.design_default_color_error));
        snackbar_error.show();
    }

    public boolean checkLogin(View view){
        EditText emailText = (EditText) findViewById(R.id.etEmailLogin);
        EditText passwordText = (EditText) findViewById(R.id.etPasswordLogin);
         if(emailText.getText().toString().matches("") || passwordText.getText().toString().matches("")){
            return false;
        }
        return true;
    }

    public void goToHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

}
