package com.example.splitit;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.splitit.OnlineDatabase.OnlineDatabase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BalanceActivity extends AppCompatActivity {
    private LineChart balance;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        balance = findViewById(R.id.balanceChart);
        balance.setDragEnabled(true);
        balance.setTouchEnabled(true);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = sharedPref.getString(getString(R.string.user_id), "-1");
        OnlineDatabase.execute(getUserBalance());

    }



    public Runnable getUserBalance() {
        return () -> {

            RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
            String URL = "http://10.0.2.2/splitit/comunication.php";

            //Create an error listener to handle errors appropriately.
            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.

                if(response.equals("failure")){
                    Log.e("BalanceActivity","failed");

                }else{
                    Log.e("BalanceActivity", response);
                    plotGraph(response);


                }
            }, error -> {
                //This code is executed if there is an error.
                Log.e("BalanceActivity","error response");

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

    private void plotGraph(String val){
        ArrayList<Entry> yVal = new ArrayList<>();
        String[] parts = val.split(":");



        for(int i=0;i<parts.length; i++){
            if(!parts[i].isEmpty()) {
                yVal.add(new Entry(i, Float.parseFloat(String.valueOf(parts[i]))));
            }
        }
        LineDataSet set1 = new LineDataSet(yVal, "");

        set1.setValueTextColor(Color.WHITE);
        set1.setLabel("");
        set1.setLineWidth(5f);
        set1.setFillAlpha(500);
        set1.setColor(Color.rgb(0,83, 87));
        set1.setDrawFilled(true);

        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.gradient_btn);
            set1.setFillDrawable(drawable);
        }
        else {
            set1.setFillColor(Color.BLACK);
        }


        ArrayList<ILineDataSet> dataset = new ArrayList<>();
        dataset.add(set1);

        LineData data = new LineData(dataset);
        Description desc = new Description();
        desc.setText("");
        desc.setTextSize(28);
        balance.setDescription(desc);
        balance.getXAxis().setAxisLineColor(R.color.blue);
        balance.getAxisRight().setTextColor(Color.WHITE);
        balance.getAxisLeft().setTextColor(Color.WHITE);
        balance.getXAxis().setTextColor(Color.WHITE);
        balance.getXAxis().setDrawGridLines(false);
        balance.getAxisLeft().setDrawGridLines(false);
        balance.getAxisRight().setDrawGridLines(false);

        balance.setData(data);
        balance.invalidate();
    }
}
