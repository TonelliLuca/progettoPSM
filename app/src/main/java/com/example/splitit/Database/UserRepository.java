package com.example.splitit.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.splitit.RecyclerView.User;

import java.util.List;


public class UserRepository {
    private final UserDAO userDAO;
    private final LiveData<List<User>> userList;
    public UserRepository(Application application){
        SplititDatabase db = SplititDatabase.getDatabase(application);
        userDAO = db.userDAO();
        userList = userDAO.getUsers();
    }

    public LiveData<List<User>> getUserList(){return userList;}

    public void addUser(final User user){
        SplititDatabase.databaseWriterExecutor.execute(() -> userDAO.addUser(user));
    }

    public void deleteUser(final User user){
        SplititDatabase.databaseWriterExecutor.execute(() -> userDAO.removeUser(user));
    }


}