package com.example.splitit.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.splitit.RecyclerView.GroupItem;

import java.util.List;


public class GroupRepository {
    private final GroupItemDAO groupItemDAO;

    private final LiveData<Long> lastId;

    public GroupRepository(Application application){
        SplititDatabase db = SplititDatabase.getDatabase(application);
        groupItemDAO = db.groupItemDAO();

        lastId=groupItemDAO.getLastId();

    }

    public LiveData<List<GroupItem>> getGroupItemList(String id){return groupItemDAO.getGroupItems(id);}

    public void addGroupItem(final GroupItem groupItem){

        SplititDatabase.databaseWriterExecutor.execute(new Runnable(){
            @Override
            public void run() {
                groupItemDAO.addGroupItem(groupItem);

            }
        });
    }

    public LiveData<Long> getLastId(){return lastId; }

    public void removeGroup(GroupItem g){
        SplititDatabase.databaseWriterExecutor.execute(new Runnable(){
            @Override
            public void run() {
                groupItemDAO.removeGroup(g);

            }
        });
    }

    public LiveData<Long> getGroupAdmin(String id){return groupItemDAO.getGroupAdmin(id);}


}
