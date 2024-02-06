package com.tvisha.trooptime.activity.activity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tvisha.trooptime.activity.activity.Helper.Utilities;
import com.tvisha.trooptime.activity.activity.api_handlers.JSONHandlers;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akhil on 16/5/18.
 */

public class UsersTable extends DbContext {

    final public static String user_id = "user_id";
    final public static String name = "name";
    final public static String display_name = "display_name";
    final public static String role = "role";
    final public static String email = "email";
    final public static String mobile = "mobile";
    final public static String user_avatar = "user_avatar";
    final public static String finger_print = "finger_print";
    final public static String emp_id = "emp_id";
    final public static String shift_id = "shift_id";
    final public static String status = "status";
    final public static String reporting_boss_id = "reporting_boss_id";
    final public static String reporting_hr_id = "reporting_hr_id";
    final public static String reporting_permission_id = "reporting_permission_id";
    final public static String created_at = "created_at";
    final public static String updated_at = "updated_at";
    final private static String TABLE_NAME = "users";
    public static String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            user_id + " Integer ," +
            name + " Text ," +
            display_name + " Text ," +
            role + " Integer ," +
            email + " Text ," +
            mobile + " Text ," +
            user_avatar + " Text ," +
            finger_print + " Text ," +
            emp_id + " Text ," +
            shift_id + " Integer ," +
            status + " Integer ," +
            reporting_boss_id + " Text ," +
            reporting_hr_id + " Text ," +
            reporting_permission_id + " Text ," +
            created_at + " datetime," +
            updated_at + " datetime" +
            ");";
    final private SQLiteDatabase db = database;
    private Context context;

    public UsersTable(Context context) {
        super(context);
        this.context = context;
    }

    public boolean saveUser(UsersModel usersModel) {
        Cursor cursor = null;
        long status = 0;
        try {
            ContentValues data = new ContentValues();
            data.put(user_id, usersModel.getUserId());
            data.put(name, usersModel.getName());
            data.put(display_name, usersModel.getDisplayName());
            data.put(role, usersModel.getRole());
            data.put(email, usersModel.getEmail());
            data.put(mobile, usersModel.getMobile());
            data.put(user_avatar, usersModel.getUserAvatar());
            data.put(finger_print, usersModel.getFingerPrint());
            data.put(emp_id, usersModel.getEmpId());
            data.put(shift_id, usersModel.getShiftId());
            data.put(reporting_boss_id, usersModel.getReportingBossId());
            data.put(reporting_hr_id, usersModel.getReportingHrId());
            data.put(reporting_permission_id, usersModel.getReportingPermissionId());
            data.put(updated_at, Utilities.getCurrentDateTimeNew());

            String query = "select count(user_id) as count from " + TABLE_NAME + "  WHERE " + user_id + " = '" + usersModel.getUserId() + "'";
            cursor = db.rawQuery(query, null);
            int userCount = 0;
            if (cursor != null && cursor.getCount() > 0) {
                try {
                    if (cursor.moveToFirst()) {
                        userCount = cursor.getInt(cursor.getColumnIndex("count"));
                        cursor.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (userCount > 0) {
                status = db.update(TABLE_NAME, data, user_id + " = '" + usersModel.getUserId() + "'", null);
            } else {

                data.put(created_at, Utilities.getCurrentDateTimeNew());

                status = db.insert(TABLE_NAME, null, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        if (status > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateUser(UsersModel usersModel) {
        Cursor cursor = null;
        long status = 0;
        try {
            ContentValues data = new ContentValues();
            data.put(user_id, usersModel.getUserId());
            data.put(name, usersModel.getName());
            data.put(display_name, usersModel.getDisplayName());
            data.put(role, usersModel.getRole());
            data.put(email, usersModel.getEmail());
            data.put(mobile, usersModel.getMobile());
            data.put(user_avatar, usersModel.getUserAvatar());
            data.put(finger_print, usersModel.getFingerPrint());
            data.put(emp_id, usersModel.getEmpId());
            data.put(shift_id, usersModel.getShiftId());
            data.put(created_at, "");
            data.put(updated_at, "");

            status = db.update(TABLE_NAME, data, user_id + " = '" + usersModel.getUserId() + "'", null);

        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        if (status > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateUserFingerPrint(JSONObject jsonObject) {
        Cursor cursor = null;
        long status = 0;
        try {
            ContentValues data = new ContentValues();
            data.put(user_id, JSONHandlers.getString(jsonObject, "user_id"));
            data.put(name, JSONHandlers.getString(jsonObject, "name"));
            data.put(display_name, JSONHandlers.getString(jsonObject, "display_name"));
            data.put(role, JSONHandlers.getString(jsonObject, "role"));
            data.put(email, JSONHandlers.getString(jsonObject, "email"));
            data.put(mobile, JSONHandlers.getString(jsonObject, "mobile"));
            data.put(user_avatar, JSONHandlers.getString(jsonObject, "user_avatar"));
            data.put(finger_print, JSONHandlers.getString(jsonObject, "finger_print"));
            data.put(emp_id, JSONHandlers.getString(jsonObject, "emp_id"));
            data.put(shift_id, JSONHandlers.getString(jsonObject, "shift_id"));
            data.put(updated_at, Utilities.getCurrentDateTimeNew());

            status = db.update(TABLE_NAME, data, user_id + " = '" + JSONHandlers.getString(jsonObject, "user_id") + "'", null);

        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        if (status > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateAdminFingerPrint(String userId, String fingerPrint) {
        Cursor cursor = null;
        long status = 0;
        try {
            ContentValues data = new ContentValues();
            data.put(finger_print, fingerPrint);
            data.put(updated_at, Utilities.getCurrentDateTimeNew());
            status = db.update(TABLE_NAME, data, user_id + " = '" + userId + "'", null);

        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        if (status > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean addUserFingerPrint(String userId, String fingerPrint, String userAvatar) {
        Cursor cursor = null;
        long status = 0;
        try {
            ContentValues data = new ContentValues();
            data.put(finger_print, fingerPrint);
            data.put(updated_at, Utilities.getCurrentDateTimeNew());
            if (!userAvatar.equals("")) {
                data.put(user_avatar, userAvatar);
            }

            status = db.update(TABLE_NAME, data, user_id + " = '" + userId + "'", null);

        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        if (status > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<UsersModel> getUsers() {
        //open();
        List<UsersModel> users = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " group by user_id";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {

                do {
                    UsersModel usersModel = new UsersModel();
                    usersModel.setUserId(cursor.getInt(cursor.getColumnIndex(user_id)));
                    usersModel.setName(cursor.getString(cursor.getColumnIndex(name)));
                    usersModel.setDisplayName(cursor.getString(cursor.getColumnIndex(name)));
                    usersModel.setRole(cursor.getString(cursor.getColumnIndex(role)));
                    usersModel.setEmail(cursor.getString(cursor.getColumnIndex(email)));
                    usersModel.setMobile(cursor.getString(cursor.getColumnIndex(mobile)));
                    usersModel.setUserAvatar(cursor.getString(cursor.getColumnIndex(user_avatar)));
                    usersModel.setFingerPrint(cursor.getString(cursor.getColumnIndex(finger_print)));
                    usersModel.setEmpId(cursor.getString(cursor.getColumnIndex(emp_id)));
                    usersModel.setShiftId(cursor.getString(cursor.getColumnIndex(shift_id)));
                    usersModel.setStatus(cursor.getString(cursor.getColumnIndex(status)));
                    usersModel.setReportingBossId(cursor.getString(cursor.getColumnIndex(reporting_boss_id)));
                    usersModel.setReportingPermissionId(cursor.getString(cursor.getColumnIndex(reporting_permission_id)));
                    usersModel.setReportingHrId(cursor.getString(cursor.getColumnIndex(reporting_hr_id)));
                    users.add(usersModel);

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
        return users;
    }

    public List<UsersModel> getAdmins() {
        //open();
        List<UsersModel> users = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + role + " = 1";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {

                do {
                    UsersModel usersModel = new UsersModel();
                    usersModel.setUserId(cursor.getInt(cursor.getColumnIndex(user_id)));
                    usersModel.setName(cursor.getString(cursor.getColumnIndex(name)));
                    usersModel.setDisplayName(cursor.getString(cursor.getColumnIndex(name)));
                    usersModel.setRole(cursor.getString(cursor.getColumnIndex(role)));
                    usersModel.setEmail(cursor.getString(cursor.getColumnIndex(email)));
                    usersModel.setMobile(cursor.getString(cursor.getColumnIndex(mobile)));
                    usersModel.setUserAvatar(cursor.getString(cursor.getColumnIndex(user_avatar)));
                    usersModel.setFingerPrint(cursor.getString(cursor.getColumnIndex(finger_print)));
                    usersModel.setEmpId(cursor.getString(cursor.getColumnIndex(emp_id)));
                    usersModel.setShiftId(cursor.getString(cursor.getColumnIndex(shift_id)));
                    usersModel.setStatus(cursor.getString(cursor.getColumnIndex(status)));
                    // usersModel.setReportingBossId(cursor.getString(cursor.getColumnIndex(reporting_boss_id)));
                    //  usersModel.setReportingPermissionId(cursor.getString(cursor.getColumnIndex(reporting_permission_id)));
                    users.add(usersModel);

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
        return users;
    }

    public UsersModel getUserByUserId(String userId) {
        //open();
        UsersModel usersModel = new UsersModel();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + user_id + " = " + userId;
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                usersModel.setUserId(cursor.getInt(cursor.getColumnIndex(user_id)));
                usersModel.setName(cursor.getString(cursor.getColumnIndex(name)));
                usersModel.setDisplayName(cursor.getString(cursor.getColumnIndex(name)));
                usersModel.setRole(cursor.getString(cursor.getColumnIndex(role)));
                usersModel.setEmail(cursor.getString(cursor.getColumnIndex(email)));
                usersModel.setMobile(cursor.getString(cursor.getColumnIndex(mobile)));
                usersModel.setUserAvatar(cursor.getString(cursor.getColumnIndex(user_avatar)));
                usersModel.setFingerPrint(cursor.getString(cursor.getColumnIndex(finger_print)));
                usersModel.setEmpId(cursor.getString(cursor.getColumnIndex(emp_id)));
                usersModel.setShiftId(cursor.getString(cursor.getColumnIndex(shift_id)));
                usersModel.setStatus(cursor.getString(cursor.getColumnIndex(status)));
                usersModel.setReportingBossId(cursor.getString(cursor.getColumnIndex(reporting_boss_id)));
                usersModel.setReportingPermissionId(cursor.getString(cursor.getColumnIndex(reporting_permission_id)));
                usersModel.setReportingHrId(cursor.getString(cursor.getColumnIndex(reporting_hr_id)));
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
        return usersModel;
    }

    public UsersModel getUserByUsername(String username) {
        //open();
        UsersModel usersModel = new UsersModel();
        Cursor cursor = null;
        try {

            String query = "";

            if (username.contains("@")) {
                query = "SELECT * FROM " + TABLE_NAME + " WHERE " + email + " = " + username;
            } else {
                query = "SELECT * FROM " + TABLE_NAME + " WHERE " + mobile + " = " + username;
            }

            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                usersModel.setUserId(cursor.getInt(cursor.getColumnIndex(user_id)));
                usersModel.setName(cursor.getString(cursor.getColumnIndex(name)));
                usersModel.setDisplayName(cursor.getString(cursor.getColumnIndex(name)));
                usersModel.setRole(cursor.getString(cursor.getColumnIndex(role)));
                usersModel.setEmail(cursor.getString(cursor.getColumnIndex(email)));
                usersModel.setMobile(cursor.getString(cursor.getColumnIndex(mobile)));
                usersModel.setUserAvatar(cursor.getString(cursor.getColumnIndex(user_avatar)));
                usersModel.setFingerPrint(cursor.getString(cursor.getColumnIndex(finger_print)));
                usersModel.setEmpId(cursor.getString(cursor.getColumnIndex(emp_id)));
                usersModel.setShiftId(cursor.getString(cursor.getColumnIndex(shift_id)));
                usersModel.setStatus(cursor.getString(cursor.getColumnIndex(status)));
                usersModel.setReportingBossId(cursor.getString(cursor.getColumnIndex(reporting_boss_id)));
                usersModel.setReportingPermissionId(cursor.getString(cursor.getColumnIndex(reporting_permission_id)));
                usersModel.setReportingHrId(cursor.getString(cursor.getColumnIndex(reporting_hr_id)));
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
        return usersModel;
    }

    public UsersModel findUserByUsername(String username) {
        //open();
        UsersModel usersModel = new UsersModel();
        Cursor cursor = null;
        try {

            String query = "";

            username = username.toLowerCase();
            if (username.contains("@")) {
                query = "SELECT * FROM " + TABLE_NAME + " WHERE " + email + " = '" + username + "'";
            } else {
                query = "SELECT * FROM " + TABLE_NAME + " WHERE " + mobile + " = '" + username + "' OR LOWER(" + emp_id + ") = '" + username + "'";
            }

            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                usersModel.setUserId(cursor.getInt(cursor.getColumnIndex(user_id)));
                usersModel.setName(cursor.getString(cursor.getColumnIndex(name)));
                usersModel.setDisplayName(cursor.getString(cursor.getColumnIndex(name)));
                usersModel.setRole(cursor.getString(cursor.getColumnIndex(role)));
                usersModel.setEmail(cursor.getString(cursor.getColumnIndex(email)));
                usersModel.setMobile(cursor.getString(cursor.getColumnIndex(mobile)));
                usersModel.setUserAvatar(cursor.getString(cursor.getColumnIndex(user_avatar)));
                usersModel.setFingerPrint(cursor.getString(cursor.getColumnIndex(finger_print)));
                usersModel.setEmpId(cursor.getString(cursor.getColumnIndex(emp_id)));
                usersModel.setShiftId(cursor.getString(cursor.getColumnIndex(shift_id)));
                usersModel.setStatus(cursor.getString(cursor.getColumnIndex(status)));
                usersModel.setReportingBossId(cursor.getString(cursor.getColumnIndex(reporting_boss_id)));
                usersModel.setReportingPermissionId(cursor.getString(cursor.getColumnIndex(reporting_permission_id)));
                usersModel.setReportingHrId(cursor.getString(cursor.getColumnIndex(reporting_hr_id)));
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
        return usersModel;
    }

    public boolean isFingerPrintExists(String finger_print) {
        Boolean isExist = false;
        Cursor cursor = null;
        try {
            String query = "SELECT count(*) as count FROM " + TABLE_NAME + " WHERE " + finger_print + " = " + finger_print;
            cursor = db.rawQuery(query, null);
            int count = 0;
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("count"));
            }

            if (count > 0) {
                isExist = true;
            } else {
                isExist = false;
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return isExist;
    }
}

