package com.example.splitit;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.splitit.Database.UserGroupCrossRef;
import com.example.splitit.OnlineDatabase.OnlineDatabase;
import com.example.splitit.RecyclerView.GroupAdapter;
import com.example.splitit.RecyclerView.GroupItem;
import com.example.splitit.RecyclerView.OnItemListener;
import com.example.splitit.RecyclerView.User;
import com.example.splitit.ViewModel.AddUserViewModel;
import com.example.splitit.ViewModel.AddViewModel;
import com.example.splitit.ViewModel.ListViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements OnItemListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String LOG="HomeFragment";
    private GroupAdapter adapter;
    private RecyclerView recyclerView;
    private ListViewModel listViewModel;
    private AddViewModel addViewModel;
    private AddUserViewModel addUser;
    String user_code=null;
    String user_id=null;

    @Override
    public void onItemClick(int position) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if(appCompatActivity!=null){
            listViewModel.selected(adapter.getItemFiltered(position));
            GroupItem a = listViewModel.getSelected().getValue();
            Log.e("GroupItem","selected id: "+a.getId());
            Intent intent = new Intent(getActivity(), DetailsGroupActivity.class);
            intent.putExtra("group_ID",a.getId());
            startActivity(intent);
        }
    }

    @Override
    public void onDelete(long id,int posu, int posr) {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.home, container, false);

    }

    public void setLineChart() {
        LineChart lineChart = requireView().findViewById(R.id.lineChart);
        lineChart.setDragEnabled(true);
        //lineChart.setScaleEnabled(false);
        lineChart.setTouchEnabled(true);

        ArrayList<Entry> yVal = new ArrayList<>();

        yVal.add(new Entry(0, 60f));
        yVal.add(new Entry(1, 70f));
        yVal.add(new Entry(2, 80f));
        yVal.add(new Entry(3, 90f));
        yVal.add(new Entry(4, 30f));
        yVal.add(new Entry(5, 50f));
        yVal.add(new Entry(6, 75f));

        LineDataSet set1 = new LineDataSet(yVal, "");

        set1.setValueTextColor(Color.WHITE);
        set1.setLabel("");
        set1.setLineWidth(5f);
        set1.setFillAlpha(500);
        set1.setColor(Color.rgb(0,83, 87));
        set1.setDrawFilled(true);

        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.gradient_btn);
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
        lineChart.setDescription(desc);
        lineChart.getXAxis().setAxisLineColor(R.color.blue);
        lineChart.getAxisRight().setTextColor(Color.WHITE);
        lineChart.getAxisLeft().setTextColor(Color.WHITE);
        lineChart.getXAxis().setTextColor(Color.WHITE);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);

        lineChart.setData(data);

    }

    private void setRecyclerView(final Activity activity){
        recyclerView = requireView().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        final OnItemListener listener = this;
        adapter=new GroupAdapter(activity,listener);
        recyclerView.setAdapter(adapter);
    }



    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        if(activity != null){

            addUser = new ViewModelProvider((ViewModelStoreOwner) activity).get(AddUserViewModel.class);
            addViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(AddViewModel.class);
            listViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(ListViewModel.class);
            OnlineDatabase.execute(getGroupsOnline(view));
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            user_code = sharedPref.getString(getString(R.string.user_code),"0");
            user_id = sharedPref.getString(getString(R.string.user_id),"-1");
            Log.e(LOG, "user code: "+user_code+" User id: "+user_id);
            OnlineDatabase.execute(getActualUser(view));
            Utilities.setUpToolbar((AppCompatActivity) getActivity(), "SplitIt");
            setDialog(activity);
            setRecyclerView(activity);
            setLineChart();


            listViewModel.getGroupItems(user_id).observe((LifecycleOwner) activity, new Observer<List<GroupItem>>() {
                @Override
                public void onChanged(List<GroupItem> groupItems) {
                    adapter.setData(groupItems);
                }
            });
            FloatingActionButton floatingActionButton = view.findViewById(R.id.fab_add);
            floatingActionButton.setOnClickListener(v -> 
                    Utilities.insertFragment((AppCompatActivity) activity, new AddFragment(), "AddFragment"));


        }else{
            Log.e(LOG, "Activity is null");
        }

    }

    public void setDialog(final Activity activity){
        Button qrButton = requireView().findViewById(R.id.btn_qrcode);

        qrButton.setOnClickListener(v -> {

            Log.e("QRCODE", "created");
            DialogQrcodeFragment dialog = new DialogQrcodeFragment();
            dialog.show(getChildFragmentManager(), "QrCode Dialog");
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }


    public Runnable getGroupsOnline(View view) {
        Runnable task = () -> {

            RequestQueue MyRequestQueue = Volley.newRequestQueue(this.getContext());
            String URL = "http://10.0.2.2/splitit/comunication.php";

            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //This code is executed if the server responds, whether or not the response contains data.
                    //The String 'response' contains the server's response.

                    if(response.equals("failure")){
                        Log.e(LOG,"failed");

                    }else{
                        Log.e(LOG,response.toString());
                        saveGroups(response.toString());
                    }
                }
            }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                @Override
                public void onErrorResponse(VolleyError error) {
                    //This code is executed if there is an error.
                    Log.e(LOG,"error response");

                }
            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<String, String>();
                    MyData.put("id", String.valueOf(user_id)); //Add the data you'd like to send to the server.
                    MyData.put("request",String.valueOf(0));
                    return MyData;
                }
            };

            MyRequestQueue.add(MyStringRequest);
        };
        return task;

    }

    private void saveGroups(String s){
        ArrayList<GroupItem> list = Utilities.parseGroupItems(s);
        Log.e(LOG,"Groups num:"+list.size());
        if(list.size()>0){
            for(int i = 0; i<list.size();i++){
                GroupItem g = list.get(i);
                addViewModel.addGroupItem(g);

            }
        }
        ArrayList<UserGroupCrossRef> listRef = Utilities.parseUserGroupCrossRef(s);
        Log.e(LOG,"Ref num:"+list.size());
        if(listRef.size()>0){
            for(int i = 0; i<listRef.size();i++){
                UserGroupCrossRef r = listRef.get(i);
                addUser.addNewRef(r);

            }
        }
    }

    public Runnable getActualUser(View view) {
        Runnable task = () -> {

            RequestQueue MyRequestQueue = Volley.newRequestQueue(this.getContext());
            String URL = "http://10.0.2.2/splitit/comunication.php";

            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //This code is executed if the server responds, whether or not the response contains data.
                    //The String 'response' contains the server's response.

                    if(response.equals("failure")){
                        Log.e(LOG,"failed");

                    }else{
                        Log.e(LOG,response.toString());
                        saveUser(response.toString());
                    }
                }
            }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                @Override
                public void onErrorResponse(VolleyError error) {
                    //This code is executed if there is an error.
                    Log.e(LOG,"error response");

                }
            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<String, String>();
                    MyData.put("id", String.valueOf(user_id)); //Add the data you'd like to send to the server.
                    MyData.put("request",String.valueOf(1));
                    return MyData;
                }
            };

            MyRequestQueue.add(MyStringRequest);
        };
        return task;

    }


    private void saveUser(String s){
        ArrayList<User> list = Utilities.parseUser(s);
        Log.e(LOG,"User num:"+list.size());
        if(list.size()>0){
            for(int i = 0; i<list.size();i++){
                User u = list.get(i);
                addUser.addUser(u);

            }
        }
    }


}
