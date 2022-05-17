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

    @Transaction
    @Query(value = "UPDATE UserGroupCrossRef SET ref_pay = 1 WHERE group_id=:id")
    void payGroup(String id);

    @Transaction
    @Query(value = "SELECT COUNT(group_id) FROM UserGroupCrossRef WHERE user_id =:val AND ref_pay==0 ")
    LiveData<String> countGroupToComplete(String val);

    @Transaction
    @Query(value = "SELECT SUM(ref_balance) from UserGroupCrossRef JOIN `group` ON `group`.group_id = UserGroupCrossRef.group_id WHERE  user_id=:val AND ref_pay==1")
    LiveData<String> totalCountPayments(String val);

    @Transaction
    @Query(value = "SELECT SUM(ref_balance) from UserGroupCrossRef JOIN `group` ON `group`.group_id = UserGroupCrossRef.group_id WHERE   group_admin=:val AND group_complete==1")
    LiveData<String> totalCountReceived(String val);


}
