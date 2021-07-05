package com.example.splitit.Database;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.splitit.RecyclerView.GroupItem;


import java.util.List;

public class UsersWithGroup {
    @Embedded public User user;
    @Relation(
            parentColumn="user_id",
            entityColumn="group_id",
            associateBy = @Junction(UserGroupCrossRef.class)
    )
    public List<GroupItem> groups;
}
