package com.example.splitit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.List;

public class DetailsFragment extends Fragment implements OnItemListener, NavigationView.OnNavigationItemSelectedListener{
    private final long groupId;
    private AddUserViewModel vm;
    private List<User> userList;
    private UserAdapter adapter;
    private RecyclerView recyclerView;
    private List<UserGroupCrossRef> refUser;

    public DetailsFragment(long groupId){
        this.groupId = groupId;
    }


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.detailed_group,container, false);

    }

    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        final Activity activity=getActivity();
        if(activity!=null){

            Log.e("DetailsFragment","id group: "+groupId);

            PieChart pieChart = activity.findViewById(R.id.pie_chart);
            pieChart.getLegend().setEnabled(true);

            ArrayList<PieEntry> NoOfEmp = new ArrayList();

            NoOfEmp.add(new PieEntry(945f, 0));
            NoOfEmp.add(new PieEntry(1040f, 1));
            NoOfEmp.add(new PieEntry(1133f, 2));
            NoOfEmp.add(new PieEntry(1240f, 3));
            NoOfEmp.add(new PieEntry(1369f, 4));
            NoOfEmp.add(new PieEntry(1487f, 5));
            NoOfEmp.add(new PieEntry(1501f, 6));
            NoOfEmp.add(new PieEntry(1645f, 7));
            NoOfEmp.add(new PieEntry(1578f, 8));
            NoOfEmp.add(new PieEntry(1695f, 9));
            PieDataSet dataSet = new PieDataSet(NoOfEmp, "");

            ArrayList year = new ArrayList();

            year.add("2008");
            year.add("2009");
            year.add("2010");
            year.add("2011");
            year.add("2012");
            year.add("2013");
            year.add("2014");
            year.add("2015");
            year.add("2016");
            year.add("2017");
            PieData data = new PieData(dataSet);
            pieChart.setData(data);
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            pieChart.animateXY(2000, 2000);
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
            vm.searchUsers(groupId).observe((LifecycleOwner) activity, new Observer<List<GroupWithUsers>>(){


                @Override
                public void onChanged(List<GroupWithUsers> list) {
                    userList = list.get(0).users;
                    printLogList();
                    adapter.setData(userList);
                }
            });
            vm.getAllUsersBalance(groupId).observe((LifecycleOwner) activity, new Observer<List<UserGroupCrossRef>>(){


                @Override
                public void onChanged(List<UserGroupCrossRef> userGroupCrossRefs) {
                    refUser=userGroupCrossRefs;
                    adapter.setValues(userGroupCrossRefs);
                }


            });





        }



    }


    private void printLogList(){
        Log.e("UserList ", "Users size:"+userList.size());
        for(int i=0;i<userList.size();i++){


            Log.e("UserList ", String.valueOf(userList.get(i).getName()));

        }
    }

    private void setRecyclerView(final Activity activity){
        recyclerView = requireView().findViewById(R.id.recyclerViewUser);
        recyclerView.setHasFixedSize(true);
        final OnItemListener listener = this;
        adapter = new UserAdapter(activity, listener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
