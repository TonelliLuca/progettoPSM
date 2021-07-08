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
    private RefRepository refRep;

    public AddUserViewModel(@NonNull Application application) {
        super(application);
        refRep=new RefRepository(application);
    }

    public void addNewRef(UserGroupCrossRef ref){refRep.addRef(ref);}

    public LiveData<List<GroupWithUsers>> searchUsers(long val){
        return refRep.searchUsers();
    }
}
