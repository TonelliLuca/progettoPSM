package com.example.splitit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddFragment extends Fragment {
    private Button addButton;
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle sevedInstanceState){
        View view  = inflater.inflate(R.layout.add_group, container, false);
        addButton = view.findViewById(R.id.buttonAdd);
        addButton.setOnClickListener(v -> {

        });
        return view;
    }

    public void addGroup(View view){

    }
}
