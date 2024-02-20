package com.tvisha.trooptime.activity.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.tvisha.trooptime.activity.activity.model.PhoneNumberModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tvisha on 14/4/17.
 */

public class DbHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "MyDb";
    public static final String TBNAME = "phone_table";
    public static final String MOBILENO = "mobile_no";
    public static final String NAME = "name";
    private static final int DB_VERSION = 1;
    public static String CREATE_NOTIFICATION_TABLE = "CREATE TABLE IF NOT EXISTS " + TBNAME + " (" +
            MOBILENO + " TEXT," +
            NAME + " TEXT);";
    SQLiteDatabase db;

    public DbHelper(Context context) {
        super(context, DBNAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_NOTIFICATION_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insesrtRecord(String name, String mobile_num) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(MOBILENO, mobile_num);
        db.insert(TBNAME, null, values);
    }

    public List<PhoneNumberModel> getPhoneNumberData() {
        List<PhoneNumberModel> items_list = new ArrayList<PhoneNumberModel>();
        try {
            String query = "select * from " + TBNAME;
            db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    PhoneNumberModel model = new PhoneNumberModel();
                    model.setReportName(cursor.getString(cursor.getColumnIndex(NAME)));
                    model.setReportPhoneNumber(cursor.getString(cursor.getColumnIndex(MOBILENO)));
                    items_list.add(model);
                } while (cursor.moveToNext());
                return items_list;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
