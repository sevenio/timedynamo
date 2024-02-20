package com.tvisha.trooptime.activity.activity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tvisha.trooptime.activity.activity.helper.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;


public class UserBreakTable extends DbContext {

    final public static String id = "id";
    final public static String user_break_id = "user_break_id";
    final public static String break_id = "break_id";
    final public static String a_id = "a_id";
    final public static String attendance_id = "attendance_id";
    final public static String start_time = "start_time";
    final public static String end_time = "end_time";
    final public static String is_sync = "is_sync";
    final public static String type = "type";
    final public static String check_in_branch_id = "check_in_branch_id";
    final public static String check_out_branch_id = "check_out_branch_id";
    final public static String created_at = "created_at";
    final public static String updated_at = "updated_at";
    final private static String TABLE_NAME = "user_break";
    public static String CREATE_BREAK_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            id + " Integer PRIMARY KEY AUTOINCREMENT," +
            user_break_id + " Integer," +
            break_id + " Integer," +
            a_id + " Integer," +
            attendance_id + " Integer ," +
            start_time + " datetime ," +
            end_time + " datetime ," +
            is_sync + " Integer ," +
            type + " Integer ," +
            check_out_branch_id + " Integer ," +
            check_in_branch_id + " Integer ," +
            created_at + " datetime," +
            updated_at + " datetime" +
            ");";
    final private SQLiteDatabase db = database;
    private Context context;

    public UserBreakTable(Context context) {
        super(context);
        this.context = context;
    }

    public boolean insertBreak(UserBreakModel userBreakModel) {
        long status = 0;
        ContentValues data = new ContentValues();
        data.put(user_break_id, userBreakModel.getUserBreakId());
        data.put(break_id, userBreakModel.getBreakId());
        data.put(a_id, userBreakModel.getaId());
        data.put(attendance_id, userBreakModel.getAttendanceId());
        data.put(start_time, userBreakModel.getStartTime());
        data.put(end_time, userBreakModel.getEndTime());
        data.put(is_sync, userBreakModel.getIsSync());
        data.put(type, userBreakModel.getType());
        data.put(check_in_branch_id, userBreakModel.getCheck_in_branch_id());
        data.put(check_out_branch_id, userBreakModel.getCheck_out_branch_id());
        data.put(created_at, Utilities.getCurrentDateTimeNew());
        data.put(updated_at, Utilities.getCurrentDateTimeNew());

        status = db.insert(TABLE_NAME, null, data);

        if (status > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void checkAndInsertUserBreak(UserBreakModel userBreakModel) {
        Cursor cursor = null;
        try {

            String query = "select count(" + id + ") as count from " + TABLE_NAME + " WHERE " + user_break_id + " = " + userBreakModel.getUserBreakId();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                if (cursor.getInt(cursor.getColumnIndex("count")) > 0) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(user_break_id, userBreakModel.getUserBreakId());
                    contentValues.put(break_id, userBreakModel.getBreakId());
                    contentValues.put(a_id, userBreakModel.getaId());
                    contentValues.put(attendance_id, userBreakModel.getUserBreakId());
                    contentValues.put(start_time, userBreakModel.getUserBreakId());
                    contentValues.put(end_time, userBreakModel.getUserBreakId());
                    contentValues.put(is_sync, userBreakModel.getIsSync());
                    contentValues.put(type, userBreakModel.getType());
                    contentValues.put(check_in_branch_id, userBreakModel.getCheck_in_branch_id());
                    contentValues.put(check_out_branch_id, userBreakModel.getCheck_out_branch_id());
                    db.update(TABLE_NAME, contentValues, user_break_id + " = " + userBreakModel.getUserBreakId(), null);
                } else {
                    insertBreak(userBreakModel);
                }
            } else {
                insertBreak(userBreakModel);
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public boolean updateBreak(ContentValues data, String where) {
        long status = 0;
        data.put(updated_at, Utilities.getCurrentDateTimeNew());

        status = db.update(TABLE_NAME, data, where, null);

        if (status > 0) {
            return true;
        } else {
            return false;
        }
    }

    public UserBreakModel getUserCurrentRunningBreak(String attendanceId) {
        UserBreakModel res = new UserBreakModel();
        Cursor cursor = null;
        try {

            String query = "select * from " + TABLE_NAME + " WHERE " + a_id + " = " + attendanceId + " order by " + start_time + " DESC," + id + " DESC limit 1";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                if (cursor.getString(cursor.getColumnIndex(end_time)) == null || cursor.getString(cursor.getColumnIndex(end_time)).equals("") || cursor.getString(cursor.getColumnIndex(end_time)).equals("0000-00-00 00:00:00")) {

                    res.setaId(cursor.getInt(cursor.getColumnIndex(a_id)));
                    res.setAttendanceId(cursor.getInt(cursor.getColumnIndex(attendance_id)));
                    res.setBreakId(cursor.getInt(cursor.getColumnIndex(break_id)));
                    res.setId(cursor.getInt(cursor.getColumnIndex(id)));
                    res.setIsSync(cursor.getInt(cursor.getColumnIndex(is_sync)));
                    res.setStartTime(cursor.getString(cursor.getColumnIndex(start_time)));
                    res.setEndTime(cursor.getString(cursor.getColumnIndex(end_time)));
                    res.setType(cursor.getInt(cursor.getColumnIndex(type)));
                    res.setCheck_in_branch_id(cursor.getInt(cursor.getColumnIndex(check_in_branch_id)));
                    res.setCheck_out_branch_id(cursor.getInt(cursor.getColumnIndex(check_out_branch_id)));
                }
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return res;
    }

    public JSONArray getNonSyncBreaks() {
        JSONArray jsonArray = new JSONArray();
        Cursor cursor = null;
        try {
            String query = "select * from " + TABLE_NAME + " WHERE " + is_sync + " = 0 and " + attendance_id + " <> 0";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(user_break_id, cursor.getString(cursor.getColumnIndex(user_break_id)));
                        jsonObject.put(break_id, cursor.getString(cursor.getColumnIndex(break_id)));
                        jsonObject.put(attendance_id, cursor.getString(cursor.getColumnIndex(attendance_id)));
                        jsonObject.put(start_time, cursor.getString(cursor.getColumnIndex(start_time)));
                        jsonObject.put(end_time, cursor.getString(cursor.getColumnIndex(end_time)));
                        jsonObject.put(is_sync, cursor.getString(cursor.getColumnIndex(is_sync)));
                        jsonObject.put(type, cursor.getString(cursor.getColumnIndex(type)));
                        jsonObject.put(check_in_branch_id, cursor.getString(cursor.getColumnIndex(check_in_branch_id)));
                        jsonObject.put(check_out_branch_id, cursor.getString(cursor.getColumnIndex(check_out_branch_id)));
                        String referenceId = Utilities.getReferenceId(this.context) + "-" + cursor.getString(cursor.getColumnIndex(id));
                        jsonObject.put("reference_id", referenceId);
                        jsonArray.put(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return jsonArray;
    }


    public Boolean updateBreaks(JSONArray jsonArray) {
        Boolean status = false;
        Cursor cursor = null;
        try {

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                AttendanceTable attendanceTable = new AttendanceTable(this.context);
                int aid = attendanceTable.getAid(jsonObject.getString(attendance_id));

                if (jsonObject.has("reference_id")) {
                    int bid = Utilities.parseReferenceId(this.context, jsonObject.getString("reference_id"));
                    if (bid != 0) {
                        ContentValues data = new ContentValues();
                        data.put(user_break_id, jsonObject.getInt(user_break_id));
                        data.put(is_sync, 1);
                        data.put(updated_at, Utilities.getCurrentDateTimeNew());
                        db.update(TABLE_NAME, data, id + " = " + bid, null);
                    } else {
                        UserBreakModel userBreakModel = new UserBreakModel();
                        userBreakModel.setUserBreakId(jsonObject.getInt(user_break_id));
                        userBreakModel.setaId(aid);
                        userBreakModel.setBreakId(jsonObject.getInt(break_id));
                        userBreakModel.setAttendanceId(jsonObject.getInt(attendance_id));
                        userBreakModel.setStartTime(jsonObject.getString(start_time));
                        userBreakModel.setEndTime(jsonObject.getString(end_time));
                        userBreakModel.setType(jsonObject.getInt(type));
                        userBreakModel.setCheck_in_branch_id(jsonObject.getInt(check_in_branch_id));
                        userBreakModel.setCheck_out_branch_id(jsonObject.getInt(check_out_branch_id));
                        userBreakModel.setIsSync(1);
                        checkAndInsertUserBreak(userBreakModel);
                    }
                } else {
                    UserBreakModel userBreakModel = new UserBreakModel();
                    userBreakModel.setUserBreakId(jsonObject.getInt(user_break_id));
                    userBreakModel.setaId(aid);
                    userBreakModel.setBreakId(jsonObject.getInt(break_id));
                    userBreakModel.setAttendanceId(jsonObject.getInt(attendance_id));
                    userBreakModel.setStartTime(jsonObject.getString(start_time));
                    userBreakModel.setEndTime(jsonObject.getString(end_time));
                    userBreakModel.setType(jsonObject.getInt(type));
                    userBreakModel.setCheck_in_branch_id(jsonObject.getInt(check_in_branch_id));
                    userBreakModel.setCheck_out_branch_id(jsonObject.getInt(check_out_branch_id));
                    userBreakModel.setIsSync(1);
                    checkAndInsertUserBreak(userBreakModel);
                }
            }
            status = true;
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return status;
    }

    public Boolean saveBreaks(UserBreakModel userBreakModel) {
        Boolean status = false;
        Cursor cursor = null;
        try {
            AttendanceTable attendanceTable = new AttendanceTable(this.context);
            int aid = attendanceTable.getAid(userBreakModel.getAttendanceId() + "");

            if (userBreakModel.getReferenceId() != null && !userBreakModel.getReferenceId().equals("")) {
                int bid = Utilities.parseReferenceId(this.context, userBreakModel.getReferenceId());
                if (bid != 0) {
                    ContentValues data = new ContentValues();
                    data.put(user_break_id, userBreakModel.getUserBreakId());
                    data.put(is_sync, 1);
                    data.put(updated_at, Utilities.getCurrentDateTimeNew());
                    db.update(TABLE_NAME, data, id + " = " + bid, null);
                }
                return true;
            }

            String query = "select count(" + user_break_id + ") as count from " + TABLE_NAME + " WHERE " + user_break_id + " = " + userBreakModel.getUserBreakId();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(cursor.getColumnIndex("count"));
                if (count > 0) {
                    ContentValues data = new ContentValues();
                    data.put(break_id, userBreakModel.getBreakId());
                    data.put(a_id, aid);
                    data.put(attendance_id, userBreakModel.getAttendanceId());
                    data.put(start_time, userBreakModel.getStartTime());
                    data.put(type, userBreakModel.getType());
                    data.put(check_in_branch_id, userBreakModel.getCheck_in_branch_id());
                    data.put(check_out_branch_id, userBreakModel.getCheck_out_branch_id());
                    if (userBreakModel.getEndTime() != null && userBreakModel.getEndTime().equals("0000-00-00 00:00:00")) {
                        data.put(end_time, userBreakModel.getEndTime());
                    }
                    data.put(updated_at, Utilities.getCurrentDateTimeNew());

                    db.update(TABLE_NAME, data, user_break_id + " = " + userBreakModel.getUserBreakId(), null);
                } else {
                    userBreakModel.setaId(aid);
                    insertBreak(userBreakModel);
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
            status = true;
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return status;
    }

}



