package com.example.splitit.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.splitit.RecyclerView.GroupItem;

import java.util.List;


public class GroupRepository {
    private GroupItemDAO groupItemDAO;
    private LiveData<List<GroupItem>> groupItemList;
    private LiveData<Long> lastId;

    public GroupRepository(Application application){
        SplititDatabase db = SplititDatabase.getDatabase(application);
        groupItemDAO = db.groupItemDAO();
        groupItemList = groupItemDAO.getGroupItems();
        lastId=groupItemDAO.getLastId();

    }

    public LiveData<List<GroupItem>> getGroupItemList(){return groupItemList;}

    public void addGroupItem(final GroupItem groupItem){

        SplititDatabase.databaseWriterExecutor.execute(new Runnable(){
            @Override
            public void run() {
                groupItemDAO.addGroupItem(groupItem);

            }
        });
    }

    public LiveData<Long> getLastId(){return lastId; }



}
