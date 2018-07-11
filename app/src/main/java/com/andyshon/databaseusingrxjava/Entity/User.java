package com.andyshon.databaseusingrxjava.Entity;

/**
 * Created by andyshon on 10.07.18.
 */

public class User {
    private String name, city;
    private int id, age, gender;

    public User() {}

    public User(int id, String name, int age, String city, int gender) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.city = city;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }


    @Override
    public String toString() {
        return "Name:" + this.name + "\nAge:" + this.age + "\nGender:" + this.gender + "\nCity:" + this.city;
    }
}
