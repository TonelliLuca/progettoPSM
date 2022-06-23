package com.example.splitit.RecyclerView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;
import com.example.splitit.Utilities;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupViewHolder> {
    private static final int SIZE = 1000;
    private List<GroupItem> groupItemList=new ArrayList<>();
    private List<GroupItem> groupItemFiltered = new ArrayList<>();
    private final Activity activity;
    private final OnItemListener listener;


    public GroupAdapter(Activity activity, OnItemListener listener){
        this.listener = listener;
        this.activity = activity;

    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_layout, parent, false);
        return new GroupViewHolder(layoutItem,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        GroupItem currentItem = groupItemList.get(position);
        String image_path = currentItem.getImageResource();
        Utilities.getGroupImage(String.valueOf(currentItem.getGroupName()), holder.groupImage);
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
        return groupItemList.size();
    }

    public void setData(List<GroupItem> list) {
        this.groupItemList = new ArrayList<>(list);
        this.groupItemFiltered = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    public GroupItem getItemFiltered(int position){
        return groupItemList.get(position);
    }
}
