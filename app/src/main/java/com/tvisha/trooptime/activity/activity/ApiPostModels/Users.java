package com.tvisha.trooptime.activity.activity.ApiPostModels;

/**
 * Created by tvisha on 14/8/18.
 */

/*public class Users {
}*/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Users {


    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("display_name")
    @Expose
    private String display_name;
    @SerializedName("email")
    @Expose
    private String email;

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

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}


