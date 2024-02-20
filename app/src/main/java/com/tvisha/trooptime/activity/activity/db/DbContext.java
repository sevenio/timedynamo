package com.tvisha.trooptime.activity.activity.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class DbContext {

    protected SQLiteDatabase database;
    private DbHelper dbHelper;
    private Context mContext;

    public DbContext(Context context) {
        this.mContext = context;
        dbHelper = DbHelper.getInstance(mContext);
        open();


    }

    public void open() throws SQLException {
        if (dbHelper == null)
            dbHelper = DbHelper.getInstance(mContext);
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
        database = null;
    }
}
