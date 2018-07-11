package com.andyshon.databaseusingrxjava.Database;


import com.andyshon.databaseusingrxjava.Entity.User;

import java.util.List;

/**
 * Created by andyshon on 10.07.18.
 */

public interface DatabaseCallback {

    interface ViewDetail {
        void onUserAdded(String name);
        void onUserDeleted(String name);
        void onGetUserById(User user);
        void onUserUpdate(String name);
        void onDataNotAvailable();
    }

    interface ViewMain {
        void onGetAllUsers(List<User> users);
        void onDataNotAvailable();
    }
}
