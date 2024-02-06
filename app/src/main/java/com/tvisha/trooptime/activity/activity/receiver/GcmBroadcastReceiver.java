package com.tvisha.trooptime.activity.activity.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.tvisha.trooptime.activity.activity.Helper.NotificationHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

public class GcmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

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
