package com.example.splitit.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;

public class GroupViewHolder extends RecyclerView.ViewHolder{
    ImageView groupImage;
    TextView groupName;
    public GroupViewHolder(@NonNull View itemView) {
        super(itemView);
        groupName = itemView.findViewById(R.id.groupName);
        groupImage = itemView.findViewById(R.id.groupImage);
    }
}
