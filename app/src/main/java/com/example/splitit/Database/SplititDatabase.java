package com.example.splitit.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.splitit.RecyclerView.GroupItem;
import com.example.splitit.RecyclerView.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database( version=2,entities = {GroupItem.class, User.class, UserGroupCrossRef.class})
public abstract class SplititDatabase extends RoomDatabase {
    private static volatile SplititDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS=4;


    static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract GroupItemDAO groupItemDAO();
    public abstract UserDAO userDAO();
    public abstract RefDAO refDAO();
    static SplititDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (SplititDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SplititDatabase.class, "database").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
