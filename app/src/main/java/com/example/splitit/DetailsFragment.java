package com.example.splitit;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.splitit.Database.UserGroupCrossRef;
import com.example.splitit.OnlineDatabase.OnlineDatabase;
import com.example.splitit.RecyclerView.OnItemListener;
import com.example.splitit.RecyclerView.User;
import com.example.splitit.RecyclerView.UserAdapter;
import com.example.splitit.ViewModel.AddUserViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailsFragment extends Fragment implements OnItemListener, NavigationView.OnNavigationItemSelectedListener{


    private final long groupId;
    private final String userId;
    private final String groupName;
    private AddUserViewModel vm;
    private List<User> userList;
    private UserAdapter adapter;
    private List<UserGroupCrossRef> refUser;
    private UserGroupCrossRef userToDelete;
    private PieChart pieChart;
    private EditText et_balance;
    private ImageButton btn_send_balance;
    private boolean admin=false;
    private final long adminId;
    private boolean closeFlag=false;


    public DetailsFragment(long groupId, String groupName, String userId, long adminId){
        this.groupName = groupName;
        this.groupId = groupId;
        this.userId = userId;
        this.adminId = adminId;
    }


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.detailed_group,container, false);

    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        final Activity activity=getActivity();
        if(activity!=null){

            ImageView iv_grouImage = activity.findViewById(R.id.iv_group_image);
            iv_grouImage.setImageResource(R.drawable.group);

            Utilities.getGroupImage(String.valueOf(this.groupName), iv_grouImage);
            TextView tv_groupName = activity.findViewById(R.id.tv_group_name);
            tv_groupName.setText(groupName);
            btn_send_balance = activity.findViewById(R.id.btn_send_balance);
            et_balance = activity.findViewById(R.id.et_balance);
            Button btn_submit = activity.findViewById(R.id.group_submit);

            pieChart = activity.findViewById(R.id.pie_chart);
            pieChart.getLegend().setEnabled(true);

            Legend l = pieChart.getLegend();
            l.setTextSize(14f);
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setForm(Legend.LegendForm.LINE);
            l.setTextColor(Color.WHITE);
            l.setEnabled(true);

            // if no need to add description
            pieChart.getDescription().setEnabled(false);

            vm=new ViewModelProvider((ViewModelStoreOwner) activity).get(AddUserViewModel.class);

            if(this.adminId == Long.parseLong(userId)){
                this.admin=true;
                btn_send_balance.setEnabled(false);
                et_balance.setEnabled(false);



            }else{
                btn_submit.setEnabled(false);
            }

            setRecyclerView(activity);

            vm.getAllUsersBalance(groupId).observe((LifecycleOwner) activity, userGroupCrossRefs -> {
                if(refUser != null && userGroupCrossRefs.size()==refUser.size()){

                    for(int i = 0; i<refUser.size(); i++){
                        if(refUser.get(i).getBalance() != userGroupCrossRefs.get(i).getBalance()){
                            refUser = userGroupCrossRefs;
                            adapter.setValues(userGroupCrossRefs);
                            updateGraph();

                            return;
                        }
                    }

                }else{
                    refUser = userGroupCrossRefs;
                    adapter.setValues(userGroupCrossRefs);
                    updateGraph();
                }


            });

            vm.searchUsers(groupId).observe((LifecycleOwner) activity, list -> {
                if(list.size()>0) {
                    List<User> userList1 = Utilities.remapUserList(list.get(0).users, adminId);
                    if(userList != null && userList.size() == userList1.size()){

                        for(int i = 0; i<userList1.size(); i++){
                            if(userList1.get(i).getId() != userList.get(i).getId()){
                                userList = Utilities.remapUserList(userList1, adminId);


                                adapter.setData(userList);
                                updateGraph();

                                return;
                            }
                        }

                    }else{

                        userList = Utilities.remapUserList(userList1, adminId);



                        adapter.setData(userList);

                        updateGraph();

                    }

                }
            });




            btn_send_balance.setOnClickListener(v -> OnlineDatabase.execute(setNewBalance()));

            btn_submit.setOnClickListener(v -> new AlertDialog.Builder(v.getContext())
                    .setTitle("Conferma pagamento")
                    .setMessage("Sei sicuro di voler procedere con il pagamento?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        OnlineDatabase.execute(payGroupOnline());
                        getActivity().finish();
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show());

        }

        setDialog();
        callAsynchronousTask();
    }

    public void setDialog(){
        Button btn_addUser = requireView().findViewById(R.id.btn_add_to_group);
        btn_addUser.setOnClickListener(v -> {
            DialogAddUserSelection dialog = new DialogAddUserSelection(groupId);
            dialog.show(getChildFragmentManager(), "User Selection Dialog");
        });
    }



    private void setRecyclerView(final Activity activity){
        RecyclerView recyclerView = requireView().findViewById(R.id.recyclerViewUser);
        recyclerView.setHasFixedSize(true);
        final OnItemListener listener = this;
        adapter = new UserAdapter(activity, listener, this.groupId, this.userId, this.admin, this.adminId);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(int position) {

    }


    @Override
    public void onDelete(long id,int posu, int posr) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        OnlineDatabase.execute(deleteRef(getView(),String.valueOf(id),String.valueOf(groupId)));
        if(appCompatActivity!=null) {
            Runnable task = () -> {

                userToDelete=vm.searchSpecRef(groupId, id);

                vm.removeRef(userToDelete);
            };

            ExecutorService ex=Executors.newFixedThreadPool(1);
            ex.execute(task);
        }
        adapter.uploadData(posu,posr);
        adapter.notifyDataSetChanged();
        updateGraph();

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }



    public void updateGraph(){
        if(userList == null || refUser == null){
            return;
        }


        ArrayList<PieEntry> NoOfEmp = new ArrayList<>();
        float total=0;
        for(int i=0;i<userList.size();i++){
            for(int j=0;j<refUser.size();j++){
                if(userList.get(i).getId() == refUser.get(j).getUserId()){
                    //Log.e("Graph",(float)refUser.get(j).getBalance()+" "+userList.get(i).getName()+" "+userList.get(i).getId()+" = "+refUser.get(j).getUser_id());
                    NoOfEmp.add(new PieEntry((float)refUser.get(j).getBalance(), userList.get(i).getName()));
                    total+=(float)refUser.get(j).getBalance();
                }
            }

        }


        PieDataSet dataSet = new PieDataSet(NoOfEmp, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.animateXY(2000, 2000);
        pieChart.setCenterText(String.valueOf(total).concat("€"));

    }


    public Runnable deleteRef(View view,String idU,String idG) {
        return () -> {

            RequestQueue MyRequestQueue = Volley.newRequestQueue(this.requireContext());
            String URL = "http://"+Utilities.IP+"/splitit/comunication.php";

            //Create an error listener to handle errors appropriately.
            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.

                if(response.equals("failure")){
                    Log.e("DetailsFragment","failed");

                }
            }, error -> {
                //This code is executed if there is an error.
                Log.e("DetailsFragment","error response");

            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<>();
                    MyData.put("id", idU); //Add the data you'd like to send to the server.
                    MyData.put("group_id", idG);
                    MyData.put("request",String.valueOf(3));
                    return MyData;
                }
            };

            MyRequestQueue.add(MyStringRequest);
        };

    }

    public Runnable setNewBalance() {
        return () -> {

            RequestQueue MyRequestQueue = Volley.newRequestQueue(this.requireContext());
            String URL = "http://"+Utilities.IP+"/splitit/comunication.php";

            //Create an error listener to handle errors appropriately.
            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.

                if(response.equals("failure")){
                    Log.e("DetailsFragment","failed");

                }
            }, error -> {
                //This code is executed if there is an error.
                Log.e("DetailsFragment","error response");

            }) {
                protected Map<String, String> getParams() {

                    Map<String, String> MyData = new HashMap<>();
                    MyData.put("id", userId); //Add the data you'd like to send to the server.
                    MyData.put("idGruppo", String.valueOf(groupId));
                    MyData.put("request",String.valueOf(5));
                    if(et_balance.getText().toString().matches("")){
                        MyData.put("bilancio",String.valueOf(0));
                    }else{
                        MyData.put("bilancio",et_balance.getText().toString());
                    }

                    return MyData;
                }
            };

            MyRequestQueue.add(MyStringRequest);
        };

    }

    public Runnable getBalance() {
        if(this.getContext() != null) {
            return () -> {
                RequestQueue MyRequestQueue = Volley.newRequestQueue(this.getContext());
                String URL = "http://"+Utilities.IP+"/splitit/comunication.php";

                //Create an error listener to handle errors appropriately.
                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                    //This code is executed if the server responds, whether or not the response contains data.
                    //The String 'response' contains the server's response.

                    if (response.equals("failure")) {
                        Log.e("DetailsFragment", "failed");
                        System.out.println(userId + "," + groupId);
                    } else {

                        saveBalance(response);
                    }
                }, error -> {
                    //This code is executed if there is an error.
                    Log.e("DetailsFragment", "error response");

                }) {
                    protected Map<String, String> getParams() {

                        Map<String, String> MyData = new HashMap<>();
                        MyData.put("id", userId); //Add the data you'd like to send to the server.
                        MyData.put("idGruppo", String.valueOf(groupId));
                        MyData.put("request", String.valueOf(7));

                        return MyData;
                    }
                };

                MyRequestQueue.add(MyStringRequest);
            };
        }else{
            return null;
        }

    }

    public Runnable verifyGroupClosing() {
        if(this.getContext() != null) {
            return () -> {
                RequestQueue MyRequestQueue = Volley.newRequestQueue(this.getContext());
                String URL = "http://"+Utilities.IP+"/splitit/comunication.php";

                //Create an error listener to handle errors appropriately.
                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                    //This code is executed if the server responds, whether or not the response contains data.
                    //The String 'response' contains the server's response.

                    if (response.equals("failure")) {
                        Log.e("DetailsFragment", "failed");

                    } else {

                        if(Utilities.parseClosing(response)){

                            btn_send_balance.setEnabled(false);
                            et_balance.setEnabled(false);
                            Toast toast = Toast.makeText(getContext(), "L'admin ha confermato il gruppo, potrai visualizzarlo nello storico", Toast.LENGTH_LONG);
                            toast.show();
                            closeFlag = true;
                        }

                    }
                }, error -> {
                    //This code is executed if there is an error.
                    Log.e("DetailsFragment", "error response");

                }) {
                    protected Map<String, String> getParams() {

                        Map<String, String> MyData = new HashMap<>();
                        MyData.put("id", userId); //Add the data you'd like to send to the server.
                        MyData.put("idGruppo", String.valueOf(groupId));
                        MyData.put("request", String.valueOf(11));

                        return MyData;
                    }
                };

                MyRequestQueue.add(MyStringRequest);
            };
        }else{
            return null;
        }

    }


    private void callAsynchronousTask(){
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask(){

            @Override
            public void run() {
                handler.post(() -> {
                    try{
                        if(getBalance()==null){
                            handler.removeCallbacksAndMessages(null);
                        }
                        if(!closeFlag) {
                            OnlineDatabase.execute(verifyGroupClosing());
                        }
                        OnlineDatabase.execute(getBalance());
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask,0,2000);
    }


    private void saveBalance(String response){
        ArrayList <UserGroupCrossRef> res = Utilities.parseUserGroupCrossRef(response);
        ArrayList <User> users = Utilities.parseUser(response);
        controlUserDelete(res);
        System.out.println(res.size());
        System.out.println(users.size());
        for(int i = 0 ; i<res.size(); i++){

            vm.addNewRef(res.get(i));
        }

        for(int i = 0 ; i<users.size(); i++){
            vm.addUser(users.get(i));
        }
    }

    private void controlUserDelete(ArrayList <UserGroupCrossRef> res ){
        List<UserGroupCrossRef> userListTemp=refUser;

        if(userListTemp.size()>res.size()){
            for(int i=0; i<res.size(); i++){
                userListTemp.remove(res.get(i));
            }
            if(userListTemp.size()!=0){
                for(int i=0; i<userListTemp.size(); i++){
                    vm.removeRef(userListTemp.get(i));
                }
            }
        }

    }



    public Runnable payGroupOnline() {
        if(this.getContext() != null) {
            return () -> {
                RequestQueue MyRequestQueue = Volley.newRequestQueue(this.getContext());
                String URL = "http://"+Utilities.IP+"/splitit/comunication.php";

                //Create an error listener to handle errors appropriately.
                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                    //This code is executed if the server responds, whether or not the response contains data.
                    //The String 'response' contains the server's response.

                    if (response.equals("failure")) {
                        Log.e("DetailsFragment", "failed");


                    }
                }, error -> {
                    //This code is executed if there is an error.
                    Log.e("DetailsFragment", "error response");

                }) {
                    protected Map<String, String> getParams() {

                        Map<String, String> MyData = new HashMap<>();
                        MyData.put("id", userId); //Add the data you'd like to send to the server.
                        MyData.put("idGruppo", String.valueOf(groupId));
                        MyData.put("admin", userId);
                        MyData.put("request", String.valueOf(8));

                        return MyData;
                    }
                };

                MyRequestQueue.add(MyStringRequest);
            };
        }else{
            return null;
        }

    }


}
