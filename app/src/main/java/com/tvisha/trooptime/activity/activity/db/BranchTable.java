package com.tvisha.trooptime.activity.activity.db;

/**
 * Created by tvisha on 17/9/18.
 */

/*
public class BranchTable {
}
*/

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tvisha.trooptime.activity.activity.helper.Utilities;

import java.util.ArrayList;
import java.util.List;


public class BranchTable extends DbContext {

    final public static String branch_id = "branch_id";
    final public static String branch_name = "branch_name";
    final public static String created_at = "created_at";
    final public static String updated_at = "updated_at";
    final private static String TABLE_NAME = "branches";
    public static String CREATE_BRANCH_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            branch_id + " Integer ," +
            branch_name + " Text ," +
            created_at + " datetime," +
            updated_at + " datetime" +
            ");";
    final private SQLiteDatabase db = database;
    private Context context;

    public BranchTable(Context context) {
        super(context);
        this.context = context;
    }

    public boolean saveBranch(BranchModel branchModel) {
        Cursor cursor = null;
        long status = 0;
        try {
            ContentValues data = new ContentValues();
            data.put(branch_id, branchModel.getBranch_id());
            data.put(branch_name, branchModel.getBranch_name());
            data.put(updated_at, Utilities.getCurrentDateTimeNew());

            String query = "select count(branch_id) as count from " + TABLE_NAME + "  WHERE " + branch_id + " = '" + branchModel.getBranch_id() + "'";
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
                status = db.update(TABLE_NAME, data, branch_id + " = '" + branchModel.getBranch_id() + "'", null);
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

    public boolean updateBranch(BranchModel branchModel) {
        Cursor cursor = null;
        long status = 0;
        try {
            ContentValues data = new ContentValues();
            data.put(branch_id, branchModel.getBranch_id());
            data.put(branch_name, branchModel.getBranch_name());


            data.put(created_at, "");
            data.put(updated_at, "");

            status = db.update(TABLE_NAME, data, branch_id + " = '" + branchModel.getBranch_id() + "'", null);

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


    public List<BranchModel> getBranches() {
        //open();
        List<BranchModel> branches = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " group by branch_id";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {

                do {
                    BranchModel branchModel = new BranchModel();
                    branchModel.setBranch_id(cursor.getInt(cursor.getColumnIndex(branch_id)));
                    branchModel.setBranch_name(cursor.getString(cursor.getColumnIndex(branch_name)));

                    branches.add(branchModel);

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
        return branches;
    }


    public BranchModel getBranchByBranchId(int branchId) {
        //open();
        BranchModel branchModel = new BranchModel();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + branch_id + " = " + branchId;
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                branchModel.setBranch_id(cursor.getInt(cursor.getColumnIndex(branch_id)));
                branchModel.setBranch_name(cursor.getString(cursor.getColumnIndex(branch_name)));

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
        return branchModel;
    }


}

