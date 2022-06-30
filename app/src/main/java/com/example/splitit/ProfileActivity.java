package com.example.splitit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.splitit.OnlineDatabase.OnlineDatabase;
import com.example.splitit.ViewModel.AddUserViewModel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {


    ImageView userImageView;
    private static final String ROOT_URL = "http://"+Utilities.IP+"/splitit/uploadImage.php";
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST =1 ;
    private Bitmap bitmap;
    private String user_id;
    private String filePath;
    private TextView tv_user_name;
    private TextView tv_email;
    private TextView tv_user_name_small;
    private TextView tv_user_code;
    private TextView groupCount;
    private TextView totPay;
    private TextView totRec;
    private AddUserViewModel userInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = sharedPref.getString(getString(R.string.user_id), "-1");
        setContentView(R.layout.activity_profile);
        groupCount = findViewById(R.id.group_count);
        totRec = findViewById(R.id.tot_rec);
        totPay = findViewById(R.id.tot_pay);
        userInfo = new ViewModelProvider((ViewModelStoreOwner) this).get(AddUserViewModel.class);
        userInfo.countGroupToComplete(user_id).observe((LifecycleOwner) this, value -> {

            groupCount.setText(value==null?"0":value);

        });
        userInfo.totalCountPayments(user_id).observe((LifecycleOwner) this, value -> {

            totPay.setText(value==null?"0€":value+"€");

        });
        userInfo.totalCountReceived(user_id).observe((LifecycleOwner) this, value -> {

            totRec.setText(value==null?"0€":value+"€");

        });


        //initializing views
        userImageView =  findViewById(R.id.user_image);

        Utilities.getImage( sharedPref.getString(getString(R.string.user_id), "-1"), userImageView);

        tv_user_name = findViewById(R.id.tv_user_name);
        tv_user_name.setText(sharedPref.getString(getString(R.string.user_name), "-1"));

        tv_user_name_small = findViewById(R.id.user_name_small);
        tv_user_name_small.setText(sharedPref.getString(getString(R.string.user_name), "-1"));

        tv_email = findViewById(R.id.email);
        tv_email.setText(sharedPref.getString(getString(R.string.user_email), "-1"));

        tv_user_code = findViewById(R.id.user_code);
        tv_user_code.setText(sharedPref.getString(getString(R.string.user_code), "-1"));
        //adding click listener to button
        findViewById(R.id.user_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE))) {

                    } else {
                        ActivityCompat.requestPermissions(ProfileActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }
                } else {

                    showFileChooser();
                }


            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            filePath = getPath(picUri);
            if (filePath != null) {
                try {

                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                    uploadBitmap(bitmap);
                    OnlineDatabase.execute(setUserImageName());
                    userImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(
                        ProfileActivity.this,"no image selected",
                        Toast.LENGTH_LONG).show();
            }
        }

    }
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        int index=cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        String path = cursor.getString(index);
        cursor.close();

        return path;
    }


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ROOT_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        //System.out.println(response.toString());
                        //JSONArray obj = new JSONArray(new String(response.data));
                        String string = new String(response.data);
                        Toast.makeText(getApplicationContext(),"Caricamento completato", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println( error.getMessage());
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("ProfileActivity",""+error.getMessage());
                        Log.e("ProfileActivity", "Failed with error msg:\t" + error.getMessage());
                        Log.e("ProfileActivity", "Error StackTrace: \t" + error.getStackTrace());
                        // edited here
                        try {
                            byte[] htmlBodyBytes = error.networkResponse.data;

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        if (error.getMessage() == null){

                        }
                    }
                }) {


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                //System.currentTimeMillis()
                params.put("image", new DataPart(user_id + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);

    }

    public Runnable setUserImageName(){
        Runnable task = () -> {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            user_id = sharedPref.getString(getString(R.string.user_id),"-1");

            RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

            String URL = "http://"+Utilities.IP+"/splitit/comunication.php";

            //Create an error listener to handle errors appropriately.
            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.

                if(response.equals("failure")){
                    Log.e("ProfileActivity","failed");
                }else{

                }
            }, error -> {
                //This code is executed if there is an error.
                Log.e("ProfileActivity","error response");

            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<>();
                    MyData.put("id", String.valueOf(user_id)); //Add the data you'd like to send to the server.
                    MyData.put("img_name", String.valueOf(user_id)); //Add the data you'd like to send to the server.
                    MyData.put("request",String.valueOf(9));
                    return MyData;
                }
            };

            MyRequestQueue.add(MyStringRequest);

        };
        return task;

    }




}