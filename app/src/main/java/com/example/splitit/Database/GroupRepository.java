package com.example.splitit.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.splitit.RecyclerView.GroupItem;

import java.util.List;


public class GroupRepository {
    private GroupItemDAO groupItemDAO;
    private LiveData<List<GroupItem>> groupItemList;
    private long id;
    public GroupRepository(Application application){
        SplititDatabase db = SplititDatabase.getDatabase(application);
        groupItemDAO = db.groupItemDAO();
        groupItemList = groupItemDAO.getGroupItems();
    }

    public LiveData<List<GroupItem>> getGroupItemList(){return groupItemList;}

    public long addGroupItem(final GroupItem groupItem){

        SplititDatabase.databaseWriterExecutor.execute(new Runnable(){
            @Override
            public void run() {
                setVal(groupItemDAO.addGroupItem(groupItem));

            }
        });
        return id;
    }

    public void setVal(long val){
        id=val;
    }

}
