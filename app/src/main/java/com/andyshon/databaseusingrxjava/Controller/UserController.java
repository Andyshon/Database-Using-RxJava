package com.andyshon.databaseusingrxjava.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.andyshon.databaseusingrxjava.Activity.MainActivity;
import com.andyshon.databaseusingrxjava.Database.DatabaseCallback;
import com.andyshon.databaseusingrxjava.Database.DatabaseModel;
import com.andyshon.databaseusingrxjava.Database.UserContract;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by andyshon on 10.07.18.
 */

public class UserController {

    private DatabaseCallback.ViewMain callbackViewMain;
    private DatabaseCallback.ViewDetail callbackViewDetail;
    private DatabaseModel model;


    public UserController(Context context) {

        if (context instanceof MainActivity)
            callbackViewMain = (DatabaseCallback.ViewMain) context;
        else
            callbackViewDetail = (DatabaseCallback.ViewDetail) context;

        model = new DatabaseModel(context);
    }


    public void getAllUsers() {

        model.getAllUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(s -> callbackViewMain.onDataNotAvailable())
                .subscribe(s -> callbackViewMain.onGetAllUsers(s));
    }


    public void getUserById(String id) {
        int userId = Integer.parseInt(id);
        model.getUserById(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> callbackViewDetail.onGetUserById(s));
    }


    public void updateUser(int id, String name, int age, String city, int gender) {
        model.updateUser(id, name, age, city, gender)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onComplete() {
                        callbackViewDetail.onUserUpdate(name);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("UserController", "Eror:" + e.getMessage());
                        callbackViewDetail.onDataNotAvailable();
                    }
                });
    }


    public void addUser(ContentValues values) {

        // Вставляем новый ряд в базу данных и запоминаем его идентификатор
        values.put(UserContract.UserEntry.COLUMN_NAME, values.getAsString(UserContract.UserEntry.COLUMN_NAME));
        values.put(UserContract.UserEntry.COLUMN_AGE, values.getAsString(UserContract.UserEntry.COLUMN_AGE));
        values.put(UserContract.UserEntry.COLUMN_CITY, values.getAsString(UserContract.UserEntry.COLUMN_CITY));
        values.put(UserContract.UserEntry.COLUMN_GENDER, UserContract.UserEntry.GENDER_MALE);

        model.addUser(values)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onComplete() {
                        callbackViewDetail.onUserAdded(values.getAsString(UserContract.UserEntry.COLUMN_NAME));
                    }

                    @Override
                    public void onError(Throwable e) {
                        callbackViewDetail.onDataNotAvailable();
                    }
                });
    }


    public void deleteUser(int id, String name) {
        model.deleteUser(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onComplete() {
                        callbackViewDetail.onUserDeleted(name);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callbackViewDetail.onDataNotAvailable();
                    }
                });
    }
}
