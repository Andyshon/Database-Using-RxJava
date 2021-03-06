package com.andyshon.databaseusingrxjava.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by andyshon on 10.07.18.
 */

public class UserDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Guests.db";

    /**
     * Version of database. When changing schemes, to increment
     */
    private static final int DATABASE_VERSION = 1;


    public UserDbHelper(Context context) {
        super(context, UserContract.UserEntry.TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // string for query create table
        String SQL_CREATE_GUESTS_TABLE = "CREATE TABLE " + UserContract.UserEntry.TABLE_NAME + " ("
                + UserContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserContract.UserEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_CITY + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_GENDER + " INTEGER NOT NULL DEFAULT 3, "
                + UserContract.UserEntry.COLUMN_AGE + " INTEGER NOT NULL DEFAULT 0);";

        sqLiteDatabase.execSQL(SQL_CREATE_GUESTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w("SQLite", "Update from version " + i + " to version " + i1);

        // delete old table and create new
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        // create new table
        onCreate(sqLiteDatabase);
    }
}
