package com.example.splitit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class DetailsUserFragment extends Fragment {

    private ImageView iw_user_img_layout;
    private ImageView iw_user_img_header;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.detailed_user, container, false);

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FragmentActivity activity = getActivity();
        if (activity != null) {

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            LinearLayout btn_profile = view.findViewById(R.id.btn_profile);
            LinearLayout btn_balance = view.findViewById(R.id.btn_balance);
            LinearLayout btn_logout = view.findViewById(R.id.btn_logout);
            LinearLayout btn_store = view.findViewById(R.id.btn_store);

            TextView tv_user_name = view.findViewById(R.id.tv_user_name);
            iw_user_img_header = view.findViewById(R.id.user_img_header);
            iw_user_img_layout = view.findViewById(R.id.user_img_layout);

            tv_user_name.setText(sharedPref.getString(getString(R.string.user_name), "-1"));
            Utilities.getImage( sharedPref.getString(getString(R.string.user_id), "-1"), iw_user_img_header);
            Utilities.getImage( sharedPref.getString(getString(R.string.user_id), "-1"), iw_user_img_layout);


            btn_profile.setOnClickListener(v -> {
                Intent intentRegistration = new Intent(activity, ProfileActivity.class);
                startActivity(intentRegistration);
                activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            });

            btn_balance.setOnClickListener(v -> {
                Intent intentRegistration = new Intent(activity, BalanceActivity.class);
                startActivity(intentRegistration);
                activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            });

            btn_store.setOnClickListener(v -> {
                Intent intentRegistration = new Intent(activity, StoreActivity.class);
                startActivity(intentRegistration);
                activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            });

            btn_logout.setOnClickListener(v -> {
                Intent intentRegistration = new Intent(activity, LoginActivity.class);
                SharedPreferences sharedPref1 = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPref1.edit();
                editor.clear();
                editor.apply();
                activity.finish();  //Kill the activity from which you will go to next activity
                startActivity(intentRegistration);
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        Utilities.getImage( sharedPref.getString(getString(R.string.user_id), "-1"), iw_user_img_header);
        Utilities.getImage( sharedPref.getString(getString(R.string.user_id), "-1"), iw_user_img_layout);
    }
}
