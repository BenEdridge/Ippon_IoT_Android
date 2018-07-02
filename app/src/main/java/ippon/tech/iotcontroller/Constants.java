package ippon.tech.iotcontroller;

import com.amazonaws.regions.Regions;

public final class Constants {
    public static final class AWS {
        static String CUSTOM_ENDPOINT = BuildConfig.AWS_IOT_ENDPOINT;
        static String COGNITO_POOL_ID = BuildConfig.AWS_COGNITO_POOL;
        static Regions REGION = Regions.fromName(BuildConfig.AWS_REGION);
    }
}
