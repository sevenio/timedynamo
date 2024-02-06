package com.tvisha.trooptime.activity.activity.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class DbHelper extends SQLiteOpenHelper {

    public final static String db_name = "time_dynamo_mobile";
    private final static int db_Version = 1;
    private static DbHelper instance;
    Context ctx;

    public DbHelper(Context context) {
        super(context, db_name, null, db_Version);
        ctx = context;
    }

    public static synchronized DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context);
        }
        return instance;
    }

    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String[] tables = new String[]{
                    UserTable.CREATE_USER_TABLE,
                    VisitTable.CREATE_USER_VISIT_TABLE,
                    UsersTable.CREATE_USERS_TABLE,
                    AttendanceTable.CREATE_ATTENDANCE_TABLE,
                    BreakTable.CREATE_BREAK_TABLE,
                    UserBreakTable.CREATE_BREAK_TABLE,
                    UserShiftTable.CREATE_USERSHIFT_TABLE,
                    BranchTable.CREATE_BRANCH_TABLE
            };

            for (String query : tables) {
                try {

                    db.execSQL(query);
                } catch (Exception e) {
                    Toast.makeText(ctx, "error: " + e.getMessage() + " ", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
