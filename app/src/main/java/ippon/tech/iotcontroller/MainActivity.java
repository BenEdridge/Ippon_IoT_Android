package ippon.tech.iotcontroller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.model.GroupNameAndArn;
import com.amazonaws.services.iot.model.ThingAttribute;
import com.amazonaws.services.iotdata.AWSIotDataClient;

import java.util.List;

// Based on the example here
//https://github.com/awslabs/aws-sdk-android-samples/blob/master/TemperatureControl/src/com/amazonaws/demo/temperaturecontrol/TemperatureActivity.java
//
public class MainActivity extends AppCompatActivity {

    private CognitoCachingCredentialsProvider credentialsProvider;
    private AWSIotDataClient iotDataClient;
    private AWSIotClient iotClient;

    // List of groups
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recycleviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        // Setting up Cognito for unauthed access
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                Constants.AWS.COGNITO_POOL_ID,
                Constants.AWS.REGION
        );

        iotDataClient = new AWSIotDataClient(credentialsProvider);
        iotDataClient.setEndpoint(Constants.AWS.CUSTOM_ENDPOINT);

        iotClient = new AWSIotClient(credentialsProvider);
        iotClient.setEndpoint(Constants.AWS.CUSTOM_ENDPOINT);

        // Setting up list of groups
        // https://developer.android.com/guide/topics/ui/layout/recyclerview
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        recycleviewAdapter = new ThingAdapter(null);
        recyclerView.setAdapter(recycleviewAdapter);

//        initThingGroupList();
        initThingList();
    }

    private void initThingGroupList() {
        AsyncThingGroupDownloader thingGroupDownloader = new AsyncThingGroupDownloader(this, iotDataClient, iotClient);
        thingGroupDownloader.execute();
    }

    private void initThingList() {
        AsyncThingDownloader thingDownloader = new AsyncThingDownloader(this, iotDataClient, iotClient);
        thingDownloader.execute();
    }

    public void updateGroupUI(List<GroupNameAndArn> list){

        recycleviewAdapter = new ThingGroupAdapter(list);
        recyclerView.setAdapter(recycleviewAdapter);
        recycleviewAdapter.notifyDataSetChanged();
    }

    public void updateThingUI(List<ThingAttribute> list){

        recycleviewAdapter = new ThingAdapter(list);
        recyclerView.setAdapter(recycleviewAdapter);
        recycleviewAdapter.notifyDataSetChanged();
    }

    /** Called when the user touches the refresh */
    public void onRefreshClick(View view) {
        initThingList();
        recycleviewAdapter.notifyDataSetChanged();
    }

    /** Called when the user touches the send device details */
    public void onSendDeviceDetailsClick(View view) {

    }
}
