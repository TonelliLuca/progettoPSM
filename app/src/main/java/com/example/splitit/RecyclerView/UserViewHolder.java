package com.example.splitit.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ImageView userImage;
    TextView userName;
    TextView tv_userId;
    ImageView userRemove;
    String user_id;
    long idUser;
    int posU;
    int posR;
    boolean admin;
    TextView user_amount;

    private final OnItemListener listener;

    public UserViewHolder(@NonNull View itemView, OnItemListener listener, boolean admin, String user_id) {
        super(itemView);
        this.admin = admin;
        userImage = itemView.findViewById(R.id.card_user_image);
        userRemove=itemView.findViewById(R.id.delete_user);
        userName = itemView.findViewById(R.id.user_name);
        tv_userId = itemView.findViewById(R.id.id_user_card);
        this.user_id = user_id;
        this.listener=listener;
        itemView.setOnClickListener(this);
        userRemove.setOnClickListener(this);
        user_amount = itemView.findViewById(R.id.somma_utente);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.delete_user){
            if((!this.admin) && Long.parseLong(this.user_id)!=this.idUser){
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
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> listener.onDelete(idUser, posU, posR))

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }
}