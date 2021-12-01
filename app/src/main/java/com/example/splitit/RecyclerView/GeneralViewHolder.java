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

public class GeneralViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ImageView userImage;
    TextView userName;
    TextView tv_userId;
    ImageView userRemove;
    String user_id;
    long idUser;
    int posU;
    int posR;
    boolean admin;
    boolean adminFlag;
    View itemViewAdmin;


    private final OnItemListener listener;

    public GeneralViewHolder(@NonNull View itemView, View itemViewAdmin, OnItemListener listener, boolean admin, String user_id) {
        super(itemView);
        this.itemViewAdmin = itemViewAdmin;
        userImage = itemView.findViewById(R.id.card_user_image);
        userRemove=itemView.findViewById(R.id.delete_user);
        userName = itemView.findViewById(R.id.user_name);
        tv_userId = itemView.findViewById(R.id.id_user_card);
        this.admin = admin;
        this.user_id = user_id;
        this.listener=listener;
        itemView.setOnClickListener(this);
        userRemove.setOnClickListener(this);

    }

    public GeneralViewHolder(GeneralViewHolder holder) {
        super(holder.itemView);
        userImage = itemView.findViewById(R.id.card_user_image);
        userRemove=itemView.findViewById(R.id.delete_user);
        userName = itemView.findViewById(R.id.user_name);
        tv_userId = itemView.findViewById(R.id.id_user_card);
        this.admin = holder.admin;
        this.user_id = holder.user_id;
        this.listener= holder.listener;
        itemView.setOnClickListener(this);
        userRemove.setOnClickListener(this);
    }

    public AdminViewHolder getAdminHolder(){
        return new AdminViewHolder(new GeneralViewHolder(itemViewAdmin,null, listener, admin,user_id));
    }


    @Override
    public void onClick(View v) {
        Log.e("aaa","user_id "+this.user_id+" idUser "+this.idUser+" admin "+this.admin);
        if(v.getId()==R.id.delete_user){
            if((!this.admin) && Long.valueOf(this.user_id)!=this.idUser){
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Errore!!")
                        .setMessage("Solo l'amministratore del wallet può rimuovere gli utenti")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setNeutralButton(android.R.string.cancel, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }else{
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Rimuovi utente")
                        .setMessage("Sei sicuro di voler rimuovere l'utente dal gruppo?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onDelete(idUser, posU, posR);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }
    /*
    //This method is used to set the admin specs in a card
    public void refresh(){
        //TODO personalizzare la card dell'admin
        itemView.setBackgroundColor( Color.parseColor("#5D1049")); //without theme

    }*/
}