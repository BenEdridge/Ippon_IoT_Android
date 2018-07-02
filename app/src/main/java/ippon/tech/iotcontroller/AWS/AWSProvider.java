package ippon.tech.iotcontroller.AWS;

import android.app.Application;
import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iotdata.AWSIotData;
import com.amazonaws.services.iotdata.AWSIotDataClient;

import ippon.tech.iotcontroller.Constants;

// https://developer.android.com/reference/android/app/Application
public class AWSProvider  {

    private static volatile AWSProvider ourInstance;

    private static CognitoCachingCredentialsProvider credentialsProvider;
    private static AWSIotDataClient iotDataClient;
    private static AWSIotClient iotClient;

    public static AWSProvider getInstance(Context context) {
        if (ourInstance == null) { //if there is no instance available... create new one
            synchronized (AWSProvider.class) {
                if (ourInstance == null) ourInstance = new AWSProvider();

                // Setting up Cognito for unauthed access
                credentialsProvider = new CognitoCachingCredentialsProvider(
                        context,
                        Constants.AWS.COGNITO_POOL_ID,
                        Constants.AWS.REGION
                );

                iotDataClient = new AWSIotDataClient(credentialsProvider);
                iotDataClient.setEndpoint(Constants.AWS.CUSTOM_ENDPOINT);

                iotClient = new AWSIotClient(credentialsProvider);
                iotClient.setEndpoint(Constants.AWS.CUSTOM_ENDPOINT);
            }
        }
        return ourInstance;
    }


    private AWSProvider() {
    }

    public static AWSIotDataClient getIotDataClient() {
        return iotDataClient;
    }

    public static AWSIotClient getIotData() {
        return iotClient;
    }
}
