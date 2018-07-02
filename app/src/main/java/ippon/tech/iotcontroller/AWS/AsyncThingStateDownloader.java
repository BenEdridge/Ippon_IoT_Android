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
import com.amazonaws.services.iotdata.model.GetThingShadowRequest;
import com.amazonaws.services.iotdata.model.GetThingShadowResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

import ippon.tech.iotcontroller.MainActivity;
import ippon.tech.iotcontroller.ThingDetailsActivity;

public class AsyncThingStateDownloader extends AsyncTask <Void, Void, AsyncTaskResult<String>> {

    private AsyncTaskResult<String> result;
    private WeakReference<Activity> parent;
    private AWSIotDataClient dataClient;
    private String name;

    public AsyncThingStateDownloader(Activity activity, AWSIotDataClient dataClient, String name) {
        this.parent = new WeakReference<>(activity);
        this.dataClient = dataClient;
        this.name = name;
    }

    @Override
    protected AsyncTaskResult<String> doInBackground(Void... voids) {
        try {

            GetThingShadowRequest getThingShadowRequest = new GetThingShadowRequest();
            getThingShadowRequest.setThingName(name);

            GetThingShadowResult getThingShadowResult = dataClient.getThingShadow(getThingShadowRequest);

            byte[] bytes = new byte[getThingShadowResult.getPayload().remaining()];
            getThingShadowResult.getPayload().get(bytes);

            // Todo encoding here?
            result = new AsyncTaskResult<>(new String(bytes));

            Log.d(this.getClass().toString(), result.getResult());
            return result;
        }
        catch (Exception e) {
            Log.e(this.getClass().toString(), e.toString(), result.getError());
            return result;
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<String> result) {
        Context context = parent.get();

        if(context!= null) {
            int spacesToIndentEachLevel = 2;
            try {
                String parsedJson = new JSONObject(result.getResult())
                        .toString(spacesToIndentEachLevel);
                ((MainActivity) context).showThingPopup(parsedJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(this.getClass().toString(), "no context!");
        }
    }
}
