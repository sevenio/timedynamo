package com.tvisha.trooptime.activity.activity.AutoComplete;

/**
 * Created by akhil on 28/6/18.
 */

public class User {

    public String userId = " ";
    public String name = "";
    public String displayName = "";
    public String email = "";

    public User(String userId, String name, String empId, String email) {
        this.userId = userId;
        this.name = name;
        this.displayName = empId;
        this.email = email;

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
