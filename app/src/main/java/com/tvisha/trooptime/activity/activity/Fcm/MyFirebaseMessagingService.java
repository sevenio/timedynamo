package com.tvisha.trooptime.activity.activity.Fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tvisha.trooptime.activity.activity.ApiPostModels.CommonResponse;
import com.tvisha.trooptime.activity.activity.Helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.Helper.Utilities;
import com.tvisha.trooptime.activity.activity.HomeActivity;
import com.tvisha.trooptime.activity.activity.api.ApiClient;
import com.tvisha.trooptime.activity.activity.api.ApiInterface;
import com.tvisha.trooptime.R;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tvisha on 24/2/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    public static int count = 0;

    SharedPreferences sharedPreferences;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        callToserver(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(SharePreferenceKeys.SP_LOGIN_STATUS,false)){
            return;
        }
        if (remoteMessage!=null) {
            int c = sharedPreferences.getInt("notificationCount", 0);
            c++;
            sharedPreferences.edit().putInt("notificationCount", c).apply();
            // Log.e(TAG, "Notification Message TITLE: " + remoteMessage.getNotification().getTitle());
            //  Log.e(TAG, "Notification Message BODY: " + remoteMessage.getNotification().getBody());
            //  Log.e(TAG, "Notification Message DATA: " + remoteMessage.getData().toString());
            //Calling method to generate notification
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData());
        }
    }

    //This method is only generating push notification
    private void sendNotification(String messageTitle, String messageBody, Map<String, String> row) {

        Intent notificationIntent = new Intent(getApplicationContext(), HomeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notification = new Notification.Builder(this);
        notification.setSmallIcon(getNotificationIcon());
        notification.setContentTitle(messageTitle);
        notification.setContentText(messageBody);
        notification.setAutoCancel(true);
        notification.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.firebase_notification))
                .setSound(defaultSoundUri)
                .setContentIntent(contentIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(count, notification.build());
        count++;


          /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notification.setColor(getResources().getColor(R.color.colorPrimaryDark));
        }*/


    }

    private int getNotificationIcon() {
        return R.drawable.ic_firebase_smallnotification;
    }
    public void callToserver(String refreshedToken){
        try {
            sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
            if (sharedPreferences.getBoolean(SharePreferenceKeys.SP_LOGIN_STATUS, false)) {
                sendRegistrationToServer(refreshedToken,sharedPreferences.getString(SharePreferenceKeys.USER_ID, ""),sharedPreferences.getString(SharePreferenceKeys.API_KEY, ""),sharedPreferences.getString(SharePreferenceKeys.DEVICE_ID, ""));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendRegistrationToServer(String refreshedToken,String user_id,String token,String deviceId) {


        if (Utilities.isNetworkAvailable(getApplicationContext())) {

            callSaveFcmApi(refreshedToken,user_id,token,deviceId);
        }
    }

    public void callSaveFcmApi(String refreshedToken,String userId,String token,String deviceId) {

        Call<CommonResponse> call = ApiClient.getClient().create(ApiInterface.class).saveFcmToken(token, userId, refreshedToken, "1", deviceId);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse apiResponse = response.body();

                if (apiResponse != null) {

                }

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {


            }

        });
    }
}