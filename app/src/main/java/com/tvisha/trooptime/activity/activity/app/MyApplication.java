package com.tvisha.trooptime.activity.activity.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.tvisha.trooptime.activity.activity.apiPostModels.GetAwsConfigResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.HomePageResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.UserRequestListResponse;
import com.tvisha.trooptime.activity.activity.helper.Constants;
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.helper.SocketConstants;
import com.tvisha.trooptime.activity.activity.helper.SocketIo;
import com.tvisha.trooptime.activity.activity.helper.UpdateHelper;
import com.tvisha.trooptime.activity.activity.helper.Utilities;
import com.tvisha.trooptime.activity.activity.api.ApiClient;
import com.tvisha.trooptime.activity.activity.api.response.SelfAttendenceApiResponce;
import com.tvisha.trooptime.activity.activity.handlers.HandlerHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {


    public static HomePageResponse homePageResponse = null;
    public static UserRequestListResponse UserRequestListResponse = null;
    public static SelfAttendenceApiResponce selfAttendenceApiResponce = null;
    public static String AWS_KEY = "";
    public static String AWS_SECRET_KEY = "";
    public static String AWS_BUCKET = "";
    public static String AWS_REGION = "";
    public static String AWS_BASE_URL = "";
    @SuppressLint("HandlerLeak")
    public Handler socketHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SocketConstants.SOCKET_CONNECT:


                    Utilities.syncAttendance(MyApplication.this);
                    SocketIo socketIo = SocketIo.getInstance();
                    Socket socket = socketIo.getSocket();
                    socket.emit(SocketConstants.EVENT_SYNC_DATA, new JSONObject());
                    break;
                case SocketConstants.SOCKET_DISCONNECT:

                    break;
                case SocketConstants.SOCKET_SYNC_DATA:
                    try {
                        final JSONObject syncDataObject = (JSONObject) msg.obj;

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (syncDataObject.has("users")) {
                                    try {
                                        boolean update_employee = Utilities.addEmployee(MyApplication.this, syncDataObject.getJSONArray("users"));
                                        if (update_employee) {
                                            if (HandlerHolder.userdetectHandler != null) {
                                                HandlerHolder.userdetectHandler.obtainMessage(Constants.EMPLOYESSD_ADDED).sendToTarget();
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                Utilities.syncData(MyApplication.this, syncDataObject);

                            }
                        }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SocketConstants.SOCKET_SYNC_USER:
                    try {
                        final JSONObject jsonObject = (JSONObject) msg.obj;

                        if (jsonObject != null) {
                            Utilities.addEmployee(MyApplication.this, jsonObject);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SocketConstants.SOCKET_SYNC_ATTENDANCE:
                    try {
                        JSONArray jsonArray = (JSONArray) msg.obj;
                        if (jsonArray.length() > 0) {
                            Utilities.updateAttendance(MyApplication.this, jsonArray);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SocketConstants.SOCKET_SYNC_BREAK:
                    try {

                        JSONArray jsonArray = (JSONArray) msg.obj;
                        Utilities.updateBreak(MyApplication.this, jsonArray);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    Activity activity;
    UsbManager mManager;

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        this.activity = activity;


    }

    public void remoteConfig() {
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        Map<String, Object> defaultValue = new HashMap<>();
        defaultValue.put(UpdateHelper.KEY_UPDATE_ENABLE, true);
        defaultValue.put(UpdateHelper.KEY_UPDATE_VERSION, "2.0");
        defaultValue.put(UpdateHelper.KEY_UPDATE_URL, "https://play.google.com/store/apps/details?id=com.tvisha.trooptime");
        remoteConfig.setDefaultsAsync(defaultValue);
        remoteConfig.fetch(5)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            remoteConfig.fetchAndActivate();
                        }
                    }
                });
    }

    public void initSocket() {
        HandlerHolder.socketHandler = socketHandler;
        SocketIo socketIo = SocketIo.getInstance();
        socketIo.setContext(getApplicationContext());
        socketIo.setHandlers(socketHandler);
        socketIo.connectSocket();
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        //initSocket();
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    public Activity getActivity() {
        return activity;
    }

    public static SharedPreferences sharedPreferences;
    @Override
    public void onCreate() {
    /*    if(Utilities.getLoginStatus(this)){
            initSocket();
        }
        registerActivityLifecycleCallbacks(this);
*/
        sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME,MODE_PRIVATE);

        if (sharedPreferences.getBoolean(SharePreferenceKeys.SP_LOGIN_STATUS,false)) {

            AWS_KEY = sharedPreferences.getString(SharePreferenceKeys.AWS_KEY,"");
            AWS_BASE_URL = sharedPreferences.getString(SharePreferenceKeys.AWS_BASE_URL,"");
            AWS_BUCKET = sharedPreferences.getString(SharePreferenceKeys.AWS_BUCKET,"");
            AWS_SECRET_KEY = sharedPreferences.getString(SharePreferenceKeys.AWS_SECRET_KEY,"");
            AWS_REGION = sharedPreferences.getString(SharePreferenceKeys.AWS_REGION,"");

            HandlerHolder.applicationHandlers = uiHandler;
            callAwsKeys();


        }

        super.onCreate();
    }
    /*public void ls () {
        try {
            Process process = Runtime.getRuntime().exec("ping 192.168.2.2");
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = "";
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    Log.e("reader--> ",""+line);
                    output += line + "\n";
                }
            }catch (Exception e){
                Log.e("reader--> ",""+e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            Log.e("reader--> "," 1 "+e.getMessage());
            e.printStackTrace();
        }
    }*/

    @SuppressLint("HandlerLeak")
    Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.LOGIN_SUCCESSFUL:
                    callAwsKeys();
                    break;
            }
        }
    };
    public void callAwsKeys(){
        if (Utilities.isNetworkAvailable(getApplicationContext())){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    callGetAwsConfig();
                }
            }).start();
        }
    }
    private void callGetAwsConfig() {
        try {
            Call<GetAwsConfigResponse> call = ApiClient.getInstance().getAwsConfig(Constants.TOKEN);
            call.enqueue(new Callback<GetAwsConfigResponse>() {
                @Override
                public void onResponse(Call<GetAwsConfigResponse> call, Response<GetAwsConfigResponse> response) {
                    GetAwsConfigResponse apiResponse = response.body();
                    if (apiResponse != null) {
                        if (apiResponse.isSuccess()) {
                            decryptData(apiResponse);
                        }

                    }

                }

                @Override
                public void onFailure(Call<GetAwsConfigResponse> call, Throwable t) {

                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void decryptData(GetAwsConfigResponse apiResponse) {

        try {
            String base64 = apiResponse.getData();
            if (base64 != null) {
                byte[] data = Base64.decode(base64, Base64.DEFAULT);

                try {
                    String text = new String(data, "UTF-8");

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(text);
                        if (jsonObject != null) {
                            AWS_KEY = jsonObject.getString("key");
                            AWS_SECRET_KEY = jsonObject.getString("secret");
                            AWS_BUCKET = jsonObject.getString("bucket");
                            AWS_REGION = jsonObject.getString("region");
                            AWS_BASE_URL = jsonObject.getString("base_url");
                            sharedPreferences.edit().putString(SharePreferenceKeys.AWS_KEY,AWS_KEY).apply();
                            sharedPreferences.edit().putString(SharePreferenceKeys.AWS_BUCKET,AWS_BUCKET).apply();
                            sharedPreferences.edit().putString(SharePreferenceKeys.AWS_REGION,AWS_REGION).apply();
                            sharedPreferences.edit().putString(SharePreferenceKeys.AWS_BASE_URL,AWS_BASE_URL).apply();
                            sharedPreferences.edit().putString(SharePreferenceKeys.AWS_SECRET_KEY,AWS_SECRET_KEY).apply();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    @Override
    public void onTerminate() {
        unregisterActivityLifecycleCallbacks(this);
        super.onTerminate();
    }

    public String getRunningActivityName(Context mContext) {
        String activityName = "";
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            activityName = tasks.get(0).topActivity.getClassName();
        }
        return activityName;
    }


}
