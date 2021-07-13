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
    private String query = "";

    /*public static Runnable addUser(User user, String pass, String code){
        Runnable task = () -> {
            try {
                URL url = new URL("http://127.0.0.1/splitit/registration.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                OutputStream outputStream = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("nome", "UTF-8")+"="+URLEncoder.encode(user.getName(), "UTF-8")
                        +"&&"+URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(user.getEmail(), "UTF-8")
                        +"&&"+URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(pass, "UTF-8")
                        +"&&"+URLEncoder.encode("img", "UTF-8")+"="+URLEncoder.encode("ic_baseline:android_24", "UTF-8")
                        +"&&"+URLEncoder.encode("code", "UTF-8")+"="+URLEncoder.encode(code, "UTF-8");
                writer.write(data);
                writer.flush();
                writer.close();

            } catch (MalformedURLException e) {
                e.getMessage();
            } catch (IOException e) {
                e.getMessage();
            }
        };
        return task;
    }*/

    public static void execute(Runnable command) {
        new Thread(command).start();
    }


}
