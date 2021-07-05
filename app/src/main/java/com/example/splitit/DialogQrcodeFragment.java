package com.example.splitit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogQrcodeFragment extends DialogFragment {
    private QRGen qrg= new QRGen();
    TextView textView ;
    ImageView imageView;


    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
        View view = inflater.inflate(R.layout.qr_code, container, false);
        textView = view.findViewById(R.id.close_dialog);
        imageView = view.findViewById(R.id.qr_code_view);

        qrg.generate(requireActivity(),"CIAO CIAO CIAO", 200, imageView);
        textView.setOnClickListener(v -> getDialog().dismiss());
        return view;
    }
}
