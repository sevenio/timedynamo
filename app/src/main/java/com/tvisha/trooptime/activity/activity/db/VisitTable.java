package com.tvisha.trooptime.activity.activity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tvisha.trooptime.activity.activity.helper.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VisitTable extends DbContext {

    final public static String ID = "Id";
    final public static String USER_ID = "user_id";
    final public static String CheckIn = "check_in";
    final public static String CheckOut = "check_out";
    final public static String date = "date";
    final public static String status = "status";
    final public static String CREATED_AT = "created_at";
    final public static String UPDATED_AT = "updated_at";
    final public static String SendStatus = "send_status";
    final public static String IsManual = "is_manual";
    final public static String company_id = "company_id";
    final private static String TABLE_NAME = "Visits";
    public static String CREATE_USER_VISIT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            ID + " Integer PRIMARY KEY AUTOINCREMENT," +
            USER_ID + " Text ," +
            CheckIn + "  Text," +
            CheckOut + " Text," +
            date + " Text," +
            company_id + " Text," +
            status + " Integer," +
            SendStatus + " Integer," +
            IsManual + " Integer," +
            CREATED_AT + " datetime," +
            UPDATED_AT + " datetime" +
            ");";
    final private SQLiteDatabase db = database;
    private Context context;

    public VisitTable(Context context) {
        super(context);
        this.context = context;
    }

    public long userVisit(VisitModel visit) {
        int visit_status = 1000;
        try {
            ContentValues data = new ContentValues();


            String qry = "SELECT COUNT(*) from  " + TABLE_NAME + " " +
                    "WHERE " + USER_ID + " = '" + visit.getUSER_ID() +
                    "' and  " + date + " = '" + visit.getDate() +
                    "' and  " + CheckIn + " = '" + visit.getCheckIn() +
                    "' and  " + CREATED_AT + " = '" + visit.getCreatedAt() + "' ";


            Cursor cursor = db.rawQuery(qry, null);
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            long i;
            String label = " 1." + qry + "-" + count + "    ";

            data.put(USER_ID, visit.getUSER_ID());
            data.put(status, visit.getStatus());
            data.put(CheckIn, visit.getCheckIn());
            data.put(CheckOut, visit.getCheckOut());
            data.put(date, visit.getDate());
            data.put(IsManual, visit.getIsManual());
            data.put(CREATED_AT, visit.getCreatedAt());
            data.put(UPDATED_AT, Utilities.getCurrentDateTime());
            if (count > 0) {
                String condition =
                        USER_ID + " = '" + visit.getUSER_ID() +
                                "' and  " + date + " = '" + visit.getDate() +
                                "' and  " + CheckIn + " = '" + visit.getCheckIn() +
                                "' and  " + CREATED_AT + " = '" + visit.getCreatedAt() + "' ";

                i = db.update(TABLE_NAME,
                        data,
                        condition,
                        null);

                if (i > 0) {
                    visit_status = 1000;
                } else {
                    visit_status = 10000;
                }

            } else {
                i = db.insert(TABLE_NAME, null, data);
                if (i > 0) {
                    visit_status = 2000;
                } else {
                    visit_status = 20000;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            visit_status = 0;
        }
        //close();
        return visit_status;
    }

    public VisitModel getUserVisitStatus(VisitModel visit) {
        Cursor cursor = null;
        int visit_status = 1000;
        VisitModel model = new VisitModel();
        try {

            String qry = "SELECT " + USER_ID + "," + status + "," + CheckIn + "," + date + "," + SendStatus + " from  " + TABLE_NAME + " " +
                    " WHERE " +
                    USER_ID + " = '" + visit.getUSER_ID() + "' " +
                    "and (" +
                    IsManual + " = '1' OR " +
                    IsManual + " = '2' ) " +
                    //"and "+CREATED_AT +" != "+null+"  "+
                    "ORDER BY " + CREATED_AT + " DESC limit 1";

            cursor = db.rawQuery(qry, null);


            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    visit_status = cursor.getInt(cursor.getColumnIndex(status));

                    model.setUSER_ID(cursor.getString(0));
                    model.setStatus(cursor.getString(1));
                    model.setCheckIn(cursor.getString(2));
                    model.setDate(cursor.getString(3));

                    cursor.close();
                    /*if (visit_status == 6) {
                        model = createNewVisit(visit);
                    }*/
                } else {
                    visit_status = 1000;
                    model.setStatus("1000");
                }
            } else {
                model.setUSER_ID(visit.getUSER_ID());
                model.setStatus("0");
                model.setCheckIn(Utilities.getCurrentTime());
                model.setDate(Utilities.getCurrentDate());
                //model = createNewVisit(visit);
            }
            ;
            getVisits();
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(context, "Status: " + visit_status+"  "+e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return model;
    }

    public boolean updateVisitStatus(VisitModel visit, int visit_status) {
        boolean updatestatus = false;
        ContentValues data = new ContentValues();
        data.put(USER_ID, visit.getUSER_ID());
        data.put(status, visit_status + "");

        data.put(CheckIn, visit.getCheckIn());
        data.put(CheckOut, visit.getCheckOut());
        data.put(date, visit.getDate());

        data.put(CREATED_AT, Utilities.getCurrentDateTime());
        data.put(UPDATED_AT, Utilities.getCurrentDateTime());
        data.put(IsManual, visit.getIsManual());
        data.put(SendStatus, visit.getSendStatus());

        long i = db.insert(TABLE_NAME, null, data);

        if (i > 0) {
            updatestatus = true;
        } else {
            updatestatus = false;
        }
        return updatestatus;
    }

    public List<VisitModel> getVisits() {
        //open();
        List<VisitModel> visits = new ArrayList<>();

        Cursor cursor = null;
        try {
            String query = "select * from " + TABLE_NAME + " ORDER BY " + CREATED_AT + " DESC ";
            cursor = db.rawQuery(query, null);
            String data = "";
            if (cursor.moveToFirst()) {

                do {
                    VisitModel visitModel = new VisitModel();

                    visitModel.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                    visitModel.setUSER_ID(cursor.getString(cursor.getColumnIndex(USER_ID)));
                    visitModel.setCheckIn(cursor.getString(cursor.getColumnIndex(CheckIn)));
                    visitModel.setCheckOut(cursor.getString(cursor.getColumnIndex(CheckOut)));
                    visitModel.setDate(cursor.getString(cursor.getColumnIndex(date)));
                    visitModel.setStatus(cursor.getString(cursor.getColumnIndex(status)));
                    visitModel.setCreatedAt(cursor.getString(cursor.getColumnIndex(CREATED_AT)));
                    visitModel.setSendStatus(cursor.getInt(cursor.getColumnIndex(SendStatus)));
                    visitModel.setIsManual(cursor.getInt(cursor.getColumnIndex(IsManual)));
                    visits.add(visitModel);


                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) cursor.close();
        }
        //close();
        return visits;
    }

    public boolean updateSendStatusOfVisit(int visitTableID, int sendStatus) {
        boolean updatestatus = false;
        ContentValues data = new ContentValues();
        data.put(SendStatus, sendStatus + "");

        long i = db.update(TABLE_NAME, data, ID + " = '" + visitTableID + "'", null);

        if (i > 0) {
            updatestatus = true;
        } else {
            updatestatus = false;
        }
        return updatestatus;
    }

    public JSONArray getVisitsJSONArray() {

        JSONArray visitsArray = new JSONArray();
        Cursor cursor = null;
        try {
            String query = "select * from " + TABLE_NAME + " WHERE " + SendStatus + " = '0' ORDER BY " + CREATED_AT + " DESC ";
            cursor = db.rawQuery(query, null);
            String data = "";
            if (cursor.moveToFirst()) {

                do {
                    JSONObject visitObj = new JSONObject();
                    String time = cursor.getString(cursor.getColumnIndex(CheckIn));
                    String dateStr = cursor.getString(cursor.getColumnIndex(date));
                    //visitObj.put("security_key", "ba5d400506348b7d54088203324f5224");
                    visitObj.put("id", "" + cursor.getString(cursor.getColumnIndex(ID)));
                    visitObj.put("user_id", cursor.getString(cursor.getColumnIndex(USER_ID)));
                    visitObj.put("activity", cursor.getString(cursor.getColumnIndex(status)));
                    visitObj.put("time", time);
                    visitObj.put("date", dateStr);
                    visitObj.put("date_time", dateStr + " " + time);
                    visitObj.put("time_stamp", cursor.getString(cursor.getColumnIndex(CREATED_AT)));
                    visitObj.put("is_manual", cursor.getInt(cursor.getColumnIndex(IsManual)) + "");
                    visitsArray.put(visitObj);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) cursor.close();
        }
        return visitsArray;
    }

    public VisitModel getVisitByTime() {
        Cursor cursor = null;
        VisitModel model = null;
        try {

            String qry = "SELECT " + USER_ID + "," + status + "," + CheckIn + "," + date + "," + IsManual + "," + ID + " from  " + TABLE_NAME + " " +
                    "WHERE " + SendStatus + " = '0' " +
                    "AND " + status + " != '0' " +
                    " ORDER BY " + CREATED_AT + " ASC limit 1 ";

            cursor = db.rawQuery(qry, null);

            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    model = new VisitModel();
                    model.setUSER_ID(cursor.getString(0));
                    model.setStatus(cursor.getString(1));
                    model.setCheckIn(cursor.getString(2));
                    model.setDate(cursor.getString(3));
                    model.setIsManual(cursor.getInt(4));
                    model.setID(cursor.getInt(5));
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return model;
    }
}
