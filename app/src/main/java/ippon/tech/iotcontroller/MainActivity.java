package ippon.tech.iotcontroller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.amazonaws.services.iot.model.ThingAttribute;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import ippon.tech.iotcontroller.AWS.AWSProvider;
import ippon.tech.iotcontroller.AWS.AsyncThingDownloader;
import ippon.tech.iotcontroller.AWS.AsyncThingStateDownloader;

// Based on the example here
//https://github.com/awslabs/aws-sdk-android-samples/tree/master/TemperatureControl
//
public class MainActivity extends AppCompatActivity {

    // RecyclerView for our list of groups
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recycleviewAdapter;

    BarChart barChart;
    List<BarEntry> entries = new ArrayList<>();
    BarDataSet set;
    BarData barData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get our singleton instance
        AWSProvider.getInstance(this);

        // Setting up list of groups
        // https://developer.android.com/guide/topics/ui/layout/recyclerview
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        recycleviewAdapter = new ThingAdapter(null);
        recyclerView.setAdapter(recycleviewAdapter);

        initChart();
        initThingList();
    }

    private void initChart() {

        barChart = findViewById(R.id.chart);
//        barChart.setFitBars(true); // make the x-axis fit exactly all bars
    }

    private void initThingList() {
        AsyncThingDownloader thingDownloader = new AsyncThingDownloader(this, AWSProvider.getIotDataClient(), AWSProvider.getIotData());
        thingDownloader.execute();
    }

    public void updateThingUI(List<ThingAttribute> list){

        recycleviewAdapter = new ThingAdapter(list);
        recyclerView.setAdapter(recycleviewAdapter);

        // For each thing get a temperature from AWS
        for(ThingAttribute a: list) {

            // Pull the JSON for each
            AWSProvider.getIotDataClient().setEndpoint(Constants.AWS.CUSTOM_ENDPOINT_FULL);
            AsyncThingStateDownloader thingStateDownloader = new AsyncThingStateDownloader(this, AWSProvider.getIotDataClient(), a.getThingName(), "Main");
            thingStateDownloader.execute();
        }
        recycleviewAdapter.notifyDataSetChanged();
    }

    /** Called when the user touches the refresh */
    public void onRefreshClick(View view) {

        set = new BarDataSet(entries, "BarDataSet");
        barData = new BarData(set);

        barChart.setData(barData);
        barChart.setDrawGridBackground(false);
        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setEnabled(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMaximum(40);
        leftAxis.setAxisMinimum(-5);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawGridLines(false);

        Legend l = barChart.getLegend();
        l.setEnabled(false);

        barChart.getAxisRight().setEnabled(false);
        barChart.invalidate();

        Log.d(this.getClass().toString(), String.valueOf(set.getEntryCount()));

        recycleviewAdapter.notifyDataSetChanged();
    }

    public void updateGraph(String parsedJson) {

        BarEntry barEntry = new BarEntry(1, 5);
        entries.add(barEntry);
    }
}
