package com.example.splitit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.splitit.Database.UserGroupCrossRef;
import com.example.splitit.OnlineDatabase.OnlineDatabase;
import com.example.splitit.RecyclerView.User;
import com.example.splitit.ViewModel.AddUserViewModel;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DialogAddUserSelection extends DialogFragment{

    private EditText addCode;
    private Button addQrCode;
    private Button submit;
    private final long groupId;
    private AddUserViewModel addUser;
    private ActivityResultLauncher<Intent> resultLauncher;


    public DialogAddUserSelection(long groupId){
        super();
        this.groupId=groupId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        IntentResult intentResult = IntentIntegrator.parseActivityResult( result.getResultCode(), result.getData());
                        // if the intentResult is null then
                        // toast a message as "cancelled"
                        if (intentResult != null) {
                            if (intentResult.getContents() == null) {
                                Toast toast = Toast.makeText(getActivity().getBaseContext(), "Cancelled", Toast.LENGTH_SHORT);

                                toast.show();
                            } else {
                                // if the intentResult is not null we'll set
                                // the content and format of scan message
                                //EditText userCode = findViewById(R.id.et_add_code);
                                addCode.setText(intentResult.getContents());

                                Log.e("ActivityDetails",intentResult.getContents());
                                Log.e("ActivityDetails",intentResult.getFormatName());
                            }
                        }
                    }
                }
        );
        
    }

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
            addCode = view.findViewById(R.id.et_add_code);
            addQrCode = view.findViewById(R.id.btn_qr_reader);
            submit = view.findViewById(R.id.btn_submit);

            addUser = new ViewModelProvider((ViewModelStoreOwner) activity).get(AddUserViewModel.class);

            submit.setOnClickListener(v ->{
                if(! addCode.getText().toString().matches("")) {
                    OnlineDatabase.execute(addNewUser(view));
                    Objects.requireNonNull(getDialog()).dismiss();
                }else {
                    Toast toast = Toast.makeText(getContext(), "Please insert a Code or scan a QR code", Toast.LENGTH_LONG);
                    toast.show();
                }

            });
            addQrCode.setOnClickListener(v -> {

                IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
                intentIntegrator.setPrompt("Scan a QR Code");
                intentIntegrator.setOrientationLocked(false);

                resultLauncher.launch(intentIntegrator.createScanIntent());



            });


        }
    }

    public Runnable addNewUser(View view) {
        Runnable task = () -> {

            RequestQueue MyRequestQueue = Volley.newRequestQueue(this.getContext());
            String URL = "http://"+Utilities.IP+"/splitit/comunication.php";

            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //This code is executed if the server responds, whether or not the response contains data.
                    //The String 'response' contains the server's response.

                    if(response.equals("failure")){
                        Log.e("DialogUser","failed");
                        Toast toast = Toast.makeText(getContext(), "Wrong code, please try again ", Toast.LENGTH_LONG);

                        toast.show();

                    }else{
                        Log.e("DialogUser", response);
                        saveLocalUser(response);
                    }
                }
            }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                @Override
                public void onErrorResponse(VolleyError error) {
                    //This code is executed if there is an error.
                    Log.e("DialogUser","error response");

                }
            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<String, String>();
                    MyData.put("id", String.valueOf(0)); //Add the data you'd like to send to the server.
                    MyData.put("request",String.valueOf(4));
                    MyData.put("code",addCode.getText().toString());
                    MyData.put("idGruppo",String.valueOf(groupId));
                    Log.e("DialogUser",addCode.getText().toString().concat(String.valueOf(groupId)));
                    return MyData;
                }
            };

            MyRequestQueue.add(MyStringRequest);
        };
        return task;

    }

    private void saveLocalUser(String user){
        User u = Utilities.parseUser(user).get(0);
        addUser.addUser(u);
        addUser.addNewRef(new UserGroupCrossRef(u.getId(), groupId,false,0));
    }


}
