package com.example.splitit.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface RefDAO {
    /*
    @Transaction
    @Query("SELECT * FROM user")
    public LiveData<List<GroupWithUsers>> getUsersWithGroup();

    @Transaction
    @Query("SELECT * FROM `group`")
    public LiveData<List<UsersWithGroup>> getGroupWithUsers();

*/
    @Transaction
    @Query(value = "SELECT * FROM 'group' WHERE group_id=:val ")
    LiveData<List<GroupWithUsers>> getUserFromGroup(String val);

    @Transaction
    @Query(value ="SELECT * FROM UserGroupCrossRef WHERE group_id=:val")
    LiveData<List<UserGroupCrossRef>> getAllUserBalance(String val);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addRef(UserGroupCrossRef u);

    @Delete
    void removeRef(UserGroupCrossRef u);

    @Transaction
    @Query(value = "SELECT * FROM UserGroupCrossRef WHERE group_id=:groupId AND user_id=:userId")
    UserGroupCrossRef searchSpecRef(String groupId,String userId);

    @Transaction
    @Query(value = "SELECT ref_balance FROM UserGroupCrossRef WHERE  user_id=:val AND ref_pay==1")
    LiveData<List<Double>> getAllPayments(String val);


}
