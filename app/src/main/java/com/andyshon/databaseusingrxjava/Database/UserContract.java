package com.andyshon.databaseusingrxjava.Database;

import android.provider.BaseColumns;

/**
 * Created by andyshon on 10.07.18.
 */

public final class UserContract {
    private UserContract(){}

    public static final class UserEntry implements BaseColumns {
        public final static String TABLE_NAME = "users";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_CITY = "city";
        public final static String COLUMN_GENDER = "gender";
        public final static String COLUMN_AGE = "age";

        public static final int GENDER_FEMALE = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_UNKNOWN = 2;
    }
}
