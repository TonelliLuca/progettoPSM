package com.example.splitit.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;

public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ImageView groupImage;
    TextView groupName;
    private OnItemListener listener;
    public GroupViewHolder(@NonNull View itemView, OnItemListener listener) {
        super(itemView);
        groupName = itemView.findViewById(R.id.groupName);
        groupImage = itemView.findViewById(R.id.groupImage);
        this.listener=listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onItemClick(getAdapterPosition());
    }
}
