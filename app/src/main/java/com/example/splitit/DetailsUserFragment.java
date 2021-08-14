package com.example.splitit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DetailsUserFragment extends Fragment {

    private LinearLayout btn_profile;
    private LinearLayout btn_balance;
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
            btn_profile = view.findViewById(R.id.btn_profile);
            btn_balance = view.findViewById(R.id.btn_balance);
            iw_user_img_header = view.findViewById(R.id.user_img_header);
            iw_user_img_layout = view.findViewById(R.id.user_img_layout);

            Utilities.getImage( sharedPref.getString(getString(R.string.user_id), "-1"), iw_user_img_header);
            Utilities.getImage( sharedPref.getString(getString(R.string.user_id), "-1"), iw_user_img_layout);

            btn_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentRegistration = new Intent(activity, ProfileActivity.class);
                    startActivity(intentRegistration);
                    activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
            });

            btn_balance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentRegistration = new Intent(activity, BalanceActivity.class);
                    startActivity(intentRegistration);
                    activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
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
