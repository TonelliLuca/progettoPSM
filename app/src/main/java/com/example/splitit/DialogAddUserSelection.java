package com.example.splitit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.google.zxing.integration.android.IntentIntegrator;

import java.util.Objects;

public class DialogAddUserSelection extends DialogFragment{

    private EditText addCode;
    private Button addQrCode;
    private Button submit;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
        View view = inflater.inflate(R.layout.add_user_selection, container, false);
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        if (activity != null) {
            addCode = view.findViewById(R.id.btn_add_code);
            addQrCode = view.findViewById(R.id.btn_qr_reader);
            submit = view.findViewById(R.id.btn_submit);

            addQrCode.setOnClickListener(v -> {
                IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
                intentIntegrator.setPrompt("Scan a barcode or QR Code");
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.initiateScan();
            });
        }
    }




}
