package com.andyshon.databaseusingrxjava;

/**
 * Created by andyshon on 11.07.18.
 */

public enum FilterOperation {

    FILTER_OPERATION;

    public enum ByAge {

        LESSTHAN("age less than"),
        MORETHAN("age more than"),
        EQUALS("age equals");

        public static ByAge toMyEnum (String myEnumString) {
            try {
                System.out.println("value of:" + valueOf(myEnumString));
                return valueOf(myEnumString);
            } catch (Exception ex) {
                // For error cases
                return EQUALS;
            }
        }

        private String age;

        ByAge(String s) {
            this.age = s;
        }

        @Override public String toString(){
            return age;
        }

    }

    public enum ByGender {

        MALE("male"),
        FEMALE("female"),
        BOTH("both");

        public static ByGender toMyEnum (String myEnumString) {
            try {
                System.out.println("value of22:" + valueOf(myEnumString));
                return valueOf(myEnumString);
            } catch (Exception ex) {
                // For error cases
                return MALE;
            }
        }

        private String gender;

        ByGender(String s) {
            this.gender = s;
        }

        @Override public String toString(){
            return gender;
        }

    }
}
