package com.example.splitit.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ImageView userImage;
    TextView userName;
    TextView user_amount;

    private final OnItemListener listener;
    public UserViewHolder(@NonNull View itemView, OnItemListener listener) {
        super(itemView);
        userImage = itemView.findViewById(R.id.user_image);
        userName = itemView.findViewById(R.id.user_name);
        user_amount = itemView.findViewById(R.id.somma_utente);
        this.listener=listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onItemClick(getAdapterPosition());

    }
}
