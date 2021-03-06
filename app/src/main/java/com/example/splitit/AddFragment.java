package com.example.splitit;

import static android.widget.Toast.makeText;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.splitit.OnlineDatabase.OnlineDatabase;
import com.example.splitit.ViewModel.AddUserViewModel;
import com.example.splitit.ViewModel.AddViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddFragment extends DialogFragment {

    private static final String ROOT_URL = "http://"+Utilities.IP+"/splitit/uploadGroupImage.php";
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST =1 ;
    ImageView groupImage;
    private Bitmap groupBitmap;
    private EditText nameText;
    private long lastId=0;
    private long userId;
    private String filePath;

    RequestQueue requestQueue;




    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle sevedInstanceState){
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.add_group, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        final Activity activity=getActivity();

        if(activity!=null){

            // Instantiate the cache
            Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 10240 * 10240); // 10MB cap
            // Set up the network to use HttpURLConnection as the HTTP client.
            Network network = new BasicNetwork(new HurlStack());
            // Instantiate the RequestQueue with the cache and network.
            requestQueue = new RequestQueue(cache, network);
            requestQueue.start();

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            userId = Long.parseLong(sharedPref.getString(getString(R.string.user_id),"-1"));
            AddViewModel addViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(AddViewModel.class);
            AddUserViewModel addUserRef = new ViewModelProvider((ViewModelStoreOwner) activity).get(AddUserViewModel.class);
            Utilities.setUpToolbar((AppCompatActivity) activity, "Make a group");
            nameText = view.findViewById(R.id.groupNameAdd);
            groupImage = view.findViewById(R.id.image_group_add);

            view.findViewById(R.id.buttonAdd).setOnClickListener(v -> {


                if (checkAdd()) {
                    OnlineDatabase.execute(addGroupOnline(view));
                    Utilities.setUpToolbar((AppCompatActivity) activity, "SplitIt");
                    Objects.requireNonNull(this.getDialog()).dismiss();
                    //((AppCompatActivity) activity).getSupportFragmentManager().popBackStack();

                } else {
                    Snackbar snackbar_error = Snackbar.make(view, R.string.error_login, Snackbar.LENGTH_SHORT);
                    View snackbar_error_view = snackbar_error.getView();
                    snackbar_error_view.setBackgroundColor(ContextCompat.getColor(activity, R.color.balance_neg));
                    snackbar_error.show();
                }
            });

        }

        groupImage.setOnClickListener(view1 -> {
            if ((ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                if ((ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE))) {

                } else {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_PERMISSIONS);
                }
            } else {
                showFileChooser();
            }
        });

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            filePath = getPath(picUri);
            if (filePath != null) {
                try {
                    groupBitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), picUri);

                    //OnlineDatabase.execute(setUserImageName());
                    groupImage.setImageBitmap(groupBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else
            {
                makeText(
                        getContext(),"no image selected",
                        Toast.LENGTH_LONG).show();
            }
        }else{
        }

    }
    public String getPath(Uri uri) {
        Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = requireContext().getContentResolver().query(
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
        if(bitmap == null){
            //bitmap = Bitmap.createBitmap(groupImage.getDrawable().getIntrinsicWidth(), groupImage.getDrawable().getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            BitmapDrawable drawable = (BitmapDrawable) groupImage.getDrawable();
            bitmap = drawable.getBitmap();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadGroupBitmap(final Bitmap bitmap) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ROOT_URL,
                response -> {
                    //JSONArray obj = new JSONArray(new String(response.data));
                    String string = new String(response.data);
                    //makeText(requireContext().getApplicationContext(),"Caricamento completato", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    System.out.println( error.getMessage());
                    //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                    // edited here
                    try {
                        byte[] htmlBodyBytes = error.networkResponse.data;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                }) {


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                //System.currentTimeMillis()
                params.put("image", new DataPart(nameText.getText() + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        requestQueue.add(volleyMultipartRequest);

    }

    public boolean checkAdd(){
        String tmp = nameText.getText().toString();
        tmp = tmp.replaceAll("\\s+","");
        return !tmp.isEmpty();
    }

    public Runnable addGroupOnline(View view) {
        Runnable task = () -> {

            RequestQueue MyRequestQueue = Volley.newRequestQueue(this.requireContext());
            String URL = "http://"+Utilities.IP+"/splitit/comunication.php";

            //Create an error listener to handle errors appropriately.
            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.

                if(response.equals("failure")){

                }else{
                    setLastId(response);
                    uploadGroupBitmap(groupBitmap);
                }
            }, error -> {
                //This code is executed if there is an error.

            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<>();
                    MyData.put("id", String.valueOf(userId)); //Add the data you'd like to send to the server.
                    MyData.put("request",String.valueOf(2));
                    JSONObject jObjectData = new JSONObject();
                    try {
                        jObjectData.put("nome",nameText.getText().toString());
                        jObjectData.put("img","ic_baseline:android_24");
                        jObjectData.put("admin",userId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    MyData.put("specs",jObjectData.toString());
                    return MyData;
                }
            };

            requestQueue.add(MyStringRequest);
        };
        return task;

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Utilities.setUpToolbar((AppCompatActivity) requireActivity(),"SplitIt");
    }

    private void setLastId(String s){
        this.lastId=Long.parseLong(s);
    }
}
