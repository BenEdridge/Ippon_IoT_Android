package ippon.tech.iotcontroller;

import com.amazonaws.regions.Regions;

public final class Constants {

    // Replace these here with your own
    public static final class AWS {
        static String CUSTOM_ENDPOINT = "iot.ap-southeast-2.amazonaws.com";
        static String COGNITO_POOL_ID = "replace";
        static Regions REGION = Regions.AP_SOUTHEAST_2;
    }
}
