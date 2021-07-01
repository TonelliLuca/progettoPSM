package com.example.splitit;



import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.splitit.RecyclerView.GroupAdapter;
import com.example.splitit.RecyclerView.GroupItem;
import com.example.splitit.RecyclerView.OnItemListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements OnItemListener, NavigationView.OnNavigationItemSelectedListener{
    private static final String LOG="HomeFragment";
    private QRGen qrg= new QRGen();
    private GroupAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public void onItemClick(int position) {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.home, container, false);

    }

    private void setRecyclerView(final Activity activity){
        recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        List<GroupItem> list = new ArrayList();
        list.add(new GroupItem("ic_baseline:android_24",  "Group1"));
        list.add(new GroupItem("ic_baseline:android_24",  "Group2"));
        list.add(new GroupItem("ic_baseline:android_24",  "Group3"));
        list.add(new GroupItem("ic_baseline:android_24",  "Group4"));
        adapter=new GroupAdapter(activity, list);
        recyclerView.setAdapter(adapter);
    }



    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        if(activity != null){
            Utilities.setUpToolbar((AppCompatActivity) getActivity(), "SplitIt");
            setRecyclerView(activity);
            qrg.generate(activity,"CIAO CIAO CIAO", 200);
            FloatingActionButton floatingActionButton = view.findViewById(R.id.fab_add);
            floatingActionButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                Utilities.insertFragment((AppCompatActivity) activity, new AddFragment(), "AddFragment");
                }
            });
        }else{
            Log.e(LOG, "Activity is null");
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}
