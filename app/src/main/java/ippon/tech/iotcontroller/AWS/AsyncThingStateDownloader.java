package ippon.tech.iotcontroller.AWS;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.services.iotdata.AWSIotDataClient;
import com.amazonaws.services.iotdata.model.GetThingShadowRequest;
import com.amazonaws.services.iotdata.model.GetThingShadowResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import ippon.tech.iotcontroller.MainActivity;
import ippon.tech.iotcontroller.ThingDetailsActivity;

public class AsyncThingStateDownloader extends AsyncTask <Void, Void, AsyncTaskResult<String>> {

    private AsyncTaskResult<String> result;
    private WeakReference<Activity> parent;
    private String type;
    private AWSIotDataClient dataClient;
    private String name;

    public AsyncThingStateDownloader(Activity activity, AWSIotDataClient dataClient, String name, String type) {
        this.parent = new WeakReference<>(activity);
        this.dataClient = dataClient;
        this.name = name;
        this.type = type;
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

    // Todo fix this nastiness of casting how do we go about Dynamic type casting??
    @Override
    protected void onPostExecute(AsyncTaskResult<String> result) {
        Context context = parent.get();

        if(context!= null) {
            int spacesToIndentEachLevel = 2;
            try {
                JSONObject parsedJson = new JSONObject(result.getResult());
                String parsedToString = parsedJson.toString(spacesToIndentEachLevel);

                // Todo setup standard object for JSON parsing
                if(type.equals("Main")) {
                    ((MainActivity) context).updateGraph("1");
                } else {
                    ((ThingDetailsActivity) context).updateJson(parsedToString);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(this.getClass().toString(), "no context!");
        }
    }
}
