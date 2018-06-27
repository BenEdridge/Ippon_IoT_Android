package ippon.tech.iotcontroller;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.model.GroupNameAndArn;
import com.amazonaws.services.iot.model.ListThingGroupsRequest;
import com.amazonaws.services.iot.model.ListThingGroupsResult;
import com.amazonaws.services.iotdata.AWSIotDataClient;

import java.lang.ref.WeakReference;
import java.util.List;

public class AsyncThingGroupDownloader extends AsyncTask <Void, Void, List<GroupNameAndArn>> {

    private List<GroupNameAndArn> nameAndArnList;
    private WeakReference<Activity> parent;
    private AWSIotDataClient dataClient;
    private AWSIotClient iotClient;

    public AsyncThingGroupDownloader(Activity activity, AWSIotDataClient dataClient, AWSIotClient iotClient) {
        this.parent = new WeakReference<>(activity);
        this.dataClient = dataClient;
        this.iotClient = iotClient;
    }

    @Override
    protected List<GroupNameAndArn> doInBackground(Void... voids) {
        try {
            ListThingGroupsRequest listThingGroupsRequest = new ListThingGroupsRequest();
            listThingGroupsRequest.setMaxResults(20);
            listThingGroupsRequest.setRecursive(true);


            ListThingGroupsResult listThingGroupsResult = iotClient.listThingGroups(listThingGroupsRequest);
            nameAndArnList = listThingGroupsResult.getThingGroups();

            Log.d(this.getClass().toString(), nameAndArnList.toString());

            return nameAndArnList;
        }
        catch (Exception e) {
            Log.e(this.getClass().toString(), e.toString());
            return nameAndArnList;
        }
    }

    @Override
    protected void onPostExecute(List<GroupNameAndArn> groupNameAndArns) {
        Context context = parent.get();

        if(context!= null) {
            ((MainActivity) context).updateGroupUI(groupNameAndArns);
        } else {
            Log.e(this.getClass().toString(), "no context!");
        }
    }
}
