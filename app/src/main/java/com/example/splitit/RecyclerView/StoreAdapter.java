package com.example.splitit.RecyclerView;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.Database.UserGroupCrossRef;
import com.example.splitit.R;
import com.example.splitit.Utilities;
import com.example.splitit.ViewModel.AddUserViewModel;

import java.util.ArrayList;
import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int SIZE = 100;
    private List<User> userItemList = new ArrayList<>();
    private List<User> userItemFiltered = new ArrayList<>();
    private final Activity activity;
    private final OnItemListener listener;
    private List<UserGroupCrossRef> balance;
    private final AddUserViewModel vm;
    private final long groupId;
    private String user_id;
    private boolean admin;
    private long admin_id;


    public StoreAdapter(Activity activity, OnItemListener listener, long groupId, String user_id, boolean admin, long admin_id) {
        this.listener = listener;
        this.activity = activity;
        this.vm = new ViewModelProvider((ViewModelStoreOwner) activity).get(AddUserViewModel.class);
        this.groupId = groupId;
        this.user_id = user_id;
        this.admin = admin;
        this.admin_id = admin_id;

    }


    @Override
    public int getItemViewType(int position) {
        // based on you list you will return the ViewType
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("BIND", " viewType : "+ viewType);

        View layoutItem;
        if (viewType == 0) {
            layoutItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.storic_card_admin_layout, parent, false);
            return new StoreViewHolderAdmin(layoutItem, admin, this.user_id);
        } else {
            layoutItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.storic_card_layout, parent, false);
            return new StoreViewHolderUser(layoutItem, admin, this.user_id);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.e("BIND", " position: "+ position);

        User currentItem = userItemList.get(position);
        UserGroupCrossRef ref;

        for (int i = 0; i < balance.size(); i++) {

            if (balance.get(i).getUser_id() == currentItem.getId()) {
                Log.e("BIND", "balance : " + balance.size());
                ref = balance.get(i);
                if (currentItem.getId() == admin_id) {
                    Log.e("BIND", "ADMIN: "+currentItem.getId()+"/"+admin_id);
                    StoreViewHolderAdmin storeViewHolderAdmin = (StoreViewHolderAdmin) holder;
                    storeViewHolderAdmin.userName.setText(currentItem.getName());
                    storeViewHolderAdmin.idUser = currentItem.getId();
                    storeViewHolderAdmin.posU =  holder.getAdapterPosition();
                    storeViewHolderAdmin.posR = i;
                    storeViewHolderAdmin.tv_userId.setText(String.valueOf(currentItem.getId()));
                    Utilities.getImage(String.valueOf(currentItem.getId()), storeViewHolderAdmin.userImage);

                } else {
                    Log.e("BIND", "USER: "+currentItem.getId()+"/"+admin_id);
                    StoreViewHolderUser storeViewHolderUser = (StoreViewHolderUser) holder;
                    storeViewHolderUser.userName.setText(currentItem.getName());
                    storeViewHolderUser.user_amount.setText(String.valueOf(ref.getBalance()));
                    storeViewHolderUser.idUser = currentItem.getId();
                    storeViewHolderUser.posU =  holder.getAdapterPosition();
                    storeViewHolderUser.posR = i;
                    storeViewHolderUser.tv_userId.setText(String.valueOf(currentItem.getId()));
                    Utilities.getImage(String.valueOf(currentItem.getId()), storeViewHolderUser.userImage);


                }


            }

        }


        /*if(tmp!=-1){
            UserViewHolder userHolder = new UserViewHolder(holder);
            userHolder.userName.setText(currentItem.getName());
            userHolder.user_amount.setText(String.valueOf(ref.getBalance()));
            userHolder.idUser=currentItem.getId();
            userHolder.posU=position;

            Utilities.getImage(String.valueOf(currentItem.getId()), userHolder.userImage);

        } else {
            AdminViewHolder adminHolder = (AdminViewHolder)holder;
            adminHolder.userName.setText(currentItem.getName());
            adminHolder.idUser=currentItem.getId();
            adminHolder.posU=position;

            Utilities.getImage(String.valueOf(currentItem.getId()), adminHolder.userImage);
        }*/


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

        /*holder.userName.setText(currentItem.getName());
        holder.user_amount.setText(String.valueOf(ref.getBalance()));
        holder.idUser=currentItem.getId();
        holder.posU=position;

        Utilities.getImage(String.valueOf(currentItem.getId()), holder.userImage);*/

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

