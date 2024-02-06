package com.tvisha.trooptime.activity.activity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tvisha.trooptime.activity.activity.Helper.Constants;
import com.tvisha.trooptime.activity.activity.Helper.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by akhil on 16/5/18.
 */

public class AttendanceTable extends DbContext {

    final public static String id = "id";
    final public static String attendance_id = "attendance_id";
    final public static String user_id = "user_id";
    final public static String check_in = "check_in";
    final public static String check_out = "check_out";
    final public static String is_checkin_manual = "is_checkin_manual";
    final public static String is_checkout_manual = "is_checkout_manual";
    final public static String is_sync = "is_sync";
    final public static String check_in_branch_id = "check_in_branch_id";
    final public static String check_out_branch_id = "check_out_branch_id";
    final public static String created_at = "created_at";
    final public static String updated_at = "updated_at";
    final private static String TABLE_NAME = "attendace";
    public static String CREATE_ATTENDANCE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            id + " Integer PRIMARY KEY AUTOINCREMENT," +
            attendance_id + " Integer," +
            user_id + " Integer ," +
            check_in + "  datetime," +
            check_out + " datetime," +
            is_checkin_manual + " Integer," +
            is_checkout_manual + " Integer," +
            is_sync + " Integer," +
            check_out_branch_id + " Integer ," +
            check_in_branch_id + " Integer ," +
            created_at + " datetime," +
            updated_at + " datetime" +
            ");";
    final private SQLiteDatabase db = database;
    private Context context;

    public AttendanceTable(Context context) {
        super(context);
        this.context = context;
    }

    public JSONArray getUsersNonSyncAttendance() {

        JSONArray attendanceArray = new JSONArray();
        Cursor cursor = null;
        try {
            String query = "select * from " + TABLE_NAME + " WHERE " + is_sync + " = '0' ORDER BY " + created_at + " DESC ";
            cursor = db.rawQuery(query, null);
            String data = "";
            if (cursor.moveToFirst()) {
                do {
                    JSONObject attendanceObj = new JSONObject();
                    attendanceObj.put("user_id", cursor.getString(cursor.getColumnIndex(user_id)));
                    attendanceObj.put("check_in", cursor.getString(cursor.getColumnIndex(check_in)));
                    attendanceObj.put("check_out", cursor.getString(cursor.getColumnIndex(check_out)));
                    attendanceObj.put("is_checkin_manual", cursor.getString(cursor.getColumnIndex(is_checkin_manual)));
                    attendanceObj.put("is_checkout_manual", cursor.getString(cursor.getColumnIndex(is_checkout_manual)));
                    attendanceObj.put("check_in_branch_id", cursor.getString(cursor.getColumnIndex(check_in_branch_id)));
                    attendanceObj.put("check_out_branch_id", cursor.getString(cursor.getColumnIndex(check_out_branch_id)));
                    attendanceArray.put(attendanceObj);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) cursor.close();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return attendanceArray;
    }

    public void checkAttendanceIdAndInsert(AttendanceModel attendanceModel) {
        Cursor cursor = null;
        try {

            if (attendanceModel.getAttendanceId() == 0) {
                insertAttendance(attendanceModel);
            } else {
                String query = "SELECT count(" + id + ") as count FROM " + TABLE_NAME + " WHERE " + attendance_id + " = " + attendanceModel.getAttendanceId();
                cursor = db.rawQuery(query, null);
                try {
                    if (cursor.moveToNext()) {
                        if (cursor.getInt(cursor.getColumnIndex("count")) > 0) {
                            ContentValues data = new ContentValues();
                            data.put(attendance_id, attendanceModel.getAttendanceId());
                            data.put(check_in, attendanceModel.getCheckIn());
                            data.put(check_out, attendanceModel.getCheckOut());
                            data.put(is_checkin_manual, attendanceModel.getIsCheckinManual());
                            data.put(is_checkout_manual, attendanceModel.getIsCheckoutManual());
                            data.put(check_in_branch_id, attendanceModel.getCheck_in_branch_id());
                            data.put(check_out_branch_id, attendanceModel.getCheck_out_branch_id());
                            data.put(is_sync, attendanceModel.getIsSync());
                            db.update(TABLE_NAME, data, attendance_id + " = " + attendanceModel.getAttendanceId(), null);
                        } else {
                            insertAttendance(attendanceModel);
                        }
                    } else {
                        insertAttendance(attendanceModel);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public boolean insertAttendance(AttendanceModel attendanceModel) {
        long status = 0;
        ContentValues data = new ContentValues();
        data.put(attendance_id, attendanceModel.getAttendanceId());
        data.put(user_id, attendanceModel.getUserId());
        data.put(check_in, attendanceModel.getCheckIn());
        data.put(check_out, attendanceModel.getCheckOut());
        data.put(is_checkin_manual, attendanceModel.getIsCheckinManual());
        data.put(is_checkout_manual, attendanceModel.getIsCheckoutManual());
        data.put(check_in_branch_id, attendanceModel.getCheck_in_branch_id());
        data.put(check_out_branch_id, attendanceModel.getCheck_out_branch_id());
        data.put(is_sync, attendanceModel.getIsSync());
        data.put(created_at, Utilities.getCurrentDateTimeNew());
        data.put(updated_at, Utilities.getCurrentDateTimeNew());

        status = db.insert(TABLE_NAME, null, data);
        if (status > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean checkAndInsertAttendance(AttendanceModel attendanceModel) {
        Boolean status = false;
        Cursor cursor = null;
        long result = 0;
        try {

            if (attendanceModel.getUserId() == null) {
                return false;
            }

            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + user_id + " = " + attendanceModel.getUserId() + " ORDER BY " + check_in + " DESC," + id + " DESC limit 1";
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0 && attendanceModel.getIsCheckinManual() == 0) {
                if (cursor.moveToFirst()) {
                    AttendanceModel userAttandanceModel = new AttendanceModel();
                    userAttandanceModel.setUserId(cursor.getString(cursor.getColumnIndex(user_id)));
                    userAttandanceModel.setCheckIn(cursor.getString(cursor.getColumnIndex(check_in)));
                    userAttandanceModel.setCheckOut(cursor.getString(cursor.getColumnIndex(check_out)));
                    userAttandanceModel.setId(cursor.getInt(cursor.getColumnIndex(id)));
                    if (!(userAttandanceModel.getCheckIn().equals("") || userAttandanceModel.getCheckIn().equals("0000-00-00 00:00:00")) && (userAttandanceModel.getCheckOut().equals("") || userAttandanceModel.getCheckOut().equals("0000-00-00 00:00:00"))) {
                        ContentValues data = new ContentValues();
                        data.put(check_out, attendanceModel.getCheckOut());
                        data.put(is_checkout_manual, attendanceModel.getIsCheckoutManual());
                        data.put(is_sync, 0);
                        data.put(check_in_branch_id, attendanceModel.getCheck_in_branch_id());
                        data.put(check_out_branch_id, attendanceModel.getCheck_out_branch_id());
                        data.put(updated_at, Utilities.getCurrentDateTimeNew());
                        result = db.update(TABLE_NAME, data, id + " = " + userAttandanceModel.getId(), null);
                        if (result > 0) {
                            status = true;
                        } else {
                            status = false;
                        }
                    } else {
                        ContentValues data = new ContentValues();
                        data.put(attendance_id, attendanceModel.getAttendanceId());
                        data.put(user_id, attendanceModel.getUserId());
                        data.put(check_in, attendanceModel.getCheckIn());
                        data.put(check_out, attendanceModel.getCheckOut());
                        data.put(is_checkin_manual, attendanceModel.getIsCheckinManual());
                        data.put(is_checkout_manual, attendanceModel.getIsCheckoutManual());
                        data.put(check_in_branch_id, attendanceModel.getCheck_in_branch_id());
                        data.put(check_out_branch_id, attendanceModel.getCheck_out_branch_id());
                        data.put(is_sync, attendanceModel.getIsSync());
                        data.put(created_at, Utilities.getCurrentDateTimeNew());
                        data.put(updated_at, Utilities.getCurrentDateTimeNew());

                        result = db.insert(TABLE_NAME, null, data);
                        if (result > 0) {
                            status = true;
                        } else {
                            status = false;
                        }
                    }
                }
            } else {
                ContentValues data = new ContentValues();
                data.put(user_id, attendanceModel.getUserId());
                data.put(attendance_id, attendanceModel.getAttendanceId());
                data.put(check_in, attendanceModel.getCheckIn());
                data.put(check_out, attendanceModel.getCheckOut());
                data.put(is_checkin_manual, attendanceModel.getIsCheckinManual());
                data.put(is_checkout_manual, attendanceModel.getIsCheckoutManual());
                data.put(check_in_branch_id, attendanceModel.getCheck_in_branch_id());
                data.put(check_out_branch_id, attendanceModel.getCheck_out_branch_id());
                data.put(is_sync, attendanceModel.getIsSync());
                data.put(created_at, Utilities.getCurrentDateTimeNew());
                data.put(updated_at, Utilities.getCurrentDateTimeNew());

                result = db.insert(TABLE_NAME, null, data);
                if (result > 0) {
                    status = true;
                } else {
                    status = false;
                }
            }

            return status;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return status;
    }

    public JSONObject getUserCurrentStatus(String userId) {
        JSONObject res = new JSONObject();
        int status = Constants.NO_ACTIVITY;
        int aid = 0, checkInBranchId = 0;
        Cursor cursor = null;
        try {
            AttendanceModel attendanceModel = new AttendanceModel();
            attendanceModel.setCheckIn("");
            attendanceModel.setCheckOut("");
            attendanceModel.setUserId("");
            attendanceModel.setId(0);

            String query = "select * from " + TABLE_NAME + " WHERE " + user_id + " = " + userId + " ORDER BY " + check_in + " DESC," + id + " DESC limit 1";
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                attendanceModel.setCheckIn(cursor.getString(cursor.getColumnIndex(check_in)));
                attendanceModel.setCheckOut(cursor.getString(cursor.getColumnIndex(check_out)));
                attendanceModel.setUserId(cursor.getString(cursor.getColumnIndex(user_id)));
                attendanceModel.setId(cursor.getInt(cursor.getColumnIndex(id)));
                attendanceModel.setCheck_in_branch_id(cursor.getInt(cursor.getColumnIndex(check_in_branch_id)));
                attendanceModel.setCheck_out_branch_id(cursor.getInt(cursor.getColumnIndex(check_out_branch_id)));
            }

            if (!attendanceModel.getUserId().equals("")) {
                if (!(attendanceModel.getCheckIn().equals("") || attendanceModel.getCheckIn().equals("0000-00-00 00:00:00")) && (attendanceModel.getCheckOut().equals("") || attendanceModel.getCheckOut().equals("0000-00-00 00:00:00"))) {
                    status = Constants.ACTIVITY_CHECKIN;
                    aid = attendanceModel.getId();
                    checkInBranchId = attendanceModel.getCheck_in_branch_id();
                } else {
                    status = Constants.NO_ACTIVITY;
                    aid = 0;
                }
            }

        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        try {
            res.put("status", status);
            res.put("aid", aid);
            res.put("check_in_branch_id", checkInBranchId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public int getAttendanceIdById(int aid) {
        JSONObject res = new JSONObject();
        int attendanceId = 0;
        Cursor cursor = null;
        try {
            String query = "select * from " + TABLE_NAME + " WHERE " + id + " = " + aid;
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                attendanceId = cursor.getInt(cursor.getColumnIndex(attendance_id));
            }
            cursor.close();

        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return attendanceId;
    }

    public JSONArray getNonSyncAttendance() {
        JSONArray jsonArray = new JSONArray();
        Cursor cursor = null;
        try {
            String query = "select * from " + TABLE_NAME + " WHERE " + is_sync + " = 0";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(attendance_id, cursor.getString(cursor.getColumnIndex(attendance_id)));
                        jsonObject.put(user_id, cursor.getString(cursor.getColumnIndex(user_id)));
                        jsonObject.put(check_in, cursor.getString(cursor.getColumnIndex(check_in)));
                        jsonObject.put(check_out, cursor.getString(cursor.getColumnIndex(check_out)));
                        jsonObject.put(is_checkin_manual, cursor.getString(cursor.getColumnIndex(is_checkin_manual)));
                        jsonObject.put(is_checkout_manual, cursor.getString(cursor.getColumnIndex(is_checkout_manual)));
                        jsonObject.put(check_in_branch_id, cursor.getString(cursor.getColumnIndex(check_in_branch_id)));
                        jsonObject.put(check_out_branch_id, cursor.getString(cursor.getColumnIndex(check_out_branch_id)));
                        jsonObject.put(is_sync, cursor.getString(cursor.getColumnIndex(is_sync)));
                        String referenceId = Utilities.getReferenceId(this.context) + "-" + cursor.getString(cursor.getColumnIndex(id));
                        jsonObject.put("reference_id", referenceId);
                        jsonArray.put(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return jsonArray;
    }

    public Boolean updateAttendance(JSONArray jsonArray) {
        Boolean status = false;
        Cursor cursor = null;
        try {

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has("reference_id")) {
                    int aid = Utilities.parseReferenceId(this.context, jsonObject.getString("reference_id"));
                    if (aid != 0) {
                        try {
                            ContentValues data = new ContentValues();
                            data.put(attendance_id, jsonObject.optString(attendance_id));
                            data.put(is_sync, 1);
                            data.put(check_in_branch_id, jsonObject.optString(check_in_branch_id));
                            data.put(check_out_branch_id, jsonObject.optString(check_out_branch_id));
                            data.put(updated_at, Utilities.getCurrentDateTimeNew());
                            int result = db.update(TABLE_NAME, data, id + " = " + aid, null);
                            if (result > 0) {
                                ContentValues breakData = new ContentValues();
                                breakData.put("attendance_id", jsonObject.getString(attendance_id));
                                db.update("user_break", breakData, "a_id = " + aid, null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        AttendanceModel attendanceModel = new AttendanceModel();
                        attendanceModel.setAttendanceId(Integer.parseInt(jsonObject.getString(attendance_id)));
                        attendanceModel.setUserId(jsonObject.getString(user_id));
                        attendanceModel.setCheckIn(jsonObject.getString(check_in));
                        attendanceModel.setCheckOut(jsonObject.getString(check_out));
                        attendanceModel.setIsCheckinManual(jsonObject.getInt(is_checkin_manual));
                        attendanceModel.setIsCheckoutManual(jsonObject.getInt(is_checkout_manual));
                        attendanceModel.setCheck_in_branch_id(jsonObject.getInt(check_in_branch_id));
                        attendanceModel.setCheck_out_branch_id(jsonObject.getInt(check_out_branch_id));
                        attendanceModel.setIsSync(1);
                        checkAttendanceIdAndInsert(attendanceModel);
                    }
                } else {
                    AttendanceModel attendanceModel = new AttendanceModel();
                    attendanceModel.setAttendanceId(Integer.parseInt(jsonObject.getString(attendance_id)));
                    attendanceModel.setUserId(jsonObject.getString(user_id));
                    attendanceModel.setCheckIn(jsonObject.getString(check_in));
                    attendanceModel.setCheckOut(jsonObject.getString(check_out));
                    attendanceModel.setIsCheckinManual(jsonObject.getInt(is_checkin_manual));
                    attendanceModel.setIsCheckoutManual(jsonObject.getInt(is_checkout_manual));
                    attendanceModel.setCheck_in_branch_id(jsonObject.getInt(check_in_branch_id));
                    attendanceModel.setCheck_out_branch_id(jsonObject.getInt(check_out_branch_id));
                    attendanceModel.setIsSync(1);
                    checkAttendanceIdAndInsert(attendanceModel);
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

    public Boolean saveAttendance(AttendanceModel attendanceModel) {
        Boolean status = false;
        Cursor cursor = null;
        try {

            if (attendanceModel.getReferenceId() != null && !attendanceModel.getReferenceId().equals("")) {
                int aid = Utilities.parseReferenceId(this.context, attendanceModel.getReferenceId());
                if (aid != 0) {
                    ContentValues data = new ContentValues();
                    data.put(attendance_id, attendanceModel.getAttendanceId());
                    data.put(is_sync, 1);
                    data.put(updated_at, Utilities.getCurrentDateTimeNew());
                    int result = db.update(TABLE_NAME, data, id + " = " + aid, null);
                    if (result > 0) {
                        ContentValues breakData = new ContentValues();
                        breakData.put("attendance_id", attendanceModel.getAttendanceId());
                        db.update("user_break", breakData, "a_id = " + aid, null);
                    }
                    return true;
                }
            }

            String query = "select count(" + attendance_id + ") as count from " + TABLE_NAME + " WHERE " + attendance_id + " = " + attendanceModel.getAttendanceId();
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                int count = cursor.getInt(cursor.getColumnIndex("count"));
                if (count > 0) {
                    ContentValues data = new ContentValues();
                    data.put(user_id, attendanceModel.getUserId());
                    data.put(check_in, attendanceModel.getCheckIn());
                    if (!(attendanceModel.getCheckOut().equals("") || attendanceModel.getCheckOut().equals("0000-00-00 00:00:00"))) {
                        data.put(check_out, attendanceModel.getCheckOut());
                    }
                    data.put(is_checkin_manual, attendanceModel.getIsCheckinManual());
                    if (attendanceModel.getIsCheckoutManual() != 0) {
                        data.put(is_checkout_manual, attendanceModel.getIsCheckoutManual());
                    }
                    data.put(check_in_branch_id, attendanceModel.getCheck_in_branch_id());
                    data.put(check_out_branch_id, attendanceModel.getCheck_out_branch_id());
                    data.put(updated_at, Utilities.getCurrentDateTimeNew());
                    db.update(TABLE_NAME, data, attendance_id + " = " + attendanceModel.getAttendanceId(), null);
                } else {
                    insertAttendance(attendanceModel);
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

    public int getAid(String attendanceId) {
        int aid = 0;
        Cursor cursor = null;
        try {
            String query = "select id from " + TABLE_NAME + " WHERE " + attendance_id + " = " + attendanceId;
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                aid = cursor.getInt(cursor.getColumnIndex(id));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return aid;
    }


}
