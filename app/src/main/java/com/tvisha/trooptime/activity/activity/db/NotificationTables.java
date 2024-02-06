package com.tvisha.trooptime.activity.activity.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import androidx.lifecycle.LiveData;

import com.tvisha.trooptime.activity.activity.Model.Notification;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationTables extends DbContext{
    final private SQLiteDatabase db = database;
    private Context context;
    public static final String TABLE_NAME = "notification";
    public static final String ID = "id";
    public static final String NOTIFICATION_DATA = "notification_data";
    public static final String NOTIFICATION_TYPE = "notification_type";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";


    public NotificationTables(Context context) {
        super(context);
        this.context = context;
    }

    public static String CREATE_NOTIFICATION_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (" +
            ID + "Integer PRIMARY KEY AUTOINCREMENT, "+
            NOTIFICATION_DATA + " TEXT , "+
            NOTIFICATION_DATA + " TEXT , "+
            CREATED_AT +" datetime , "+
            UPDATED_AT +" datetime , "+
            ")";

    public void saveNotification(JSONObject jsonObject){
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(NOTIFICATION_DATA,jsonObject.toString());
            contentValues.put(NOTIFICATION_TYPE,jsonObject.optInt("type"));
            contentValues.put(CREATED_AT,jsonObject.optString("created_at"));
            contentValues.put(UPDATED_AT,jsonObject.optString("updated_at"));
            db.insert(TABLE_NAME,null,contentValues);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @SuppressLint("Range")
    public  ArrayList<Notification> getNotifications(int type,int offset){
        ArrayList<Notification> notificationList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM notification WHERE "+NOTIFICATION_TYPE+" = "+type+" order by created_at desc limit 50 offset "+offset;
            cursor = db.rawQuery(query,null);
            if (cursor!=null && cursor.moveToFirst()){
                do {
                    Notification notification = new Notification();
                    notification.setCreated_at(cursor.getString(cursor.getColumnIndex(CREATED_AT)));
                    notification.setUpdated_at(cursor.getString(cursor.getColumnIndex(UPDATED_AT)));
                    notification.setnotificationdata(cursor.getString(cursor.getColumnIndex(NOTIFICATION_DATA)));
                    notification.setnotificationtype(cursor.getInt(cursor.getColumnIndex(NOTIFICATION_TYPE)));
                    notificationList.add(notification);
                }while (cursor.moveToNext());

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (cursor!=null){
                cursor.close();
            }
        }
        return notificationList;
    }
    public LiveData<List<Notification>> getNotificationss(int type){
        return null;
    }
}
