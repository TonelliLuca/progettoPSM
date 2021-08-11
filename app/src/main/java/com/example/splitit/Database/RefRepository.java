package com.example.splitit.Database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RefRepository {
    private final RefDAO refDAO;
    private LiveData<List<UsersWithGroup>> userList;
    private LiveData<List<GroupWithUsers>> groupList;
    private LiveData<List<GroupWithUsers>> allUserInGroup;
    private LiveData<UserGroupCrossRef> refFound;
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

    public LiveData<List<UserGroupCrossRef>> getAllUsersBalance(long val){
        return refDAO.getAllUserBalance(String.valueOf(val));
    }

    public void removeRef(UserGroupCrossRef ref){
        SplititDatabase.databaseWriterExecutor.execute(new Runnable(){
            @Override
            public void run() {
                refDAO.removeRef(ref);
            }
        });
    }
    
    public UserGroupCrossRef searchSpecRef(long groupId,long userId){
        Log.e("RefRepo","find ref group: "+ groupId+ " user: "+userId);

        return refDAO.searchSpecRef(String.valueOf(groupId),String.valueOf(userId));

    }

    public LiveData<List<Double>> getAllPayments(String userId){return refDAO.getAllPayments(userId);}

    public void payGroup(String id){refDAO.payGroup(id);}

}
