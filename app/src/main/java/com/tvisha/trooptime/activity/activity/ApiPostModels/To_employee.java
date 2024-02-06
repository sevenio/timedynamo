package com.tvisha.trooptime.activity.activity.ApiPostModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tvisha on 14/8/18.
 */

/*
public class To_employee {
}
*/

public class To_employee {

    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("name")
    @Expose
    private String name;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}