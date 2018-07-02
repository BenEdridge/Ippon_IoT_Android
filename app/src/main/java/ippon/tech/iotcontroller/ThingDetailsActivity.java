package ippon.tech.iotcontroller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.AmazonWebServiceClient;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityClient;
import com.google.gson.Gson;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iotdata.AWSIotDataClient;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import ippon.tech.iotcontroller.AWS.AsyncThingStateDownloader;

public class ThingDetailsActivity extends AppCompatActivity {

    private CognitoCachingCredentialsProvider credentialsProvider;
    private AWSIotDataClient iotDataClient;
    private AWSIotClient iotClient;

    String thingName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ///////////////////////////////////////////////////////////////////////////////

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

        /////////////////////////////////////////////////////////////////////////////

        Intent intent = getIntent();
        thingName = intent.getStringExtra("ThingName");

        TextView textView = findViewById(R.id.textViewTitle);
        textView.append(" " + thingName);

        loadThingShadow();
    }

    public void updateJson(String result) {
        EditText editText = findViewById(R.id.editTextThingDetails);


        if (result != null) {
            int spacesToIndentEachLevel = 2;
            try {
                String parsedJson = new JSONObject(result).toString(spacesToIndentEachLevel);
                editText.setText(parsedJson);
            } catch (JSONException e) {
                editText.setText("Error parsing JSON");
                e.printStackTrace();
            }
        }

        editText.setText("Empty Shadow");
    }

    private void loadThingShadow() {
        AsyncThingStateDownloader thingStateDownloader = new AsyncThingStateDownloader(this, iotDataClient, thingName);
        thingStateDownloader.execute();
    }
}
