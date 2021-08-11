package com.example.splitit;



import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.splitit.Database.UserGroupCrossRef;
import com.example.splitit.RecyclerView.GroupItem;
import com.example.splitit.RecyclerView.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

public class Utilities {

    public static void insertFragment(AppCompatActivity activity, Fragment fragment, String tag) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container_view with this fragment,
        transaction.replace(R.id.fragment_container_view, fragment, tag);

        //add the transaction to the back stack so the user can navigate back
        if (!(fragment instanceof HomeFragment) && !(fragment instanceof SettingsFragment)) {
            transaction.addToBackStack(tag);
        }

        // Commit the transaction
        transaction.commit();
    }

    static void setUpToolbar(AppCompatActivity activity, String title){
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setTitle(title);

        if(activity.getSupportActionBar()==null){
            activity.setSupportActionBar(toolbar);
        }
    }

    static ArrayList<GroupItem> parseGroupItems(String s){
        ArrayList<GroupItem> list= new ArrayList<>();
        Log.e("Utility","parse GroupItems String s: "+s);
        try {
            JSONArray jArray= new JSONArray(s);
            Log.e("Utility","JSONArray length: "+jArray.length());
            for (int i=0; i < jArray.length(); i++)
            {
                try {
                    JSONObject oneObject = jArray.getJSONObject(i);
                    // Pulling items from the array
                    String name = oneObject.getString("nome");
                    String img = oneObject.getString("img");
                    long id = oneObject.getLong("id");
                    long admin = oneObject.getLong("admin");
                    boolean completo = Integer.parseInt(oneObject.getString("completo")) != 0;
                    Log.e("Utility","Row object params: nome:"+name+" img:"+img+" id:"+id+" admin:"+admin+" completo:"+ completo);
                    list.add(new GroupItem(id,img,name,admin,completo));
                } catch (JSONException e) {
                    Log.e("Utility",e.getMessage());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("Utility","Output list length: "+list.size());
        return list;
    }

    static ArrayList<UserGroupCrossRef> parseUserGroupCrossRef(String  s){
        ArrayList<UserGroupCrossRef> list= new ArrayList<>();
        Log.e("Utility","parse UserGroupCrossRef String s: "+s);
        try{
            JSONArray jArray= new JSONArray(s);
            Log.e("Utility","JSONArray length: "+jArray.length());
            for (int i=0; i < jArray.length(); i++){
                try {
                    JSONObject oneObject = jArray.getJSONObject(i);
                    // Pulling items from the array
                    double balance = oneObject.getDouble("bilancio");
                    long id_user = oneObject.getLong("idUtente");
                    long id_group = oneObject.getLong("idGruppo");
                    boolean pay = Integer.parseInt(oneObject.getString("pagato")) != 0;
                    Log.e("Utility","Row object params: user_id:"+id_user+" group_id:"+id_group+" pagato:"+pay+" bilancio:"+balance);
                    list.add(new UserGroupCrossRef(id_user,id_group,pay,balance));
                } catch (JSONException e) {
                    Log.e("Utility",e.getMessage());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    static ArrayList<User> parseUser(String s){
        ArrayList<User> list = new ArrayList<>();
        Log.e("UtilityU","parse User String s: "+s);
        try{
            JSONArray jArray= new JSONArray(s);
            Log.e("UtilityU","JSONArray length: "+jArray.length());
            for (int i=0; i < jArray.length(); i++){
                try {
                    JSONObject oneObject = jArray.getJSONObject(i);
                    // Pulling items from the array

                    long id = oneObject.getLong("id");
                    String name = oneObject.getString("nome");
                    String email = oneObject.getString("email");
                    String img = oneObject.getString("img");
                    String code = oneObject.getString("code");
                    Log.e("UtilityU","Row object params: id:"+id+" nome:"+name+" email:"+email+" img:"+img+" code:"+code);
                    list.add(new User(id,name,email,code,img));
                } catch (JSONException e) {
                    Log.e("UtilityU",e.getMessage());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("UtilityU",e.getMessage());

        }
        return list;

    }


}
