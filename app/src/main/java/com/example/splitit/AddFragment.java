package com.example.splitit;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.splitit.Database.RefRepository;
import com.example.splitit.Database.UserGroupCrossRef;
import com.example.splitit.RecyclerView.GroupItem;
import com.example.splitit.ViewModel.AddUserViewModel;
import com.example.splitit.ViewModel.AddViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class AddFragment extends Fragment {

    private EditText nameText;
    private long lastId;


    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle sevedInstanceState){
        return inflater.inflate(R.layout.add_group, container, false);
    }

    public void addGroup(View view){

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        final Activity activity=getActivity();

        if(activity!=null){

            AddViewModel addViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(AddViewModel.class);
            AddUserViewModel addUserRef = new ViewModelProvider((ViewModelStoreOwner) activity).get(AddUserViewModel.class);
            Utilities.setUpToolbar((AppCompatActivity) activity, "Make a group");
            nameText = activity.findViewById(R.id.groupNameAdd);

            view.findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    lastId=addViewModel.addGroupItem(new GroupItem("ic_baseline:android_24",nameText.getText().toString()));
                    addUserRef.addNewRef(new UserGroupCrossRef(new Long(0),new Long(lastId+1)));
                    ((AppCompatActivity) activity).getSupportFragmentManager().popBackStack();
                }



            });

        }
    }
}
