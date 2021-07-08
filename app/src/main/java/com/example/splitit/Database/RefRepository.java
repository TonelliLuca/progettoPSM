package com.example.splitit.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.splitit.RecyclerView.GroupItem;

import java.util.List;

public class RefRepository {
    private RefDAO refDAO;
    private LiveData<List<UsersWithGroup>> userList;
    private LiveData<List<GroupWithUsers>> groupList;
    private LiveData<List<GroupWithUsers>> allUserInGroup;
    public RefRepository(Application application){
        SplititDatabase db = SplititDatabase.getDatabase(application);
        refDAO = db.refDAO();
        //userList = refDAO.getUsersWithGroup();
        //groupList= refDAO.getGroupWithUsers();
    }

    public LiveData<List<UsersWithGroup>> getUsersGroupList(){return userList;}

    public void addRef(final UserGroupCrossRef ref){
        SplititDatabase.databaseWriterExecutor.execute(new Runnable(){
            @Override
            public void run() {
                refDAO.addRef(ref);
            }
        });
    }

    public LiveData<List<GroupWithUsers>> searchUsers(long val){
        return refDAO.getUserFromGroup(String.valueOf(val));
    }

    public void removeRef(UserGroupCrossRef ref){
        SplititDatabase.databaseWriterExecutor.execute(new Runnable(){
            @Override
            public void run() {
                refDAO.removeRef(ref);
            }
        });
    }



}
