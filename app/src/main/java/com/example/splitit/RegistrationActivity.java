package com.example.splitit;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.splitit.OnlineDatabase.OnlineDatabase;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;


public class RegistrationActivity extends AppCompatActivity {
        private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        EditText nome;
        EditText email;
        private EditText password;
        private EditText passwordCheck;
        private boolean check;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.registration);
            nome = findViewById(R.id.editTextNomeUtente);
            email = findViewById(R.id.editTextTextEmailAddress);
            password = findViewById(R.id.editTextTextPassword);
            passwordCheck = findViewById(R.id.etPasswordRegistrationCheck);
            this.check = false;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void checkRegistration(View view){
            if(nome.getText().toString().matches("") || email.getText().toString().matches("")
                    || password.getText().toString().matches("") || passwordCheck.getText().toString().matches("")){
                showErrorRegister(view, 1);
            }else if(!password.getText().toString().matches(passwordCheck.getText().toString())){
                showErrorRegister(view, 2);
            }else{
                OnlineDatabase.execute(addUser(view));
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public Runnable addUser(View view){
            return () -> {
                RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
                String url = "http://"+Utilities.IP+"/splitit/registration.php";

                //Create an error listener to handle errors appropriately.
                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, response -> {
                    //This code is executed if the server responds, whether or not the response contains data.
                    //The String 'response' contains the server's response.

                    if(response.equals("success")){
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }else if(response.equals("failure")){
                        RegistrationActivity.this.check = false;
                        showErrorRegister(view, 0);
                    }
                }, error -> {
                    //This code is executed if there is an error.
                    System.out.println(error.getMessage());
                    showErrorRegister(view, 1);
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<>();
                        MyData.put("nome", nome.getText().toString()); //Add the data you'd like to send to the server.
                        MyData.put("email", email.getText().toString()); //Add the data you'd like to send to the server.
                        MyData.put("password", password.getText().toString()); //Add the data you'd like to send to the server.
                        MyData.put("img", "avatar.png"); //Add the data you'd like to send to the server.
                        MyData.put("code", randomAlphaNumericCode(10)); //Add the data you'd like to send to the server.
                        return MyData;
                    }
                };

                MyRequestQueue.add(MyStringRequest);
            };
        }

    public void showErrorRegister(View view, int value){
        Snackbar snackbar_error;
        if(value==0){
            snackbar_error = Snackbar.make(view, R.string.error_register_mail,   Snackbar.LENGTH_SHORT);
        }else if(value==1){
            snackbar_error = Snackbar.make(view, R.string.error_register,   Snackbar.LENGTH_SHORT);
        }else{
            snackbar_error = Snackbar.make(view, R.string.error_register_password,   Snackbar.LENGTH_SHORT);
        }
        View snackbar_error_view = snackbar_error.getView();
        snackbar_error_view.setBackgroundColor(ContextCompat.getColor(this, R.color.design_default_color_error));
        snackbar_error.show();
    }

    public static String randomAlphaNumericCode(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public void onReturnPressed(View view){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.fade_out);
        finish();
    }
}
