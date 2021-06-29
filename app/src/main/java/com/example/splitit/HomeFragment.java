package com.example.splitit;



import androidx.fragment.app.Fragment;

import com.example.splitit.RecyclerView.GroupAdapter;
import com.example.splitit.RecyclerView.GroupItem;
import com.example.splitit.RecyclerView.OnItemListener;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnItemListener{
    private static final String LOG="HomeFragment";
    private QRGen qrg= new QRGen();
    private GroupAdapter adapter;
    private RecyclerView recyclerView;
    @Override
    public void onItemClick(int position) {

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
            setRecyclerView(activity);
            qrg.generate(activity,"CIAO CIAO CIAO", 200);
        }else{
            Log.e(LOG, "Activity is null");
        }
    }
}
