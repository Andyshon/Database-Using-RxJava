package com.andyshon.databaseusingrxjava;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by andyshon on 10.07.18.
 */

public class DbModel {

    private UserDbHelper userDbHelper;

    public DbModel(Context context) {
        userDbHelper = new UserDbHelper(context);
    }


    public void removeAllDatabase () {
        userDbHelper.getWritableDatabase().execSQL("delete from "+ UserContract.UserEntry.TABLE_NAME);
    }


    public void insertUsers(List<User> userList) {
        // Gets the database in write mode
        SQLiteDatabase db = userDbHelper.getWritableDatabase();
        // Создаем объект ContentValues, где имена столбцов ключи,
        // а информация о юзере является значениями ключей


        for (User user : userList) {
            ContentValues values = new ContentValues();
            values.put(UserContract.UserEntry.COLUMN_NAME, user.getName());
            values.put(UserContract.UserEntry.COLUMN_CITY, user.getCity());
            values.put(UserContract.UserEntry.COLUMN_GENDER, user.getName().length()>6 ? UserContract.UserEntry.GENDER_MALE : UserContract.UserEntry.GENDER_FEMALE);
            values.put(UserContract.UserEntry.COLUMN_AGE, user.getAge());

            long newRowId = db.insert(UserContract.UserEntry.TABLE_NAME, null, values);
        }
    }


    public Observable<List<User>> getAllUsers() {
        SQLiteDatabase db = userDbHelper.getReadableDatabase();

        String[] projection = {
                UserContract.UserEntry._ID,
                UserContract.UserEntry.COLUMN_NAME,
                UserContract.UserEntry.COLUMN_CITY,
                UserContract.UserEntry.COLUMN_GENDER,
                UserContract.UserEntry.COLUMN_AGE };

        // Делаем запрос
        Cursor cursor = db.query(
                UserContract.UserEntry.TABLE_NAME,   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                UserContract.UserEntry.COLUMN_AGE + " DESC");     // порядок сортировки


        try {

            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(UserContract.UserEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_NAME);
            int cityColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_CITY);
            int genderColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_GENDER);
            int ageColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_AGE);

            final List<User> userList = new ArrayList<>();
            // Проходим через все ряды
            while (cursor.moveToNext()) {
                // Используем индекс для получения строки или числа
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentCity = cursor.getString(cityColumnIndex);
                int currentGender = cursor.getInt(genderColumnIndex);
                int currentAge = cursor.getInt(ageColumnIndex);

                Log.d("MODEL", "CURRENT-ID:" + currentID);

                userList.add(new User(currentName, currentAge, currentCity));
            }

            Observable<List<User>> listObservable = Observable.create(new ObservableOnSubscribe<List<User>>() {
                @Override
                public void subscribe(ObservableEmitter<List<User>> e) throws Exception {

                    if (!e.isDisposed()) {
                        e.onNext(userList);
                        e.onComplete();
                    }
                }
            });

            return listObservable;

        } finally {
            // Всегда закрываем курсор после чтения
            cursor.close();
        }
    }
}
