package com.example.splitit;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
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
    private ListViewModel listViewModel;
    private AddViewModel addViewModel;
    private AddUserViewModel addUser;
    private LineChart lineChart;
    String user_code=null;
    String user_id=null;

    @Override
    public void onItemClick(int position) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if(appCompatActivity!=null){
            listViewModel.selected(adapter.getItemFiltered(position));
            GroupItem a = listViewModel.getSelected().getValue();
            assert a != null;
            Log.e("GroupItem","selected id: "+a.getId());
            Intent intent = new Intent(getActivity(), DetailsGroupActivity.class);
            intent.putExtra("group_ID",a.getId());
            intent.putExtra("group_NAME",a.getGroupName());
            intent.putExtra("group_NAME",a.getImageResource());
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

    public void setLineChart(List<Double> list) {

        ArrayList<Entry> yVal = new ArrayList<>();



        for(int i=0;i<list.size(); i++){
            yVal.add(new Entry(i, Float.parseFloat(String.valueOf(list.get(i)))));
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
        lineChart.invalidate();

    }

    private void setRecyclerView(final Activity activity){
        RecyclerView recyclerView = requireView().findViewById(R.id.recyclerView);
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

            lineChart = requireView().findViewById(R.id.lineChart);
            lineChart.setDragEnabled(true);
            //lineChart.setScaleEnabled(false);
            lineChart.setTouchEnabled(true);

            OnlineDatabase.execute(getGroupsOnline());
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            user_code = sharedPref.getString(getString(R.string.user_code),"0");
            user_id = sharedPref.getString(getString(R.string.user_id),"-1");
            Log.e(LOG, "user code: "+user_code+" User id: "+user_id);
            OnlineDatabase.execute(getActualUser());
            Utilities.setUpToolbar((AppCompatActivity) getActivity(), "SplitIt");
            setDialog();
            setRecyclerView(activity);



            listViewModel.getGroupItems(user_id).observe((LifecycleOwner) activity, groupItems -> adapter.setData(groupItems));

            addUser.getAllPayments(user_id).observe((LifecycleOwner) activity, doubles -> {
                setLineChart(doubles);
                Log.e("LineChart",String.valueOf(doubles.size()));
            });

            FloatingActionButton floatingActionButton = view.findViewById(R.id.fab_add);
            floatingActionButton.setOnClickListener(v -> 
                    Utilities.insertFragment((AppCompatActivity) activity, new AddFragment(), "AddFragment"));


        }else{
            Log.e(LOG, "Activity is null");
        }

    }

    public void setDialog(){
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


    public Runnable getGroupsOnline() {
        return () -> {

            RequestQueue MyRequestQueue = Volley.newRequestQueue(this.requireContext());
            String URL = "http://10.0.2.2/splitit/comunication.php";

            //Create an error listener to handle errors appropriately.
            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.

                if(response.equals("failure")){
                    Log.e(LOG,"failed");

                }else{
                    Log.e(LOG, response);
                    saveGroups(response);
                }
            }, error -> {
                //This code is executed if there is an error.
                Log.e(LOG,"error response");

            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<>();
                    MyData.put("id", String.valueOf(user_id)); //Add the data you'd like to send to the server.
                    MyData.put("request",String.valueOf(0));
                    return MyData;
                }
            };

            MyRequestQueue.add(MyStringRequest);
        };

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

    public Runnable getActualUser() {
        return () -> {

            RequestQueue MyRequestQueue = Volley.newRequestQueue(this.requireContext());
            String URL = "http://10.0.2.2/splitit/comunication.php";

            //Create an error listener to handle errors appropriately.
            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.

                if(response.equals("failure")){
                    Log.e(LOG,"failed");

                }else{
                    Log.e(LOG, response);
                    saveUser(response);
                }
            }, error -> {
                //This code is executed if there is an error.
                Log.e(LOG,"error response");

            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<>();
                    MyData.put("id", String.valueOf(user_id)); //Add the data you'd like to send to the server.
                    MyData.put("request",String.valueOf(1));
                    return MyData;
                }
            };

            MyRequestQueue.add(MyStringRequest);
        };

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
