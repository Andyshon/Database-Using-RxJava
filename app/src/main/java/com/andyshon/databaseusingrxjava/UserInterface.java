package com.andyshon.databaseusingrxjava;

import android.content.ContentValues;

import java.util.List;

/**
 * Created by andyshon on 10.07.18.
 */

public interface UserInterface {

    interface View {
        void populateData(List<User> userList);
    }

    interface DetailView {
        void getNewUser(ContentValues values);
    }

}
