package com.example.splitit.RecyclerView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;

import java.util.ArrayList;

public class BalanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final Activity activity;
    private ArrayList<Float> balance;

    public BalanceAdapter(Activity activity, OnItemListener listener, ArrayList<Float> balance){
        this.activity = activity;
        this.balance=balance;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutItem;
        if(viewType==0){
            layoutItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.balance_card_positive, parent, false);

        }else{
            layoutItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.balance_card_negative, parent, false);

        }

        return new BalanceViewHolder(layoutItem);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BalanceViewHolder balanceViewHolder = (BalanceViewHolder) holder;
        balanceViewHolder.balanceVal.setText(this.balance.get(position).toString());

    }

    @Override
    public int getItemCount() {
        return balance.size();
    }

    @Override
    public int getItemViewType(int position) {
        // based on you list you will return the ViewType
        if(balance.get(position)>=0){
            return 0;
        }else{
            return 1;
        }
    }

    public void setData(ArrayList<Float> bal){
        this.balance = bal;

    }
}
