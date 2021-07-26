package com.example.splitit;

import android.app.Activity;
import android.app.AsyncNotedAppOp;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.Database.GroupWithUsers;
import com.example.splitit.Database.UserGroupCrossRef;
import com.example.splitit.Database.UsersWithGroup;
import com.example.splitit.RecyclerView.GroupAdapter;
import com.example.splitit.RecyclerView.GroupItem;
import com.example.splitit.RecyclerView.OnItemListener;
import com.example.splitit.RecyclerView.User;
import com.example.splitit.RecyclerView.UserAdapter;
import com.example.splitit.ViewModel.AddUserViewModel;

import com.example.splitit.ViewModel.ListViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailsFragment extends Fragment implements OnItemListener, NavigationView.OnNavigationItemSelectedListener{
    private final long groupId;
    private AddUserViewModel vm;
    private List<User> userList;
    private UserAdapter adapter;
    private RecyclerView recyclerView;
    private List<UserGroupCrossRef> refUser;
    private UserGroupCrossRef userToDelete;
    private PieChart pieChart;

    public DetailsFragment(long groupId){
        this.groupId = groupId;
    }


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.detailed_group,container, false);

    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        final Activity activity=getActivity();
        if(activity!=null){

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
            // calling method to set center text
            pieChart.setCenterText("Amount $");
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
                        //printLogList();
                    }
                }
            });







        }



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
        //TODO eliminazione della referenza su db online 
        if(appCompatActivity!=null) {
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    userToDelete=vm.searchSpecRef(groupId, id);
                    vm.removeRef(userToDelete);

                }
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

        for(int i=0;i<userList.size();i++){
            for(int j=0;j<refUser.size();j++){
                if(userList.get(i).getId() == refUser.get(j).getUserId()){
                    //Log.e("Graph",(float)refUser.get(j).getBalance()+" "+userList.get(i).getName()+" "+userList.get(i).getId()+" = "+refUser.get(j).getUser_id());
                    NoOfEmp.add(new PieEntry((float)refUser.get(j).getBalance(), userList.get(i).getName()));
                }
            }

        }


        PieDataSet dataSet = new PieDataSet(NoOfEmp, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.animateXY(2000, 2000);

    }

}
