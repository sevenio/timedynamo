package com.tvisha.trooptime.activity.activity.ApiPostModels;

/**
 * Created by tvisha on 13/8/18.
 */

/*public class LeaveRequestResponse {
}*/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveRequestResponse {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private String message;

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

}