package com.example.splitit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.splitit.OnlineDatabase.OnlineDatabase;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class LoginActivity  extends AppCompatActivity {

    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private EditText etEmailLogin;
    private EditText etPasswordLogin;


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        etEmailLogin = findViewById(R.id.etEmailLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        Utilities.stop = true;

        //init biometric
        Executor executor = ContextCompat.getMainExecutor(this);
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
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //user_code = sharedPref.getString(getString(R.string.user_code),"0")    ;
        //user_id = sharedPref.getString(getString(R.string.user_id),"-1");
        if(!sharedPref.getString(getString(R.string.user_code), "0").equals("0")){
            biometricPrompt.authenticate(promptInfo);
        }else{
            Toast.makeText(LoginActivity.this, "Nessun profilo salvato", Toast.LENGTH_SHORT).show();
        }

    }

    public void register(View view){
        Intent intentRegistration = new Intent(this, RegistrationActivity.class);
        startActivity(intentRegistration);
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
    }

    public Runnable loginUser(View view) {
        Runnable task = () -> {

            RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
            String URL = "http://"+Utilities.IP+"/splitit/login.php";

            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //This code is executed if the server responds, whether or not the response contains data.
                    //The String 'response' contains the server's response.

                    if(response.equals("failure")){
                        Log.e("login","failure");
                        showErrorLogin(view);
                    }else{
                        Log.e("login", response);
                        saveProfile(response);
                        goToHome();
                    }
                }
            }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                @Override
                public void onErrorResponse(VolleyError error) {
                    //This code is executed if there is an error.
                    Log.e("login","error response");
                    showErrorLogin(view);
                }
            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<String, String>();

                    MyData.put("email", etEmailLogin.getText().toString()); //Add the data you'd like to send to the server.
                    MyData.put("password", etPasswordLogin.getText().toString()); //Add the data you'd like to send to the server.
                    return MyData;
                }
            };

            MyRequestQueue.add(MyStringRequest);
        };
        return task;

    }
    public boolean checkLogin(){
        return !etEmailLogin.getText().toString().matches("") && !etPasswordLogin.getText().toString().matches("");
    }

    public void showErrorLogin(View view){
        Snackbar snackbar_error = Snackbar.make(view, R.string.error_login,   Snackbar.LENGTH_SHORT);
        View snackbar_error_view = snackbar_error.getView();
        snackbar_error_view.setBackgroundColor(ContextCompat.getColor(this, R.color.design_default_color_error));
        snackbar_error.show();
    }

    public void login(View view){
        if(checkLogin()){
            OnlineDatabase.execute(loginUser(view));
        }else{
            showErrorLogin(view);
        }
    }

    public void goToHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    public void saveProfile(String val){
        String[] values =val.split(":");
        SharedPreferences sharedPref =   PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.user_code), values[0]);
        editor.putString(getString(R.string.user_id), values[1]);
        editor.putString(getString(R.string.user_name), values[2]);
        editor.putString(getString(R.string.user_email), values[3]);
        editor.apply();
        String code = sharedPref.getString(getString(R.string.user_code),"0");
        Log.e("login", "user code: "+code);

    }

}
