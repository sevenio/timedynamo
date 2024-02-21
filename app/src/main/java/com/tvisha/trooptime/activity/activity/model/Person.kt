package com.tvisha.trooptime.activity.activity.model;

import java.io.Serializable;

/**
 * Created by koti on 23/5/17.
 */

public class Person implements Serializable {


    private String userId;
    private String name;
    private String email;

    public Person(String n, String e, String userId) {
        name = n;
        email = e;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return name;
    }
}
