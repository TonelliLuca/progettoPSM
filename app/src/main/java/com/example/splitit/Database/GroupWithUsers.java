package com.example.splitit.Database;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.splitit.RecyclerView.GroupItem;

import java.security.acl.Group;
import java.util.List;

public class GroupWithUsers {
    @Embedded public GroupItem group;
    @Relation(
            parentColumn="group_id",
            entityColumn="user_id",
            associateBy=@Junction(UserGroupCrossRef.class)
    )
    public List<User> users;
}
