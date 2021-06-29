package com.example.splitit.RecyclerView;

public class GroupItem {
    private String groupName;
    private String imageResource;

    public GroupItem(String imageResource, String groupName){
        this.groupName = groupName;
        this.imageResource = imageResource;
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


}
