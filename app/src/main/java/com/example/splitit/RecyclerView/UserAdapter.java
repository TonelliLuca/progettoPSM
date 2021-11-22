package com.example.splitit.RecyclerView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.Database.UserGroupCrossRef;
import com.example.splitit.R;
import com.example.splitit.Utilities;
import com.example.splitit.ViewModel.AddUserViewModel;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
    private static final int SIZE = 100;
    private List<User> userItemList=new ArrayList<>();
    private List<User> userItemFiltered = new ArrayList<>();
    private final Activity activity;
    private final OnItemListener listener;
    private List<UserGroupCrossRef> balance;
    private final AddUserViewModel vm;
    private final long groupId;
    private String user_id;
    private boolean admin;
    private long admin_id;

    public UserAdapter(Activity activity, OnItemListener listener, long groupId, String user_id, boolean admin, long admin_id){
        this.listener = listener;
        this.activity = activity;
        this.vm= new ViewModelProvider((ViewModelStoreOwner) activity).get(AddUserViewModel.class);
        this.groupId= groupId;
        this.user_id = user_id;
        this.admin=admin;
        this.admin_id = admin_id;

    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card_layout, parent, false);
        return new UserViewHolder(layoutItem,listener, this.admin, this.user_id);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User currentItem = userItemList.get(position);
        UserGroupCrossRef ref=null;
        for(int i =0;i<balance.size();i++){
            if(balance.get(i).getUser_id() == currentItem.getId()){
                ref=balance.get(i);
                holder.posR=i;
                holder.tv_userId.setText(String.valueOf(currentItem.getId()));
                //call the refresh method to update the graphic for admin user
                if(currentItem.getId() == this.admin_id){

                    holder.refresh();

                }
            }
        }


        String image_path = currentItem.getImg();

        /*if (image_path.contains("ic_")) {
            Drawable drawable = ContextCompat.getDrawable(activity, activity.getResources()
                    .getIdentifier(image_path, "drawable",
                            activity.getPackageName()));
            holder.imageCardView.setImageDrawable(drawable);
        } else {
            Bitmap bitmap = Utilities.getImageBitmap(activity, Uri.parse(image_path));
            if (bitmap != null){
                holder.imageCardView.setImageBitmap(bitmap);
            }
        }*/

        holder.userName.setText(currentItem.getName());
        holder.user_amount.setText(String.valueOf(ref.getBalance()));
        holder.idUser=currentItem.getId();
        holder.posU=position;

        Utilities.getImage(String.valueOf(currentItem.getId()), holder.userImage);




    }

    @Override
    public int getItemCount() {
        return userItemList.size();
    }

    public void setData(List<User> list) {
        this.userItemList = new ArrayList<>(list);
        this.userItemFiltered = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    public void setValues( List<UserGroupCrossRef> ref){
        this.balance=new ArrayList<>(ref);
        notifyDataSetChanged();
    }

    public void uploadData(int posu,int posr){
        userItemList.remove(posu);
        balance.remove(posr);
        this.notifyItemRemoved(posu);
        this.notifyItemRangeChanged(posu, userItemList.size());
    }
    public User getItemFiltered(int position){
        return userItemList.get(position);
    }




}
