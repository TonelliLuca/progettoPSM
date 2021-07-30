package com.example.splitit.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;
import com.example.splitit.ViewModel.AddUserViewModel;

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ImageView userImage;
    TextView userName;
    TextView user_amount;
    ImageView userRemove;
    long idUser;
    int posU;
    int posR;


    private final OnItemListener listener;
    public UserViewHolder(@NonNull View itemView, OnItemListener listener) {
        super(itemView);
        userImage = itemView.findViewById(R.id.user_image);
        userRemove=itemView.findViewById(R.id.delete_user);
        userName = itemView.findViewById(R.id.user_name);
        user_amount = itemView.findViewById(R.id.somma_utente);
        this.listener=listener;
        itemView.setOnClickListener(this);
        userRemove.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.delete_user){
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Rimuovi utente")
                    .setMessage("Sei sicuro di voler rimuovere l'utente dal gruppo?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onDelete(idUser,posU,posR);
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }


    }
}
