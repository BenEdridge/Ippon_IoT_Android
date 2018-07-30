## Android IoT management app for AWS IoT

### What the app can do:
1. List devices in AWS IoT with device shadow and attributes
2. Graph temperature and humidity in real-time from sensors
3. Set SNS notifications for a rule (Creates rule in AWS IoT)

### Prereqs:
AWS environment and device setup. please see the ESP repo: https://github.com/BenEdridge/Ippon_IoT_ESP

### Setup
You need to set the AWS settings in your `local.properties` files before you can build 
this example. Follow this example by replacing the values from the `terraform` output from the 
other repo: https://github.com/BenEdridge/Ippon_IoT_ESP 

```
# Your AWS values/endpoints are here so on't check this into version control as this is a demo app 
# If you build the resulting APK and distribute it these values will be included
  
aws_iot_endpoint="iot.ap-southeast-2.amazonaws.com"
aws_cognito_pool_id="ap-southeast-2:1234567"
aws_iot_endpoint_full="abcdefgh.iot.ap-southeast-2.amazonaws.com"
  
# from: com.amazonaws.regions.Regions
aws_region="ap-southeast-2"
```
![Screenshot of App](https://raw.githubusercontent.com/BenEdridge/Ippon_IoT_Android/aws_iot_app_screenshot_shadow.png)