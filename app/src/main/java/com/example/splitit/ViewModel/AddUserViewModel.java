package com.example.splitit.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.splitit.Database.RefRepository;
import com.example.splitit.Database.UserGroupCrossRef;

public class AddUserViewModel extends AndroidViewModel {
    private RefRepository refRep;

    public AddUserViewModel(@NonNull Application application) {
        super(application);
        refRep=new RefRepository(application);
    }

    public void addNewRef(UserGroupCrossRef ref){refRep.addRef(ref);}
}
