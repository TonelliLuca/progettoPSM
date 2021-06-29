package com.example.splitit.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GroupViewHolder extends RecyclerView.ViewHolder{
    ImageView groupImage;
    TextView groupName;
    public GroupViewHolder(@NonNull View itemView) {
        super(itemView);

    }
}
