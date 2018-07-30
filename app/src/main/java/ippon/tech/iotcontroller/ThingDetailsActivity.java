package ippon.tech.iotcontroller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ippon.tech.iotcontroller.AWS.AWSProvider;
import ippon.tech.iotcontroller.AWS.AsyncThingStateDownloader;

public class ThingDetailsActivity extends AppCompatActivity {

    String thingName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        thingName = intent.getStringExtra("ThingName");

        TextView textView = findViewById(R.id.textViewTitle);
        textView.append(" " + thingName);

        loadThingShadow(thingName);
    }

    private void loadThingShadow(String name) {

        AWSProvider.getIotDataClient().setEndpoint(Constants.AWS.CUSTOM_ENDPOINT_FULL);
        AsyncThingStateDownloader thingStateDownloader = new AsyncThingStateDownloader(this, AWSProvider.getIotDataClient(), name, "Details");
        thingStateDownloader.execute();
    }

    ///////////////////////////////////////////////////
    // UI Update methods
    ///////////////////////////////////////////////////

    public void updateJson(String json) {
        EditText editText = findViewById(R.id.editTextThingDetails);

        if (json != null) {
            editText.setText(json);
        }
        else {
            editText.setText("Empty thing details");
        }
    }

    public void showThingPopup(String json){

        AlertDialog alertDialog = new AlertDialog.Builder(ThingDetailsActivity.this).create();
        alertDialog.setMessage(json);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    //For setting up our SNS notifications based on temperature
    public void onNotifyClick(View view) {

    }

    //For setting up our SNS notifications based on temperature
    public void onDeleteClick(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(ThingDetailsActivity.this).create();
        alertDialog.setMessage("Delete this thing");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
