package com.example.splitit;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.example.splitit.Database.UserGroupCrossRef;
import com.example.splitit.RecyclerView.GroupItem;
import com.example.splitit.ViewModel.AddUserViewModel;
import com.example.splitit.ViewModel.AddViewModel;
import com.google.android.material.snackbar.Snackbar;

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

                addViewModel.addGroupItem(new GroupItem(1,"ic_baseline:android_24", nameText.getText().toString(),userId,false));

                addUserRef.addNewRef(new UserGroupCrossRef(userId, lastId + 1,false,0));

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
}
