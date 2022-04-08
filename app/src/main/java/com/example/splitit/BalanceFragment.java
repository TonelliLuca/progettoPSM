package com.example.splitit;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.splitit.OnlineDatabase.OnlineDatabase;
import com.example.splitit.RecyclerView.BalanceAdapter;
import com.example.splitit.RecyclerView.GroupAdapter;
import com.example.splitit.RecyclerView.OnItemListener;
import com.example.splitit.RecyclerView.UserAdapter;
import com.example.splitit.ViewModel.ListViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BalanceFragment extends Fragment {

    private String user_id= null;
    private ArrayList<Float> balance=new ArrayList<>();
    private BalanceAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<String> balanceName= new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflate, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflate.inflate(R.layout.balance_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        Activity activity = getActivity();
        if(activity != null){
            Utilities.stop=true;
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            user_id = sharedPref.getString(getString(R.string.user_id),"-1");
            setRecyclerView(activity);
            OnlineDatabase.execute(getUserBalance());



        }

    }

    private void setRecyclerView(final Activity activity){
        recyclerView = requireView().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        this.adapter = new BalanceAdapter(activity, null, balance);
        recyclerView.setAdapter(this.adapter);

    }

    public Runnable getUserBalance() {
        return () -> {

            RequestQueue MyRequestQueue = Volley.newRequestQueue(this.requireContext());
            String URL = "http://10.0.2.2/splitit/comunication.php";

            //Create an error listener to handle errors appropriately.
            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.

                if(response.equals("failure")){
                    Log.e("BalanceActivity","failed");

                }else{
                    Log.e("BalanceActivity", response);
                    saveBalance(response);


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

    public void saveBalance(String response){
        ArrayList<String> balanceInfo = Utilities.parseBalance(response, Long.valueOf(user_id));
        for (int i = 0; i<balanceInfo.size(); i++){
            String[] info = balanceInfo.get(i).split("/");
            this.balance.add(Float.valueOf(info[0]));
            this.balanceName.add(info[1]);

        }
        if(this.balance.size()>0){
            adapter.setData(balance,balanceName);
            Log.e("balance", "balance size>0");
            adapter.notifyDataSetChanged();

        }
    }
}
