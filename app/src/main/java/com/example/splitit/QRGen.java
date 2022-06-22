package com.example.splitit;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


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


