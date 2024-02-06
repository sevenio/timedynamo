package com.tvisha.trooptime.activity.activity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UserTable extends DbContext {

    final public static String ID = "Id";
    final public static String USER_ID = "user_id";
    final public static String NAME = "name";
    final public static String EMAIL = "email";
    final public static String PHONE = "phone";
    final public static String EMP_ID = "emp_id";
    final public static String DEPARTMENT = "department";
    final public static String thumbImage = "thumbImage";
    final public static String userImage = "photo";
    final public static String CREATED_AT = "created_at";
    final public static String UPDATED_AT = "updated_at";
    final private static String TABLE_NAME = "finger_print_user";
    public static String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            ID + " Integer PRIMARY KEY AUTOINCREMENT," +
            USER_ID + " Text ," +
            NAME + " Text," +
            EMAIL + " Text," +
            thumbImage + " Text," +
            userImage + " Text," +
            EMP_ID + " Text," +
            DEPARTMENT + " Text," +
            PHONE + " Integer," +
            CREATED_AT + " TIMESTAMP," +
            UPDATED_AT + " TIMESTAMP);";
    final private SQLiteDatabase db = database;
    private Context context;

    public UserTable(Context context) {
        super(context);
        this.context = context;
    }

    public boolean userRegister(UserModel user, TextView tv) {
        //open();
        Cursor cursor = null;

        Toast.makeText(context, " USer data: " + user.toString(), Toast.LENGTH_LONG).show();

        ContentValues data = new ContentValues();

        data.put(USER_ID, user.getUser_id());
        data.put(NAME, user.getName());
        data.put(EMAIL, user.getEmail());
        data.put(PHONE, user.getMobile());
        data.put(thumbImage, user.getThumbImage());
        data.put(userImage, user.getUserImage());
        data.put(EMP_ID, user.getEmp_id());
        data.put(DEPARTMENT, user.getDepartment());
        long ststus;
        try {

            String query = "select " + USER_ID + ", " + EMAIL + ", " + PHONE + " from " + TABLE_NAME + "  WHERE " +
                    USER_ID + " = '" + user.getUser_id() + "'  OR " +
                    EMP_ID + " = '" + user.getEmp_id() + "'";

            cursor = db.rawQuery(query, null);
            if (null != cursor && cursor.getCount() > 0) {
                /*if (cursor.moveToFirst()) {
                    if(cursor.getString(0).equals(user.getUser_id())){
                        Toast.makeText(context, "Employee id is already regestered.",Toast.LENGTH_LONG).show();
                    }else if(cursor.getString(1).equals(user.getEmail())){
                        Toast.makeText(context, "Email id is already regestered.",Toast.LENGTH_LONG).show();
                        
                    }else if(cursor.getString(2).equals(user.getMobile())){
                        Toast.makeText(context, "Mobile is already regestered.",Toast.LENGTH_LONG).show();
                        
                    }else{
                        Toast.makeText(context, "User already regestered.",Toast.LENGTH_LONG).show();
                        
                    }
                }*/
                ststus = db.update(TABLE_NAME, data, USER_ID + " = '" + user.getUser_id() + "' OR " + EMP_ID + " = '" + user.getEmp_id() + "'", null);
                Toast.makeText(context, "data=>stat: " + ststus + " updated successfully true" + user.toString(), Toast.LENGTH_LONG).show();
            } else {

                ststus = db.insert(TABLE_NAME, null, data);
                Toast.makeText(context, "data=>stat: " + ststus + " regestered successfully true" + user.toString(), Toast.LENGTH_LONG).show();
            }

            if (ststus > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Toast.makeText(context, "data=>stat: " + e.getMessage() + " false " + user.toString(), Toast.LENGTH_LONG).show();
            tv.setText(e.getMessage());
            tv.setTextSize(10);
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return false;
    }

    public List<UserModel> getUsers() {
        //open();
        List<UserModel> users = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "select " + ID + "," + USER_ID + "," + NAME + "," + PHONE + "," + EMAIL + "," + thumbImage + "," + userImage + "," + EMP_ID + "," + DEPARTMENT + " from " + TABLE_NAME;
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {

                do {
                    UserModel userModel = new UserModel();
                    userModel.setId(cursor.getInt(0));
                    userModel.setUser_id(cursor.getString(1));
                    userModel.setName(cursor.getString(2));
                    userModel.setMobile(cursor.getString(3));
                    userModel.setEmail(cursor.getString(4));
                    userModel.setThumbImage(cursor.getString(5));
                    userModel.setUserImage(cursor.getString(6));
                    userModel.setEmp_id(cursor.getString(7));
                    userModel.setDepartment(cursor.getString(8));

                    users.add(userModel);

                } while (cursor.moveToNext());

            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        //close();
        return users;
    }

    public UserModel getUserByEmpID(String emp_id) {
        Cursor cursor = null;
        UserModel userModel = new UserModel();
        try {
            String query = "select " + ID + "," + USER_ID + "," + NAME + "," + PHONE + "," + EMAIL + "," + thumbImage + "," + userImage + "," + EMP_ID + "," + DEPARTMENT + " from " + TABLE_NAME + " Where " + EMP_ID + " = '" + (emp_id != null ? emp_id.toUpperCase() : " ") + "'";
            cursor = db.rawQuery(query, null);


            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    userModel = new UserModel();
                    userModel.setId(cursor.getInt(0));
                    userModel.setUser_id(cursor.getString(1));
                    userModel.setName(cursor.getString(2));
                    userModel.setMobile(cursor.getString(3));
                    userModel.setEmail(cursor.getString(4));
                    userModel.setThumbImage(cursor.getString(5));
                    userModel.setUserImage(cursor.getString(6));
                    userModel.setEmp_id(cursor.getString(7));
                    userModel.setDepartment(cursor.getString(8));


                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        //getUsers();
        return userModel;
    }

    public UserModel getUserByMobile(String mobile) {
        Cursor cursor = null;
        UserModel userModel = new UserModel();
        try {
            String query = "select " + ID + "," + USER_ID + "," + NAME + "," + PHONE + "," + EMAIL + "," + thumbImage + "," + userImage + "," + EMP_ID + "," + DEPARTMENT + " from " + TABLE_NAME + " Where " + PHONE + " = '" + mobile + "'";
            cursor = db.rawQuery(query, null);


            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    userModel.setId(cursor.getInt(0));
                    userModel.setUser_id(cursor.getString(1));
                    userModel.setName(cursor.getString(2));
                    userModel.setMobile(cursor.getString(3));
                    userModel.setEmail(cursor.getString(4));
                    userModel.setThumbImage(cursor.getString(5));
                    userModel.setUserImage(cursor.getString(6));
                    userModel.setEmp_id(cursor.getString(7));
                    userModel.setDepartment(cursor.getString(8));


                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return userModel;
    }

    public UserModel getUserByUSerID(String user_id) {
        Cursor cursor = null;
        try {
            String query = "select " + ID + "," + USER_ID + "," + NAME + "," + PHONE + "," + EMAIL + "," + thumbImage + "," + userImage + "," + EMP_ID + "," + DEPARTMENT + " from " + TABLE_NAME + " Where " + USER_ID + " = '" + user_id + "'";
            cursor = db.rawQuery(query, null);


            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    UserModel userModel = new UserModel();

                    userModel.setId(cursor.getInt(0));
                    userModel.setUser_id(cursor.getString(1));

                    userModel.setName(cursor.getString(2));
                    userModel.setMobile(cursor.getString(3));
                    userModel.setEmail(cursor.getString(4));
                    userModel.setThumbImage(cursor.getString(5));
                    userModel.setUserImage(cursor.getString(6));
                    userModel.setEmp_id(cursor.getString(7));
                    userModel.setDepartment(cursor.getString(8));
                    return userModel;
                    //Toast.makeText(context, "Query: "+userModel.toString(), Toast.LENGTH_LONG).show();
                }
            } else {
                //Toast.makeText(context, "Query: "+cursor.getCount()+" cursor is empty "+query, Toast.LENGTH_LONG).show();
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(context, "Query: eroor in bymobile  "+e.getMessage(), Toast.LENGTH_LONG).show();

        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return null;
    }

    public boolean updateUser(UserModel user) {
        //open();
        Cursor cursor = null;
        long ststus = 0;
        try {
            ContentValues data = new ContentValues();
            data.put(USER_ID, user.getUser_id());
            data.put(thumbImage, user.getThumbImage());
            data.put(userImage, user.getUserImage());
            data.put(EMP_ID, user.getEmp_id());

            String query = "select " + EMP_ID + ", " + EMAIL + ", " + PHONE + " from " + TABLE_NAME + "  WHERE " + EMP_ID + " = '" + user.getEmp_id() + "'";
            cursor = db.rawQuery(query, null);

            if (null != cursor && cursor.getCount() > 0) {
                cursor.close();
                ststus = db.update(TABLE_NAME, data, EMP_ID + " = '" + user.getEmp_id() + "'", null);
            } else {
                ststus = db.insert(TABLE_NAME, null, data);
            }

        } catch (Exception e) {
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }

        if (ststus > 0) {

            return true;
        } else {
            return false;
        }
    }

    public boolean saveUser(UserModel user) {
        //open();
        Cursor cursor = null;
        long ststus = 0;
        try {
            ContentValues data = new ContentValues();
            data.put(USER_ID, user.getUser_id());
            data.put(NAME, user.getName());
            data.put(EMAIL, user.getEmail());
            data.put(PHONE, user.getMobile());
            data.put(thumbImage, user.getThumbImage());
            data.put(userImage, user.getUserImage());
            data.put(EMP_ID, user.getEmp_id());
            data.put(DEPARTMENT, user.getDepartment());

            String query = "select " + EMP_ID + ", " + EMAIL + ", " + PHONE + " from " + TABLE_NAME + "  WHERE " + EMP_ID + " = '" + user.getEmp_id() + "'";
            cursor = db.rawQuery(query, null);
            if (null != cursor && cursor.getCount() > 0) {
                cursor.close();
                ststus = db.update(TABLE_NAME, data, EMP_ID + " = '" + user.getEmp_id() + "'", null);
            } else {
                ststus = db.insert(TABLE_NAME, null, data);
            }

        } catch (Exception e) {
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }

        if (ststus > 0) {

            return true;
        } else {
            return false;
        }
    }

}
