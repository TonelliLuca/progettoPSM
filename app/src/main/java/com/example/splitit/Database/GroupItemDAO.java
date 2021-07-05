package com.example.splitit.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.splitit.RecyclerView.GroupItem;

import java.util.List;

@Dao
public interface GroupItemDAO{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addGroupItem(GroupItem gp);
    @Transaction
    @Query("SELECT * FROM `group` ORDER BY group_id DESC ")
    LiveData<List<GroupItem>> getCardItems();
}
