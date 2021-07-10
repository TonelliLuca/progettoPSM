package com.example.splitit.RecyclerView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<GroupViewHolder> {
    private static final int SIZE = 100;
    private List<GroupItem> userItemList=new ArrayList<>();;
    private List<GroupItem> userItemFiltered = new ArrayList<>();
    private Activity activity;
    private OnItemListener listener;

    public UserAdapter(Activity activity, OnItemListener listener){
        this.listener = listener;
        this.activity = activity;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card_layout, parent, false);
        return new GroupViewHolder(layoutItem,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        GroupItem currentItem = userItemList.get(position);


        String image_path = currentItem.getImageResource();

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

        holder.groupName.setText(currentItem.getGroupName());
        System.out.println(currentItem.getGroupName());
    }

    @Override
    public int getItemCount() {
        return userItemList.size();
    }

    public void setData(List<GroupItem> list) {
        this.userItemList = new ArrayList<>(list);
        this.userItemFiltered = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    public GroupItem getItemFiltered(int position){
        return userItemList.get(position);
    }
}
