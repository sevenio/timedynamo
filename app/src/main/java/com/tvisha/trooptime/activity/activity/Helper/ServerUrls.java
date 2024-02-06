package com.tvisha.trooptime.activity.activity.Helper;

/**
 * Created by tvisha on 9/2/17.
 */

public class ServerUrls {

    //lOCAL Lan
/*  public static final String BaseUrl = "http://192.168.2.42/time-dynamo/public/api/attendance/";
    public static String SOCKET_URL = "http://192.168.2.42:5000/";*/

    //lOCAL Wifi
/*  public static final String BaseUrl = "http://192.168.0.197/time-dynamo/public/api/attendance/";
    public static String SOCKET_URL = "http://192.168.0.197:5000/";*/


    // 1.18
/*    public static final String BaseUrl = "http://192.168.2.50/time-dynamo/public/api/attendance/";
   public static String SOCKET_URL = "http://192.168.2.50:5000/";*/

    //testing
  /*public static final String BaseUrl = "http://www.tvishasystems.com/webdemo/timedynamo_testing/public/api/attendance/";
  public static String SOCKET_URL = "http://www.tvishasystems.com:5000/";*/


    //live
   public static final String BaseUrl  = "https://www.timedynamo.com/api/attendance/";
   public static String SOCKET_URL = "http://www.timedynamo.com:5000/";


    public static final String Forgeturl = BaseUrl + "send-forgot-password";
    public static final String Self_Attendence = BaseUrl + "user-attendance";
    public static final String Team_Attendence = BaseUrl + "team-attendance";
    public static final String All_Attendence = BaseUrl + "all-attendance";
    public static final String SelfCheckinStatus = BaseUrl + "check-is-user-checked-in";
    public static final String Logout = BaseUrl + "logout";
    public static final String SaveDeviceToken = BaseUrl + "save-device-token";
    public static final String s3Url = "https://s3.amazonaws.com/time-dynamo.files/";
    public static final String GET_EMPLOYEE_EMAIL_LIST = BaseUrl + "get-cc-user-details";
    public static final String LEAVE_REQUEST = BaseUrl + "leave-request";
    public static final String Loginurl = BaseUrl + "login";
    public static String imagesUri = "/android/obb/com.attendance/user_images";
    public static String image_url = "https://s3.amazonaws.com/time-dynamo.files/";
    public static String BaseUrl2 = "https://www.troopcrm.com/api/time/";
    public static final String GET_BUSINESS_LIST = BaseUrl2 + "get-business-contacts";
    public static final String GET_EMPLOYEE_LIST = BaseUrl2 + "employees-list";
    public static final String CLIENT_VISIT = BaseUrl2 + "client-visit";

    public static final String PERMISSION_REQUEST = BaseUrl2 + "permission-request";
}
