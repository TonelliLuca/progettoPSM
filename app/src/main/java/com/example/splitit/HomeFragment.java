package com.example.splitit;



import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.splitit.RecyclerView.GroupAdapter;
import com.example.splitit.RecyclerView.GroupItem;
import com.example.splitit.RecyclerView.OnItemListener;
import com.example.splitit.ViewModel.ListViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements OnItemListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String LOG="HomeFragment";
    private GroupAdapter adapter;
    private RecyclerView recyclerView;
    private ListViewModel listViewModel;

    @Override
    public void onItemClick(int position) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if(appCompatActivity!=null){
            listViewModel.selected(adapter.getItemFiltered(position));
            GroupItem a = listViewModel.getSelected().getValue();
            Log.e("GroupItem","selected id: "+a.getId());
            Intent intent = new Intent(getActivity(), DetailsGroupActivity.class);
            intent.putExtra("group_ID",a.getId());
            startActivity(intent);
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.home, container, false);

    }

    public void setLineChart() {
        LineChart lineChart = requireView().findViewById(R.id.lineChart);
        lineChart.setDragEnabled(true);
        //lineChart.setScaleEnabled(false);
        lineChart.setTouchEnabled(true);

        ArrayList<Entry> yVal = new ArrayList<>();

        yVal.add(new Entry(0, 60f));
        yVal.add(new Entry(1, 70f));
        yVal.add(new Entry(2, 80f));
        yVal.add(new Entry(3, 90f));
        yVal.add(new Entry(4, 30f));
        yVal.add(new Entry(5, 50f));
        yVal.add(new Entry(6, 75f));

        LineDataSet set1 = new LineDataSet(yVal, "");

        set1.setValueTextColor(Color.WHITE);
        set1.setLabel("");
        set1.setLineWidth(5f);
        set1.setFillAlpha(500);
        set1.setColor(Color.rgb(0,83, 87));
        set1.setDrawFilled(true);

        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.gradient_btn);
            set1.setFillDrawable(drawable);
        }
        else {
            set1.setFillColor(Color.BLACK);
        }


        ArrayList<ILineDataSet> dataset = new ArrayList<>();
        dataset.add(set1);

        LineData data = new LineData(dataset);
        Description desc = new Description();
        desc.setText("");
        desc.setTextSize(28);
        lineChart.setDescription(desc);
        lineChart.getXAxis().setAxisLineColor(R.color.blue);
        lineChart.getAxisRight().setTextColor(Color.WHITE);
        lineChart.getAxisLeft().setTextColor(Color.WHITE);
        lineChart.getXAxis().setTextColor(Color.WHITE);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);

        lineChart.setData(data);

    }

    private void setRecyclerView(final Activity activity){
        recyclerView = requireView().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        final OnItemListener listener = this;
        adapter=new GroupAdapter(activity,listener);
        recyclerView.setAdapter(adapter);
    }



    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        if(activity != null){
            Utilities.setUpToolbar((AppCompatActivity) getActivity(), "SplitIt");
            setDialog(activity);
            setRecyclerView(activity);
            setLineChart();
            listViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(ListViewModel.class);
            listViewModel.getGroupItems().observe((LifecycleOwner) activity, new Observer<List<GroupItem>>() {
                @Override
                public void onChanged(List<GroupItem> groupItems) {
                    adapter.setData(groupItems);
                }
            });
            FloatingActionButton floatingActionButton = view.findViewById(R.id.fab_add);
            floatingActionButton.setOnClickListener(v ->
                    Utilities.insertFragment((AppCompatActivity) activity, new AddFragment(), "AddFragment"));
        }else{
            Log.e(LOG, "Activity is null");
        }

    }

    public void setDialog(final Activity activity){
        Button qrButton = requireView().findViewById(R.id.btn_qrcode);

        qrButton.setOnClickListener(v -> {

            Log.e("QRCODE", "created");
            DialogQrcodeFragment dialog = new DialogQrcodeFragment();
            dialog.show(getChildFragmentManager(), "QrCode Dialog");
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }


}
