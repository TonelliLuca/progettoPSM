package com.example.splitit.RecyclerView;

import android.app.Activity;

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

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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


    public UserAdapter(Activity activity, OnItemListener listener, long groupId, String user_id, boolean admin, long admin_id) {
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
        if(userItemList.size()==1 && userItemList.get(0).getId()!=admin_id){
            return 1;
        }
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutItem;
        if (viewType == 0) {
            layoutItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_card_layout, parent, false);
            return new AdminViewHolder(layoutItem, admin, this.user_id);
        } else {
            layoutItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card_layout, parent, false);
            return new UserViewHolder(layoutItem, listener, admin, this.user_id);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        User currentItem = userItemList.get(position);
        UserGroupCrossRef ref;

        for (int i = 0; i < balance.size(); i++) {

            if (balance.get(i).getUser_id() == currentItem.getId()) {
                ref = balance.get(i);
                if (currentItem.getId() == admin_id) {
                    AdminViewHolder adminViewHolder = (AdminViewHolder) holder;
                    adminViewHolder.userName.setText(currentItem.getName());
                    adminViewHolder.idUser = currentItem.getId();
                    adminViewHolder.posU =  holder.getAdapterPosition();
                    adminViewHolder.posR = i;
                    adminViewHolder.tv_userId.setText(String.valueOf(currentItem.getId()));
                    Utilities.getImage(String.valueOf(currentItem.getId()), adminViewHolder.userImage);
                } else {
                    UserViewHolder userViewHolder = (UserViewHolder) holder;
                    userViewHolder.userName.setText(currentItem.getName());
                    userViewHolder.user_amount.setText(String.valueOf(ref.getBalance()));
                    userViewHolder.idUser = currentItem.getId();
                    userViewHolder.posU =  holder.getAdapterPosition();
                    userViewHolder.posR = i;
                    userViewHolder.tv_userId.setText(String.valueOf(currentItem.getId()));
                    Utilities.getImage(String.valueOf(currentItem.getId()), userViewHolder.userImage);

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
