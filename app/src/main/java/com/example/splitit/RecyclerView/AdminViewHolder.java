package com.example.splitit.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;

public class AdminViewHolder extends GeneralViewHolder{

    public AdminViewHolder(@NonNull View itemView, View itemViewAdmin, OnItemListener listener, boolean admin, String user_id) {
        super(itemView,itemViewAdmin, listener, admin, user_id);
    }
    public AdminViewHolder(GeneralViewHolder holder) {
        super(holder);
    }
}
