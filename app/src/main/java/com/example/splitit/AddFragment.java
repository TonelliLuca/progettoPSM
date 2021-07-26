package com.example.splitit;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.splitit.Database.UserGroupCrossRef;
import com.example.splitit.OnlineDatabase.OnlineDatabase;
import com.example.splitit.RecyclerView.GroupItem;
import com.example.splitit.ViewModel.AddUserViewModel;
import com.example.splitit.ViewModel.AddViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddFragment extends Fragment {

    private EditText nameText;
    private long lastId=0;
    private long userId;



    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle sevedInstanceState){
        return inflater.inflate(R.layout.add_group, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        final Activity activity=getActivity();

        if(activity!=null){
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            userId = Long.valueOf(sharedPref.getString(getString(R.string.user_id),"-1"));
            AddViewModel addViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(AddViewModel.class);
            AddUserViewModel addUserRef = new ViewModelProvider((ViewModelStoreOwner) activity).get(AddUserViewModel.class);
            Utilities.setUpToolbar((AppCompatActivity) activity, "Make a group");
            nameText = activity.findViewById(R.id.groupNameAdd);
            addViewModel.getLastId().observe((LifecycleOwner) activity, aLong -> {
                if(aLong != null)
                    lastId=aLong;
            });

            view.findViewById(R.id.buttonAdd).setOnClickListener(v -> {
                OnlineDatabase.execute(addGroupOnline(view));
                addViewModel.addGroupItem(new GroupItem(lastId,"ic_baseline:android_24", nameText.getText().toString(),userId,false));

                addUserRef.addNewRef(new UserGroupCrossRef(userId, lastId,false,0));

                if (checkAdd()) {

                    ((AppCompatActivity) activity).getSupportFragmentManager().popBackStack();

                } else {
                    Snackbar snackbar_error = Snackbar.make(view, R.string.error_login, Snackbar.LENGTH_SHORT);
                    View snackbar_error_view = snackbar_error.getView();
                    snackbar_error_view.setBackgroundColor(ContextCompat.getColor(activity, R.color.design_default_color_error));
                    snackbar_error.show();
                }
            });

        }
    }

    public boolean checkAdd(){
        return !nameText.getText().toString().matches("");
    }



    public Runnable addGroupOnline(View view) {
        Runnable task = () -> {

            RequestQueue MyRequestQueue = Volley.newRequestQueue(this.getContext());
            String URL = "http://10.0.2.2/splitit/comunication.php";

            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //This code is executed if the server responds, whether or not the response contains data.
                    //The String 'response' contains the server's response.

                    if(response.equals("failure")){
                        Log.e("AddFragment","failed");

                    }else{
                        Log.e("AddFragment",response.toString());
                        setLastId(response.toString());
                    }
                }
            }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                @Override
                public void onErrorResponse(VolleyError error) {
                    //This code is executed if there is an error.
                    Log.e("AddFragment","error response");

                }
            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<String, String>();
                    MyData.put("id", String.valueOf(userId)); //Add the data you'd like to send to the server.
                    MyData.put("request",String.valueOf(2));
                    JSONObject jObjectData = new JSONObject();
                    try {
                        jObjectData.put("nome",nameText.getText().toString());
                        jObjectData.put("img","ic_baseline:android_24");
                        jObjectData.put("admin",userId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    MyData.put("specs",jObjectData.toString());
                    return MyData;
                }
            };

            MyRequestQueue.add(MyStringRequest);
        };
        return task;

    }

    private void setLastId(String s){
        this.lastId=Long.valueOf(s);
    }
}
