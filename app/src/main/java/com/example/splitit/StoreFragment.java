package com.example.splitit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.RecyclerView.GroupAdapter;
import com.example.splitit.RecyclerView.GroupItem;
import com.example.splitit.RecyclerView.OnItemListener;
import com.example.splitit.ViewModel.ListViewModel;

public class StoreFragment extends Fragment implements OnItemListener{
    private GroupAdapter adapter;
    private ListViewModel listViewModel;
    private String user_code;
    private String user_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflate, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflate.inflate(R.layout.storage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        Activity activity = getActivity();
        if(activity != null){
            Utilities.stop=true;
            listViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(ListViewModel.class);
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            user_code = sharedPref.getString(getString(R.string.user_code),"0")    ;
            user_id = sharedPref.getString(getString(R.string.user_id),"-1");
            setRecyclerView(activity);

            listViewModel.getGroupItemsComplete(user_id).observe((LifecycleOwner) activity, groupItems -> {

                adapter.setData(groupItems);

            });

        }

    }



    private void setRecyclerView(final Activity activity){
        RecyclerView recyclerView = requireView().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        final OnItemListener listener = this;
        adapter=new GroupAdapter(activity,listener);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(int position) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if(appCompatActivity!=null){
            listViewModel.selected(adapter.getItemFiltered(position));
            GroupItem a = listViewModel.getSelected().getValue();
            assert a != null;
            Log.e("GroupItem","selected id: "+a.getId());
            Intent intent = new Intent(getActivity(), DetailedGroupStore.class);
            intent.putExtra("group_ID",a.getId());
            intent.putExtra("group_NAME",a.getGroupName());
            intent.putExtra("group_IMAGE",a.getImageResource());
            intent.putExtra("user_ID",user_id);

            startActivity(intent);
        }
    }

    @Override
    public void onDelete(long id, int posu, int posr) {

    }
}
