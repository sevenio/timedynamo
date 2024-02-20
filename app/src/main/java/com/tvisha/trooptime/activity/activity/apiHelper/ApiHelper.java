package com.tvisha.trooptime.activity.activity.apiHelper;

import android.content.Context;

import com.tvisha.trooptime.activity.activity.helper.Helper;

import org.json.JSONObject;

/**
 * Created by koti on 1/2/17.
 */
public class ApiHelper {
    private static ApiHelper ourInstance = new ApiHelper();

    private ApiHelper() {
    }

    public static ApiHelper getInstance() {
        if (ourInstance == null) {
            ourInstance = new ApiHelper();
        }
        return ourInstance;
    }

    public JSONObject requestServer(Context context, JSONObject req_data, final String url) {
        JSONObject data = null;
        try {
            ApiHandler apiHandler = new ApiHandler();
            data = Helper.stringToJsonObject(apiHandler.execute(url, SetApiDataFormat.getApiData(req_data, context)).get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

}
