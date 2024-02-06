package com.tvisha.trooptime.activity.activity.ApiHelper;

import android.content.Context;
import android.content.SharedPreferences;

import com.tvisha.trooptime.activity.activity.Helper.SharePreferenceKeys;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by koti on 2/9/16.
 */
public class SetApiDataFormat {
    public static String getApiData(JSONObject data, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharePreferenceKeys.SP_NAME, Context.MODE_PRIVATE);
        try {
            String return_data = "";
            if (data == null) {
                data = new JSONObject();
            }
            //data.put("token", Constants.TIME_SERVER_KEY);
            data.put("android_device_id", "testing");
            data.put("device_type", 0);  //type of os 0=>android
            if (sharedPreferences.getBoolean(SharePreferenceKeys.SP_LOGIN_STATUS, false) && sharedPreferences.getString("user_id", null) != null) {
                data.put("user_id", sharedPreferences.getString("user_id", null));
            }
            Iterator<String> iter = data.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                return_data += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(data.getString(key), "UTF-8");
                if (iter.hasNext()) {
                    return_data += "&";
                }
            }
            if (!return_data.equals("")) {
                return return_data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
