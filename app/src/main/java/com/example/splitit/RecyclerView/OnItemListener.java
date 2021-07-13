package com.example.splitit.RecyclerView;

/**
 * Interface to manage the listener for the click on an element of the RecyclerViewGroup
 */
public interface OnItemListener{
    void onItemClick(int position);
    void onDelete(long id,int posu,int posr);
}