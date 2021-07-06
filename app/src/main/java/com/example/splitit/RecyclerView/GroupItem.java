package com.example.splitit.RecyclerView;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="group")
public class GroupItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "group_id")
    private long id;
    @ColumnInfo(name = "group_name")
    private String groupName;
    @ColumnInfo(name = "group_img")
    private String imageResource;
    @ColumnInfo(name = "group_complete")
    private boolean compete;
    @ColumnInfo(name = "group_admin")
    private int admin;



    public GroupItem(String imageResource, String groupName){
        this.groupName = groupName;
        this.imageResource = imageResource;
        this.compete=false;
        this.admin=0;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isCompete() {
        return compete;
    }

    public void setCompete(boolean compete) {
        this.compete = compete;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }
}
