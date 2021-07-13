package com.example.splitit;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.splitit.OnlineDatabase.OnlineDatabase;
import com.example.splitit.RecyclerView.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class RegistrationActivity extends AppCompatActivity {
        private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        EditText nome;
        EditText email;
        private EditText password;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.registration);
            nome = findViewById(R.id.editTextNomeUtente);
            email = findViewById(R.id.editTextTextEmailAddress);
            password = findViewById(R.id.editTextTextPassword);
        }


        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void checkRegistration(View view){
            OnlineDatabase.execute(addUser());
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public Runnable addUser(){
            Runnable task = () -> {
                RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
                String url = "http://10.0.2.2/splitit/registration.php";

                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, response -> {
                    //This code is executed if the server responds, whether or not the response contains data.
                    //The String 'response' contains the server's response.
                    System.out.println(response);
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //This code is executed if there is an error.
                        System.out.println(error.getMessage());
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<String, String>();
                        MyData.put("nome", nome.getText().toString()); //Add the data you'd like to send to the server.
                        MyData.put("email", email.getText().toString()); //Add the data you'd like to send to the server.
                        MyData.put("password", password.getText().toString()); //Add the data you'd like to send to the server.
                        MyData.put("img", "ic_baseline:android_24"); //Add the data you'd like to send to the server.
                        MyData.put("code", randomAlphaNumericCode(10)); //Add the data you'd like to send to the server.
                        return MyData;
                    }
                };

                MyRequestQueue.add(MyStringRequest);

                /*try {
                    String result = "";
                    URL url = new URL("http://10.0.2.2/splitit/registration.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");

                    OutputStream outputStream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                    String data = URLEncoder.encode("nome", "UTF-8")+"="+URLEncoder.encode(nome.getText().toString(), "UTF-8")
                            +"&&"+URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email.getText().toString(), "UTF-8")
                            +"&&"+URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password.getText().toString(), "UTF-8")
                            +"&&"+URLEncoder.encode("img", "UTF-8")+"="+URLEncoder.encode("ic_baseline:android_24", "UTF-8")
                            +"&&"+URLEncoder.encode("code", "UTF-8")+"="+URLEncoder.encode(randomAlphaNumericCode(10), "UTF-8");
                    System.out.println(data);
                    writer.write(data);
                    writer.flush();
                    writer.close();
                    outputStream.close();

                    InputStream inputStream = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    String message = "";
                    while((message = reader.readLine()) != null ){
                        result += message;
                    }

                    System.out.println(result);
                    reader.close();
                    inputStream.close();
                    conn.disconnect();


                } catch (MalformedURLException e) {
                    System.out.println(e.getMessage());
                    System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
                }*/
            };
            return task;
        }


    public static String randomAlphaNumericCode(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}
