package com.example.splitit.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.splitit.Database.GroupWithUsers;
import com.example.splitit.Database.RefRepository;
import com.example.splitit.Database.UserGroupCrossRef;
import com.example.splitit.Database.UsersWithGroup;

import java.util.List;

public class AddUserViewModel extends AndroidViewModel {
    private final RefRepository refRep;

    public AddUserViewModel(@NonNull Application application) {
        super(application);
        refRep=new RefRepository(application);
    }

    public void addNewRef(UserGroupCrossRef ref){refRep.addRef(ref);}

    public LiveData<List<GroupWithUsers>> searchUsers(long val){
        return refRep.searchUsers(val);
    }
    public LiveData<List<UserGroupCrossRef>> getAllUsersBalance(long val){return refRep.getAllUsersBalance(val);}
    public void removeRef(UserGroupCrossRef ref){refRep.removeRef(ref);}
    public UserGroupCrossRef searchSpecRef(long groupId,long userId){return refRep.searchSpecRef(groupId,userId);};
}
