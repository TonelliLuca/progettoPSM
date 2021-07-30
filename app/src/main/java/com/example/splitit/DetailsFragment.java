package com.example.splitit;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.splitit.Database.GroupWithUsers;
import com.example.splitit.Database.UserGroupCrossRef;
import com.example.splitit.OnlineDatabase.OnlineDatabase;
import com.example.splitit.RecyclerView.OnItemListener;
import com.example.splitit.RecyclerView.User;
import com.example.splitit.RecyclerView.UserAdapter;
import com.example.splitit.ViewModel.AddUserViewModel;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailsFragment extends Fragment implements OnItemListener, NavigationView.OnNavigationItemSelectedListener{
    private final long groupId;
    private final String groupName;
    private final String groupImage;
    private AddUserViewModel vm;
    private List<User> userList;
    private UserAdapter adapter;
    private RecyclerView recyclerView;
    private List<UserGroupCrossRef> refUser;
    private UserGroupCrossRef userToDelete;
    private PieChart pieChart;
    private Button btn_addUser;
    private TextView tv_groupName;
    private ImageView iv_grouImage;

    public DetailsFragment(long groupId, String groupName, String groupImage){
        this.groupName = groupName;
        this.groupId = groupId;
        this.groupImage = groupImage;
    }


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.detailed_group,container, false);

    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        final Activity activity=getActivity();
        if(activity!=null){
            iv_grouImage = activity.findViewById(R.id.iv_group_image);
            iv_grouImage.setImageResource(R.drawable.avatar);
            tv_groupName = activity.findViewById(R.id.tv_group_name);
            tv_groupName.setText(groupName);

            Log.e("DetailsFragment","id group: "+groupId);

            pieChart = activity.findViewById(R.id.pie_chart);
            pieChart.getLegend().setEnabled(true);

            Legend l = pieChart.getLegend();
            l.setTextSize(14f);
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setForm(Legend.LegendForm.LINE);
            l.setTextColor(Color.WHITE);
            l.setEnabled(true);

            // if no need to add description
            pieChart.getDescription().setEnabled(false);

            setRecyclerView(activity);

            vm=new ViewModelProvider((ViewModelStoreOwner) activity).get(AddUserViewModel.class);
            vm.getAllUsersBalance(groupId).observe((LifecycleOwner) activity, new Observer<List<UserGroupCrossRef>>(){

                @Override
                public void onChanged(List<UserGroupCrossRef> userGroupCrossRefs) {
                    refUser=userGroupCrossRefs;
                    adapter.setValues(userGroupCrossRefs);
                    updateGraph();


                }
            });

            vm.searchUsers(groupId).observe((LifecycleOwner) activity, new Observer<List<GroupWithUsers>>(){
                @Override
                public void onChanged(List<GroupWithUsers> list) {
                    if(list.size()>0) {
                        userList = list.get(0).users;
                        printLogList();
                        adapter.setData(userList);
                        updateGraph();
                    }
                }
            });

        }

        setDialog();

    }

    public void setDialog(){
        btn_addUser = requireView().findViewById(R.id.btn_add_to_group);
        btn_addUser.setOnClickListener(v -> {
            DialogAddUserSelection dialog = new DialogAddUserSelection();
            dialog.show(getChildFragmentManager(), "User Selection Dialog");
        });
    }

    private void printLogList(){
        Log.e("UserList ", "Users size:"+userList.size());
        for(int i=0;i<userList.size();i++){


            Log.e("UserList ", String.valueOf(userList.get(i).getId())+" "+userList.get(i).getName());

        }
    }

    private void setRecyclerView(final Activity activity){
        recyclerView = requireView().findViewById(R.id.recyclerViewUser);
        recyclerView.setHasFixedSize(true);
        final OnItemListener listener = this;
        adapter = new UserAdapter(activity, listener,groupId);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {

    }


    @Override
    public void onDelete(long id,int posu, int posr) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        OnlineDatabase.execute(deleteRef(getView(),String.valueOf(id),String.valueOf(groupId)));
        if(appCompatActivity!=null) {
            Runnable task = () -> {
                userToDelete=vm.searchSpecRef(groupId, id);
                vm.removeRef(userToDelete);
            };

            ExecutorService ex=Executors.newFixedThreadPool(1);
            ex.execute(task);
        }
        adapter.uploadData(posu,posr);
        adapter.notifyDataSetChanged();
        updateGraph();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }



    public void updateGraph(){
        if(userList == null || refUser == null){
            return;
        }


        ArrayList<PieEntry> NoOfEmp = new ArrayList();
        float total=0;
        for(int i=0;i<userList.size();i++){
            for(int j=0;j<refUser.size();j++){
                if(userList.get(i).getId() == refUser.get(j).getUserId()){
                    //Log.e("Graph",(float)refUser.get(j).getBalance()+" "+userList.get(i).getName()+" "+userList.get(i).getId()+" = "+refUser.get(j).getUser_id());
                    NoOfEmp.add(new PieEntry((float)refUser.get(j).getBalance(), userList.get(i).getName()));
                    total+=(float)refUser.get(j).getBalance();
                }
            }

        }


        PieDataSet dataSet = new PieDataSet(NoOfEmp, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.animateXY(2000, 2000);
        pieChart.setCenterText(String.valueOf(total).concat("€"));

    }


    public Runnable deleteRef(View view,String idU,String idG) {
        Runnable task = () -> {

            RequestQueue MyRequestQueue = Volley.newRequestQueue(this.getContext());
            String URL = "http://10.0.2.2/splitit/comunication.php";

            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //This code is executed if the server responds, whether or not the response contains data.
                    //The String 'response' contains the server's response.

                    if(response.equals("failure")){
                        Log.e("DetailsFragment","failed");

                    }else{
                        Log.e("DetailsFragment",response.toString());

                    }
                }
            }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                @Override
                public void onErrorResponse(VolleyError error) {
                    //This code is executed if there is an error.
                    Log.e("DetailsFragment","error response");

                }
            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<String, String>();
                    MyData.put("id", idU); //Add the data you'd like to send to the server.
                    MyData.put("group_id", idG);
                    MyData.put("request",String.valueOf(3));
                    return MyData;
                }
            };

            MyRequestQueue.add(MyStringRequest);
        };
        return task;

    }

}
