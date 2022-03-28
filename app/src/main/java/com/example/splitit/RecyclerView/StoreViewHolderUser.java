package com.example.splitit.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;

public class StoreViewHolderUser extends RecyclerView.ViewHolder{
    ImageView userImage;
    TextView userName;
    TextView tv_userId;
    TextView user_amount;
    String user_id;
    long idUser;
    int posU;
    int posR;
    boolean admin;

    public StoreViewHolderUser(@NonNull View itemView, boolean admin, String user_id) {
        super(itemView);
        this.admin = admin;
        userImage = itemView.findViewById(R.id.card_user_image);
        userName = itemView.findViewById(R.id.user_name);
        tv_userId = itemView.findViewById(R.id.id_user_card);
        user_amount = itemView.findViewById(R.id.user_amount);
        this.user_id = user_id;
    }
}
