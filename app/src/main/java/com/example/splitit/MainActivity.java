 package com.example.splitit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.splitit.OnlineDatabase.OnlineDatabase;
import com.google.android.material.navigation.NavigationView;
import java.util.HashMap;
import java.util.Map;

 public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



     private static final String FRAGMENT_TAG_HOME = "HomeFragment";
     Toolbar toolbar;
     DrawerLayout drawerLayout;
     NavigationView navigationView;
     FragmentManager mFragmentManager;
     ActionBarDrawerToggle toggle;
     ImageView headerImageView;
     TextView headerUserName;
     String user_id;
     String imageName;


     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

         SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
         user_id = sharedPref.getString(getString(R.string.user_id), "-1");

         /*declaration variables for navigation*/

         drawerLayout = findViewById(R.id.activity_main_layout);
         navigationView = findViewById(R.id.nav_view);
         toolbar = findViewById(R.id.toolbar);

         headerImageView = navigationView.getHeaderView(0).findViewById(R.id.header_user_image);
         OnlineDatabase.execute(getUserImageName());


         /*set action bar*/
         setSupportActionBar(toolbar);
         /*creation navigation bar menu*/
         navigationView.bringToFront();
         toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
         drawerLayout.addDrawerListener(toggle);
         toggle.syncState();


         navigationView.setCheckedItem(R.id.nav_home);

         navigationView.setNavigationItemSelectedListener(menuItem -> {
             int id = menuItem.getItemId();
             if (id == R.id.nav_dashboard) {
                 loadMenuFragment(new DetailsUserFragment());
             }else if(id == R.id.nav_logout){
                 Intent intentRegistration = new Intent(MainActivity.this, LoginActivity.class);
                 SharedPreferences sharedPref1 = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                 SharedPreferences.Editor editor = sharedPref1.edit();
                 editor.clear();
                 editor.apply();
                 finish();  //Kill the activity from which you will go to next activity
                 startActivity(intentRegistration);
             }else if(id == R.id.nav_bilancio){
                 Intent intentRegistration = new Intent(MainActivity.this, BalanceActivity.class);
                 startActivity(intentRegistration);
             }else if(id == R.id.nav_storico){
                 Intent intentRegistration = new Intent(MainActivity.this, StoreActivity.class);
                 startActivity(intentRegistration);
             }
             drawerLayout.closeDrawer(GravityCompat.START);
             return true;
         });

         if (savedInstanceState == null)
             Utilities.insertFragment(this, new HomeFragment(), FRAGMENT_TAG_HOME);
     }

     public void loadMenuFragment(Fragment fragment) {
         mFragmentManager = this.getSupportFragmentManager();
         FragmentTransaction transaction = mFragmentManager.beginTransaction();
         transaction.replace(R.id.activity_main_layout, fragment);
         transaction.addToBackStack(null);
         transaction.commit();
     }


     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.top_app_bar, menu);
         return true;
     }

     /*@Override
     public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);
         if (item.getItemId() == R.id.app_bar_option) {
             Intent intent = new Intent(this, SettingsActivity.class);
             this.startActivity(intent);
             return true;
         }
         return false;
     }*/

     @Override
     public void onResume() {
         super.onResume();
         Utilities.stop = false;
         Utilities.getImage(user_id, headerImageView);
     }


     @Override
     public void onBackPressed() {
         if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
             drawerLayout.closeDrawer(GravityCompat.START);
         } else {
             super.onBackPressed();
             Intent intent = new Intent(this, LoginActivity.class);
             //this.startActivity(intent);
             Utilities.stop = true;
             //finish();
         }
     }

    @Override
     public void onPause(){
         super.onPause();
         Utilities.stop = true;
     }


     public Runnable getUserImageName() {
         Runnable task = () -> {

             RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
             String URL = "http://"+Utilities.IP+"/splitit/comunication.php";

             //Create an error listener to handle errors appropriately.
             StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                 //This code is executed if the server responds, whether or not the response contains data.
                 //The String 'response' contains the server's response.

                 if (response.equals("failure")) {
                     Log.e("MAIN Activity", "failed");
                 } else {
                     String[] parts = response.split(":");
                     imageName = parts[0];
                     String userName = parts[1];
                     headerUserName = navigationView.getHeaderView(0).findViewById(R.id.header_user_name);
                     headerUserName.setText(userName);
                 }
             }, error -> {
                 //This code is executed if there is an error.
                 Log.e("MAIN Activity", "error response");

             }) {
                 protected Map<String, String> getParams() {
                     Map<String, String> MyData = new HashMap<>();
                     MyData.put("id", String.valueOf(user_id)); //Add the data you'd like to send to the server.
                     MyData.put("request", String.valueOf(6));
                     return MyData;
                 }
             };

             MyRequestQueue.add(MyStringRequest);

         };
         return task;


     }

     @Override
     public boolean onNavigationItemSelected(@NonNull MenuItem item) {
         return false;
     }
 }

 /*TODO
       inserire una scroll view in profilo utente (?)

  */