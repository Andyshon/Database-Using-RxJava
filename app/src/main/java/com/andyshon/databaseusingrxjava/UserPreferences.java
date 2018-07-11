package com.andyshon.databaseusingrxjava;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by andyshon on 11.07.18.
 */

public final class UserPreferences {
    private UserPreferences(){}

    public static void SaveUserPreferences (Context context, FilterOperation.ByAge byAge, FilterOperation.ByGender byGender, int age) {

        System.out.println("byAge:::" + byAge.toString());
        SharedPreferences myPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPref.edit();
        editor.putString("last_filter_age_type", byAge.toString());
        editor.putInt("last_filter_age_number", /*FilterOperation._FilterByAge*/age);
        editor.putString("last_filter_gender", String.valueOf(byGender));
        editor.apply();
    }


    public static int getFilterByAge (Context context) {
        SharedPreferences sp = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int age = sp.getInt("last_filter_age_number", 0);
        System.out.println("age = " + age);
        return age;
    }


    public static FilterOperation.ByAge getMyEnumByAge (Context context) {
        SharedPreferences sp = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String myEnumString = sp.getString("last_filter_age_type", null);
        System.out.println("myEnumString = " + myEnumString);
        return FilterOperation.ByAge.toMyEnum(myEnumString);
    }


    public static FilterOperation.ByGender getMyEnumByGender (Context context) {
        SharedPreferences sp = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String myEnumString = sp.getString("last_filter_gender", null);
        System.out.println("myEnumString = " + myEnumString);
        return FilterOperation.ByGender.toMyEnum(myEnumString);
    }
}
