package com.andyshon.databaseusingrxjava.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.andyshon.databaseusingrxjava.Entity.User;
import com.andyshon.databaseusingrxjava.FilterOperation;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by andyshon on 10.07.18.
 */

public class DatabaseModel {

    private UserDbHelper userDbHelper;
    private Context context;


    public DatabaseModel(Context context) {
        this.context = context;
        userDbHelper = new UserDbHelper(context);
    }


    public Completable addUser (ContentValues values) {

        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {

                UserDbHelper userDbHelper = new UserDbHelper(context);
                SQLiteDatabase db = userDbHelper.getWritableDatabase();

                db.insert(UserContract.UserEntry.TABLE_NAME, null, values);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }


    public Observable<List<User>> getUsersByFilter (int byAge, FilterOperation.ByAge filterAge, FilterOperation.ByGender filterGender) {

        SQLiteDatabase db = userDbHelper.getReadableDatabase();

        String[] projection = {
                UserContract.UserEntry._ID,
                UserContract.UserEntry.COLUMN_NAME,
                UserContract.UserEntry.COLUMN_CITY,
                UserContract.UserEntry.COLUMN_GENDER,
                UserContract.UserEntry.COLUMN_AGE };

        String operatorAge;
        switch (filterAge) {
            case MORETHAN: operatorAge = ">?"; break;
            case LESSTHAN: operatorAge = "<?"; break;
            default: operatorAge = "=?"; break;
        }

        String operatorGender;
        switch (filterGender) {
            case MALE: operatorGender = "1"; break;
            case FEMALE: operatorGender = "0"; break;
            default: operatorGender = "3"; break;
        }


        String selection = UserContract.UserEntry.COLUMN_AGE + operatorAge + " and " + UserContract.UserEntry.COLUMN_GENDER + "=?"/*operatorGender*/;
        String[] selectionArgs = {String.valueOf(byAge), operatorGender};

        // Make a request
        Cursor cursor = db.query(
                UserContract.UserEntry.TABLE_NAME,   // table
                projection,            // columns
                selection,                  // columns for condition WHERE
                selectionArgs,                  // values for condition WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                UserContract.UserEntry.COLUMN_AGE + " DESC");     // sorting order

        try {

            // Find the index of each column
            int idColumnIndex = cursor.getColumnIndex(UserContract.UserEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_NAME);
            int cityColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_CITY);
            int genderColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_GENDER);
            int ageColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_AGE);

            List<User> filteredUsers = new ArrayList<>();
            // Iterate through all rows
            while (cursor.moveToNext()) {
                // Use the index to get a string or number
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentCity = cursor.getString(cityColumnIndex);
                int currentGender = cursor.getInt(genderColumnIndex);
                int currentAge = cursor.getInt(ageColumnIndex);

                filteredUsers.add(new User(currentID, currentName, currentAge, currentCity, currentGender));
            }

            return Observable.create(new ObservableOnSubscribe<List<User>>() {
                @Override
                public void subscribe(ObservableEmitter<List<User>> e) throws Exception {

                    if (!e.isDisposed()) {
                        e.onNext(filteredUsers);
                        e.onComplete();
                    }
                }
            });
        } finally {
            // Always close cursor after reading
            cursor.close();
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

        // Make a request
        Cursor cursor = db.query(
                UserContract.UserEntry.TABLE_NAME,   // table
                projection,            // columns
                null,                  // columns for condition WHERE
                null,                  // values for condition WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                UserContract.UserEntry.COLUMN_AGE + " DESC");     // sorting order


        try {

            // Find the index of each column
            int idColumnIndex = cursor.getColumnIndex(UserContract.UserEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_NAME);
            int cityColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_CITY);
            int genderColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_GENDER);
            int ageColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_AGE);

            List<User> userList = new ArrayList<>();
            // Iterate through all rows
            while (cursor.moveToNext()) {
                // Use the index to get a string or number
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentCity = cursor.getString(cityColumnIndex);
                int currentGender = cursor.getInt(genderColumnIndex);
                int currentAge = cursor.getInt(ageColumnIndex);

                userList.add(new User(currentID, currentName, currentAge, currentCity, currentGender));
            }

            return Observable.create(new ObservableOnSubscribe<List<User>>() {
                @Override
                public void subscribe(ObservableEmitter<List<User>> e) throws Exception {

                    if (!e.isDisposed()) {
                        e.onNext(userList);
                        e.onComplete();
                    }
                }
            });
        } finally {
            // Always close cursor after reading
            cursor.close();
        }
    }


    public Observable<User> getUserById(int id) {
        SQLiteDatabase db = userDbHelper.getReadableDatabase();

        String[] projections = {
                UserContract.UserEntry._ID,
                UserContract.UserEntry.COLUMN_NAME,
                UserContract.UserEntry.COLUMN_CITY,
                UserContract.UserEntry.COLUMN_GENDER,
                UserContract.UserEntry.COLUMN_AGE };

        String selection = UserContract.UserEntry._ID + " LIKE ?";

        String[] selection_args = {String.valueOf(id)};

        Cursor cursor = db.query(UserContract.UserEntry.TABLE_NAME, projections, selection, selection_args,null,null,null);

        // Find the index of each column
        int idColumnIndex = cursor.getColumnIndex(UserContract.UserEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_NAME);
        int cityColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_CITY);
        int genderColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_GENDER);
        int ageColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_AGE);

        User user = new User();
        // iterate through all rows
        while (cursor.moveToNext()) {
            // Use the index to get a string or number
            int currentID = cursor.getInt(idColumnIndex);
            String currentName = cursor.getString(nameColumnIndex);
            String currentCity = cursor.getString(cityColumnIndex);
            int currentGender = cursor.getInt(genderColumnIndex);
            int currentAge = cursor.getInt(ageColumnIndex);

            System.out.println("CURRENT-Name:" + currentName);
            user.setName(currentName);
            user.setAge(currentAge);
            user.setCity(currentCity);
            user.setId(currentID);
            user.setGender(currentGender);
        }

        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> e) throws Exception {

                if (!e.isDisposed()) {
                    e.onNext(user);
                    e.onComplete();
                }
            }
        });
    }


    public Completable updateUser(int id, String name, int age, String city, int gender) {

        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {

                SQLiteDatabase db = userDbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(UserContract.UserEntry.COLUMN_NAME, name);
                values.put(UserContract.UserEntry.COLUMN_CITY, city);
                values.put(UserContract.UserEntry.COLUMN_GENDER, gender);
                values.put(UserContract.UserEntry.COLUMN_AGE, age);
                db.update(UserContract.UserEntry.TABLE_NAME, values, UserContract.UserEntry._ID + " = " + id, null);
                db.close();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }


    public Completable deleteUser(int id) {

        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {

                SQLiteDatabase db = userDbHelper.getWritableDatabase();
                db.execSQL("delete from "+ UserContract.UserEntry.TABLE_NAME + " where " + UserContract.UserEntry._ID + " = '" + id + "'");
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
