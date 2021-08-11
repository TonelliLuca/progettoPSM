package com.example.splitit.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.splitit.Database.GroupWithUsers;
import com.example.splitit.Database.RefRepository;
import com.example.splitit.Database.UserGroupCrossRef;
import com.example.splitit.Database.UserRepository;
import com.example.splitit.Database.UsersWithGroup;
import com.example.splitit.RecyclerView.User;

import java.util.List;

public class AddUserViewModel extends AndroidViewModel {
    private final RefRepository refRep;
    private final UserRepository userRep;

    public AddUserViewModel(@NonNull Application application) {
        super(application);
        refRep=new RefRepository(application);
        userRep=new UserRepository(application);
    }

    public void addNewRef(UserGroupCrossRef ref){refRep.addRef(ref);}

    public LiveData<List<GroupWithUsers>> searchUsers(long val){
        return refRep.searchUsers(val);
    }
    public LiveData<List<UserGroupCrossRef>> getAllUsersBalance(long val){return refRep.getAllUsersBalance(val);}
    public void removeRef(UserGroupCrossRef ref){refRep.removeRef(ref);}
    public UserGroupCrossRef searchSpecRef(long groupId,long userId){return refRep.searchSpecRef(groupId,userId);}

    public void addUser(User u){userRep.addUser(u);}
    public LiveData<List<Double>> getAllPayments(String userId){return refRep.getAllPayments(userId);}
}
