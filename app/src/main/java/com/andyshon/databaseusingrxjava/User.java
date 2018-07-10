package com.andyshon.databaseusingrxjava;

/**
 * Created by andyshon on 10.07.18.
 */

class User {
    private String name;
    private String city;
    private int age;


    public String getCity() {
        return city;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public User(String name, int age, String city) {
        this.name = name;
        this.age = age;
        this.city = city;
    }

    @Override
    public String toString() {
        return "User name:" + this.name;
    }
}
