package com.example.splitit;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.ContentView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class QRGen {
    private Bitmap qrcode=null;
    private ImageView qrView;



    public void generate(Activity activity, String inputValue, int smallerDimension){
        QRGEncoder qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);
        qrgEncoder.setColorBlack(Color.RED);
        qrgEncoder.setColorWhite(Color.BLUE);
        // Getting QR-Code as Bitmap
        this.qrcode = qrgEncoder.getBitmap();
        qrView=activity.findViewById(R.id.QRCodeView);
        qrView.setImageBitmap(this.qrcode);
        this.save(activity);
    }

    public Bitmap getQrCode(){
        return this.qrcode;
    }

    private void save(Activity activity){
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            try {
                // Save with location, value, bitmap returned and type of Image(JPG/PNG).
                QRGSaver qrgSaver = new QRGSaver();
                Boolean save=qrgSaver.save(Environment.getExternalStorageDirectory().getPath()+"/QRCode/".trim(), "myCode", this.qrcode, QRGContents.ImageType.IMAGE_JPEG);
                String result = save ? "Image Saved" : "Image Not Saved";
                Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }


    }

}


