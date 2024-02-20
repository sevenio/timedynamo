package com.tvisha.trooptime.activity.activity.apiPostModels;

/**
 * Created by tvisha on 14/8/18.
 */
/*

public class ToAndCcDetailsResponse {
}
*/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ToAndCcDetailsResponse {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("to_employees")
    @Expose
    private List<To_employee> to_employees = null;
    @SerializedName("cc_employess")
    @Expose
    private List<Cc_employess> cc_employess = null;
    @SerializedName("users")
    @Expose
    private List<Users> users = null;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<To_employee> getTo_employees() {
        return to_employees;
    }

    public void setTo_employees(List<To_employee> to_employees) {
        this.to_employees = to_employees;
    }

    public List<Cc_employess> getCc_employess() {
        return cc_employess;
    }

    public void setCc_employess(List<Cc_employess> cc_employess) {
        this.cc_employess = cc_employess;
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

}

