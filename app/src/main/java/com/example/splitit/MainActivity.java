 package com.example.splitit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

 public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String FRAGMENT_TAG_HOME = "HomeFragment";
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*declaration variables for navigation*/
        drawerLayout = findViewById(R.id.activity_main_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        /*set action bar*/
        setSupportActionBar(toolbar);
        /*creation navigation bar menu*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setCheckedItem(R.id.nav_home);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                    if(id == R.id.nav_dashboard) {
                        loadMenuFragment(new DetailsUserFragment());
                    }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
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
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        Log.e("Menu Creation", "Menu created");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        super.onOptionsItemSelected(item);
        Log.e("Intent menu", "Starting menu intent "+item.getItemId());
        if(item.getItemId()==R.id.app_bar_option){
            Intent intent = new Intent(this, SettingsActivity.class);
            this.startActivity(intent);
            return true;
        }
        return false;
    }


    @Override
    public void onBackPressed(){
        System.out.println(mFragmentManager.getBackStackEntryCount());
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else if (mFragmentManager.getBackStackEntryCount() > 0) {
            mFragmentManager.popBackStackImmediate();
        }else{
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}