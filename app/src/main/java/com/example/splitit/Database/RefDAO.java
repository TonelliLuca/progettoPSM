package com.example.splitit.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface RefDAO {
    @Transaction
    @Query("SELECT * FROM user")
    public LiveData<List<UsersWithGroup>> getUsersWithGroup();
    @Transaction
    @Query("SELECT * FROM `group`")
    public LiveData<List<GroupWithUsers>> getGroupWithUsers();


    @Transaction
    @Query("SELECT * FROM user WHERE 'group_id'=:val")
    public LiveData<List<UsersWithGroup>> getUserFromGroup(int val);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addRef(UserGroupCrossRef u);
}
