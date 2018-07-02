package ippon.tech.iotcontroller;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.model.GroupNameAndArn;
import com.amazonaws.services.iot.model.ThingAttribute;
import com.amazonaws.services.iotdata.AWSIotDataClient;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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

        recycleviewAdapter = new ThingAdapter(null, this);
        recyclerView.setAdapter(recycleviewAdapter);
        initThingList();

        initChart();
    }

    private void initChart() {
        LineChart lineChart = findViewById(R.id.chart);

        // Mock data
        List<Entry> entries = new ArrayList<>();

        for(int i = 0; i < 10; i ++) {
            entries.add(new Entry(i, i *2));
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "Label");

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    private void initThingList() {
        AsyncThingDownloader thingDownloader = new AsyncThingDownloader(this, AWSProvider.getIotDataClient(), AWSProvider.getIotData());
        thingDownloader.execute();
    }

    public void updateThingUI(List<ThingAttribute> list){

        recycleviewAdapter = new ThingAdapter(list, this);
        recyclerView.setAdapter(recycleviewAdapter);
        recycleviewAdapter.notifyDataSetChanged();
    }

    /** Called when the user touches the refresh */
    public void onRefreshClick(View view) {
        initThingList();
        recycleviewAdapter.notifyDataSetChanged();
    }

    public void loadThingShadow(String name) {

        AWSProvider.getIotDataClient().setEndpoint(Constants.AWS.CUSTOM_ENDPOINT_FULL);
        AsyncThingStateDownloader thingStateDownloader = new AsyncThingStateDownloader(this, AWSProvider.getIotDataClient(), name);
        thingStateDownloader.execute();
    }

    public void showThingPopup(String json){

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setMessage(json);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
