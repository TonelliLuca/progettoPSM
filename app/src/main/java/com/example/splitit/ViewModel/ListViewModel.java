package com.example.splitit.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.splitit.Database.GroupRepository;
import com.example.splitit.RecyclerView.User;
import com.example.splitit.RecyclerView.GroupItem;

import java.util.List;

public class ListViewModel extends AndroidViewModel {
    private final GroupRepository repository;
    private final MutableLiveData<GroupItem> itemSelected = new MutableLiveData<>();

    public ListViewModel(@NonNull Application application) {
        super(application);
        repository = new GroupRepository(application);

    }
    public void selected (GroupItem gp){itemSelected.setValue(gp); }

    public LiveData<GroupItem> getSelected(){return itemSelected;}

    public LiveData<List<GroupItem>> getGroupItems(String id){
        return repository.getGroupItemList(id);
    }

    public LiveData<List<GroupItem>> getGroupItemsNotComplete(String id){
        return repository.getGroupItemsNotComplete(id);
    }

    public LiveData<List<GroupItem>> getGroupItemsComplete(String id){
        return repository.getGroupItemsComplete(id);
    }
/*
    public GroupItem getGroupItem(int position){
        return groupItems.getValue() == null ? null : groupItems.getValue().get(position);
    }
*/


}
