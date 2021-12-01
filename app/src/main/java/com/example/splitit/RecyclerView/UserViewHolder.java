package com.example.splitit.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;
import com.example.splitit.Utilities;
import com.example.splitit.ViewModel.AddUserViewModel;

public class UserViewHolder extends GeneralViewHolder{
    TextView user_amount;

    public UserViewHolder(@NonNull View itemView, View itemViewAdmin, OnItemListener listener, boolean admin, String user_id) {
        super(itemView,itemViewAdmin, listener, admin, user_id);
        user_amount = itemView.findViewById(R.id.somma_utente);
    }

    public UserViewHolder(GeneralViewHolder holder) {
        super(holder);
        user_amount = itemView.findViewById(R.id.somma_utente);
    }
}