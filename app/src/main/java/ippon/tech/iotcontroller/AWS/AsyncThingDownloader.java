package ippon.tech.iotcontroller.AWS;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.model.ListThingsRequest;
import com.amazonaws.services.iot.model.ListThingsResult;
import com.amazonaws.services.iot.model.ThingAttribute;
import com.amazonaws.services.iotdata.AWSIotDataClient;

import java.lang.ref.WeakReference;
import java.util.List;

import ippon.tech.iotcontroller.MainActivity;

public class AsyncThingDownloader extends AsyncTask <Void, Void, AsyncTaskResult<List<ThingAttribute>>> {

    private List<ThingAttribute> attributeList;
    private WeakReference<Activity> parent;
    private AWSIotDataClient dataClient;
    private AWSIotClient iotClient;

    public AsyncThingDownloader(Activity activity, AWSIotDataClient dataClient, AWSIotClient iotClient) {
        this.parent = new WeakReference<>(activity);
        this.dataClient = dataClient;
        this.iotClient = iotClient;
    }

    @Override
    protected AsyncTaskResult<List<ThingAttribute>> doInBackground(Void... voids) {
        try {
            ListThingsRequest listThingsRequest = new ListThingsRequest();
            ListThingsResult listThingsResult = iotClient.listThings(listThingsRequest);
            attributeList = listThingsResult.getThings();

            Log.d(this.getClass().toString(), attributeList.toString());
            return new AsyncTaskResult<>(attributeList);
        }
        catch (Exception e) {
            Log.e(this.getClass().toString(), e.toString());
            return new AsyncTaskResult<>(e);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<List<ThingAttribute>> result) {
        Context context = parent.get();

        if(context!= null) {
            ((MainActivity) context).updateThingUI(result.getResult());
            Log.d("POST EXE", result.getResult().toString());
        } else {
            Log.e(this.getClass().toString(), "no context!", result.getError());
        }
    }


}
