package com.example.splitit.OnlineDatabase;


public abstract class  OnlineDatabase {

    public static void execute(Runnable command) {
        new Thread(command).start();
    }

}
