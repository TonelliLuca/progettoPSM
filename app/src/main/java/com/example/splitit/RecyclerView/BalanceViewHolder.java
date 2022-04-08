package com.example.splitit.RecyclerView;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;



public class BalanceViewHolder extends RecyclerView.ViewHolder{


    TextView balanceVal;
    TextView balanceName;


    public BalanceViewHolder(@NonNull View itemView) {
        super(itemView);
        this.balanceVal = itemView.findViewById(R.id.balance_val);
        this.balanceName = itemView.findViewById(R.id.group_name);

    }
}
