package com.andyshon.databaseusingrxjava;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by andyshon on 10.07.18.
 */

public class UserDbHelper extends SQLiteOpenHelper {

    /**
     * Имя файла базы данных
     */
    public static final String DATABASE_NAME = "hotel.db";

    /**
     * Версия базы данных. При изменении схемы увеличить на единицу
     */
    private static final int DATABASE_VERSION = 1;


    public UserDbHelper(Context context) {
        super(context, UserContract.UserEntry.TABLE_NAME, null, DATABASE_VERSION);
        //SQLiteDatabase.deleteDatabase(new java.io.File(DATABASE_NAME));
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Строка для создания таблицы
        String SQL_CREATE_GUESTS_TABLE = "CREATE TABLE " + UserContract.UserEntry.TABLE_NAME + " ("
                + UserContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserContract.UserEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_CITY + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_GENDER + " INTEGER NOT NULL DEFAULT 3, "
                + UserContract.UserEntry.COLUMN_AGE + " INTEGER NOT NULL DEFAULT 0);";

        sqLiteDatabase.execSQL(SQL_CREATE_GUESTS_TABLE);
        //sqLiteDatabase.execSQL("delete from "+ UserContract.UserEntry.TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w("SQLite", "Обновляемся с версии " + i + " на версию " + i1);

        // Удаляем старую таблицу и создаём новую
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        // Создаём новую таблицу
        onCreate(sqLiteDatabase);
    }
}
