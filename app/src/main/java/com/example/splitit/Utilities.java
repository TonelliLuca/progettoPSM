package com.example.splitit;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Utilities {

    static void insertFragment(AppCompatActivity activity, Fragment fragment, String tag) {
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

}
