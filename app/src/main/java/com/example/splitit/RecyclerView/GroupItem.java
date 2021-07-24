package com.example.splitit.RecyclerView;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="group")
public class GroupItem {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "group_id")
    private long id;
    @ColumnInfo(name = "group_name")
    private String groupName;
    @ColumnInfo(name = "group_img")
    private String imageResource;
    @ColumnInfo(name = "group_complete")
    private boolean compete;
    @ColumnInfo(name = "group_admin")
    private long admin;




    public GroupItem(long id,String imageResource, String groupName,long admin,boolean compete){
        this.groupName = groupName;
        this.imageResource = imageResource;
        this.compete=compete;
        this.admin=admin;
        this.id=id;


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

    public long getAdmin() {
        return admin;
    }

    public void setAdmin(long admin) {
        this.admin = admin;
    }
}
