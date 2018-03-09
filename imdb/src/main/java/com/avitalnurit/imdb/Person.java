package com.avitalnurit.imdb;

/**
 * Created by avital on 07/01/2018.
 */
public class Person {
    private String name;
    private int gender;
    private String role;

    public Person(String name, int gender, String role) {
        this.name = name;
        this.gender = gender;
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public int getGender() {
        return gender;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", gender=" + gender +
                ", role='" + role + '\'' +
                '}';
    }
}
