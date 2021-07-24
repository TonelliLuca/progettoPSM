package com.example.splitit.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"user_id","group_id"})
public class UserGroupCrossRef {
    @ColumnInfo(name="user_id")
    private long user_id;
    @ColumnInfo(name="group_id")
    private long group_id;
    @ColumnInfo(name="ref_pay")
    private boolean pay = false;
    @ColumnInfo(name="ref_balance")
    private double balance=0;


    public UserGroupCrossRef(long user_id,long group_id,boolean pay, double balance){
        this.user_id=user_id;
        this.group_id=group_id;
        this.pay=pay;
        this.balance=balance;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public long getUserId() {
        return user_id;
    }

    public void setUserId(long userId) {
        this.user_id = userId;
    }

    public long getGroupId() {
        return group_id;
    }

    public void setGroupId(long groupId) {
        this.group_id = groupId;
    }

    public boolean isPay() {
        return pay;
    }

    public void setPay(boolean pay) {
        this.pay = pay;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }




}
