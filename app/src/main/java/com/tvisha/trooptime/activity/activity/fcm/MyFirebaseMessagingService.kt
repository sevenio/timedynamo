package com.tvisha.trooptime.activity.activity.fcm

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tvisha.trooptime.R
import com.tvisha.trooptime.activity.activity.HomeActivity
import com.tvisha.trooptime.activity.activity.api.ApiClient
import com.tvisha.trooptime.activity.activity.apiPostModels.CommonResponse
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys
import com.tvisha.trooptime.activity.activity.helper.Utilities
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by tvisha on 24/2/17.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val sharedPreferences by lazy {
        getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE)
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        callToserver(s)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (!sharedPreferences.getBoolean(SharePreferenceKeys.SP_LOGIN_STATUS, false)) {
            return
        }
        var c = sharedPreferences.getInt("notificationCount", 0)

        sharedPreferences.edit().putInt("notificationCount", c + 1).apply()
        // Log.e(TAG, "Notification Message TITLE: " + remoteMessage.getNotification().getTitle());
        //  Log.e(TAG, "Notification Message BODY: " + remoteMessage.getNotification().getBody());
        //  Log.e(TAG, "Notification Message DATA: " + remoteMessage.getData().toString());
        //Calling method to generate notification
        sendNotification(
            remoteMessage.notification?.title ?: "",
            remoteMessage.notification?.body ?: "",
            remoteMessage.data
        )
    }

    //This method is only generating push notification
    private fun sendNotification(
        messageTitle: String?,
        messageBody: String?,
        row: Map<String, String>
    ) {
        val notificationIntent = Intent(applicationContext, HomeActivity::class.java)
        val flags =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
            else PendingIntent.FLAG_ONE_SHOT
        val contentIntent = PendingIntent.getActivity(
            applicationContext, 0,
            notificationIntent, flags
        )
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notification = Notification.Builder(this)
        notification.setSmallIcon(notificationIcon)
        notification.setContentTitle(messageTitle)
        notification.setContentText(messageBody)
        notification.setAutoCancel(true)
        notification.setLargeIcon(
            BitmapFactory.decodeResource(
                resources,
                R.drawable.firebase_notification
            )
        )
            .setSound(defaultSoundUri)
            .setContentIntent(contentIntent)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(count, notification.build())
        count++


        /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notification.setColor(getResources().getColor(R.color.colorPrimaryDark));
        }*/
    }

    private val notificationIcon: Int
        private get() = R.drawable.ic_firebase_smallnotification

    fun callToserver(refreshedToken: String?) {
        try {
            if (sharedPreferences.getBoolean(SharePreferenceKeys.SP_LOGIN_STATUS, false)) {
                sendRegistrationToServer(
                    refreshedToken,
                    sharedPreferences.getString(SharePreferenceKeys.USER_ID, ""),
                    sharedPreferences.getString(SharePreferenceKeys.API_KEY, ""),
                    sharedPreferences.getString(SharePreferenceKeys.DEVICE_ID, "")
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendRegistrationToServer(
        refreshedToken: String?,
        user_id: String?,
        token: String?,
        deviceId: String?
    ) {
        if (Utilities.isNetworkAvailable(
                applicationContext
            )
        ) {
            callSaveFcmApi(refreshedToken, user_id, token, deviceId)
        }
    }

    private fun callSaveFcmApi(
        refreshedToken: String?,
        userId: String?,
        token: String?,
        deviceId: String?
    ) {
        val call =
            ApiClient.getInstance().saveFcmToken(token, userId, refreshedToken, "1", deviceId)
        call.enqueue(object : Callback<CommonResponse?> {
            override fun onResponse(
                call: Call<CommonResponse?>,
                response: Response<CommonResponse?>
            ) {
                val apiResponse = response.body()
                if (apiResponse != null) {
                }
            }

            override fun onFailure(call: Call<CommonResponse?>, t: Throwable) {}
        })
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        var count = 0
    }
}