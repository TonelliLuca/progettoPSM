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

    private GroupRepository repository;
    private final MutableLiveData<Bitmap> imageBitmap = new MutableLiveData<>();

    public AddViewModel(@NonNull Application application) {
        super(application);
        repository = new GroupRepository(application);
    }

    public void setImageBitmap(Bitmap bitmap){imageBitmap.setValue(bitmap);}

    public LiveData<Bitmap> getBitmap(){return imageBitmap;}

    public long addGroupItem(GroupItem item){return repository.addGroupItem(item);}

}