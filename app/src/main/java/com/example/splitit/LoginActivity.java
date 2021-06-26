package com.example.splitit;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

public class LoginActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    public void register(View view){
        Intent intentRegistration = new Intent(this, RegistrationActivity.class);
        startActivity(intentRegistration);
    }


    public void login(View view){
        Intent intent = new Intent(this, MainActivity.class);
        if(checkLogin(view)){
            startActivity(intent);
            finish();
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

}
