package com.example.splitit;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class DialogQrcodeFragment extends DialogFragment {
    private final QRGen qrg= new QRGen();
    TextView close_view ;
    TextView user_code_view ;
    ImageView qr_code;


    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.qr_code, container, false);
        close_view = view.findViewById(R.id.close_dialog);
        user_code_view = view.findViewById(R.id.tv_user_code);
        qr_code = view.findViewById(R.id.qr_code_view);

        String user_code;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        user_code = sharedPref.getString(getString(R.string.user_code),"0");
        user_code_view.setText(user_code);

        qrg.generate(requireActivity(),user_code, 200, qr_code);
        close_view.setOnClickListener(v -> Objects.requireNonNull(getDialog()).dismiss());
        return view;
    }
}
