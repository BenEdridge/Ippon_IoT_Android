package ippon.tech.iotcontroller;

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

public class AsyncThingDownloader extends AsyncTask <Void, Void, List<ThingAttribute>> {

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
    protected List<ThingAttribute> doInBackground(Void... voids) {
        try {

            ListThingsRequest listThingsRequest = new ListThingsRequest();
            ListThingsResult listThingsResult = iotClient.listThings(listThingsRequest);
            attributeList = listThingsResult.getThings();

            Log.d(this.getClass().toString(), attributeList.toString());
            return attributeList;
        }
        catch (Exception e) {
            Log.e(this.getClass().toString(), e.toString());
            return attributeList;
        }
    }

    @Override
    protected void onPostExecute(List<ThingAttribute> thingAttributes) {
        Context context = parent.get();

        if(context!= null) {
            ((MainActivity) context).updateThingUI(thingAttributes);
            Log.d("POST EXE", thingAttributes.toString());
        } else {
            Log.e(this.getClass().toString(), "no context!");
        }
    }
}
