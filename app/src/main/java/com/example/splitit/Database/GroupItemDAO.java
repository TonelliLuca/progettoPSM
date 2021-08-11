package com.example.splitit.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.splitit.RecyclerView.GroupItem;

import java.util.List;

@Dao
public interface GroupItemDAO{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long addGroupItem(GroupItem gp);
    @Transaction
    @Query("SELECT `group`.group_id,`group`.group_name,`group`.group_img,`group`.group_admin,`group`.group_complete FROM `group` JOIN UserGroupCrossRef ON UserGroupCrossRef.group_id=`group`.group_id WHERE UserGroupCrossRef.user_id=:val ORDER BY `group`.group_id DESC ")
    LiveData<List<GroupItem>> getGroupItems(String val);

    @Transaction
    @Query("SELECT group_id FROM 'group' ORDER BY group_id DESC LIMIT 1")
    LiveData<Long> getLastId();

    @Delete
    void removeGroup(GroupItem g);

    @Transaction
    @Query("SELECT group_admin FROM 'group' WHERE group_id=:id")
    LiveData<Long> getGroupAdmin(String id);
}
