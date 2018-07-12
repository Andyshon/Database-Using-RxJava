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


        public static ByAge fromString(String text) {
            for (ByAge b : ByAge.values()) {
                if (b.age.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            return null;
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

        public static ByGender fromString(String text) {
            for (ByGender b : ByGender.values()) {
                if (b.gender.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            return null;
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
