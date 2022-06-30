package com.example.splitit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class BalanceActivity extends AppCompatActivity {
    //private LineChart balance;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_layout);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = sharedPref.getString(getString(R.string.user_id), "-1");
        //OnlineDatabase.execute(getUserBalance());
        Utilities.stop=true;
        if (savedInstanceState == null)
            Utilities.insertFragment(this, new BalanceFragment(), "BalanceFragment");
    }



    public Runnable getUserBalance() {
        return () -> {

            RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
            String URL = "http://"+Utilities.IP+"/splitit/comunication.php";

            //Create an error listener to handle errors appropriately.
            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.

                if(response.equals("failure")){

                }else{
                    //plotGraph(response);


                }
            }, error -> {
                //This code is executed if there is an error.

            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<>();
                    MyData.put("id", String.valueOf(user_id)); //Add the data you'd like to send to the server.
                    MyData.put("request",String.valueOf(10));
                    return MyData;
                }
            };

            MyRequestQueue.add(MyStringRequest);
        };

    }

}
