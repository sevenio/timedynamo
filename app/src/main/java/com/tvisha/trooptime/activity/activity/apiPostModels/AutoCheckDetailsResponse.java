package com.tvisha.trooptime.activity.activity.apiPostModels;

/*public class AutoCheckDetailsResponse {
}*/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AutoCheckDetailsResponse {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private AutoCheckDetails data;

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

    public AutoCheckDetails getData() {
        return data;
    }

    public void setData(AutoCheckDetails data) {
        this.data = data;
    }

}