package com.tvisha.trooptime.activity.activity.api_handlers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shiva prasad on 16/08/2016.
 */
public class JSONHandlers {

    public static int getInt(JSONObject obj, String key) {
        try {
            return obj.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getString(JSONObject obj, String key) {
        try {
            return obj.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return " ";
        }
    }

    public static boolean getBoolean(JSONObject obj, String key) {
        try {
            return obj.getBoolean(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static JSONArray getJSONArray(JSONObject obj, String key) {
        try {
            return obj.getJSONArray(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public static JSONObject getJSONObject(JSONObject obj, String key) {
        try {
            return obj.getJSONObject(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public static int getInt(JSONArray obj, int index) {
        try {
            return obj.getInt(index);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getString(JSONArray obj, int index) {
        try {
            return obj.getString(index);
        } catch (JSONException e) {
            e.printStackTrace();
            return " ";
        }
    }

    public static boolean getBoolean(JSONArray obj, int index) {
        try {
            return obj.getBoolean(index);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static JSONArray getJSONArray(JSONArray obj, int index) {
        try {
            return obj.getJSONArray(index);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public static JSONObject getJSONObject(JSONArray obj, int index) {
        try {
            return obj.getJSONObject(index);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public static void put(JSONObject ansFieldsJSON, String key, String val) {
        try {
            ansFieldsJSON.put(key, val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void put(JSONObject ansFieldsJSON, String key, JSONObject val) {
        try {
            ansFieldsJSON.put(key, val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void put(JSONObject ansFieldsJSON, String key, JSONArray val) {
        try {
            ansFieldsJSON.put(key, val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void put(JSONObject ansFieldsJSON, String key, int val) {
        try {
            ansFieldsJSON.put(key, val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void put(JSONObject ansFieldsJSON, String key, char val) {
        try {
            ansFieldsJSON.put(key, val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void put(JSONObject ansFieldsJSON, String key, Object val) {
        try {
            ansFieldsJSON.put(key, val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
