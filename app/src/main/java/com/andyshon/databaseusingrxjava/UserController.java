package com.andyshon.databaseusingrxjava;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by andyshon on 10.07.18.
 */

public class UserController {

    private UserInterface.View view;
    private UserInterface.DetailView detailView;
    private DbModel model;
    private Context context;


    public UserController(Context context) {

        this.context = context;
        if (context instanceof MainActivity) {
            System.out.println("context instanceof MainActivity");
            this.view = (UserInterface.View) context;
        }
        else {
            System.out.println("context instanceof else");
            this.detailView = (UserInterface.DetailView) context;
        }

        model = new DbModel(context);
    }


    public void getAllUsers () {


        model.getAllUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> view.populateData(s));
                /*.subscribe(new Consumer<List<ApiUser>>() {
                    @Override
                    public void accept(List<ApiUser> apiUsers) throws Exception {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                view.populateData(apiUsers);
                            }
                        });

                    }
                });*/
    }
}
