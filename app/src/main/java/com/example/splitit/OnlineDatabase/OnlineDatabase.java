package com.example.splitit.OnlineDatabase;

import com.example.splitit.RecyclerView.User;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Executor;

public abstract class  OnlineDatabase {

    public static void execute(Runnable command) {
        new Thread(command).start();
    }

}
