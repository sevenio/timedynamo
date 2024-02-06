package com.tvisha.trooptime.activity.activity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tvisha.trooptime.activity.activity.Helper.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akhil on 16/5/18.
 */

public class UserShiftTable extends DbContext {

    final public static String shift_id = "shift_id";
    final public static String shift_name = "shift_name";
    final public static String from_time = "from_time";
    final public static String to_time = "to_time";
    final public static String week_off = "week_off";
    final public static String created_at = "created_at";
    final public static String updated_at = "updated_at";
    final private static String TABLE_NAME = "user_shift";
    public static String CREATE_USERSHIFT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            shift_id + " Integer ," +
            shift_name + " Text ," +
            from_time + " Text ," +
            to_time + " Integer ," +
            week_off + " Text ," +
            created_at + " datetime," +
            updated_at + " datetime" +
            ");";
    final private SQLiteDatabase db = database;
    private Context context;

    public UserShiftTable(Context context) {
        super(context);
        this.context = context;
    }

    public boolean insertShift(UserShiftModel userShiftModel) {
        long status = 0;
        ContentValues data = new ContentValues();
        data.put(shift_id, userShiftModel.getShiftId());
        data.put(shift_name, userShiftModel.getShiftName());
        data.put(from_time, userShiftModel.getFromTime());
        data.put(to_time, userShiftModel.getToTime());
        data.put(week_off, userShiftModel.getWeekOff());
        data.put(created_at, Utilities.getCurrentDateTimeNew());
        data.put(updated_at, Utilities.getCurrentDateTimeNew());

        status = db.insert(TABLE_NAME, null, data);

        if (status > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean saveShift(UserShiftModel userShiftModel) {
        Cursor cursor = null;
        Boolean status = false;
        try {
            String query = "select count(" + shift_id + ") as count from " + TABLE_NAME + " WHERE " + shift_id + " = " + userShiftModel.getShiftId();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(cursor.getColumnIndex("count"));
                if (count > 0) {
                    ContentValues data = new ContentValues();
                    data.put(shift_name, userShiftModel.getShiftName());
                    data.put(from_time, userShiftModel.getFromTime());
                    data.put(to_time, userShiftModel.getToTime());
                    data.put(week_off, userShiftModel.getWeekOff());
                    data.put(updated_at, Utilities.getCurrentDateTimeNew());

                    long result = db.update(TABLE_NAME, data, shift_id + " = " + userShiftModel.getShiftId(), null);
                    if (result > 0) {
                        status = true;
                    }
                } else {
                    ContentValues data = new ContentValues();
                    data.put(shift_id, userShiftModel.getShiftId());
                    data.put(shift_name, userShiftModel.getShiftName());
                    data.put(from_time, userShiftModel.getFromTime());
                    data.put(to_time, userShiftModel.getToTime());
                    data.put(week_off, userShiftModel.getWeekOff());
                    data.put(created_at, Utilities.getCurrentDateTimeNew());
                    data.put(updated_at, Utilities.getCurrentDateTimeNew());
                    long result = db.insert(TABLE_NAME, null, data);
                    if (result > 0) {
                        status = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return status;
    }

    public List<UserShiftModel> getShifts() {
        //open();
        List<UserShiftModel> shifts = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " group by shift_id";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {

                do {
                    UserShiftModel shiftModel = new UserShiftModel();
                    shiftModel.setShiftId(cursor.getInt(cursor.getColumnIndex(shift_id)));
                    shiftModel.setShiftName(cursor.getString(cursor.getColumnIndex(shift_name)));
                    shiftModel.setFromTime(cursor.getString(cursor.getColumnIndex(from_time)));
                    shiftModel.setToTime(cursor.getString(cursor.getColumnIndex(to_time)));

                    shifts.add(shiftModel);

                } while (cursor.moveToNext());

            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        //close();
        return shifts;
    }
}


