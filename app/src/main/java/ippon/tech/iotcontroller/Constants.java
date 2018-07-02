package ippon.tech.iotcontroller;

import com.amazonaws.regions.Regions;

public final class Constants {
    public static final class AWS {
        public static String CUSTOM_ENDPOINT = BuildConfig.AWS_IOT_ENDPOINT;
        public static String CUSTOM_ENDPOINT_FULL = BuildConfig.AWS_IOT_ENDPOINT_FULL;
        public static String COGNITO_POOL_ID = BuildConfig.AWS_COGNITO_POOL;
        public static Regions REGION = Regions.fromName(BuildConfig.AWS_REGION);
    }
}
