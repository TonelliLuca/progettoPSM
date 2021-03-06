package com.example.splitit.ViewModel;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.splitit.Database.GroupRepository;
import com.example.splitit.RecyclerView.GroupItem;


public class AddViewModel extends AndroidViewModel {

    private final GroupRepository repository;
    private final MutableLiveData<Bitmap> imageBitmap = new MutableLiveData<>();
    private final LiveData<Long> lastId;
    public AddViewModel(@NonNull Application application) {
        super(application);
        repository = new GroupRepository(application);
        lastId=repository.getLastId();
    }

    public void setImageBitmap(Bitmap bitmap){imageBitmap.setValue(bitmap);}

    public LiveData<Bitmap> getBitmap(){return imageBitmap;}

    public void addGroupItem(GroupItem item){repository.addGroupItem(item);}
    public LiveData<Long> getLastId(){return lastId; }
    public LiveData<Long> getGroupAdmin(String id){ return repository.getGroupAdmin(id);}
    public void removeGroup(GroupItem g){repository.removeGroup(g);}


}
