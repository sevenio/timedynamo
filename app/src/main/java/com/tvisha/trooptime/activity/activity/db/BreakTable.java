package com.tvisha.trooptime.activity.activity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tvisha.trooptime.activity.activity.helper.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akhil on 16/5/18.
 */

public class BreakTable extends DbContext {

    final public static String break_id = "break_id";
    final public static String shift_id = "shift_id";
    final public static String break_name = "break_name";
    final public static String break_time = "break_time";
    final public static String created_at = "created_at";
    final public static String updated_at = "updated_at";
    final private static String TABLE_NAME = "break";
    public static String CREATE_BREAK_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            break_id + " Integer," +
            shift_id + " Integer," +
            break_name + " Text ," +
            break_time + " Text ," +
            created_at + " datetime," +
            updated_at + " datetime" +
            ");";
    final private SQLiteDatabase db = database;
    private Context context;

    public BreakTable(Context context) {
        super(context);
        this.context = context;
    }

    public boolean insertBreak(BreakModel breakModel) {
        long status = 0;
        ContentValues data = new ContentValues();
        data.put(break_id, breakModel.getBreakId());
        data.put(shift_id, breakModel.getBreakId());
        data.put(break_name, breakModel.getBreakName());
        data.put(created_at, Utilities.getCurrentDateTimeNew());
        data.put(updated_at, Utilities.getCurrentDateTimeNew());

        status = db.insert(TABLE_NAME, null, data);

        if (status > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean saveBreak(BreakModel breakModel) {
        Cursor cursor = null;
        Boolean status = false;
        try {
            String query = "select count(" + break_id + ") as count from " + TABLE_NAME + " WHERE " + break_id + " = " + breakModel.getBreakId();
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                int count = cursor.getInt(cursor.getColumnIndex("count"));
                if (count > 0) {
                    ContentValues data = new ContentValues();
                    data.put(break_name, breakModel.getBreakName());
                    data.put(break_time, breakModel.getBreakName());
                    data.put(updated_at, Utilities.getCurrentDateTimeNew());

                    long result = db.update(TABLE_NAME, data, break_id + " = " + breakModel.getBreakId(), null);
                    if (result > 0) {
                        status = true;
                    }
                } else {
                    ContentValues data = new ContentValues();
                    data.put(break_id, breakModel.getBreakId());
                    data.put(shift_id, breakModel.getShiftId());
                    data.put(break_name, breakModel.getBreakName());
                    data.put(break_time, breakModel.getBreakTime());
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

    public BreakModel getBreakById(int breakId) {
        Cursor cursor = null;
        BreakModel breakModel = new BreakModel();

        try {
            String query = "select * from " + TABLE_NAME + " WHERE " + break_id + " = " + breakId;
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                breakModel.setBreakId(cursor.getInt(cursor.getColumnIndex(break_id)));
                breakModel.setShiftId(cursor.getInt(cursor.getColumnIndex(shift_id)));
                breakModel.setBreakName(cursor.getString(cursor.getColumnIndex(break_name)));
                breakModel.setBreakTime(cursor.getString(cursor.getColumnIndex(break_time)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return breakModel;
    }

    public List<BreakModel> getAllBreaks() {
        Cursor cursor = null;
        List<BreakModel> breakModels = new ArrayList<>();
        try {
            String query = "select * from " + TABLE_NAME + " group by break_id";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    BreakModel breakModel = new BreakModel();
                    breakModel.setBreakId(cursor.getInt(cursor.getColumnIndex(break_id)));
                    breakModel.setShiftId(cursor.getInt(cursor.getColumnIndex(shift_id)));
                    breakModel.setBreakName(cursor.getString(cursor.getColumnIndex(break_name)));
                    breakModel.setBreakTime(cursor.getString(cursor.getColumnIndex(break_time)));
                    breakModels.add(breakModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return breakModels;
    }

    public List<BreakModel> getShiftBreaks(int shiftId) {
        Cursor cursor = null;
        List<BreakModel> breakModels = new ArrayList<>();
        try {
            String query = "select * from " + TABLE_NAME + " WHERE " + shift_id + " = " + shiftId + " group by break_id";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    BreakModel breakModel = new BreakModel();
                    breakModel.setBreakId(cursor.getInt(cursor.getColumnIndex(break_id)));
                    breakModel.setShiftId(cursor.getInt(cursor.getColumnIndex(shift_id)));
                    breakModel.setBreakName(cursor.getString(cursor.getColumnIndex(break_name)));
                    breakModel.setBreakTime(cursor.getString(cursor.getColumnIndex(break_time)));
                    breakModels.add(breakModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return breakModels;
    }

    public int getShiftBreaksCount(int shiftId) {
        Cursor cursor = null;
        int count = 0;
        try {
            String query = "select count(" + break_id + ") as count from " + TABLE_NAME + " WHERE " + shift_id + " = " + shiftId + " group by break_id";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(cursor.getColumnIndex("count"));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return count;
    }

    public Boolean removeBreaks() {
        Boolean status = false;
        try {
            int delete = db.delete(TABLE_NAME, "", null);
            if (delete > 0) {
                status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

}



