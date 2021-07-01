package com.example.splitit;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class QRGen {
    private Bitmap qrcode=null;
    private ImageView qrView;



    public void generate(Activity activity, String inputValue, int smallerDimension, ImageView image){
        QRGEncoder qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);
        qrgEncoder.setColorBlack(Color.BLACK);
        qrgEncoder.setColorWhite(Color.TRANSPARENT);
        // Getting QR-Code as Bitmap
        this.qrcode = qrgEncoder.getBitmap();
        image.setImageBitmap(this.qrcode);
    }

    public Bitmap getQrCode(){
        return this.qrcode;
    }




}


