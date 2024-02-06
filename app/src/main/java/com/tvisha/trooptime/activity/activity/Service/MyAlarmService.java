package com.tvisha.trooptime.activity.activity.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.tvisha.trooptime.activity.activity.Helper.SharePreferenceKeys;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by tvisha on 25/2/17.
 */
public class MyAlarmService extends Service {
    AlarmManager alarmManager;
    Bundle bundle;
    String userId, first_name, last_name, email;
    String per_start_time, onthe_way_time, strat_date, end_date, submit_date, moving, empty_string = "";
    Calendar today_calendar, current_time;
    long nowtime, settime, new_alaram_time, new_system_time;
    SharedPreferences preferences;
    int servicecount;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            sharedPreference();
            Calendar calendar = Calendar.getInstance();
            bundle = intent.getExtras();
            if (bundle != null) {
                getBundleData();
            }
            if (empty_string.trim().isEmpty()) {

                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                if (new_system_time != 0) {
                    settime = new_alaram_time;
                    nowtime = new_system_time;
                } else {
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, 9);
                    calendar.set(Calendar.MINUTE, 00);
                    calendar.set(Calendar.SECOND, 00);

                    settime = calendar.getTimeInMillis();
                    nowtime = System.currentTimeMillis();
                }

                SimpleDateFormat now_dateformat = new SimpleDateFormat("MMM dd yyyy HH:mm");
                String now_date = now_dateformat.format(nowtime);
                Date date_now = now_dateformat.parse(now_date);
                String set_date = now_dateformat.format(settime);
                Date date_set = now_dateformat.parse(set_date);


                AlarmReceiver receiver = new AlarmReceiver();
                IntentFilter intentFilter = new IntentFilter("com.tvishacrm.CUSTOM_INTENT");
                registerReceiver(receiver, intentFilter);

                Intent myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
                myIntent.setAction("com.tvishacrm.CUSTOM_INTENT");

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                if (pendingIntent != null) {
                    if (servicecount == 1) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                    } else {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, new_system_time, AlarmManager.INTERVAL_DAY, pendingIntent);
                    }
                }
            } else {
                claculateDatesForRemainder();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Service.START_STICKY;
    }

    private void claculateDatesForRemainder() {
        today_calendar = Calendar.getInstance();
        current_time = Calendar.getInstance();
        if (per_start_time != null && !per_start_time.trim().isEmpty()) {
            if (per_start_time.trim().toLowerCase().equals("01:00:00")) {
                today_calendar.add(Calendar.HOUR, 1);
            } else if (per_start_time.trim().toLowerCase().equals("02:00:00")) {
                today_calendar.add(Calendar.HOUR, 2);
            } else if (per_start_time.trim().toLowerCase().equals("half day")) {
                today_calendar.add(Calendar.HOUR, 4);
            }
        } else if (onthe_way_time != null && !onthe_way_time.trim().isEmpty()) {
            if (onthe_way_time.trim().toLowerCase().equals("00:15:00")) {
                today_calendar.add(Calendar.MINUTE, 15);
            } else if (onthe_way_time.trim().toLowerCase().equals("00:30:00")) {
                today_calendar.add(Calendar.MINUTE, 30);
            }
        } else if (end_date != null && !end_date.trim().isEmpty()) {
            Date dates = null;
            Date calculate_date = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            try {

                Date dd = dateFormat.parse(end_date + "-09-00-00");
                String ss = dateFormat.format(dd.getTime());

                String[] split_date = ss.split("-");
                int year = Integer.parseInt(split_date[0]);
                int month = Integer.parseInt(split_date[1]);
                int day = Integer.parseInt(split_date[2]);
                int hours = Integer.parseInt(split_date[3]);
                int minutes = Integer.parseInt(split_date[4]);
                int seconds = Integer.parseInt(split_date[5]);

                today_calendar.set(year, (month - 1), day, hours, minutes, seconds);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        sendNewNotification();
    }

    private void sendNewNotification() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTime(today_calendar.getTime());
        long system_time = System.currentTimeMillis();
        long alaram_set = calendar.getTimeInMillis();

        Intent myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        myIntent.setAction("com.tvishacrm.CUSTOM_INTENT");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        if (pendingIntent != null) {
            try {
                SimpleDateFormat now_dateformat = new SimpleDateFormat("MMM dd yyyy HH:mm");
                String now_date = now_dateformat.format(system_time);
                String set_date = now_dateformat.format(alaram_set);
                Date date_set = now_dateformat.parse(set_date);
                Date date_now = now_dateformat.parse(now_date);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void getBundleData() {
        per_start_time = bundle.getString("permission_time");
        onthe_way_time = bundle.getString("onthe_way_time");
        strat_date = bundle.getString("from_day");
        end_date = bundle.getString("to_day");
        submit_date = bundle.getString("send_time");
        empty_string = bundle.getString("select", "");
        //alramsettime       = bundle.getLong("alramsettime");
        /*new_alaram_time = bundle.getLong("newalarm_time");
        new_system_time = bundle.getLong("calendar_time");*/

    }

    private void sharedPreference() {
        preferences = getApplicationContext().getSharedPreferences(SharePreferenceKeys.SP_NAME, Context.MODE_PRIVATE);
        userId = (preferences.getString("user_id", ""));
        email = (preferences.getString("email", ""));
        first_name = (preferences.getString("first_name", ""));
        last_name = (preferences.getString("last_name", ""));
        moving = (preferences.getString("moved", ""));
        servicecount = (preferences.getInt("countforservice", 0));
    }

}
