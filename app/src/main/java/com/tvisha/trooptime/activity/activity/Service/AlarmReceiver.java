package com.tvisha.trooptime.activity.activity.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import android.widget.Toast;


import com.tvisha.trooptime.activity.activity.ApiHelper.ApiCallHelper;
import com.tvisha.trooptime.activity.activity.Helper.Constants;
import com.tvisha.trooptime.activity.activity.Helper.NotificationHelper;
import com.tvisha.trooptime.activity.activity.Helper.ServerUrls;
import com.tvisha.trooptime.activity.activity.Helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.Helper.Utilities;
import com.tvisha.trooptime.activity.activity.NotificationActivityNew;
import com.tvisha.trooptime.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by tvisha on 25/2/17.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String TAG = "MyFirebaseMsgService";
    Context app_context;
    String userId, first_name, last_name, email, today_date, current_date, checkIn_time;
    List<String> dateList, checkinList;
    boolean ischecked = false, ismoved = false;
    NotificationCompat.Builder mBuilder;
    int servicecount;
    SharedPreferences shared;
    SharedPreferences.Editor editor;


    @Override
    public void onReceive(final Context context, final Intent intent) {
        app_context = context;
        if (intent.getAction().equals("com.tvishacrm.CUSTOM_INTENT")) {


        } else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {


            if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
                Bundle bundle = intent.getExtras();
                JSONObject object = new JSONObject();
                Set<String> keys = bundle.keySet();
                for (String key : keys) {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            object.put(key, JSONObject.wrap(bundle.get(key)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                NotificationHelper.getInstatance().sendNotification(context, object.optString("gcm.notification.title"), object.optString("gcm.notification.body"));

            }
        }
    }

    public void setAlaram() {
        Calendar alarm_cal = Calendar.getInstance();

        alarm_cal.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String to_date = new SimpleDateFormat("yyyy-MM-dd").format(alarm_cal.getTime());

        try {
            Date to_day = dateFormat.parse(to_date + "-09-00-00");
            Date tomorrow = new Date(to_day.getTime() + (1000 * 60 * 60 * 24));

            String ss = dateFormat.format(tomorrow.getTime());


            String[] split_date = ss.split("-");
            int year = Integer.parseInt(split_date[0]);
            int month = Integer.parseInt(split_date[1]);
            int day = Integer.parseInt(split_date[2]);
            int hours = Integer.parseInt(split_date[3]);
            int minutes = Integer.parseInt(split_date[4]);
            int seconds = Integer.parseInt(split_date[5]);

            alarm_cal.set(year, (month - 1), day, hours, minutes, seconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long system_time = alarm_cal.getTimeInMillis();
        long alaram_set = alarm_cal.getTimeInMillis();
        Intent intent = new Intent(app_context, MyAlarmService.class);
        intent.putExtra("select", "");
        intent.putExtra("newalarm_time", alaram_set);
        intent.putExtra("calendar_time", system_time);
        app_context.startService(intent);
    }


    private void sharedPreference() {
        shared = app_context.getSharedPreferences(SharePreferenceKeys.SP_NAME, app_context.MODE_PRIVATE);
        userId = (shared.getString(SharePreferenceKeys.USER_ID, ""));
        email = (shared.getString(SharePreferenceKeys.USER_EMAIL, ""));
        first_name = (shared.getString(SharePreferenceKeys.USER_NAME, ""));
        last_name = (shared.getString(SharePreferenceKeys.USER_NAME, ""));
        ischecked = (shared.getBoolean(SharePreferenceKeys.IS_CHECKED, false));
        servicecount = (shared.getInt("countforservice", 0));
    }


    private void store() {

        /*Intent popup_activity = new Intent(context, NotificationActivity.class);
            popup_activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            popup_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(popup_activity);*/
        if (Utilities.getConnectivityStatus(app_context) > 0) {
            if (!ismoved) {
                sharedPreference();
                final Calendar calendar = Calendar.getInstance();
                current_date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
                dateList = new ArrayList<>();
                checkinList = new ArrayList<>();
                Map<String, String> data = new HashMap();
                data.put("user_id", userId);
                data.put("security_key", Constants.SERVER_KEY);
                final ApiCallHelper apiCallHelper = new ApiCallHelper(app_context, ServerUrls.Self_Attendence, data);
                apiCallHelper.execute();
                apiCallHelper.setApiCallBackListener(new ApiCallHelper.ApiCallBackListener() {
                    @Override
                    public void onCompleteTask(String responce) {
                        try {
                            JSONObject object = new JSONObject(responce);
                            if (object.getBoolean("success")) {
                                JSONArray array = new JSONArray(object.optString("data"));
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    today_date = jsonObject.getString("date");
                                    checkIn_time = jsonObject.getString("check_in");
                                    dateList.add(jsonObject.getString("date"));
                                    checkinList.add(jsonObject.getString("check_in"));
                                }
                            }
                            if (dateList.size() > 0) {
                                for (int i = 0; i < dateList.size(); i++) {
                                    String list_date = dateList.get(i);
                                    if (list_date.trim().toLowerCase().equals(current_date.trim().toLowerCase())) {
                                        String checkin = checkinList.get(i);
                                        if (checkin != null && !checkin.trim().isEmpty()) {
                                            ischecked = false;
                                            editor = shared.edit();
                                            editor.putBoolean("ischecked", ischecked);
                                            editor.putInt("countforservice", 2);
                                            editor.apply();
                                            Toast.makeText(app_context, "you are checked in", Toast.LENGTH_LONG).show();
                                            setAlaram();
                                        } else {
                                            if (servicecount != 1) {
                                                sendingNotification();
                                                Intent intent1 = new Intent(app_context, NotificationActivityNew.class);
                                                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                app_context.startActivity(intent1);
                                            } else {
                                                editor = shared.edit();
                                                editor.putInt("countforservice", 2);
                                                editor.apply();
                                                setAlaram();
                                            }
                                        }
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    private void sendingNotification() {

                        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        mBuilder = new NotificationCompat.Builder(app_context)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Troop Time")
                                .setContentText("Alert troop time notification")
                                .setSound(defaultSoundUri)
                                .setVibrate(new long[]{1000, 1000, 1000, 1000})
                                .setAutoCancel(true);
                        Intent intent = new Intent(app_context, NotificationActivityNew.class);
                        PendingIntent pi = PendingIntent.getActivity(app_context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                        mBuilder.setContentIntent(pi);
                        NotificationManager mNotificationManager = (NotificationManager) app_context.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(0, mBuilder.build());
                    }




                    @Override
                    public void onFailedToCompleteTask(Exception e) {
                    }

                    @Override
                    public void onRequestTimeout(String res) {

                    }
                });
            }
        }
    }
}
