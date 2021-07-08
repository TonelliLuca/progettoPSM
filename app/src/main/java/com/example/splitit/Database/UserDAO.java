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
public interface UserDAO{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addUser(User u);
    @Transaction
    @Query("SELECT * FROM `user` ORDER BY user_id DESC ")
    LiveData<List<User>> getUsers();
}